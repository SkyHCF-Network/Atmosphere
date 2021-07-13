/*
package net.skyhcf.atmosphere.bukkit.commands;

import lombok.SneakyThrows;
import net.frozenorb.qlib.command.Command;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class UndisguiseCommand {

    @SneakyThrows
    @Command(names = {"undisguise", "und", "undis"}, permission = "atmosphere.disguise")
    public static void undisguise(Player player){
        DisguiseCommand.setSkin(((CraftPlayer)player).getProfile(), player.getUniqueId());
        GameProfile profile = ((CraftPlayer) player).getProfile();
        Field field = profile.getClass().getDeclaredField("name");
        field.setAccessible(true);
        field.set(profile, player.getName());
    }
}
*/
