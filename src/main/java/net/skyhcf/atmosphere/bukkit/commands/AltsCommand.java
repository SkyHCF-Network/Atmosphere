package net.skyhcf.atmosphere.bukkit.commands;

import com.google.common.collect.Lists;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.skyhcf.atmosphere.bukkit.AtmosphereBukkit;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.profile.Profile;
import org.bukkit.command.CommandSender;

import java.util.List;

public class AltsCommand {

    @Command(names = {"alts"}, permission = "atmosphere.alts.admin")
    public static void alts(CommandSender sender, @Param(name = "target")Profile target){
        List<Profile> alts = Lists.newArrayList();
        List<String> altsStringList = Lists.newArrayList();
        alts.addAll(target.getAlts());
        //AtmosphereShared.getInstance().getProfileManager().getProfiles().stream().filter(profile -> target.getIp().equalsIgnoreCase(profile.getIp())).forEach(alts::add);
        for(Profile profile : alts) {
            altsStringList.add(profile.getHighestGrantOnScope(AtmosphereShared.getInstance().getServerManager().getServer(AtmosphereBukkit.getInstance().getServer().getServerName())).getRank().getColor() + profile.getUsername() + "&r");
        }
        sender.sendMessage(BukkitChat.format("&6Alts for &r" + target.getHighestGrantOnScope(AtmosphereShared.getInstance().getServerManager().getServer(AtmosphereBukkit.getInstance().getServer().getServerName())).getRank().getColor() + target.getUsername() + "&r&6:"));
        sender.sendMessage(BukkitChat.format(altsStringList.toString().replace("[", "").replace("]", "")));
    }

}
