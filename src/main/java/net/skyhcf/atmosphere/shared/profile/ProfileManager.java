package net.skyhcf.atmosphere.shared.profile;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.disguise.Disguise;
import net.skyhcf.atmosphere.shared.utils.EncryptionHelper;
import net.skyhcf.atmosphere.shared.utils.Locale;
import net.skyhcf.atmosphere.shared.utils.SystemLogger;
import org.bson.Document;

import java.util.List;
import java.util.UUID;

public class ProfileManager {

    @Getter private List<Profile> profiles = Lists.newArrayList();

    public ProfileManager(){
        refresh();
    }

    public Profile createProfile(UUID uuid, String username, String ip){
        Profile profile = new Profile(uuid, username, System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(), false, null, ip, null/*, null*/, null, Lists.newArrayList());
        Document document = new Document("uuid", String.valueOf(uuid))
                .append("username", profile.getUsername())
                .append("firstJoined", profile.getFirstJoined())
                .append("lastSeen", profile.getLastSeen())
                .append("staffChat", profile.isStaffChat())
                .append("activePrefix", (profile.getActivePrefix() == null ? null : profile.getActivePrefix().getId()))
                .append("activePrefix", (profile.getActivePrefix() == null ? null : profile.getActivePrefix().getId()))
                .append("ip", EncryptionHelper.toHexString(profile.getIp()))
                .append("currentServer", profile.getCurrentServer())
                .append("authSecret", profile.getAuthSecret())
                .append("permissions", profile.getPermissions());
        AtmosphereShared.getInstance().getMongoManager().getProfiles().insertOne(document);
        SystemLogger.log("Profile Manager", "Created profile - " + profile.getUuid().toString() + " " + Locale.VERTICAL_STRAIGHT_LINE + " " + profile.getUsername());
        this.refresh();
        return profile;
    }

    public void deleteProfile(UUID uuid){
        Document filter = new Document("uuid", String.valueOf(uuid));
        Document document = AtmosphereShared.getInstance().getMongoManager().getProfiles().find(filter).first();
        AtmosphereShared.getInstance().getMongoManager().getProfiles().deleteOne(document);
        this.refresh();
    }

    public Profile getProfile(UUID uuid){
        for(Profile profile : profiles){
            if(String.valueOf(profile.getUuid()).equalsIgnoreCase(uuid.toString())) return profile;
        }
        return null;
    }

    public boolean profileExists(UUID uuid){
        for(Document document : AtmosphereShared.getInstance().getMongoManager().getProfiles().find()){
            if(document.getString("uuid").equalsIgnoreCase(String.valueOf(uuid))) return true;
        }
        return false;
    }

    public void refresh(){
        List<Profile> profiles = Lists.newArrayList();
        for(Document document : AtmosphereShared.getInstance().getMongoManager().getProfiles().find()){
            profiles.add(new Profile(
                    UUID.fromString(document.getString("uuid")),
                    document.getString("username"),
                    document.getLong("firstJoined"),
                    document.getLong("lastSeen"),
                    (document.getLong("lastAuthentication") == null ? 0L : document.getLong("lastAuthentication")),
                    document.getBoolean("staffChat"),
                    (document.getString("activePrefix") == null ? null : AtmosphereShared.getInstance().getPrefixManager().getPrefix(document.getString("activePrefix"))),
                    document.getString("ip"),
                    document.getString("currentServer"),
                    document.getString("authSecret"),
                    /*,(document.getString("disguise") == null ? null : ),*/
                    (List<String>) document.get("permissions")
            ));
        }
        this.profiles = profiles;
    }

}
