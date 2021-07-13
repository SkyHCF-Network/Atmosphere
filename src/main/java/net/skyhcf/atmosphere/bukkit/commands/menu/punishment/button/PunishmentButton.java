package net.skyhcf.atmosphere.bukkit.commands.menu.punishment.button;

import com.google.common.collect.Lists;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.util.TimeUtils;
import net.skyhcf.atmosphere.bukkit.utils.ColorUtil;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.SharedAPI;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.punishment.Punishment;
import net.skyhcf.atmosphere.shared.server.Server;
import net.skyhcf.atmosphere.shared.utils.TimeUtil;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.List;

public class PunishmentButton extends Button {

    private final Profile profile;
    private final Punishment punishment;
    private final Server server;

    public PunishmentButton(Profile profile, Punishment punishment, Server server){
        this.profile = profile;
        this.punishment = punishment;
        this.server = server;
    }

    @Override
    public String getName(Player player) {
        return BukkitChat.format("&6" + new Date(punishment.getAddedAt()));
    }

    @Override
    public List<String> getDescription(Player player) {
        Profile viewer = AtmosphereShared.getInstance().getProfileManager().getProfile(player.getUniqueId());
        List<String> description = Lists.newArrayList();
        description.add("&7&m" + BukkitChat.LINE);
        description.add("&eAdded By&7: &r" + SharedAPI.formatNameOnScope(punishment.getAddedBy(), server));
        description.add("&eAdded Reason&7: &c" + punishment.getReason());
        if(punishment.getInternalReason() != null && viewer.hasPermission("atmosphere.head-staff")){
            description.add("&eInternal Reason&7: &c" + punishment.getInternalReason());
        }
        if(punishment.getDuration() != Long.MAX_VALUE && punishment.isActive()){
            description.add("&eTime Remaining&7: &c" + TimeUtil.formatDuration(punishment.getRemainingTime()));
        }
        description.add("&eAdded Server&7: &c" + punishment.getAddedServer().getName());
        description.add("&7&m" + BukkitChat.LINE);
        if(!punishment.isActive()){
            description.add("&c&lRemoved By &r" + SharedAPI.formatNameOnScope(punishment.getRemovedBy(), server) + "&r&7:");
            description.add("&cThe punishment was removed for&7: &f" + punishment.getRemovedReason());
            description.add("&cat &6" + TimeUtils.formatIntoCalendarString(new Date(punishment.getRemovedAt())));
            if(punishment.getDuration() != Long.MAX_VALUE){
                description.add("&cInitial Duration&7: &6" + TimeUtil.formatDuration(punishment.getDuration()));
            }
            description.add("&7&m" + BukkitChat.LINE);
        }
        return ColorUtil.formatList(description);
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.WOOL;
    }

    @Override
    public byte getDamageValue(Player player) {
        return (punishment.isActive() ? DyeColor.GREEN.getWoolData() : DyeColor.RED.getWoolData());
    }
}
