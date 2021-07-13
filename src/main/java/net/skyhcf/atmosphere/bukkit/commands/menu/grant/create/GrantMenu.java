package net.skyhcf.atmosphere.bukkit.commands.menu.grant.create;

import com.google.common.collect.Maps;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.pagination.PaginatedMenu;
import net.skyhcf.atmosphere.bukkit.commands.menu.grant.create.button.RankGrantButton;
import net.skyhcf.atmosphere.bukkit.utils.button.BackButton;
import net.skyhcf.atmosphere.bukkit.utils.grant.GrantSession;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.rank.Rank;
import org.bukkit.entity.Player;

import java.util.Map;

public class GrantMenu extends PaginatedMenu {

    private final Profile profile;
    private final GrantSession grantSession;

    public GrantMenu(Profile profile, GrantSession grantSession){
        this.profile = profile;
        this.grantSession = grantSession;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Grant - Choose a Rank";
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(4, new BackButton(null));
        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        int index = 0;
        for(Rank rank : AtmosphereShared.getInstance().getRankManager().getRanksSorted()){
            buttons.put(index++, new RankGrantButton(profile, rank, grantSession));
        }
        return buttons;
    }
}
