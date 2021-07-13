package net.skyhcf.atmosphere.bukkit.commands.parameter;

import com.google.common.collect.Lists;
import net.frozenorb.qlib.command.ParameterType;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class ProfileParameterType implements ParameterType {

    @Override
    public Object transform(CommandSender commandSender, String s) {
        for(Profile profile : AtmosphereShared.getInstance().getProfileManager().getProfiles()){
            if(profile.getUsername().equalsIgnoreCase(s)) {
                return profile;
            }
        }
        commandSender.sendMessage(BukkitChat.format("&cNo profile with name \"" + s + "\" found."));
        return null;
    }

    @Override
    public List<String> tabComplete(Player player, Set set, String s) {
        List<String> tabCompletions = Lists.newArrayList();
        for(Profile profile : AtmosphereShared.getInstance().getProfileManager().getProfiles()){
            if(Bukkit.getServer().getPlayer(profile.getUuid()) != null) tabCompletions.add(profile.getUsername());
        }
        return tabCompletions;
    }
}
