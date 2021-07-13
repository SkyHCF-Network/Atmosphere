package net.skyhcf.atmosphere.bukkit.commands.menu.grant.list.button;

import net.frozenorb.qlib.menu.Button;
import net.skyhcf.atmosphere.bukkit.AtmosphereBukkit;
import net.skyhcf.atmosphere.bukkit.commands.menu.grant.list.GrantScopeEditorMenu;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.grant.Grant;
import net.skyhcf.atmosphere.shared.profile.Profile;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class GrantScopeAddButton extends Button {

    private final Profile profile;
    private final Grant grant;

    public GrantScopeAddButton(Profile profile, Grant grant){
        this.profile = profile;
        this.grant = grant;
    }

    @Override
    public String getName(Player player) {
        return BukkitChat.format("&aAdd Scope to Grant Selection");
    }

    @Override
    public List<String> getDescription(Player player) {
        return null;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.BEACON;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        player.closeInventory();
        ConversationFactory factory = new ConversationFactory(AtmosphereBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
            public String getPromptText(ConversationContext context) {
                return BukkitChat.format("Please type the scope you would like to add to the selection, or type &ccancel &eto cancel.");
            }

            public Prompt acceptInput(ConversationContext context, String input) {
                if (input.equalsIgnoreCase("cancel")) {
                    context.getForWhom().sendRawMessage(BukkitChat.format("&cScope addition process aborted."));
                    return Prompt.END_OF_CONVERSATION;
                }
                if(grant.getScopes().contains(input.toLowerCase())){
                    player.sendMessage(BukkitChat.format("&cGrant already contains the specified scope, scope addition process aborted."));
                }
                if(!AtmosphereShared.getInstance().getServerManager().serverExists(input)){
                    player.sendMessage(BukkitChat.format("&cNo scope with name \"" + input + "\" found; scope addition process aborted."));
                    return Prompt.END_OF_CONVERSATION;
                }
                grant.getScopes().add(AtmosphereShared.getInstance().getServerManager().getServer(input).getName());
                grant.save();
                AtmosphereShared.getInstance().getGrantManager().refresh();
                return Prompt.END_OF_CONVERSATION;
            }
        }).withEscapeSequence("/no").withLocalEcho(false).withTimeout(10).thatExcludesNonPlayersWithMessage("Go away evil console!");
        Conversation con = factory.buildConversation(player);
        player.beginConversation(con);
    }
}
