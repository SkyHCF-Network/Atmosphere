package net.skyhcf.atmosphere.bukkit.commands;

import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.skyhcf.atmosphere.bukkit.AtmosphereBukkit;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.packet.StaffChatPacket;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StaffChatCommand {

    @Command(names = {"staffchat", "sc"}, permission = "atmosphere.staff")
    public static void staffChat(Player player, @Param(name = "message", wildcard = true) String message){
        Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if(!profile.getHighestGrant().getRank().isStaff()){
            player.sendMessage(BukkitChat.format("&cYour rank must be a staff rank to use this command."));
            return;
        }
        AtmosphereShared.getInstance().getRedisHandler().sendPacket(new StaffChatPacket(
                AtmosphereShared.getInstance().getProfileManager().getProfile(player.getUniqueId()).getHighestGrant().getRank().getColor() + AtmosphereShared.getInstance().getProfileManager().getProfile(player.getUniqueId()).getUsername(),
                AtmosphereBukkit.getInstance().getServer().getServerName(),
                message
        ));
    }

}
