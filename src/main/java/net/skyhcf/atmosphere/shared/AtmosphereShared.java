package net.skyhcf.atmosphere.shared;

import lombok.Getter;
import lombok.SneakyThrows;
import net.md_5.bungee.BungeeCord;
import net.skyhcf.atmosphere.bukkit.permission.PermissionUtils;
import net.skyhcf.atmosphere.shared.database.MongoManager;
import net.skyhcf.atmosphere.shared.disguise.DisguiseManager;
import net.skyhcf.atmosphere.shared.disguise.SkinManager;
import net.skyhcf.atmosphere.shared.grant.GrantManager;
import net.skyhcf.atmosphere.shared.packet.*;
import net.skyhcf.atmosphere.shared.prefix.PrefixManager;
import net.skyhcf.atmosphere.shared.profile.ProfileManager;
import net.skyhcf.atmosphere.shared.punishment.PunishmentManager;
import net.skyhcf.atmosphere.shared.rank.RankManager;
import net.skyhcf.atmosphere.shared.redis.RedisHandler;
import net.skyhcf.atmosphere.shared.redis.listeners.PacketListener;
import net.skyhcf.atmosphere.shared.server.ServerManager;
import net.skyhcf.atmosphere.shared.utils.SystemType;
import org.bukkit.Bukkit;

public class AtmosphereShared {

    @Getter private static AtmosphereShared instance;

    @Getter private SystemType currentSystemType;

    @Getter private RedisHandler redisHandler;

    @Getter private MongoManager mongoManager;
    @Getter private RankManager rankManager;
    @Getter private PrefixManager prefixManager;
    @Getter private ProfileManager profileManager;
    @Getter private PunishmentManager punishmentManager;
    @Getter private ServerManager serverManager;
    @Getter private GrantManager grantManager;
    @Getter private SkinManager skinManager;
    @Getter private DisguiseManager disguiseManager;

    @Getter private boolean dev = false;

    public AtmosphereShared(){
        onLoad();
    }

    public void onLoad(){
        instance = this;

        this.currentSystemType = getSystemType();
        if(getSystemType() == SystemType.UNKNOWN) return;
        System.out.println("Atmosphere version 2.0 is running on " + getSystemType().getSimpleName() + ".");

        this.mongoManager = new MongoManager(/*"localhost", 27017, (dev ? "Atmosphere-Dev" : "Atmosphere")*/);
        this.rankManager = new RankManager();
        this.prefixManager = new PrefixManager();
        this.profileManager = new ProfileManager();
        this.serverManager = new ServerManager();
        this.punishmentManager = new PunishmentManager();
        this.grantManager = new GrantManager();
        this.redisHandler = new RedisHandler("atmosphere-packet");

        if(currentSystemType == SystemType.BUKKIT) {
            redisHandler.registerListener(new PacketListener());
            redisHandler.registerPacket(new StaffChatPacket());
            redisHandler.registerPacket(new StaffJoinNetworkPacket());
            redisHandler.registerPacket(new StaffLeaveNetworkPacket());
            redisHandler.registerPacket(new StaffSwitchServerPacket());
            redisHandler.registerPacket(new PunishmentPacket());
            redisHandler.registerPacket(new PunishmentRemovePacket());
        }

    }

    public void shutdown(){
        if(getSystemType() == SystemType.BUKKIT){
            Bukkit.getServer().shutdown();
        }else if(getSystemType() == SystemType.BUNGEE){
            BungeeCord.getInstance().stop();
        }
    }

    public void refreshHeartbeat(){
        rankManager.refresh();
        profileManager.refresh();
        serverManager.refresh();
        punishmentManager.refresh();
        prefixManager.refresh();
        grantManager.refresh();
        if(currentSystemType == SystemType.BUKKIT) {
            PermissionUtils.updatePermissions();
        }
        /* debug:
        if(getSystemType() == SystemType.BUKKIT) BukkitLogger.log("Bukkit Logger", "Refreshed Bukkit Heartbeat"); else BungeeLogger.log("Bungee Logger", "Refreshed BungeeCord Proxy Heartbeat");  */
    }

    @SneakyThrows
    public SystemType getSystemType(){
        try{
            Class.forName("net.md_5.bungee.BungeeCord");
            return SystemType.BUNGEE;
        }catch(Exception ignored){}
        try{
            Class.forName("org.bukkit.Bukkit");
            return SystemType.BUKKIT;
        }catch(Exception ignored){}
        return SystemType.UNKNOWN;
    }

}
