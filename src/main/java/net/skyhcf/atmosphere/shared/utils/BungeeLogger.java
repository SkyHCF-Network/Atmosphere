package net.skyhcf.atmosphere.shared.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.skyhcf.atmosphere.bungee.AtmosphereBungee;

public class BungeeLogger {

    public static void log(String prefix, String message){
        AtmosphereBungee.getInstance().getProxy().getConsole().sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "[" + prefix + "] " + message)));
    }

}
