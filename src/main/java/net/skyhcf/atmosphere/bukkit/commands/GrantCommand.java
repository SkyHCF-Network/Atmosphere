package net.skyhcf.atmosphere.bukkit.commands;

import com.google.common.collect.Lists;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.skyhcf.atmosphere.bukkit.AtmosphereBukkit;
import net.skyhcf.atmosphere.bukkit.commands.menu.grant.create.GrantMenu;
import net.skyhcf.atmosphere.bukkit.utils.grant.GrantSession;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.profile.Profile;
import org.bukkit.entity.Player;

public class GrantCommand {

    @Command(names = {"grant"}, permission = "atmosphere.grant.admin")
    public static void grant(Player player, @Param(name = "target")Profile profile){
        new GrantMenu(profile, new GrantSession(profile, AtmosphereShared.getInstance().getRankManager().getRank("Default"), 0L, "", Lists.newArrayList(), player.getUniqueId(), AtmosphereShared.getInstance().getServerManager().getServer(AtmosphereBukkit.getInstance().getServer().getServerName()))).openMenu(player);
    }

}
