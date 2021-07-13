package net.skyhcf.atmosphere.bungee;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.skyhcf.atmosphere.bungee.commands.ReloadBungeeConfig;
import net.skyhcf.atmosphere.bungee.listeners.BungeeListener;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.profile.Profile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class AtmosphereBungee extends Plugin {

    @Getter public static AtmosphereBungee instance;

    @Getter private AtmosphereShared atmosphereShared;

    @Getter private ConfigurationProvider config = ConfigurationProvider.getProvider(YamlConfiguration.class);
    @Setter
    @Getter private Configuration configuration;

    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;

        this.atmosphereShared = new AtmosphereShared();

        getProxy().getScheduler().schedule(this, atmosphereShared::refreshHeartbeat, 0L, 1L, TimeUnit.SECONDS);
        getProxy().getPluginManager().registerListener(this, new BungeeListener());

        if(!getDataFolder().exists()){
            getDataFolder().mkdir();
        }

        File file = new File(getDataFolder(), "config.yml");
        if(!file.exists()){
            file.createNewFile();
            configuration = getConfig().load(file);
            configuration.set("maintenance", true);
            configuration.set("motd", "&b&lSkyHCF Network &7⎜ &f1.7.10 - 1.8.9\n&cNetwork Whitelisted &7⎜ &bhttps://www.skyhcf.net/discord");
            getConfig().save(configuration, file);
        }else{
            configuration = getConfig().load(file);
        }

        getProxy().getPluginManager().registerCommand(this, new ReloadBungeeConfig());

    }
}
