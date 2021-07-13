package net.skyhcf.atmosphere.bukkit.commands.menu.grant.create.button;

import com.google.common.collect.Lists;
import net.frozenorb.qlib.menu.Button;
import net.skyhcf.atmosphere.bukkit.AtmosphereBukkit;
import net.skyhcf.atmosphere.bukkit.commands.menu.grant.create.GrantScopeMenu;
import net.skyhcf.atmosphere.bukkit.utils.ColorUtil;
import net.skyhcf.atmosphere.bukkit.utils.grant.GrantSession;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.rank.Rank;
import net.skyhcf.atmosphere.shared.utils.TimeUtil;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class RankGrantButton extends Button {

    private final Profile profile;
    private final Rank rank;
    private final GrantSession grantSession;

    public RankGrantButton(Profile profile, Rank rank, GrantSession grantSession){
        this.profile = profile;
        this.rank = rank;
        this.grantSession = grantSession;
    }

    @Override
    public String getName(Player player) {
        return BukkitChat.format(rank.getColor() + rank.getDisplayName());
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = Lists.newArrayList();
        description.add("&7&m" + BukkitChat.LINE);
        description.add("&aLeft click to grant " + rank.getColor() + rank.getDisplayName() + "&r &ato &r" + profile.getHighestGrant().getRank().getColor() + profile.getUsername() + "&r&a.");
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
        return (byte) ColorUtil.getWoolColor(rank.getColor());
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        player.closeInventory();
        grantSession.setRank(this.rank);
        ConversationFactory factory = new ConversationFactory(AtmosphereBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
            public String getPromptText(ConversationContext context) {
                return BukkitChat.format("&ePlease type the reason for this grant, or type &ccancel &eto cancel.");
            }
            public Prompt acceptInput(ConversationContext context, String input) {
                if (input.equalsIgnoreCase("cancel")) {
                    context.getForWhom().sendRawMessage(BukkitChat.format("&cGrant process aborted."));
                    return Prompt.END_OF_CONVERSATION;
                }else {
                    grantSession.setReason(input);
                    AtmosphereBukkit.getInstance().getServer().getScheduler().runTask(AtmosphereBukkit.getInstance(), () -> {
                       durationConversation(player);
                    });
                    return Prompt.END_OF_CONVERSATION;
                }
            }
        }).withEscapeSequence("/no").withLocalEcho(false).withTimeout(10).thatExcludesNonPlayersWithMessage("Go away evil console!");
        Conversation con = factory.buildConversation(player);
        player.beginConversation(con);
    }

    private void durationConversation(Player player){
        player.closeInventory();
        ConversationFactory factory = new ConversationFactory(AtmosphereBukkit.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
            public String getPromptText(ConversationContext context) {
                return BukkitChat.format("&ePlease type a duration for this grant, (\"perm\" for permanent), or type &ccancel &eto cancel.");
            }
            public Prompt acceptInput(ConversationContext context, String input) {
                if (input.equalsIgnoreCase("cancel")) {
                    context.getForWhom().sendRawMessage(BukkitChat.format("&cGrant process aborted."));
                    return Prompt.END_OF_CONVERSATION;
                }else {
                    long duration = TimeUtil.parseTime(input);
                    if (duration <= 0L) {
                        player.sendMessage(BukkitChat.format("&cInvalid time, grant process aborted."));
                        return Prompt.END_OF_CONVERSATION;
                    }
                    grantSession.setDuration(duration);
                    new GrantScopeMenu(profile, grantSession).openMenu(player);
                    return Prompt.END_OF_CONVERSATION;
                }
            }
        }).withEscapeSequence("/no").withLocalEcho(false).withTimeout(10).thatExcludesNonPlayersWithMessage("Go away evil console!");
        Conversation con = factory.buildConversation(player);
        player.beginConversation(con);
    }
}
