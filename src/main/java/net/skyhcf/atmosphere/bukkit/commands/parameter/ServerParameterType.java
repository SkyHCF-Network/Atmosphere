package net.skyhcf.atmosphere.bukkit.commands.parameter;

import com.google.common.collect.Lists;
import net.frozenorb.qlib.command.ParameterType;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.server.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class ServerParameterType implements ParameterType {

    @Override
    public Object transform(CommandSender commandSender, String s) {
        for(Server server : AtmosphereShared.getInstance().getServerManager().getServers()){
            if(server.getId().equalsIgnoreCase(s)) return server;
        }
        commandSender.sendMessage(BukkitChat.format("&cNo server with name \"" + s + "\" found."));
        return null;
    }

    @Override
    public List<String> tabComplete(Player player, Set set, String s) {
        List<String> tabCompletions = Lists.newArrayList();
        for(Server server : AtmosphereShared.getInstance().getServerManager().getServers()){
            if(!tabCompletions.contains(server.getName())) tabCompletions.add(server.getName());
        }
        return tabCompletions;
    }
}
