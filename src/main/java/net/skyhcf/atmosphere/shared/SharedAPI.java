package net.skyhcf.atmosphere.shared;

import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.rank.Rank;
import net.skyhcf.atmosphere.shared.server.Server;

import java.util.UUID;

public class SharedAPI {

    public static Profile getProfile(UUID uuid){
        return AtmosphereShared.getInstance().getProfileManager().getProfile(uuid);
    }

    public static Rank getRank(String id){
        return AtmosphereShared.getInstance().getRankManager().getRank(id);
    }

    public static String formatName(Profile profile){
        return profile.getHighestGrant().getRank().getColor() + profile.getUsername();
    }

    public static String formatName(UUID uuid){
        if(uuid.toString().equalsIgnoreCase(Profile.getConsoleUUID().toString())) return "&4&lConsole";
        Profile profile = getProfile(uuid);
        return profile.getHighestGrant().getRank().getColor() + profile.getUsername();
    }

    public static String formatNameOnScope(UUID uuid, Server server){
        if(uuid.toString().equalsIgnoreCase(Profile.getConsoleUUID().toString())) return "&4&lConsole";
        Profile profile = getProfile(uuid);
        return profile.getHighestGrantOnScope(server).getRank().getColor() + profile.getUsername();
    }

    public static String formatNameOnScope(UUID uuid, String server){
        if(uuid.toString().equalsIgnoreCase(Profile.getConsoleUUID().toString())) return "&4&lConsole";
        Profile profile = getProfile(uuid);
        return profile.getHighestGrantOnScope(AtmosphereShared.getInstance().getServerManager().getServer(server)).getRank().getColor() + profile.getUsername();
    }

    public static Server getServer(String server){
        return AtmosphereShared.getInstance().getServerManager().getServer(server);
    }

}
