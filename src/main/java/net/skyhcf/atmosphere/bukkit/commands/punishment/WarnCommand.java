package net.skyhcf.atmosphere.bukkit.commands.punishment;

import com.google.common.collect.Lists;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Flag;
import net.frozenorb.qlib.command.Param;
import net.skyhcf.atmosphere.bukkit.AtmosphereBukkit;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.punishment.Punishment;
import net.skyhcf.atmosphere.shared.punishment.PunishmentType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class WarnCommand {

    @Command(names = {"warn"}, permission = "atmosphere.punishments.warn")
    public static void kick(CommandSender sender, @Param(name = "target") Profile target, @Flag(value = {"p", "public"}, defaultValue = true) boolean silent, @Param(name = "reason", wildcard = true) String reason){
        AtmosphereShared.getInstance().getPunishmentManager().createPunishment(new Punishment(
                UUID.randomUUID(),
                target.getUuid(),
                (sender instanceof Player ? ((Player) sender).getUniqueId() : Profile.getConsoleUUID()),
                null,
                PunishmentType.WARN,
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
