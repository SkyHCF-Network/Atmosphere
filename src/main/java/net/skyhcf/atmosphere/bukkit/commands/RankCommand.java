package net.skyhcf.atmosphere.bukkit.commands;

import com.google.common.collect.Lists;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.frozenorb.qlib.util.PaginatedOutput;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.rank.Rank;
import net.skyhcf.atmosphere.shared.rank.RankManager;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Locale;

public class RankCommand {

    private static RankManager rankManager = AtmosphereShared.getInstance().getRankManager();

    @Command(names = {"rank create"}, permission = "atmosphere.rank.admin")
    public static void rankCreate(CommandSender sender, @Param(name = "rank") String rank){
        if(rankManager.rankExists(rank)){
            sender.sendMessage(BukkitChat.format("&cThe rank " + rankManager.getRank(rank).getColor() + rankManager.getRank(rank).getDisplayName() + "&r &calready exists."));
            return;
        }
        rankManager.createRank(rank.toLowerCase(), rank);
        sender.sendMessage(BukkitChat.format("&aYou've created the &r" + rank + "&r &arank."));
    }

    @Command(names = {"rank delete"}, permission = "atmosphere.rank.admin")
    public static void rankDelete(CommandSender sender, @Param(name = "rank")Rank rank){
        rankManager.deleteRank(rank);
        sender.sendMessage(BukkitChat.format("&aYou've deleted the " + rank.getColor() + rank.getDisplayName() + "&r &arank."));
    }

