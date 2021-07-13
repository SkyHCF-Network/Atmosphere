package net.skyhcf.atmosphere.shared.utils;

import org.bukkit.ChatColor;

public class SystemLogger {

    public static void log(String message){
        System.out.println(message);
    }

    public static void log(String prefix, String message){
        System.out.println("[" + prefix + "] " + message);
    }

}
