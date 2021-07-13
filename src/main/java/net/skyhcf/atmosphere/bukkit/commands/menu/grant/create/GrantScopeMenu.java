package net.skyhcf.atmosphere.bukkit.commands.menu.grant.create;

import com.google.common.collect.Maps;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.pagination.PaginatedMenu;
import net.skyhcf.atmosphere.bukkit.commands.menu.grant.create.button.GrantConfirmButton;
import net.skyhcf.atmosphere.bukkit.commands.menu.grant.create.button.GrantGlobalScopeButton;
import net.skyhcf.atmosphere.bukkit.commands.menu.grant.create.button.GrantScopeButton;
import net.skyhcf.atmosphere.bukkit.utils.button.BackButton;
import net.skyhcf.atmosphere.bukkit.utils.grant.GrantSession;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.grant.Grant;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.server.Server;
import org.bukkit.entity.Player;

import java.util.Map;

public class GrantScopeMenu extends PaginatedMenu {

    private final Profile profile;
    private final GrantSession grantSession;

    public GrantScopeMenu(Profile profile, GrantSession grantSession){
        this.profile = profile;
        this.grantSession = grantSession;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Grant - Select a Scope";
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(3, new GrantGlobalScopeButton(grantSession));
        buttons.put(5, new GrantConfirmButton(grantSession));
        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        int index = 0;
        for(Server server : AtmosphereShared.getInstance().getServerManager().getServers()){
            buttons.put(index++, new GrantScopeButton(grantSession, server));
        }
        return buttons;
    }
}
