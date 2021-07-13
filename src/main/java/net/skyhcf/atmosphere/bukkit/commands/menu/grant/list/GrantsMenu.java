package net.skyhcf.atmosphere.bukkit.commands.menu.grant.list;

import com.google.common.collect.Maps;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.pagination.PaginatedMenu;
import net.skyhcf.atmosphere.bukkit.commands.menu.grant.list.button.GrantButton;
import net.skyhcf.atmosphere.shared.grant.Grant;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.utils.ListUtils;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class GrantsMenu extends PaginatedMenu {

    private final Profile profile;
    private List<Grant> grants;

    public GrantsMenu(Profile profile, List<Grant> grants){
        this.profile = profile;
        this.grants = grants;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Grants";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        int index = 0;
        grants = ListUtils.reverseArrayList(grants);
        for(Grant grant : grants){
            buttons.put(index++, new GrantButton(profile, grant));
        }
        return buttons;
    }
}