    @Command(names = {"rank setcolor"}, permission = "atmosphere.rank.admin")
    public static void rankSetColor(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "color") String color){
        sender.sendMessage(BukkitChat.format("&aSet color of rank &r" + rank.getColor() + rank.getDisplayName() + "&r &ato &r" + color) + color + BukkitChat.format("&r&a."));
        rank.setColor(color);
        rank.save();
    }

    @Command(names = {"rank setprefix"}, permission = "atmosphere.rank.admin")
    public static void rankSetPrefix(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "prefix", wildcard = true) String prefix){
        sender.sendMessage(BukkitChat.format("&aSet prefix of rank &r" + rank.getColor() + rank.getDisplayName() + "&r &ato &r" + prefix + "&r&a."));
        rank.setPrefix(prefix);
        rank.save();
    }

    @Command(names = {"rank setdisplayname"}, permission = "atmosphere.rank.admin")
    public static void rankSetDisplayName(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "display name", wildcard = true) String displayName){
        sender.sendMessage(BukkitChat.format("&aSet display name of rank &r" + rank.getColor() + rank.getDisplayName() + "&r &ato &r" + displayName + "&r&a."));
        rank.setDisplayName(displayName);
        rank.save();
    }

    @Command(names = {"rank setpriority"}, permission = "atmosphere.rank.admin")
    public static void rankSetPriority(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "priority") Integer priority){
        sender.sendMessage(BukkitChat.format("&aSet priority of rank &r" + rank.getColor() + rank.getDisplayName() + "&r &ato &r" + priority + "&r&a."));
        rank.setPriority(priority);
        rank.save();
    }

    @Command(names = {"rank setstaff"}, permission = "atmosphere.rank.admin")
    public static void rankSetStaff(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "staff") Boolean staff){
        sender.sendMessage(BukkitChat.format("&aSet staff status of rank &r" + rank.getColor() + rank.getDisplayName() + "&r &ato &e" + staff + "&r&a."));
        rank.setStaff(staff);
        rank.save();
    }

    @Command(names = {"rank addpermission"}, permission = "atmosphere.rank.admin")
    public static void rankAddPermission(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "permission") String perm){
        if(rank.getPermissions().contains(perm.toLowerCase())){
            sender.sendMessage(BukkitChat.format("&cError: The rank &r" + rank.getColor() + rank.getDisplayName() + "&r &calready has access to the permission &r" + perm.toLowerCase() + "&r&c."));
            return;
        }
        sender.sendMessage(BukkitChat.format("&aAdded the permission &f" + perm + "&r &ato the rank &r" + rank.getColor() + rank.getDisplayName() + "&r&a's permissions."));
        rank.getPermissions().add(perm.toLowerCase());
        rank.save();
    }

    @Command(names = {"rank removepermission"}, permission = "atmosphere.rank.admin")
    public static void rankRemovePermission(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "permission") String perm){
        if(!rank.getPermissions().contains(perm)){
            sender.sendMessage(BukkitChat.format("&cError: The rank " + rank.getColor() + rank.getDisplayName() + "&r &cdoes not have access to the permission &r" + perm.toLowerCase() + "&r&c."));
            return;
        }
        sender.sendMessage(BukkitChat.format("&aRemoved the permission &r" + perm.toLowerCase() + "&r &afrom the rank &r" + rank.getColor() + rank.getDisplayName() + "&r&a's permissions."));
        rank.getPermissions().remove(perm);
        rank.save();
    }

    @Command(names = {"rank addparent"}, permission = "atmosphere.rank.admin")
    public static void rankAddParent(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "parent") Rank parent){
        if(rank.getParents().contains(parent.getId())){
            sender.sendMessage(BukkitChat.format("&cError: The rank &r" + rank.getColor() + rank.getDisplayName() + "&r &calready has a parent of " + parent.getColor() + parent.getDisplayName() + "&r&c."));
            return;
        }
        if(rank.getId().equalsIgnoreCase(parent.getId())){
            sender.sendMessage(BukkitChat.format("&cError: A rank cannot have itself as a parent."));
            return;
        }
        sender.sendMessage(BukkitChat.format("&aAdded the parent &r" + parent.getColor() + parent.getDisplayName() + "&r&a to the rank &r" + rank.getColor() + rank.getDisplayName() + "&r&a."));
        rank.getParents().add(parent.getId());
        rank.save();
    }

    @Command(names = {"rank removeparent"}, permission = "atmosphere.rank.admin")
    public static void rankRemoveParent(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "parent") Rank parent){
        if(!rank.getParents().contains(parent.getId())){
            sender.sendMessage(BukkitChat.format("&cError: The rank &r" + rank.getColor() + rank.getDisplayName() + "&r &cdoes not have a parent of " + parent.getColor() + parent.getDisplayName() + "&r&c."));
            return;
        }
        sender.sendMessage(BukkitChat.format("&aRemoved the parent &r" + parent.getColor() + parent.getDisplayName() + "&r &afrom the rank &r" + rank.getColor() + rank.getDisplayName() + "&r&a."));
        rank.getParents().remove(parent.getId());
        rank.save();
    }

    @Command(names = {"rank list"}, permission = "atmosphere.rank.admin")
    public static void rankList(CommandSender sender, @Param(name = "page", defaultValue = "1") Integer page){
        new PaginatedOutput<Rank>() {
            @Override
            public String getHeader(int i, int i1) {
                sender.sendMessage(BukkitChat.format("&7&m" + BukkitChat.LINE));
                return BukkitChat.format("&fRank List &7(Page " + page + "/" + i1 + ")");
            }

            @Override
            public String format(Rank rank, int i) {
                return BukkitChat.format(rank.getColor() + rank.getDisplayName()) + BukkitChat.RESET + " (" + rank.getPriority() + ")";
            }
        }.display(sender, page, AtmosphereShared.getInstance().getRankManager().getRanksSorted());
        sender.sendMessage(BukkitChat.format("&7&m" + BukkitChat.LINE));
    }

    @Command(names = {"rank info"}, permission = "atmosphere.rank.admin")
    public static void rankInfo(CommandSender sender, @Param(name = "rank") Rank rank){
        sender.sendMessage(BukkitChat.format("&7&m" + BukkitChat.LINE));
        sender.sendMessage(BukkitChat.format("&9ID: &r" + rank.getId()));
        sender.sendMessage(BukkitChat.format("&9Display Name: &r" + rank.getDisplayName()));
        sender.sendMessage(BukkitChat.format("&9Display Prefix: &r" + rank.getPrefix()));
        sender.sendMessage(BukkitChat.format("&9Display Color: &r" + rank.getColor()) + rank.getColor());
        sender.sendMessage(BukkitChat.format("&9Priority: &r" + rank.getPriority()));
        List<Rank> parents = rank.getParentsAsRanks();
        sender.sendMessage(BukkitChat.format("&9Permissions: &r" + rank.getPermissions()));
        parents.sort((o1, o2) -> o2.getPriority() - o1.getPriority());
        sender.sendMessage(BukkitChat.format("&9Inheritances:"));
        for(Rank parent : parents){
            sender.sendMessage(BukkitChat.format(" &7* &r" + parent.getColor() + parent.getDisplayName()));
        }
        sender.sendMessage(BukkitChat.format("&7&m" + BukkitChat.LINE));
    }

}
