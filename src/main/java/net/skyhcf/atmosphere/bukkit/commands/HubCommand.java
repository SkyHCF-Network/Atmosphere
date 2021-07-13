package net.skyhcf.atmosphere.bukkit.commands;

import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.frozenorb.qlib.util.BungeeUtils;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.SharedAPI;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HubCommand {

    @Command(names = {"hub"}, permission = "")
    public static void hub(Player player, @Param(name = "hub id", defaultValue = "default") String targetServer){
        Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(player.getUniqueId());
        boolean offline = false;
        if(targetServer.equalsIgnoreCase("default")){
            if(SharedAPI.getProfile(player.getUniqueId()).getHighestGrantOnScope(SharedAPI.getServer(Bukkit.getServerName())).getRank().isStaff()) {
                if (SharedAPI.getServer("Hub-Restricted") == null || !SharedAPI.getServer("Hub-Restricted").isOnline()) {
                    offline = true;
                    targetServer = "Hub-01";
                } else {
                    targetServer = "Hub-Restricted";
                }
            }
        }else{
            targetServer = "Hub-01";
        }
        if(targetServer.equalsIgnoreCase("hub-restricted")){
            if(!profile.getHighestGrant().getRank().isStaff()){
                player.sendMessage(BukkitChat.format("&cYou must be &eTrial Mod &crank or higher to join &fHub-Restricted&c."));
                return;
            }
            targetServer = "Hub-Restricted";
        }
        String first = targetServer;
        switch(targetServer.toLowerCase()){
            case "hub-01":
            case "hub-02":
            case "hub-03":
            case "hub-04":
            case "hub-05" :
            case "hub-restricted": {
                break;
            } default: {
                targetServer = "CANNOT_JOIN_THIS_SERVER";
            }
        }
        if(targetServer.equals("CANNOT_JOIN_THIS_SERVER")){
            player.sendMessage(BukkitChat.format("&cNo hub with id &r" + first + "&r &cfound on your current proxy."));
            return;
        }
        if(offline){
            player.sendMessage(BukkitChat.format("&cThe &fHub-Restricted &cserver is offline, so you have been connected to a fallback server."));
        }
        player.sendMessage(BukkitChat.format("&6You are now being connected to &f" + targetServer + "&6."));
        if(profile.getHighestGrant().getRank().isStaff()){
            player.sendMessage(BukkitChat.format("&6You may visit a specific hub by typing &f/hub <hub id>&6."));
        }
        BungeeUtils.send(player, targetServer);
    }

}