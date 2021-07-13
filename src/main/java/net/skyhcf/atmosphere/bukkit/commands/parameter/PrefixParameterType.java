package net.skyhcf.atmosphere.bukkit.commands.parameter;

import com.google.common.collect.Lists;
import net.frozenorb.qlib.command.ParameterType;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.prefix.Prefix;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class PrefixParameterType implements ParameterType {

    @Override
    public Object transform(CommandSender commandSender, String s) {
        for(Prefix prefix : AtmosphereShared.getInstance().getPrefixManager().getPrefixes()){
            if(prefix.getId().equalsIgnoreCase(s)){
                return prefix;
            }
        }
        commandSender.sendMessage(BukkitChat.format("&cNo prefix with name \"" + s + "\" found."));
        return null;
    }

    @Override
    public List<String> tabComplete(Player player, Set set, String s) {
        List<String> tabCompletions = Lists.newArrayList();
        for(Prefix prefix : AtmosphereShared.getInstance().getPrefixManager().getPrefixes()){
            if(!tabCompletions.contains(prefix.getId())){
                tabCompletions.add(prefix.getId());
            }
        }
        return tabCompletions;
    }
}
