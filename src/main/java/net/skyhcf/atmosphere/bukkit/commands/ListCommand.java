package net.skyhcf.atmosphere.bukkit.commands;

import com.google.common.collect.Lists;
import net.frozenorb.qlib.command.Command;
import net.skyhcf.atmosphere.bukkit.AtmosphereBukkit;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.SharedAPI;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

public class ListCommand {

    @Command(names = {"list"}, permission = "")
    public static void list(Player player){
        List<Rank> ranks = AtmosphereShared.getInstance().getRankManager().getRanksSorted();
        player.sendMessage(BukkitChat.format(getHeader()));
        List<Profile> profiles = Lists.newArrayList();
        for(Player target : Bukkit.getServer().getOnlinePlayers()){
            Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(target.getUniqueId());
            if(player.canSee(target)) {
                profiles.add(profile);
            }
        }
/*        for(Profile profile : AtmosphereShared.getInstance().getProfileManager().getProfiles()){
            if(profile.getCurrentServer() != null && profile.getCurrentServer().equals(AtmosphereBukkit.getInstance().getSkyServer().getName())){
                profiles.add(profile);
            }
        }*/
        profiles.sort((o1, o2) -> o2.getHighestGrantOnScope(AtmosphereShared.getInstance().getServerManager().getServer(Bukkit.getServerName())).getRank().getPriority() - o1.getHighestGrantOnScope(AtmosphereShared.getInstance().getServerManager().getServer(Bukkit.getServerName())).getRank().getPriority());
        List<String> playerList = Lists.newArrayList();
        for(Profile profile : profiles){
            Player target = Bukkit.getServer().getPlayer(profile.getUuid());
            playerList.add(BukkitChat.format((target.hasMetadata("ModMode") || target.hasMetadata("modmode") ? "&7*" : "") + SharedAPI.formatNameOnScope(profile.getUuid(), AtmosphereShared.getInstance().getServerManager().getServer(Bukkit.getServerName()))) + BukkitChat.RESET);
        }
        player.sendMessage("(" + profiles.size() + "/" + Bukkit.getServer().getMaxPlayers() + ") " + playerList);
    }

    private static String getHeader() {
        StringBuilder builder = new StringBuilder();
        for (Rank rank : AtmosphereShared.getInstance().getRankManager().getRanksSorted()) {
            boolean displayed = rank.getPriority() > 0;
            if (displayed) {
                builder.append(rank.getColor() + rank.getDisplayName()).append(BukkitChat.RESET).append(", ");
            }
        }
        if (builder.length() > 2) {
            builder.setLength(builder.length() - 2);
        }
        return builder.toString();
    }

}
