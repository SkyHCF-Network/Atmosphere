package net.skyhcf.atmosphere.bukkit.permission;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.skyhcf.atmosphere.bukkit.AtmosphereBukkit;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.SharedAPI;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.utils.SystemType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;

public class PermissionUtils {

    private static final Map<Player, PermissionAttachment> attachments = Maps.newHashMap();

    public static void updatePermissions(Player player){
        if(AtmosphereShared.getInstance().getSystemType() != SystemType.BUKKIT) return;
        try {
            try {
                if (getAttachment(player) != null) player.removeAttachment(getAttachment(player));
            } catch (IllegalArgumentException ignored) {}
            PermissionAttachment attachment = player.addAttachment(AtmosphereBukkit.getInstance());
            if(attachment == null) return;
            List<String> permissions = Lists.newArrayList(AtmosphereShared.getInstance().getProfileManager().getProfile(player.getUniqueId()).getPermissionsOnScope(AtmosphereShared.getInstance().getServerManager().getServer(AtmosphereBukkit.getInstance().getServer().getServerName())));
            for (String permission : permissions) {
                attachment.setPermission((permission.startsWith("-") ? permission.substring(1) : permission), !permission.startsWith("-"));
            }
            attachments.put(player, attachment);
            AtmosphereShared.getInstance().getProfileManager().getProfile(player.getUniqueId()).setLastSeen(System.currentTimeMillis());
            player.recalculatePermissions();
            player.setDisplayName(BukkitChat.format(AtmosphereShared.getInstance().getProfileManager().getProfile(player.getUniqueId()).getHighestGrantOnScope(SharedAPI.getServer(Bukkit.getServerName())).getRank().getColor() + player.getName()));
        }catch(ConcurrentModificationException ignored){

        }
    }

    public static void updatePermissions(){
        if(AtmosphereShared.getInstance().getSystemType() != SystemType.BUKKIT) return;
        Bukkit.getServer().getOnlinePlayers().forEach(PermissionUtils::updatePermissions);
    }

    public static PermissionAttachment getAttachment(Player player){
        return attachments.getOrDefault(player, null);
    }

}
