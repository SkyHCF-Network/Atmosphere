package net.skyhcf.atmosphere.shared.redis.listeners;

import com.google.gson.JsonObject;
import mkremins.fanciful.FancyMessage;
import net.frozenorb.qmodsuite.qModSuite;
import net.skyhcf.atmosphere.bukkit.AtmosphereBukkit;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.SharedAPI;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.punishment.PunishmentType;
import net.skyhcf.atmosphere.shared.redis.RedisListener;
import net.skyhcf.atmosphere.shared.server.Server;
import net.skyhcf.atmosphere.shared.utils.Settings;
import net.skyhcf.atmosphere.shared.utils.SystemType;
import net.skyhcf.atmosphere.shared.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PacketListener extends RedisListener {

    public boolean isBukkit(){
        return AtmosphereShared.getInstance().getSystemType() == SystemType.BUKKIT;
    }

    private boolean qModEnabled;

    public PacketListener(){
        this.qModEnabled = Bukkit.getServer().getPluginManager().isPluginEnabled("qModSuite");
    }

    @net.skyhcf.atmosphere.shared.redis.PacketListener(identifier = "staffchat-packet")
    public void staffChat(JsonObject jsonObject){
        String sender = jsonObject.get("sender").getAsString();
        String server = jsonObject.get("server").getAsString();
        String message = jsonObject.get("message").getAsString();
        if(isBukkit()) {
            for (Player player : AtmosphereBukkit.getInstance().getServer().getOnlinePlayers()) {
                if (qModEnabled) {
                    if (!qModSuite.getInstance().getSilencedStaffMembers().contains(player.getUniqueId())){
                        Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(player.getUniqueId());
                        if (profile.hasPermission("atmosphere.staff")) {
                            player.sendMessage(BukkitChat.format("&9[Staff] &7[" + server + "] &r" + sender + "&r&7: &b") + message);
                        }
                    }
                }else{
                    Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(player.getUniqueId());
                    if (profile.hasPermission("atmosphere.staff")) {
                        player.sendMessage(BukkitChat.format("&9[Staff] &7[" + server + "] &r" + sender + "&r&7: &b") + message);
                    }
                }
            }
        }
    }

    @net.skyhcf.atmosphere.shared.redis.PacketListener(identifier = "staff-join-packet")
    public void staffJoinNetwork(JsonObject jsonObject){
        String sender = jsonObject.get("sender").getAsString();
        String server = jsonObject.get("server").getAsString();
        if(isBukkit()){
            for(Player player : AtmosphereBukkit.getInstance().getServer().getOnlinePlayers()){
                Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(player.getUniqueId());
                if(profile.hasPermission("atmosphere.staff")){
                    player.sendMessage(BukkitChat.format("&9[Staff] &r" + sender + "&r &ajoined &bthe network (" + server + ")."));
                }
            }
        }
    }

    @net.skyhcf.atmosphere.shared.redis.PacketListener(identifier = "staff-leave-packet")
    public void staffLeftNetwork(JsonObject jsonObject){
        String sender = jsonObject.get("sender").getAsString();
        String server = jsonObject.get("server").getAsString();
        if(isBukkit()){
            for(Player player : AtmosphereBukkit.getInstance().getServer().getOnlinePlayers()){
                Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(player.getUniqueId());
                if(profile.hasPermission("atmosphere.staff")){
                    player.sendMessage(BukkitChat.format("&9[Staff] &r" + sender + "&r &cleft &bthe network (from " + server + ")."));
                }
            }
        }
    }

    @net.skyhcf.atmosphere.shared.redis.PacketListener(identifier = "staff-switch-packet")
    public void staffSwitchServer(JsonObject jsonObject){
        String sender = jsonObject.get("sender").getAsString();
        String from = jsonObject.get("from").getAsString();
        String server = jsonObject.get("server").getAsString();
        if(isBukkit()){
            for(Player player : AtmosphereBukkit.getInstance().getServer().getOnlinePlayers()){
                Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(player.getUniqueId());
                if(profile.hasPermission("atmosphere.staff")){
                    player.sendMessage(BukkitChat.format("&9[Staff] &r" + sender + "&r &bswitched from " + from + " to " + server + "."));
                }
            }
        }
    }

    @net.skyhcf.atmosphere.shared.redis.PacketListener(identifier = "punishment-packet")
    public void punishment(JsonObject jsonObject) {
        UUID target = UUID.fromString(jsonObject.get("target").getAsString());
        UUID addedBy = UUID.fromString(jsonObject.get("addedBy").getAsString());
        String reason = jsonObject.get("reason").getAsString();
        String internalReason = (jsonObject.get("internalReason") == null ? null : jsonObject.get("internalReason").getAsString());
        Server server = AtmosphereShared.getInstance().getServerManager().getServer(jsonObject.get("server").getAsString());
        long duration = Long.parseLong(jsonObject.get("duration").getAsString());
        boolean silent = Boolean.parseBoolean(jsonObject.get("silent").getAsString());
        PunishmentType punishmentType = PunishmentType.valueOf(jsonObject.get("punishmentType").getAsString());
        if (AtmosphereShared.getInstance().getSystemType() == SystemType.BUKKIT) {
            Server currentServer = AtmosphereShared.getInstance().getServerManager().getServer(AtmosphereBukkit.getInstance().getServer().getServerName());
            String addedByDisplay = SharedAPI.formatNameOnScope(addedBy, currentServer);
            String targetDisplay = SharedAPI.formatNameOnScope(target, currentServer);
            for (Player player : AtmosphereBukkit.getInstance().getServer().getOnlinePlayers()) {
                Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(player.getUniqueId());
                FancyMessage fancyMessage;
                if (duration == Long.MAX_VALUE) {
                    fancyMessage = new FancyMessage(BukkitChat.format((silent ? "&7[Silent] " : "") + targetDisplay + "&r &awas " + punishmentType.getAddedDisplay() + "&r &aby " + addedByDisplay + "&r&a."));
                    fancyMessage.tooltip(BukkitChat.format("&eReason&7: &c" + reason + (internalReason != null ? "\n&eInternal Reason&7: &c" + internalReason : "")));
                } else {
                    fancyMessage = new FancyMessage(BukkitChat.format((silent ? "&7[Silent] " : "") + targetDisplay + "&r &awas temporarily " + punishmentType.getAddedDisplay() + "&r &aby " + addedByDisplay + "&r&a."));
                    fancyMessage.tooltip(BukkitChat.format("&eReason&7: &c" + reason + (internalReason != null ? "\n&eInternal Reason&7: &c" + internalReason : "") + "\n&eDuration&7: &c" + TimeUtil.formatDuration(duration)));
                }
                if (silent) {
                    if (profile.getHighestGrantOnScope(currentServer).getRank().isStaff()) {
                        fancyMessage.send(player);
                    }
                } else {
                    if (profile.getHighestGrantOnScope(currentServer).getRank().isStaff()) {
                        fancyMessage.send(player);
                    } else {
                        player.sendMessage(BukkitChat.format((silent ? "&7[Silent] " : "") + targetDisplay + "&r &awas " + punishmentType.getAddedDisplay() + "&r &aby " + addedByDisplay + "&r&a."));
                    }
                }
            }
            if (AtmosphereShared.getInstance().getSystemType() == SystemType.BUKKIT) {
                Player targetPlayer = AtmosphereBukkit.getInstance().getServer().getPlayer(target);
                if (targetPlayer == null) return;
                if (punishmentType == PunishmentType.BAN && AtmosphereShared.getInstance().getSystemType() == SystemType.BUKKIT) {
                    if (duration == Long.MAX_VALUE) {
                        AtmosphereBukkit.getInstance().getServer().getScheduler().runTask(AtmosphereBukkit.getInstance(), () -> {
                            targetPlayer.kickPlayer(BukkitChat.format("&cYour account has been suspended from the SkyHCF Network.\n\n&cAppeal at " + Settings.SERVER_WEBSITE + "."));
                        });
                    } else {
                        AtmosphereBukkit.getInstance().getServer().getScheduler().runTask(AtmosphereBukkit.getInstance(), () -> {
                            targetPlayer.kickPlayer(BukkitChat.format("&cYour account has been suspended from the SkyHCF Network.\n\n&cExpires in " + TimeUtil.formatDuration(duration) + "."));
                        });

                    }
                } else if (punishmentType == PunishmentType.BLACKLIST && AtmosphereShared.getInstance().getSystemType() == SystemType.BUKKIT) {
                    AtmosphereBukkit.getInstance().getServer().getScheduler().runTask(AtmosphereBukkit.getInstance(), () -> {
                        targetPlayer.kickPlayer(BukkitChat.format("&cYour account has been blacklisted from the SkyHCF Network.\n\n&cThis type of punishment cannot be appealed."));
                    });
                } else if(punishmentType == PunishmentType.KICK && AtmosphereShared.getInstance().getSystemType() == SystemType.BUKKIT){
                    AtmosphereBukkit.getInstance().getServer().getScheduler().runTask(AtmosphereBukkit.getInstance(), () -> {
                        targetPlayer.kickPlayer(BukkitChat.format("&cYou were kicked by a staff member: &r" + reason));
                    });
                }else if(punishmentType == PunishmentType.WARN){
                    targetPlayer.sendMessage("");
                    targetPlayer.sendMessage(BukkitChat.format("&c&lYou have been warned due to &e" + reason + "&r&c&l."));
                    targetPlayer.sendMessage("");
                }
            }
        }
    }

    @net.skyhcf.atmosphere.shared.redis.PacketListener(identifier = "punishment-remove-packet")
    public void punishmentRemove(JsonObject jsonObject){
        if(!isBukkit()) return;
        UUID target = UUID.fromString(jsonObject.get("target").getAsString());
        UUID removedBy = UUID.fromString(jsonObject.get("removedBy").getAsString());
        String reason = jsonObject.get("reason").getAsString();
        PunishmentType punishmentType = PunishmentType.valueOf(jsonObject.get("punishmentType").getAsString());
        boolean silent = Boolean.parseBoolean(jsonObject.get("silent").getAsString());

        Server server = AtmosphereShared.getInstance().getServerManager().getServer(AtmosphereBukkit.getInstance().getServer().getServerName());

        FancyMessage fancyMessage = new FancyMessage(BukkitChat.format((silent ? "&7[Silent] " : "") + SharedAPI.formatNameOnScope(target, server) + "&r &awas " + punishmentType.getRemovedDisplay() + " by " + SharedAPI.formatNameOnScope(removedBy, server) + "&r&a."));
        fancyMessage.tooltip(BukkitChat.format("&eReason&7: &c" + reason));
        if(!silent){
            for(Player player : AtmosphereBukkit.getInstance().getServer().getOnlinePlayers()){
                Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(player.getUniqueId());
                if(!profile.getHighestGrantOnScope(server).getRank().isStaff()){
                    player.sendMessage(BukkitChat.format((silent ? "&7[Silent] " : "") + SharedAPI.formatNameOnScope(target, server) + "&r &awas " + punishmentType.getRemovedDisplay() + " by " + SharedAPI.formatNameOnScope(removedBy, server) + "&r&a."));
                }else{
                    fancyMessage.send(player);
                }
            }
        }else{
            for(Player player : AtmosphereBukkit.getInstance().getServer().getOnlinePlayers()){
                Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(player.getUniqueId());
                if(profile.getHighestGrantOnScope(server).getRank().isStaff()){
                    fancyMessage.send(player);
                }
            }
        }

    }

}
