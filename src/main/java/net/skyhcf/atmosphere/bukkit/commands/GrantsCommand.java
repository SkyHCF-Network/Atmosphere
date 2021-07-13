package net.skyhcf.atmosphere.bukkit.commands;

import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.skyhcf.atmosphere.bukkit.commands.menu.grant.list.GrantsMenu;
import net.skyhcf.atmosphere.shared.profile.Profile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GrantsCommand {

    @Command(names = {"grants"}, permission = "atmosphere.grant.admin")
    public static void grants(CommandSender sender, @Param(name = "profile")Profile profile){
        new GrantsMenu(profile, profile.getGrants()).openMenu((Player) sender);
    }

}
