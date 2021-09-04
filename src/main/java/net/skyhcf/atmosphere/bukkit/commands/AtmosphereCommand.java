/*package net.skyhcf.atmosphere.bukkit.commands;

import net.frozenorb.qlib.command.Command;
import net.skyhcf.atmosphere.bukkit.AtmosphereBukkit;
import net.skyhcf.atmosphere.bukkit.utils.OSUtil;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AtmosphereCommand {

    @Command(names = {"atmosphere"}, permission = "")
    public void atmosphere(CommandSender sender){

        sender.sendMessage(BukkitChat.format("&7&m----------------------------------------------"));
        sender.sendMessage(BukkitChat.format("&7"));
        sender.sendMessage(BukkitChat.format("&b&lAtmosphere"));
        sender.sendMessage(BukkitChat.format("&7"));
        sender.sendMessage(BukkitChat.format("&7Atmosphere is a custom coded network core."));
        sender.sendMessage(BukkitChat.format("&7This includes a rank system, punishment system"));
        sender.sendMessage(BukkitChat.format("&7and a multi server chatting system."));
        sender.sendMessage(BukkitChat.format("&7"));
        sender.sendMessage(BukkitChat.format("&bVersion: &72.1.4"));
        sender.sendMessage(BukkitChat.format("&bBranch: &7Production"));
        sender.sendMessage(BukkitChat.format("&7"));
        sender.sendMessage(BukkitChat.format("&7"));
        sender.sendMessage(BukkitChat.format("&7&m----------------------------------------------"));
    }

    @Command(names = {"atmosphere admin"}, hidden = true, permission = "")
    public void atmosphereadmin(Player sender){

        if (AtmosphereBukkit.PluginOwner.contains(sender.getUniqueId())) {

            sender.sendMessage(BukkitChat.format("&7&m--------------------------------"));
            sender.sendMessage(BukkitChat.format("&7"));
            sender.sendMessage(BukkitChat.format("&bAtmosphere Version&7: &a2.1.4"));
            sender.sendMessage(BukkitChat.format("&bSpigot Version&7: &a" + Bukkit.getBukkitVersion()));
            sender.sendMessage(BukkitChat.format("&bServer Name&7: &a" + Bukkit.getServerName()));
            sender.sendMessage(BukkitChat.format("&bHost OS Name&7: &a" + OSUtil.getOsName()));
            sender.sendMessage(BukkitChat.format("&7"));
            sender.sendMessage(BukkitChat.format("&7&m--------------------------------"));

            return;
        }
            sender.sendMessage(BukkitChat.format("&cNo Permission."));
    }
}*/
