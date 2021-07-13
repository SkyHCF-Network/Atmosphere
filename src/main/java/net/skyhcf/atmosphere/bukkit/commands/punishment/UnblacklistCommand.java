package net.skyhcf.atmosphere.bukkit.commands.punishment;

import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Flag;
import net.frozenorb.qlib.command.Param;
import net.skyhcf.atmosphere.bukkit.AtmosphereBukkit;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.SharedAPI;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.packet.PunishmentRemovePacket;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.punishment.Punishment;
import net.skyhcf.atmosphere.shared.punishment.PunishmentType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnblacklistCommand {

    @Command(names = {"unblacklist"}, permission = "atmosphere.punishments.unblacklist")
    public static void unban(CommandSender sender, @Param(name = "target") Profile target, @Flag(value = {"p", "public"}, defaultValue = true) boolean silent, @Param(name = "reason", wildcard = true) String reason){
        if(!target.hasActivePunishment(PunishmentType.BLACKLIST)){
            sender.sendMessage(BukkitChat.format(SharedAPI.formatNameOnScope(target.getUuid(), AtmosphereShared.getInstance().getServerManager().getServer(AtmosphereBukkit.getInstance().getServer().getServerName())) + "&r &cdoes not have an active punishment."));
            return;
        }
        Punishment punishment = target.getActivePunishments(PunishmentType.BLACKLIST).get(0);
        punishment.setRemovedAt(System.currentTimeMillis());
        punishment.setRemovedBy(((sender instanceof Player) ? ((Player) sender).getUniqueId() : Profile.getConsoleUUID()));
        punishment.setRemovedReason(reason);
        punishment.save();
        AtmosphereShared.getInstance().getRedisHandler().sendPacket(new PunishmentRemovePacket(punishment, silent));
    }

}
