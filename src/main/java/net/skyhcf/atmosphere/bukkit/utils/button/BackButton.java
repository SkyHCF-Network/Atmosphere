package net.skyhcf.atmosphere.bukkit.utils.button;

import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import net.skyhcf.atmosphere.bukkit.AtmosphereBukkit;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class BackButton extends Button {

    private final Menu menu;

    public BackButton(Menu menu){
        this.menu = menu;
    }


    @Override
    public String getName(Player player) {
        return BukkitChat.ORANGE + "Go Back";
    }

    @Override
    public List<String> getDescription(Player player) {
        return null;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.BED;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        AtmosphereBukkit.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(AtmosphereBukkit.getInstance(), () -> {
            if(menu == null){
                player.closeInventory();
            }else{
                menu.openMenu(player);
            }
        }, 1L);

    }

}
