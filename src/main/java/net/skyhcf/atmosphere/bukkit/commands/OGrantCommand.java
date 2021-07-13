package net.skyhcf.atmosphere.bukkit.commands;

import com.google.common.collect.Lists;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.grant.Grant;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.rank.Rank;
import net.skyhcf.atmosphere.shared.server.Server;
import net.skyhcf.atmosphere.shared.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class OGrantCommand {

    @Command(names = {"ogrant"}, permission = "console")
    public static void oGrant(CommandSender sender, @Param(name = "target")Profile target, @Param(name = "rank")Rank rank, @Param(name = "duration") String time, @Param(name = "scope")String scope, @Param(name = "reason") String reason){
        if(TimeUtil.parseTime(time) <= 0L){
            sender.sendMessage(BukkitChat.format("&cInvalid time."));
            return;
        }
        AtmosphereShared.getInstance().getGrantManager().createGrant(
                new Grant(
                        UUID.randomUUID(),
                        rank,
                        target.getUuid(),
                        (sender.getName().equalsIgnoreCase("CONSOLE") ? Profile.getConsoleUUID() : ((Player) sender).getUniqueId()),
                        null,
                        AtmosphereShared.getInstance().getServerManager().getServer(Bukkit.getServer().getServerName()),
                        System.currentTimeMillis(),
                        (time.equals("perm") || time.equals("permanent") ? Long.MAX_VALUE : TimeUtil.parseTime(time)),
                        0L,
                        reason,
                        null,
                        (scope.equals("global") ? Lists.newArrayList() : Lists.newArrayList(scope))
                )
        );
        sender.sendMessage(BukkitChat.format("&aSuccessfully granted " + rank.getColor() + rank.getDisplayName() + "&r &ato &r" + target.getHighestGrantOnScope(AtmosphereShared.getInstance().getServerManager().getServer(Bukkit.getServerName())).getRank().getColor() + target.getUsername() + "&r &afor a period of &e" + (time.equals("perm") || time.equals("permanent") ? "permanent" : TimeUtil.formatDuration(TimeUtil.parseTime(time))) + " &ron scope &e" + scope.toLowerCase() + "&r&a."));
        Bukkit.getServer().getPlayer(target.getUuid()).sendMessage(BukkitChat.format("&aYou have been granted the &r" + rank.getColor() + rank.getDisplayName() + "&r &arank for a period of " + (time.equals("perm") || time.equals("permanent") ? "permanent" : TimeUtil.formatDuration(TimeUtil.parseTime(time))) + "&a."));
    }

}
