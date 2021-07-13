package net.skyhcf.atmosphere.bukkit.commands;

import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.skyhcf.atmosphere.shared.SharedAPI;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ProfileCommands {

    @Command(names = {"profile info"}, permission = "atmosphere.profile.admin")
    public static void profileInfo(CommandSender sender, @Param(name = "target")Profile target){
        sender.sendMessage(BukkitChat.format("&7&m" + BukkitChat.LINE));
        sender.sendMessage(BukkitChat.format("&9Username&7: &r" + target.getDisplayNameOnScope(SharedAPI.getServer(Bukkit.getServerName())) + "&r (" + target.getUsername() + ")"));
        sender.sendMessage(BukkitChat.format("&9Global Playtime&7: &c" + TimeUtil.formatDuration(System.currentTimeMillis() - target.getFirstJoined())));
        sender.sendMessage(BukkitChat.format("&9Permissions&7: &r") + target.getPermissions().toString());
        if(target.getCurrentServer() == null){
            sender.sendMessage(BukkitChat.format("&9Target was last seen &c" + TimeUtil.formatDuration(System.currentTimeMillis() - target.getLastSeen()) + "&r &9ago."));
        }else{
            sender.sendMessage(BukkitChat.format("&9Target is currently &aonline &9at &c" + target.getCurrentServer() + "&r&9. &7&o(Online for " + TimeUtil.formatDuration(System.currentTimeMillis() - target.getLastSeen()) + ")"));
        }
        sender.sendMessage(BukkitChat.format("&7&m" + BukkitChat.LINE));
    }

    @Command(names = {"profile addpermission"}, permission = "atmosphere.profile.admin")
    public static void profileAddPermission(CommandSender sender, @Param(name = "target") Profile target, @Param(name = "permission") String permission){
        if(target.hasPermissionPriorToLogin(permission.toLowerCase())){
            sender.sendMessage(BukkitChat.format("&cTarget already has access to the specified permission."));
            return;
        }
        sender.sendMessage(BukkitChat.format("&aAdded the permission &r" + permission.toLowerCase() + "&r &ato &r" + target.getDisplayName() + "&r&a's profile."));
        target.getPermissions().add(permission.toLowerCase());
        target.save();
    }

    @Command(names = {"profile removepermission"}, permission = "atmosphere.profile.admin")
    public static void profileRemovePermission(CommandSender sender, @Param(name = "target") Profile target, @Param(name = "permission") String permission){
        if(!target.hasPermissionPriorToLogin(permission.toLowerCase())){
            sender.sendMessage(BukkitChat.format("&cTarget does not have access to the specified permission."));
            return;
        }
        sender.sendMessage(BukkitChat.format("&aRemoved the permission &r" + permission.toLowerCase() + "&r &afrom &r" + target.getDisplayName() + "&r&a's profile."));
        target.getPermissions().remove(permission.toLowerCase());
        target.save();
    }



}
