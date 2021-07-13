package net.skyhcf.atmosphere.bukkit.listeners;

import com.google.common.collect.Lists;
import net.skyhcf.atmosphere.bukkit.AtmosphereBukkit;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.SharedAPI;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.punishment.Punishment;
import net.skyhcf.atmosphere.shared.punishment.PunishmentType;
import net.skyhcf.atmosphere.shared.utils.Settings;
import net.skyhcf.atmosphere.shared.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.regex.Pattern;

public class BukkitListener implements Listener {

    @EventHandler
    public static void chat(AsyncPlayerChatEvent e){
        Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(e.getPlayer().getUniqueId());
        String prefix = (profile.getActivePrefix() == null ? "" : profile.getActivePrefix().getPrefix());
        String displayName = prefix + BukkitChat.RESET + profile.getHighestGrantOnScope(AtmosphereShared.getInstance().getServerManager().getServer(AtmosphereBukkit.getInstance().getServer().getServerName())).getRank().getPrefix() + e.getPlayer().getName();
        if(isPluginEnabled("HCTeams") || isPluginEnabled("Hub")) e.setFormat(BukkitChat.RESET + BukkitChat.format(displayName) + BukkitChat.LIGHT_GRAY + ": " + BukkitChat.RESET + "%2$s");
    }

    @EventHandler
    public void join(AsyncPlayerPreLoginEvent e){
        Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(e.getUniqueId());
        if(profile == null) return;
        if(profile.hasActivePunishment(PunishmentType.BAN)){
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
            Punishment punishment = profile.getActivePunishments(PunishmentType.BAN).get(0);
            if(punishment.getDuration() == Long.MAX_VALUE) {
                e.setKickMessage(BukkitChat.format("&cYour account has been suspended from the " + Settings.SERVER_NAME + " Network.\n\n&cAppeal at " + Settings.SERVER_WEBSITE + "."));
            }else {
                e.setKickMessage(BukkitChat.format("&cYour account has been suspended from the " + Settings.SERVER_NAME + " Network.\n\n&cExpires in " + punishment.getRemainingText() + "."));
            }
        }
        for(Profile alt : profile.getAlts()){
            if(alt.hasActivePunishment(PunishmentType.BLACKLIST)){
                e.setKickMessage(BukkitChat.format("&cYour account has been blacklisted from the SkyHCF Network due to a blacklist in relation to " + alt.getUsername() + ".\n\n&cThis type of punishment cannot be appealed."));
                e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
                break;
            }
        }
        if(profile.hasActivePunishment(PunishmentType.BLACKLIST)){
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
            Punishment punishment = profile.getActivePunishments(PunishmentType.BAN).get(0);
            e.setKickMessage(BukkitChat.format("&cYour account has been blacklisted from the SkyHCF Network.\n\n&cThis type of punishment cannot be appealed."));
        }
        List<Profile> alts = Lists.newArrayList();
        for(Profile profile1 : AtmosphereShared.getInstance().getProfileManager().getProfiles()){
            if(profile1.getIp().equalsIgnoreCase(profile.getIp()) && !profile.getUuid().toString().equals(profile1.getIp())){
                alts.add(profile1);
            }
        }
    }
        /*doesnt_work:
        for(Profile alt : alts){
            if(alt.hasActivePunishment(PunishmentType.BAN)){
                AtmosphereShared.getInstance().getPunishmentManager().createPunishment(new Punishment(
                        UUID.randomUUID(),
                        alt.getUuid(),
                        Profile.getConsoleUUID(),
                        null,
                        PunishmentType.BAN,
                        AtmosphereShared.getInstance().getServerManager().getServer(AtmosphereBukkit.getInstance().getServer().getServerName()),
                        System.currentTimeMillis(),
                        Long.MAX_VALUE,
                        0L,
                        "Punishment Evasion",
                        "In association with " + profile.getUsername() + ".",
                        null,
                        Lists.newArrayList()
                ), true);
            }
        }*/

    @EventHandler
    public void command(PlayerCommandPreprocessEvent e){
        Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(e.getPlayer().getUniqueId());
        if(profile == null) return;
        if(e.getPlayer().hasMetadata("AuthLocked")){
            if(!e.getMessage().startsWith("/auth") && !e.getMessage().startsWith("/2fa")) {
                e.getPlayer().sendMessage(BukkitChat.format("&cPlease provide your two-factor code. \"/auth <code>\" to authenticate."));
                e.setCancelled(true);
                return;
            }
        }
        if(profile.hasActivePunishment(PunishmentType.BAN)){
            e.setCancelled(true);
            e.getPlayer().sendMessage(BukkitChat.format("&cYou cannot use commands whilst you are banned."));
        }
    }

    @EventHandler
    public void initialJoin(PlayerJoinEvent e){
        Profile profile = SharedAPI.getProfile(e.getPlayer().getUniqueId());
        if(profile == null) return;
        if(AtmosphereShared.getInstance().isDev()) {
            if ((System.currentTimeMillis() - profile.getLastAuthentication()) <= TimeUtil.parseTime("3h") && profile.getAuthSecret() != null) {
                e.getPlayer().setMetadata("AuthLocked", new FixedMetadataValue(AtmosphereBukkit.getInstance(), true));
                (new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (e.getPlayer() == null || profile.getLastAuthentication() > TimeUtil.parseTime("3h") && profile.getAuthSecret() != null) {
                            this.cancel();
                            return;
                        }
                        e.getPlayer().sendMessage(BukkitChat.format("&cPlease provide your two-factor code. \"/auth <code>\" to authenticate."));
                    }
                }).runTaskTimer(AtmosphereBukkit.getInstance(), 0L, (20 * 6));

            }
        }
    }



    @EventHandler
    public void openInventory(InventoryOpenEvent e){
        if(e.getPlayer().hasMetadata("AuthLocked")){
            e.setCancelled(true);
            e.getPlayer().closeInventory();
        }
    }

    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent e){
        Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(e.getPlayer().getUniqueId());
        if(e.getPlayer().hasMetadata("AuthLocked")){
            e.setCancelled(true);
            e.getPlayer().sendMessage(BukkitChat.format("&cPlease provide your two-factor code. \"/auth <code>\" to authenticate."));
            return;
        }
        if(profile.hasActivePunishment(PunishmentType.MUTE)){
            e.setCancelled(true);
            Punishment punishment = profile.getActivePunishments(PunishmentType.MUTE).get(0);
            if(punishment.getDuration() == Long.MAX_VALUE){
                e.getPlayer().sendMessage(BukkitChat.format("&cYou are currently silenced."));
            }else{
                e.getPlayer().sendMessage(BukkitChat.format("&cYou are currently silenced. Your mute will be lifted in &c&l" + punishment.getRemainingText() + "&r&c."));
            }
        }else{
            if(profile.isStaffChat()){
                e.setCancelled(true);
                Bukkit.dispatchCommand(e.getPlayer(), "staffchat " + e.getMessage());
                return;
            }
            List<String> banned = Lists.newArrayList();
            for(String word : banned){
                if(e.getMessage().toLowerCase().equals(word.toLowerCase())){
                    e.setCancelled(true);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tempmute " + e.getPlayer().getName() + " 7d \"" + e.getMessage() + "\"");
                }
            }
        }
    }

    private static boolean isPluginEnabled(String pl){
        return (AtmosphereBukkit.getInstance().getServer().getPluginManager().getPlugin(pl) == null);
    }

}
