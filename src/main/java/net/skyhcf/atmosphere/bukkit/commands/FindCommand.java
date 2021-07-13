package net.skyhcf.atmosphere.bukkit.commands;

import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.skyhcf.atmosphere.shared.SharedAPI;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class FindCommand {

    @Command(names = {"find", "locate"}, permission = "atmosphere.staff")
    public static void find(CommandSender sender, @Param(name = "profile")Profile target){
        if(target.getCurrentServer() == null){
            sender.sendMessage(BukkitChat.format("&cNo profile with name \"" + target.getUsername() + "\" was found on this proxy."));
            return;
        }
        sender.sendMessage(BukkitChat.format(SharedAPI.formatNameOnScope(target.getUuid(), SharedAPI.getServer(Bukkit.getServerName())) + "&r &eis currently &aonline &r&eat &a" + target.getCurrentServer() + " (CA-Proxy-01)&r&e."));
    }

}
