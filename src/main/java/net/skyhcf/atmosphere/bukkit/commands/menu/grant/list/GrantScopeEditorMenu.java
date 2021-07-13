package net.skyhcf.atmosphere.bukkit.commands.menu.grant.list;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.pagination.PaginatedMenu;
import net.skyhcf.atmosphere.bukkit.commands.menu.grant.list.button.GrantScopeButton;
import net.skyhcf.atmosphere.bukkit.utils.button.BackButton;
import net.skyhcf.atmosphere.shared.grant.Grant;
import net.skyhcf.atmosphere.shared.profile.Profile;
import org.bukkit.entity.Player;

import java.util.Map;

public class GrantScopeEditorMenu extends PaginatedMenu {

    private final Profile profile;
    private final Grant grant;

    public GrantScopeEditorMenu(Profile profile, Grant grant){
        this.profile = profile;
        this.grant = grant;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(4, new BackButton(new GrantsMenu(profile, profile.getGrants())));
        return buttons;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Grant Scope Editor";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        int index = 0;
        for(String scope : grant.getScopes()){
            buttons.put(index++, new GrantScopeButton(profile, grant, scope));
        }
        return buttons;
    }
}
