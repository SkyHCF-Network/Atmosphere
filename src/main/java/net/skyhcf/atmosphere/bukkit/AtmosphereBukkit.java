package net.skyhcf.atmosphere.bukkit;

import lombok.Getter;
import net.frozenorb.qlib.command.FrozenCommandHandler;
import net.frozenorb.qlib.command.parameter.BooleanParameterType;
import net.frozenorb.qlib.command.parameter.IntegerParameterType;
import net.frozenorb.qlib.qLib;
import net.frozenorb.qlib.util.TPSUtils;
import net.skyhcf.atmosphere.bukkit.commands.parameter.PrefixParameterType;
import net.skyhcf.atmosphere.bukkit.commands.parameter.ProfileParameterType;
import net.skyhcf.atmosphere.bukkit.commands.parameter.RankParameterType;
import net.skyhcf.atmosphere.bukkit.commands.parameter.ServerParameterType;
import net.skyhcf.atmosphere.bukkit.license.BukkitLicense;
import net.skyhcf.atmosphere.bukkit.listeners.BukkitListener;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.prefix.Prefix;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.rank.Rank;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AtmosphereBukkit extends JavaPlugin {

    @Getter private static AtmosphereBukkit instance;
    @Getter private AtmosphereShared atmosphereShared;

    @Getter private BukkitLicense bukkitLicense;
    @Getter private net.skyhcf.atmosphere.shared.server.Server skyServer;

    public static List<UUID> PluginOwner = new ArrayList<>();


    @Override
    public void onEnable() {
        instance = this;
        PluginOwner.add(UUID.fromString("116d2b9e-9f58-4457-aa19-215408826a7d"));
        this.atmosphereShared = new AtmosphereShared();

        getServer().getScheduler().runTaskTimer(this, atmosphereShared::refreshHeartbeat, 10, 10);

        FrozenCommandHandler.registerAll(this);
        FrozenCommandHandler.registerParameterType(Integer.class, new IntegerParameterType());
        FrozenCommandHandler.registerParameterType(Boolean.class, new BooleanParameterType());
        FrozenCommandHandler.registerParameterType(Profile.class, new ProfileParameterType());
        FrozenCommandHandler.registerParameterType(Rank.class, new RankParameterType());
        FrozenCommandHandler.registerParameterType(Server.class, new ServerParameterType());
        FrozenCommandHandler.registerParameterType(Prefix.class, new PrefixParameterType());

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        if(!new BukkitLicense(getConfig().getString("license-key"), "https://www.skyhcf.net/license/verify.php", this).setSecurityKey("PBnYo2wOL0RMPqneAJjLu81kD5l4izQI5yfX").setConsoleLog(BukkitLicense.LogType.NORMAL).register()) return;

        getServer().getPluginManager().registerEvents(new BukkitListener(), this);

        if(!AtmosphereShared.getInstance().getServerManager().serverExists(getServer().getServerName())){
            AtmosphereShared.getInstance().getServerManager().createServer(getServer().getServerName());
        }

        getServer().getScheduler().runTaskTimer(this, () -> {
            skyServer = AtmosphereShared.getInstance().getServerManager().getServer(getServer().getServerName());
            skyServer.setMaxPlayerCount(getServer().getMaxPlayers());
            skyServer.setPlayerCount(getServer().getOnlinePlayers().size());
            skyServer.setOnline(true);
            skyServer.setWhitelisted(getServer().hasWhitelist());
            skyServer.setTps(TPSUtils.getTPS());
            skyServer.save();
        }, 1L, 1L);

    }

    @Override
    public void onDisable() {
        skyServer.setOnline(false);
        skyServer.setTps(0);
        skyServer.setPlayerCount(0);
        skyServer.save();
    }
}
