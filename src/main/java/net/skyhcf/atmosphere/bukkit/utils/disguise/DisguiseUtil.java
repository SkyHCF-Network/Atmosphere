package net.skyhcf.atmosphere.bukkit.utils.disguise;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.scoreboard.FrozenScoreboardHandler;
import net.frozenorb.qlib.util.UUIDUtils;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.util.com.mojang.authlib.Agent;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.com.mojang.authlib.GameProfileRepository;
import net.minecraft.util.com.mojang.authlib.ProfileLookupCallback;
import net.minecraft.util.com.mojang.authlib.properties.Property;
import net.minecraft.util.com.mojang.util.UUIDTypeAdapter;
import net.skyhcf.atmosphere.bukkit.AtmosphereBukkit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.craftbukkit.libs.joptsimple.OptionSet;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class DisguiseUtil {

    @SneakyThrows
    public static UUID getUuid(String name){
        URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
        InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());
        String uuid = (new JsonParser()).parse(reader_0).getAsJsonObject().get("id").getAsString();
        return fromTrimmed(uuid.toString());
    }

    public static Property getProp(String skin) {
        try {
            URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + skin);
            InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());
            String uuid = (new JsonParser()).parse(reader_0).getAsJsonObject().get("id").getAsString();
            URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
            JsonObject textureProperty = (new JsonParser()).parse(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String texture = textureProperty.get("value").getAsString();
            String signature = textureProperty.get("signature").getAsString();
            return new Property("textures", texture, signature);
        } catch (IOException|IllegalStateException iOException) {
            return null;
        }
    }

    @SneakyThrows
    public static void setSkin(final Player player, String skin) {
        CraftPlayer cp = ((CraftPlayer) player);
        GameProfile gameProfile = cp.getProfile();
        Property property = getProp(skin);
        if(property != null){
            player.disguise(skin);
            ((CraftPlayer) player).disguisedProfile.getProperties().removeAll("textures");
            ((CraftPlayer) player).disguisedProfile.getProperties().put("textures", property);
            ((CraftPlayer) player).getProfile().getProperties().removeAll("textures");
            ((CraftPlayer) player).getProfile().getProperties().put("textures", property);
            for(Player viewer : Bukkit.getServer().getOnlinePlayers()){
                viewer.hidePlayer(player);
                viewer.showPlayer(player);
            }
        }
    }

    @SneakyThrows
    public static void undisguise(Player player){
        CraftPlayer cp = ((CraftPlayer) player);
        GameProfile gameProfile = cp.getProfile();
        Property property = getProp(player.getName());

        Field field = cp.getClass().getDeclaredField("disguisedName");
        field.setAccessible(true);
        field.set(cp, player.getName());
        field.setAccessible(false);

        Field field1 = cp.getClass().getDeclaredField("disguisedProfile");
        field1.setAccessible(true);
        field1.set(cp, gameProfile);
        field1.setAccessible(false);

        Field field2 = cp.getClass().getDeclaredField("originalPlayerListName");
        field2.setAccessible(true);
        field2.set(cp, gameProfile.getName());
        field2.setAccessible(false);

    }

    public static UUID fromTrimmed(String trimmedUUID) throws IllegalArgumentException {
        if (trimmedUUID == null) throw new IllegalArgumentException();
        StringBuilder builder = new StringBuilder(trimmedUUID.trim());
        /* Backwards adding to avoid index adjustments */
        try {
            builder.insert(20, "-");
            builder.insert(16, "-");
            builder.insert(12, "-");
            builder.insert(8, "-");
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException();
        }
        return null;
    }
}
