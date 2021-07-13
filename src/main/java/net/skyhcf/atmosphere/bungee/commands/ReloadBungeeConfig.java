package net.skyhcf.atmosphere.bungee.commands;

import lombok.SneakyThrows;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.skyhcf.atmosphere.bungee.AtmosphereBungee;
import net.skyhcf.atmosphere.shared.chat.BungeeChat;

import java.io.File;

public class ReloadBungeeConfig extends Command {


    public ReloadBungeeConfig() {
        super("reloadbungeeconfig", "atmosphere.admin");
    }

    @Override
    @SneakyThrows
    public void execute(CommandSender commandSender, String[] strings) {
        AtmosphereBungee.getInstance().setConfiguration(AtmosphereBungee.getInstance().getConfig().load(new File(AtmosphereBungee.getInstance().getDataFolder(), "config.yml")));
        commandSender.sendMessage(BungeeChat.format("&aReloaded the Atmosphere BungeeCord Proxy configuration."));
    }
}
