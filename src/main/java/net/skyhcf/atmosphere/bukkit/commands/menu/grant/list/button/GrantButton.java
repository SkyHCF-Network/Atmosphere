package net.skyhcf.atmosphere.bukkit.commands.menu.grant.list.button;

import com.google.common.collect.Lists;
import net.frozenorb.qlib.menu.Button;
import net.skyhcf.atmosphere.bukkit.AtmosphereBukkit;
import net.skyhcf.atmosphere.bukkit.commands.menu.grant.list.GrantScopeEditorMenu;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.SharedAPI;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.grant.Grant;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.utils.TimeUtil;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Date;
import java.util.List;

public class GrantButton extends Button {

    private final Profile profile;
    private final Grant grant;

    public GrantButton(Profile profile, Grant grant){
        this.profile = profile;
        this.grant = grant;
    }



    @Override
    public String getName(Player player) {
        return BukkitChat.format("&6" + new Date(grant.getAddedAt()).toString());
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = Lists.newArrayList();
        description.add("&7&m" + BukkitChat.LINE);
        description.add("&eRank&7: &r" + grant.getRank().getColor() + grant.getRank().getDisplayName());
        description.add("&eAdded By&7: &r" + SharedAPI.formatName(grant.getAddedBy()));
        description.add("&eAdded Server&7: &c" + grant.getAddedServer().getName());
        description.add("&eAdded Reason&7: &c" + grant.getReason());
        if(grant.getDuration() != Long.MAX_VALUE && grant.isActive()) {
            description.add("&eTime Remaining&7: &c" + grant.getRemainingText());
        }
        description.add(BukkitChat.format("&7&m" + BukkitChat.LINE));
        description.add("&eScopes&7:");
        if(grant.getScopes().size() == 0) {
            description.add("&r &7* &cglobal");
        }else{
            int size = grant.getScopes().size();
            for(String s : grant.getScopes()) {
                description.add("&r &7* &c" + s);
            }
        }
        if(!(grant.getRemovedAt() == 0L)){
            description.add("&7&m" + BukkitChat.LINE);
            description.add("&c&lRemoved by &r" + SharedAPI.formatName(grant.getRemovedBy()) + "&r&7:");
            description.add("&cThe grant was removed for&7: &r" + grant.getRemovedReason());
            description.add("&cat &6" + new Date(grant.getAddedAt()) + "&r&c.");
            if(grant.getDuration() != Long.MAX_VALUE){
                description.add("&cInitial Duration&7: &6" + TimeUtil.formatDuration(grant.getDuration()));
            }
        }else{
            description.add("&7&m" + BukkitChat.LINE);
            description.add("&c&lLeft Click to revoke this grant");
            if(grant.getScopes().size() > 0) {
                description.add("&a&lRight Click to edit scopes of this grant");
            }
        }
        description.add("&7&m" + BukkitChat.LINE);
        int index = 0;
        for(String s : description){
            description.set(index++, BukkitChat.format(s));
        }
        return description;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.WOOL;
    }

    @Override
    public byte getDamageValue(Player player) {
        return (grant.getRemovedAt() == 0L ? DyeColor.GREEN.getWoolData() : DyeColor.RED.getWoolData());
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        switch(clickType) {
            case LEFT: {
                if (grant.getRemovedAt() == 0L) {
                    removalReasonConversation(player);
                }
                break;
            }case RIGHT: {
                if(grant.getScopes().size() > 0){
                    new GrantScopeEditorMenu(profile, grant).openMenu(player);
                }
                break;
            } default: {

            }
        }
    }

    private void removalReasonConversation(Player player) {
        player.closeInventory();
        ConversationFactory factory = new ConversationFactory(AtmosphereBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
            public String getPromptText(ConversationContext context) {
                return BukkitChat.YELLOW + "Please type a reason for this grant to be removed, or type " + BukkitChat.LIGHT_RED + "cancel " + BukkitChat.YELLOW + "to cancel.";
            }

            public Prompt acceptInput(ConversationContext context, String input) {
                if (input.equalsIgnoreCase("cancel")) {
                    context.getForWhom().sendRawMessage(BukkitChat.LIGHT_RED + "Grant removal process aborted.");
                    return Prompt.END_OF_CONVERSATION;
                }
                AtmosphereBukkit.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(AtmosphereBukkit.getInstance(), () -> {
                    grant.setRemovedReason(input);
                    grant.setRemovedAt(System.currentTimeMillis());
                    grant.setRemovedBy(player.getUniqueId());
                    grant.save();
                    player.sendMessage(BukkitChat.LIGHT_GREEN + "The grant has been removed.");
                }, 1L);
                AtmosphereShared.getInstance().getGrantManager().refresh();
                return Prompt.END_OF_CONVERSATION;
            }
        }).withEscapeSequence("/no").withLocalEcho(false).withTimeout(10).thatExcludesNonPlayersWithMessage("Go away evil console!");
        Conversation con = factory.buildConversation(player);
        player.beginConversation(con);
    }
}
