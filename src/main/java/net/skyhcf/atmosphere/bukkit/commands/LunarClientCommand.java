package net.skyhcf.atmosphere.bukkit.commands;

import com.lunarclient.bukkitapi.LunarClientAPI;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.skyhcf.atmosphere.bukkit.AtmosphereBukkit;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.SharedAPI;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.server.Server;
import org.bukkit.entity.Player;

public class LunarClientCommand {

    @Command(names = {"lunar", "lc"}, permission = "")
    public static void lunar(Player player, @Param(name = "target")Player target){
        Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(target.getUniqueId());
        boolean runningLC = LunarClientAPI.getInstance().isRunningLunarClient(target);
        Server server = AtmosphereShared.getInstance().getServerManager().getServer(AtmosphereBukkit.getInstance().getServer().getServerName());
        player.sendMessage(BukkitChat.format(SharedAPI.formatNameOnScope(profile.getUuid(), server) + (runningLC ? " &r&ais currently running Lunar Client." : " &r&c is currently NOT running Lunar Client.")));
    }

}
