package net.skyhcf.atmosphere.bukkit.commands.menu.punishment;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.punishment.PunishmentType;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;
import java.util.Map;

public class MainPunishmentsListMenu extends Menu {

    private final Profile profile;

    public MainPunishmentsListMenu(Profile profile){
        this.profile = profile;
    }

    @Override
    public String getTitle(Player player) {
        return "Punishments";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if(profile.hasPermission("atmosphere.head-staff")){
            buttons.put(1, getButton(PunishmentType.WARN));
            buttons.put(3, getButton(PunishmentType.MUTE));
            buttons.put(5, getButton(PunishmentType.BAN));
            buttons.put(7, getButton(PunishmentType.BLACKLIST));
        }else{
            buttons.put(1, getButton(PunishmentType.WARN));
            buttons.put(4, getButton(PunishmentType.MUTE));
            buttons.put(7, getButton(PunishmentType.BAN));
        }
        return buttons;
    }


    private Button getButton(PunishmentType punishmentType){
        switch(punishmentType){
            case WARN: {
                return new Button() {
                    @Override
                    public String getName(Player player) {
                        return BukkitChat.format("&eWarnings");
                    }

                    @Override
                    public List<String> getDescription(Player player) {
                        return null;
                    }

                    @Override
                    public Material getMaterial(Player player) {
                        return Material.WOOL;
                    }

                    @Override
                    public byte getDamageValue(Player player) {
                        return DyeColor.YELLOW.getWoolData();
                    }

                    @Override
                    public void clicked(Player player, int slot, ClickType clickType) {
                        new PunishmentsListMenu(profile, punishmentType).openMenu(player);
                    }
                };
            }case MUTE: {
                return new Button() {
                    @Override
                    public String getName(Player player) {
                        return BukkitChat.format("&6Mutes");
                    }

                    @Override
                    public List<String> getDescription(Player player) {
                        return null;
                    }

                    @Override
                    public Material getMaterial(Player player) {
                        return Material.WOOL;
                    }

                    @Override
                    public byte getDamageValue(Player player) {
                        return DyeColor.ORANGE.getWoolData();
                    }

                    @Override
                    public void clicked(Player player, int slot, ClickType clickType) {
                        new PunishmentsListMenu(profile, punishmentType).openMenu(player);
                    }
                };
            }case BAN: {
                return new Button() {
                    @Override
                    public String getName(Player player) {
                        return BukkitChat.format("&cBans");
                    }

                    @Override
                    public List<String> getDescription(Player player) {
                        return null;
                    }

                    @Override
                    public Material getMaterial(Player player) {
                        return Material.WOOL;
                    }

                    @Override
                    public byte getDamageValue(Player player) {
                        return DyeColor.RED.getWoolData();
                    }

                    @Override
                    public void clicked(Player player, int slot, ClickType clickType) {
                        new PunishmentsListMenu(profile, punishmentType).openMenu(player);
                    }
                };
            }case BLACKLIST: {
                return new Button() {
                    @Override
                    public String getName(Player player) {
                        return BukkitChat.format("&4Blacklists");
                    }

                    @Override
                    public List<String> getDescription(Player player) {
                        return null;
                    }

                    @Override
                    public Material getMaterial(Player player) {
                        return Material.WOOL;
                    }

                    @Override
                    public byte getDamageValue(Player player) {
                        return DyeColor.BLACK.getWoolData();
                    }

                    @Override
                    public void clicked(Player player, int slot, ClickType clickType) {
                        new PunishmentsListMenu(profile, punishmentType).openMenu(player);
                    }
                };
            } default: {
                return null;
            }
        }
    }
}
