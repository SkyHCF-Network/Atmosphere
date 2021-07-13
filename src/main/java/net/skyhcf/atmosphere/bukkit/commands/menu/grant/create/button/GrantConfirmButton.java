package net.skyhcf.atmosphere.bukkit.commands.menu.grant.create.button;

import net.frozenorb.qlib.menu.Button;
import net.skyhcf.atmosphere.bukkit.utils.grant.GrantSession;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.grant.Grant;
import net.skyhcf.atmosphere.shared.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class GrantConfirmButton extends Button {

    private final GrantSession grantSession;

    public GrantConfirmButton(GrantSession grantSession){
        this.grantSession = grantSession;
    }

    @Override
    public String getName(Player player) {
        return BukkitChat.format("&aConfirm Grant");
    }

    @Override
    public List<String> getDescription(Player player) {
        return null;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.DIAMOND_SWORD;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        player.closeInventory();
        player.sendMessage(BukkitChat.format("&aYou've granted &r" + grantSession.getProfile().getHighestGrant().getRank().getColor() + grantSession.getProfile().getUsername() + "&r &athe " + grantSession.getRank().getColor() + grantSession.getRank().getDisplayName() + "&r &arank for a period of " + (grantSession.getDuration() == Long.MAX_VALUE ? "permanent" : TimeUtil.formatDuration(grantSession.getDuration())) + "&r &aon scopes &e" + (grantSession.getScopes().size() == 0 ? "global" : grantSession.getScopes().toString().replace("[", "").replace("]", "").replace(",", "&a,&e") + "&r&a.")));
        Bukkit.getServer().getPlayer(grantSession.getProfile().getUuid()).sendMessage(BukkitChat.format("&aYou have been granted the &r" + grantSession.getRank().getColor() + grantSession.getRank().getDisplayName() + "&r &arank for a period of " + (grantSession.getDuration() == Long.MAX_VALUE ? "permanent" : TimeUtil.formatDuration(grantSession.getDuration())) + "&a."));
        AtmosphereShared.getInstance().getGrantManager().createGrant(
                new Grant(
                        UUID.randomUUID(),
                        grantSession.getRank(),
                        grantSession.getProfile().getUuid(),
                        grantSession.getAddedBy(),
                        null,
                        grantSession.getAddedServer(),
                        System.currentTimeMillis(),
                        grantSession.getDuration(),
                        0L,
                        grantSession.getReason(),
                        null,
                        grantSession.getScopes()
                        ));
    }
}
