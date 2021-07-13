package net.skyhcf.atmosphere.bukkit.commands.menu.grant.list.button;

import com.google.common.collect.Lists;
import net.frozenorb.qlib.menu.Button;
import net.skyhcf.atmosphere.bukkit.AtmosphereBukkit;
import net.skyhcf.atmosphere.bukkit.commands.menu.grant.list.GrantScopeEditorMenu;
import net.skyhcf.atmosphere.bukkit.utils.ColorUtil;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.grant.Grant;
import net.skyhcf.atmosphere.shared.profile.Profile;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class GrantScopeButton extends Button {

    private final Profile profile;
    private final Grant grant;
    private final String scope;

    public GrantScopeButton(Profile profile, Grant grant, String scope){
        this.profile = profile;
        this.grant = grant;
        this.scope = scope;
    }

    @Override
    public String getName(Player player) {
        return BukkitChat.format("&6" + scope);
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = Lists.newArrayList();
        description.add("&7&m" + BukkitChat.LINE);
        description.add("&c&lClick to remove from scope selection");
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
        return DyeColor.GREEN.getWoolData();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        player.closeInventory();
        ConversationFactory factory = new ConversationFactory(AtmosphereBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
            public String getPromptText(ConversationContext context) {
                return BukkitChat.format("&ePlease type &cconfirm &eto confirm removing this scope, or type &ccancel &eto cancel.");
            }

            public Prompt acceptInput(ConversationContext context, String input) {
                if (input.equalsIgnoreCase("cancel")) {
                    context.getForWhom().sendRawMessage(BukkitChat.format("&cScope removal process aborted."));
                    return Prompt.END_OF_CONVERSATION;
                }else if(input.equalsIgnoreCase("confirm")){
                    grant.getScopes().remove(scope);
                    grant.save();
                    new GrantScopeEditorMenu(profile, grant).openMenu(player);
                }
                AtmosphereShared.getInstance().getGrantManager().refresh();
                return Prompt.END_OF_CONVERSATION;
            }
        }).withEscapeSequence("/no").withLocalEcho(false).withTimeout(10).thatExcludesNonPlayersWithMessage("Go away evil console!");
        Conversation con = factory.buildConversation(player);
        player.beginConversation(con);
    }
}
