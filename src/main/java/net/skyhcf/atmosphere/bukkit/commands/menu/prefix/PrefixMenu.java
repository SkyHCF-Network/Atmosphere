package net.skyhcf.atmosphere.bukkit.commands.menu.prefix;

import com.google.common.collect.Maps;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.pagination.PaginatedMenu;
import net.skyhcf.atmosphere.bukkit.commands.menu.prefix.prefix.PrefixButton;
import net.skyhcf.atmosphere.bukkit.utils.button.BackButton;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.prefix.Prefix;
import org.bukkit.entity.Player;

import java.util.Map;

public class PrefixMenu extends PaginatedMenu {

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(4, new BackButton(null));
        return buttons;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Prefixes";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        int index = 0;
        for(Prefix prefix : AtmosphereShared.getInstance().getPrefixManager().getPrefixes()){
            buttons.put(index++, new PrefixButton(AtmosphereShared.getInstance().getProfileManager().getProfile(player.getUniqueId()), prefix));
        }
        return buttons;
    }
}
