package net.skyhcf.atmosphere.bukkit.commands;

import lombok.SneakyThrows;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.com.mojang.authlib.properties.Property;
import net.skyhcf.atmosphere.bukkit.AtmosphereBukkit;
import net.skyhcf.atmosphere.bukkit.utils.disguise.DisguiseUtil;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class DisguiseCommand {

    @Command(names = {"disguise", "d", "dis"}, permission = "atmosphere.disguise")
    @SneakyThrows
    public static void disguise(Player player, @Param(name = "target") String username){
        if(player.isDisguised()){
            player.sendMessage(BukkitChat.format("&cYou are already disguised!"));
            return;
        }
        /*Field nameField = cp.getClass().getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(cp, username);
        nameField.setAccessible(false);*/

        DisguiseUtil.setSkin(player, username);
        player.sendMessage(BukkitChat.format("&aYou are now disguised as &7" + username + "&r&a."));
    }

    @Command(names = {"undisguise"}, permission = "atmosphere.undisguise")
    public static void undisguise(Player player){
        if(!player.isDisguised()) {
            player.sendMessage(BukkitChat.format("&cYou are not disguised."));
            return;
        }
        player.undisguise();
        DisguiseUtil.setSkin(player, player.getName());
        player.sendMessage(BukkitChat.format("&aYou are no longer disguised."));
    }

}
