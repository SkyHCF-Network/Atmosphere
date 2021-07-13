package net.skyhcf.atmosphere.bukkit.commands;

import net.frozenorb.qlib.command.Command;
import net.skyhcf.atmosphere.shared.SharedAPI;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.profile.Profile;
import org.bukkit.entity.Player;

public class ToggleStaffChatCommand {

    @Command(names = {"togglestaffchat", "tsc", "togglesc"}, permission = "atmosphere.staff")
    public static void toggleStaffChat(Player player){
        Profile profile = SharedAPI.getProfile(player.getUniqueId());
        profile.setStaffChat(!profile.isStaffChat());
        profile.save();
        player.sendMessage(BukkitChat.format((profile.isStaffChat() ? "&aYou have toggled on staff chat." : "&cYou have toggled off staff chat.")));
    }

}
