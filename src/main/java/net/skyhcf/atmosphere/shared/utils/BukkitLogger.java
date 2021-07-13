package net.skyhcf.atmosphere.shared.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class BukkitLogger {

    public static void log(String prefix, String message){
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "[" + prefix + "] " + message));
    }

}
