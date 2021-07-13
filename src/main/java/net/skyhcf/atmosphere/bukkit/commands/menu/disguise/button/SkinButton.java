/*
package net.skyhcf.atmosphere.bukkit.commands.menu.disguise.button;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import net.frozenorb.qlib.menu.Button;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.skyhcf.atmosphere.bukkit.commands.DisguiseCommand;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.List;

public class SkinButton extends Button {

    private String username;

    public SkinButton(String username){

    }

    @Override
    public String getName(Player player) {
        return "";
    }

    @Override
    public List<String> getDescription(Player player) {
        return Lists.newArrayList();
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.SKULL;
    }

    @Override
    @SneakyThrows
    public void clicked(Player player, int slot, ClickType clickType) {
        GameProfile profile = ((CraftPlayer) player).getProfile();
        Field field = profile.getClass().getDeclaredField("name");
        field.setAccessible(true);
        field.set(profile, username);
        player.closeInventory();
        player.sendMessage(BukkitChat.format("&aSuccess! You now look like &e" + username + "&r&a."));
    }
}
*/
