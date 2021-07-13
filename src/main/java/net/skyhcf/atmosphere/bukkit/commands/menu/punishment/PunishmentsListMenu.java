package net.skyhcf.atmosphere.bukkit.commands.menu.punishment;

import com.google.common.collect.Maps;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.pagination.PaginatedMenu;
import net.skyhcf.atmosphere.bukkit.AtmosphereBukkit;
import net.skyhcf.atmosphere.bukkit.commands.menu.punishment.button.PunishmentButton;
import net.skyhcf.atmosphere.bukkit.utils.button.BackButton;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.punishment.Punishment;
import net.skyhcf.atmosphere.shared.punishment.PunishmentType;
import net.skyhcf.atmosphere.shared.utils.ListUtils;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class PunishmentsListMenu extends PaginatedMenu {

    private final Profile profile;
    private final PunishmentType punishmentType;

    public PunishmentsListMenu(Profile profile, PunishmentType punishmentType){
        this.profile = profile;
        this.punishmentType = punishmentType;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(4, new BackButton(new MainPunishmentsListMenu(profile)));
        return buttons;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return punishmentType.getMenuDisplay();
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        int index = 0;
        Map<Integer, Button> buttons = Maps.newHashMap();
        List<Punishment> punishments = ListUtils.reverseArrayList(profile.getPunishments(punishmentType));
        for(Punishment punishment : punishments){
            buttons.put(index++, new PunishmentButton(profile, punishment, AtmosphereShared.getInstance().getServerManager().getServer(AtmosphereBukkit.getInstance().getServer().getServerName())));
        }
        return buttons;
    }
}
