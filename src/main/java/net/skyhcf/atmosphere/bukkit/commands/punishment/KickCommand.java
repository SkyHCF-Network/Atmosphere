package net.skyhcf.atmosphere.bukkit.commands.punishment;

import com.google.common.collect.Lists;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Flag;
import net.frozenorb.qlib.command.Param;
import net.skyhcf.atmosphere.bukkit.AtmosphereBukkit;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.SharedAPI;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.punishment.Punishment;
import net.skyhcf.atmosphere.shared.punishment.PunishmentType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class KickCommand {

    @Command(names = {"kick"}, permission = "atmosphere.punishments.kick")
    public static void kick(CommandSender sender, @Param(name = "target")Profile target, @Flag(value = {"p", "public"}, defaultValue = true) boolean silent, @Param(name = "reason", wildcard = true) String reason){
        if(sender instanceof Player){
            if(!AtmosphereShared.getInstance().getServerManager().getServer(AtmosphereBukkit.getInstance().getServer().getServerName()).getId().equalsIgnoreCase(target.getCurrentServer())){
                sender.sendMessage(BukkitChat.format(SharedAPI.formatNameOnScope(target.getUuid(), AtmosphereShared.getInstance().getServerManager().getServer(AtmosphereBukkit.getInstance().getServer().getServerName()))));
                return;
            }
        }
        AtmosphereShared.getInstance().getPunishmentManager().createPunishment(new Punishment(
                UUID.randomUUID(),
                target.getUuid(),
                (sender instanceof Player ? ((Player) sender).getUniqueId() : Profile.getConsoleUUID()),
                null,
                PunishmentType.KICK,
                AtmosphereShared.getInstance().getServerManager().getServer(AtmosphereBukkit.getInstance().getServer().getServerName()),
                System.currentTimeMillis(),
                Long.MAX_VALUE,
                0L,
                reason,
                null,
                null,
                Lists.newArrayList()
        ), silent);
    }

}
