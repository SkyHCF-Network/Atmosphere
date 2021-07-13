package net.skyhcf.atmosphere.bukkit.commands.menu.prefix.prefix;

import com.google.common.collect.Lists;
import net.frozenorb.qlib.menu.Button;
import net.skyhcf.atmosphere.bukkit.AtmosphereBukkit;
import net.skyhcf.atmosphere.bukkit.utils.ColorUtil;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.prefix.Prefix;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.utils.Settings;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class PrefixButton extends Button {

   private final Profile profile;
   private final Prefix prefix;

   public PrefixButton(Profile profile, Prefix prefix){
       this.profile = profile;
       this.prefix = prefix;
   }

    @Override
    public String getName(Player player) {
        return BukkitChat.format(prefix.getColor() + prefix.getName());
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = Lists.newArrayList();
        boolean isPrefix = false;
        if(profile.getActivePrefix() != null && profile.getActivePrefix().getId().equalsIgnoreCase(prefix.getId())){
            isPrefix = true;
        }
        description.add("&7&m" + BukkitChat.LINE);
        description.add("&eDisplay&7: &r" + prefix.getPrefix() + profile.getHighestGrantOnScope(AtmosphereShared.getInstance().getServerManager().getServer(AtmosphereBukkit.getInstance().getServer().getServerName())).getRank().getPrefix() + profile.getUsername() + "&r&7: &fHello!");
        description.add("&r");
        description.add((!profile.hasPermission("atmosphere.prefixes.use." + prefix.getId()) ? "&c&lYou cannot use this prefix." : (isPrefix ? "&c&lClick to remove your prefix." : "&a&lClick to make this your prefix.")));
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
        boolean isPrefix = false;
        if(profile.getActivePrefix() != null && profile.getActivePrefix().getId().equalsIgnoreCase(prefix.getId())){
            isPrefix = true;
        }
        return (isPrefix ? DyeColor.GREEN.getWoolData() : DyeColor.RED.getWoolData());
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        boolean isPrefix = false;
        if(profile.getActivePrefix() != null && profile.getActivePrefix().getId().equalsIgnoreCase(prefix.getId())){
            isPrefix = true;
        }
        if(isPrefix){
            player.sendMessage(BukkitChat.format("&cYour prefix has been removed."));
            profile.setActivePrefix(null);
            profile.save();
        }else{
            if(profile.hasPermission("atmosphere.prefixes.use." + prefix.getId())) {
                player.sendMessage(BukkitChat.format("&aYou have equipped the &r" + prefix.getColor() + prefix.getName() + "&r &aprefix."));
                profile.setActivePrefix(prefix);
                profile.save();
            }else{
                player.sendMessage(BukkitChat.format("&cYou cannot use this prefix. You may purchase it on our store at " + Settings.SERVER_STORE + "."));
            }
        }
    }
}
