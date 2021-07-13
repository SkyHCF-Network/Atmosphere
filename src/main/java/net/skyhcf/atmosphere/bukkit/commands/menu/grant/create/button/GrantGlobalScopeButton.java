package net.skyhcf.atmosphere.bukkit.commands.menu.grant.create.button;

import net.frozenorb.qlib.menu.Button;
import net.skyhcf.atmosphere.bukkit.utils.grant.GrantSession;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.grant.Grant;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class GrantGlobalScopeButton extends Button {

    private final GrantSession grantSession;

    public GrantGlobalScopeButton(GrantSession grantSession){
        this.grantSession = grantSession;
    }

    @Override
    public String getName(Player player) {
        return BukkitChat.format("&aClick to make this grant global");
    }

    @Override
    public List<String> getDescription(Player player) {
        return null;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.BEACON;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        grantSession.getScopes().clear();
    }
}
