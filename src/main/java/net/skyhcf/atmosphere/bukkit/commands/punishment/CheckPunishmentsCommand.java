package net.skyhcf.atmosphere.bukkit.commands.punishment;

import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.skyhcf.atmosphere.bukkit.commands.menu.punishment.MainPunishmentsListMenu;
import net.skyhcf.atmosphere.shared.profile.Profile;
import org.bukkit.entity.Player;

public class CheckPunishmentsCommand {

    @Command(names = {"checkpunishments", "c", "history", "cp", "hist"}, permission = "atmosphere.staff")
    public static void checkPunishments(Player player, @Param(name = "target")Profile target){
        new MainPunishmentsListMenu(target).openMenu(player);
    }

}
