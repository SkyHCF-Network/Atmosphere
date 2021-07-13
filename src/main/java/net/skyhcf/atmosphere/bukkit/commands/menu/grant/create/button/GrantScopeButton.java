package net.skyhcf.atmosphere.bukkit.commands.menu.grant.create.button;

import com.google.common.collect.Lists;
import net.frozenorb.qlib.menu.Button;
import net.skyhcf.atmosphere.bukkit.utils.ColorUtil;
import net.skyhcf.atmosphere.bukkit.utils.grant.GrantSession;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.server.Server;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class GrantScopeButton extends Button {

    private final GrantSession grantSession;
    private final Server scope;

    public GrantScopeButton(GrantSession grantSession, Server scope){
        this.grantSession = grantSession;
        this.scope = scope;
    }

    @Override
    public String getName(Player player) {
        return BukkitChat.format((grantSession.getScopes().contains(scope.getName()) ? "&a" + scope.getName() : "&c" + scope.getName()));
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = Lists.newArrayList();
        description.add("&7&m" + BukkitChat.LINE);
        description.add("&9Left Click to " + (grantSession.getScopes().contains(scope.getName()) ? "&cremove" : "&aadd") + "&r " + scope.getName() + " &r&9to the scope selection.");
        description.add("&7&m" + BukkitChat.LINE);
        description = ColorUtil.formatList(description);
        return description;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.WOOL;
    }

    @Override
    public byte getDamageValue(Player player) {
        return (grantSession.getScopes().contains(scope.getName()) ? DyeColor.GREEN.getWoolData() : DyeColor.RED.getWoolData());
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if(grantSession.getScopes().contains(scope.getName())){
            grantSession.getScopes().remove(scope.getName());
        }else{
            grantSession.getScopes().add(scope.getName());
        }
    }
}
