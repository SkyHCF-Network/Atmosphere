package net.skyhcf.atmosphere.bukkit.commands;

import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.skyhcf.atmosphere.bukkit.commands.menu.prefix.PrefixMenu;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.prefix.Prefix;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrefixCommands {

    @Command(names = {"prefix create"}, permission = "atmosphere.prefixes.admin")
    public static void prefixCreate(CommandSender sender, @Param(name = "prefix") String prefixId){
        if(AtmosphereShared.getInstance().getPrefixManager().prefixExists(prefixId)){
            Prefix prefix = AtmosphereShared.getInstance().getPrefixManager().getPrefix(prefixId);
            sender.sendMessage(BukkitChat.format("&cError: The prefix &r" + prefix.getColor() + prefix.getName() + "&r &calready exists."));
            return;
        }
        AtmosphereShared.getInstance().getPrefixManager().createPrefix(prefixId, "&r", "&7[&r" + prefixId + "&7]&r");
        sender.sendMessage(BukkitChat.format("&aYou've created the prefix &r" + prefixId + "&r."));
    }

    @Command(names = {"prefix delete"}, permission = "atmosphere.prefixes.admin")
    public static void prefixDelete(CommandSender sender, @Param(name = "prefix") Prefix prefix){
        AtmosphereShared.getInstance().getPrefixManager().deletePrefix(prefix);
    }

    @Command(names = {"prefix setcolor"}, permission = "atmosphere.prefixes.admin")
    public static void prefixSetColor(CommandSender sender, @Param(name = "prefix") Prefix prefix, @Param(name = "color") String color){
        prefix.setColor(color);
        prefix.save();
        sender.sendMessage(BukkitChat.format("&aUpdated color of &r" + prefix.getColor() + prefix.getName() + "&r &ato &r" + prefix.getColor()) + prefix.getColor() + BukkitChat.format("&r&a."));
    }

    @Command(names = {"prefix setprefix"}, permission = "atmosphere.prefixes.admin")
    public static void prefixSetPrefix(CommandSender sender, @Param(name = "prefix") Prefix prefix, @Param(name = "prefix", wildcard = true) String color){
        prefix.setPrefix(color);
        prefix.save();
        sender.sendMessage(BukkitChat.format("&aUpdated prefix of &r" + prefix.getColor() + prefix.getName() + "&r &ato &r" + prefix.getPrefix() + BukkitChat.format("&r&a.")));
    }

    @Command(names = {"prefix setdisplayname"}, permission = "atmosphere.prefixes.admin")
    public static void prefixSetDisplayName(CommandSender sender, @Param(name = "prefix") Prefix prefix, @Param(name = "display name", wildcard = true) String displayName){
        String before = prefix.getName();
        prefix.setName(displayName);
        prefix.save();
        sender.sendMessage(BukkitChat.format("&aUpdated display name of " + prefix.getColor() + before + "&r &ato &r" + displayName + "&r&a."));
    }

    @Command(names = {"prefix"}, permission = "")
    public static void prefix(Player player){
        new PrefixMenu().openMenu(player);
    }
}
