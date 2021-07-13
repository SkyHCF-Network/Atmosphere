package net.skyhcf.atmosphere.bungee.listeners;

import io.netty.channel.epoll.EpollServerChannelConfig;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.event.EventHandler;
import net.skyhcf.atmosphere.bungee.AtmosphereBungee;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.chat.BungeeChat;
import net.skyhcf.atmosphere.shared.packet.StaffJoinNetworkPacket;
import net.skyhcf.atmosphere.shared.packet.StaffLeaveNetworkPacket;
import net.skyhcf.atmosphere.shared.packet.StaffSwitchServerPacket;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.utils.EncryptionHelper;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.concurrent.TimeUnit;

public class BungeeListener implements Listener {

    @EventHandler
    public void join(PostLoginEvent e){
        if(!AtmosphereShared.getInstance().getProfileManager().profileExists(e.getPlayer().getUniqueId())){
            AtmosphereShared.getInstance().getProfileManager().createProfile(e.getPlayer().getUniqueId(), e.getPlayer().getName(), e.getPlayer().getAddress().getAddress().getHostAddress());
        }
        BungeeCord.getInstance().getScheduler().schedule(AtmosphereBungee.getInstance(), () -> {
            Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(e.getPlayer().getUniqueId());
            if(profile == null || e.getPlayer().getServer() == null) return;
            for(String perm : profile.getPermissions()){
                e.getPlayer().setPermission(perm, true);
            }
            profile.setLastSeen(System.currentTimeMillis());
            profile.setCurrentServer(e.getPlayer().getServer().getInfo().getName());
            profile.setUsername(e.getPlayer().getName());
            profile.setIp(EncryptionHelper.toHexString(e.getPlayer().getAddress().getAddress().getHostAddress()));
            profile.save();
            if(profile.getHighestGrant().getRank().isStaff()) AtmosphereShared.getInstance().getRedisHandler().sendPacket(new StaffJoinNetworkPacket(
                    profile.getHighestGrant().getRank().getColor() + profile.getUsername(),
                    e.getPlayer().getServer().getInfo().getName()
            ));
        }, 1100, TimeUnit.MILLISECONDS);

    }

    @EventHandler
    public void login(LoginEvent e) {
        if (AtmosphereBungee.getInstance().getConfiguration().getBoolean("maintenance")) {
            Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(e.getConnection().getUniqueId());
            if (profile == null) {
                e.setCancelled(true);
                e.setCancelReason(BungeeChat.format("&cThe network is currently whitelisted.\n\n&cYou may find additional information on our discord at https://www.skyhcf.net/discord."));
            } else {
                if (!profile.hasPermissionPriorToLogin("atmosphere.admin")) {
                    e.setCancelled(true);
                    e.setCancelReason(BungeeChat.format("&cThe network is currently whitelisted.\n\n&cYou may find additional information on our discord at https://www.skyhcf.net/discord."));
                }
            }
        }
    }

    @EventHandler
    public void leave(PlayerDisconnectEvent e){
        BungeeCord.getInstance().getScheduler().schedule(AtmosphereBungee.getInstance(), () -> {
            Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(e.getPlayer().getUniqueId());
            profile.setCurrentServer(null);
            profile.setLastSeen(System.currentTimeMillis());
            profile.save();
            if(profile == null || e.getPlayer().getServer() == null) return;
            if(profile.getHighestGrant().getRank().isStaff()){
                AtmosphereShared.getInstance().getRedisHandler().sendPacket(new StaffLeaveNetworkPacket(profile.getHighestGrant().getRank().getColor() + profile.getUsername(), e.getPlayer().getServer().getInfo().getName()));
            }
        }, 1100, TimeUnit.MILLISECONDS);
    }

    @EventHandler
    public void pingServer(ProxyPingEvent e){
        ServerPing serverPing = e.getResponse();
        serverPing.setDescription(BungeeChat.format(AtmosphereBungee.getInstance().getConfiguration().getString("motd")));
        if(AtmosphereBungee.getInstance().getConfiguration().getBoolean("maintenance")) {
            serverPing.setVersion(new ServerPing.Protocol("Maintenance", -8));
        }
        e.setResponse(serverPing);
    }

    @EventHandler
    public void switchServer(ServerSwitchEvent e){
        if(e.getFrom() == null) return;
        Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(e.getPlayer().getUniqueId());
        if(profile == null || e.getPlayer().getServer() == null) return;
        profile.setCurrentServer(e.getPlayer().getServer().getInfo().getName());
        profile.save();
        if(profile.getHighestGrant().getRank().isStaff()){
            BungeeCord.getInstance().getScheduler().schedule(AtmosphereBungee.getInstance(), () -> {
                AtmosphereShared.getInstance().getRedisHandler().sendPacket(new StaffSwitchServerPacket(profile.getHighestGrant().getRank().getColor() + profile.getUsername(), e.getFrom().getName(), e.getPlayer().getServer().getInfo().getName()));
            }, 1100, TimeUnit.MILLISECONDS);
        }
    }

    @EventHandler
    public void joinNetwork(ServerConnectEvent event){
        Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(event.getPlayer().getUniqueId());
        if(profile.getHighestGrant().getRank().isStaff() && event.getReason() == ServerConnectEvent.Reason.JOIN_PROXY) {
            event.setTarget(BungeeCord.getInstance().getServerInfo("Hub-Restricted"));
            return;
        }
        if(event.getReason() == ServerConnectEvent.Reason.KICK_REDIRECT || event.getReason() == ServerConnectEvent.Reason.LOBBY_FALLBACK){
            event.setTarget(BungeeCord.getInstance().getServerInfo("hub-01"));
        }
    }

}
