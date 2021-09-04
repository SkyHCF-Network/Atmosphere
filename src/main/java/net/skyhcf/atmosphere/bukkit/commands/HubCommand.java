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

import static java.lang.Thread.sleep;

public class HubCommand {

    @Command(names = {"hub"}, permission = "")
    public static void hub(Player player, @Param(name = "hub id", defaultValue = "default") String targetServer) throws InterruptedException {
        if (SharedAPI.getServer("Hub-01").isOnline()) {
            if (!player.getServer().getServerName().equalsIgnoreCase("Hub-01")) {
                targetServer = "Hub-01";
                player.sendMessage(BukkitChat.format("&6Finding an open Hub server..."));
                sleep(500);
                player.sendMessage(BukkitChat.format("&6Sending you to &fHub-01&6..."));
                BungeeUtils.send(player, targetServer);
                player.sendMessage(BukkitChat.format("&6Connected to &fHub-01&6."));
                return;
                    }
            player.sendMessage(BukkitChat.format("&cYou are already connected to a Hub."));
            return;
                }
        player.sendMessage(BukkitChat.format("&cNo Hubs are currently online. Try again later."));
            }
}
