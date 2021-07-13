package net.skyhcf.atmosphere.bukkit.commands;

import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.frozenorb.qlib.util.BungeeUtils;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import org.bukkit.entity.Player;

public class ServerCommand {

    @Command(names = {"server"}, permission = "atmosphere.staff")
    public static void server(Player player, @Param(name = "server") String server){
        player.sendMessage(BukkitChat.format("&6Connecting you to &f" + server + "&6..."));
        BungeeUtils.send(player, server);
    }

}
