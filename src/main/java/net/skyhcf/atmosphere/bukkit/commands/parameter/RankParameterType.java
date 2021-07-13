package net.skyhcf.atmosphere.bukkit.commands.parameter;

import com.google.common.collect.Lists;
import net.frozenorb.qlib.command.ParameterType;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.rank.Rank;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class RankParameterType implements ParameterType {

    @Override
    public Object transform(CommandSender commandSender, String s) {
        for(Rank rank : AtmosphereShared.getInstance().getRankManager().getRanks()){
            if(rank.getId().equalsIgnoreCase(s)) return rank;
        }
        commandSender.sendMessage(BukkitChat.format("&cThe rank named \"" + s + "\" was not found."));
        return null;
    }

    @Override
    public List<String> tabComplete(Player player, Set set, String s) {
        List<Rank> ranks = AtmosphereShared.getInstance().getRankManager().getRanks();
        List<String> tabCompletions = Lists.newArrayList();
        ranks.sort((o1, o2) -> o2.getPriority() - o1.getPriority());
        for(Rank rank : AtmosphereShared.getInstance().getRankManager().getRanks()){
            tabCompletions.add(rank.getId());
        }
        return tabCompletions;
    }
}
