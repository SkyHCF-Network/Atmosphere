package net.skyhcf.atmosphere.shared.profile;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.skyhcf.atmosphere.bukkit.AtmosphereBukkit;
import net.skyhcf.atmosphere.bungee.AtmosphereBungee;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.grant.Grant;
import net.skyhcf.atmosphere.shared.prefix.Prefix;
import net.skyhcf.atmosphere.shared.punishment.Punishment;
import net.skyhcf.atmosphere.shared.punishment.PunishmentType;
import net.skyhcf.atmosphere.shared.rank.Rank;
import net.skyhcf.atmosphere.shared.server.Server;
import net.skyhcf.atmosphere.shared.utils.SystemType;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Profile {

    private UUID uuid;
    private String username;
    private long firstJoined;
    private long lastSeen;
    private long lastAuthentication;
    private boolean staffChat;
    private Prefix activePrefix;
    private String ip;
    private String currentServer;
    private String authSecret;
    /*private Disguise disguise;*/
    private List<String> permissions;

    public void save(){
        Document filter = new Document("uuid", String.valueOf(uuid));
        Document oldDocument = AtmosphereShared.getInstance().getMongoManager().getProfiles().find(filter).first();
        Document newDocument = new Document("uuid", String.valueOf(this.uuid))
                .append("username", this.username)
                .append("firstJoined", this.firstJoined)
                .append("lastSeen", this.lastSeen)
                .append("lastAuthentication", this.lastAuthentication)
                .append("staffChat", this.staffChat)
                .append("activePrefix", (activePrefix == null ? null : activePrefix.getId()))
                .append("ip", this.ip)
                .append("currentServer", this.currentServer)
                .append("authSecret", this.authSecret)
                /*.append("disguise", (this.disguise == null ? disguise.getUuid().toString())*/
                .append("permissions", this.permissions);
        AtmosphereShared.getInstance().getMongoManager().getProfiles().replaceOne(oldDocument, newDocument);
    }

    public List<String> getPermissionsOnScope(Server server){
        List<String> permissions = getHighestGrantOnScope(server).getRank().getPermissions();
        for(String perm : this.permissions){
            if(!permissions.contains(perm)) {
                permissions.add(perm);
            }
        }
        for(String perm : permissions){
            if(!permissions.contains(perm)) {
                permissions.add(perm);
            }
        }
        for(Grant grant : getActiveGrantsOnScope(server)){
            for(Rank parent : grant.getRank().getParentsAsRanks()){
                for(String perm : parent.getPermissions()){
                    if(!permissions.contains(perm)) permissions.add(perm);
                }
            }
        }
        return permissions;
    }

    public String getDisplayName(){
        return getHighestGrant().getRank().getColor() + this.username;
    }

    public String getDisplayNameOnScope(Server server){
        return getHighestGrantOnScope(server).getRank().getColor() + this.username;
    }

    public List<String> getPermissions(){
        List<String> permissions = Lists.newArrayList();
        for(String perm : this.permissions){
            if(!permissions.contains(perm)) {
                permissions.add(perm);
            }
        }
        for(Grant grant : getActiveGrants()){
            for(String perm : grant.getRank().getPermissions()){
                if(!permissions.contains(perm)){
                    permissions.add(perm);
                }
            }
            for(Rank parent : grant.getRank().getParentsAsRanks()){
                for(String perm : parent.getPermissions()){
                    if(!permissions.contains(perm)) permissions.add(perm);
                }
            }
        }
        return permissions;
    }

    public boolean hasActivePunishment(){
        for(Punishment punishment : AtmosphereShared.getInstance().getPunishmentManager().getPunishments()){
            if(punishment.getTarget().toString().equalsIgnoreCase(uuid.toString()) && punishment.isActive()) return true;
        }
        return false;
    }

    public boolean hasActivePunishment(PunishmentType punishmentType){
        for(Punishment punishment : AtmosphereShared.getInstance().getPunishmentManager().getPunishments()){
            if(punishment.getPunishmentType() == punishmentType && punishment.getTarget().toString().equalsIgnoreCase(uuid.toString()) && punishment.isActive()) return true;
        }
        return false;
    }

    public List<Profile> getAlts(){
        List<Profile> profiles = Lists.newArrayList();
        for(Profile profile : AtmosphereShared.getInstance().getProfileManager().getProfiles()){
            if(profile.getIp().equalsIgnoreCase(ip)){
                profiles.add(profile);
            }
        }
        return profiles;
    }

    public List<Punishment> getActivePunishments(){
        List<Punishment> punishments = Lists.newArrayList();
        for(Punishment punishment : AtmosphereShared.getInstance().getPunishmentManager().getPunishments()){
            if(punishment.getTarget().toString().equalsIgnoreCase(uuid.toString()) && punishment.isActive()){
                punishments.add(punishment);
            }
        }
        return punishments;
    }

    public List<Punishment> getActivePunishments(PunishmentType punishmentType){
        List<Punishment> punishments = Lists.newArrayList();
        for(Punishment punishment : AtmosphereShared.getInstance().getPunishmentManager().getPunishments()){
            if(punishment.getTarget().toString().equalsIgnoreCase(uuid.toString()) && punishment.isActive() && punishment.getPunishmentType() == punishmentType){
                punishments.add(punishment);
            }
        }
        return punishments;
    }

    public List<Punishment> getPunishments(PunishmentType punishmentType){
        List<Punishment> punishments = Lists.newArrayList();
        for(Punishment punishment : AtmosphereShared.getInstance().getPunishmentManager().getPunishments()){
            if(punishment.getTarget().toString().equalsIgnoreCase(uuid.toString()) && punishment.getPunishmentType() == punishmentType){
                punishments.add(punishment);
            }
        }
        return punishments;
    }

    public List<Punishment> getPunishments(){
        List<Punishment> punishments = Lists.newArrayList();
        for(Punishment punishment : AtmosphereShared.getInstance().getPunishmentManager().getPunishments()){
            if(punishment.getTarget().toString().equalsIgnoreCase(uuid.toString())){
                punishments.add(punishment);
            }
        }
        return punishments;
    }

    public static UUID getConsoleUUID(){
        return UUID.fromString("00000000-0000-0000-0000-000000000000");
    }

    public List<Grant> getGrants(){
        List<Grant> grants = Lists.newArrayList();
        for(Grant grant : AtmosphereShared.getInstance().getGrantManager().getGrants()){
            if(String.valueOf(grant.getTarget()).equalsIgnoreCase(String.valueOf(uuid))) grants.add(grant);
        }
        return grants;
    }

    public List<Grant> getGrantsOnScope(Server server){
        List<Grant> grants = Lists.newArrayList();
        for(Grant grant : AtmosphereShared.getInstance().getGrantManager().getGrants()){
            if(String.valueOf(grant.getTarget()).equalsIgnoreCase(String.valueOf(uuid)) && grant.getScopes().contains(server.getId().toLowerCase())) {
                grants.add(grant);
            }
        }
        return grants;
    }

    public List<Grant> getActiveGrants(){
        List<Grant> grants = Lists.newArrayList();
        AtmosphereShared.getInstance().getGrantManager().getGrants().stream().filter(grant -> grant.isActive() && grant.getTarget().toString().equalsIgnoreCase(uuid.toString())).forEach(grants::add);
        return grants;
    }

    public List<Grant> getActiveGrantsOnScope(Server scope){
        List<Grant> grants = Lists.newArrayList();
        AtmosphereShared.getInstance().getGrantManager().getGrants().stream().filter(grant -> grant.isActive() && grant.getTarget().toString().equalsIgnoreCase(uuid.toString())).forEach(grant -> {
            if(grant.getScopes().contains(scope.getName()) || grant.getScopes().size() == 0) grants.add(grant);
        });
        return grants;
    }

    public Rank getHighestRank(){
        Rank highest = AtmosphereShared.getInstance().getRankManager().getRank("default");
        AtmosphereShared.getInstance().getGrantManager().refresh();
        for(Grant grant : getActiveGrants()){
            if(highest.getPriority() < grant.getRank().getPriority()) {
                highest = grant.getRank();
            }
        }
        return highest;
    }

    public Rank getHighestRankOnScope(Server server){
        Rank highest = AtmosphereShared.getInstance().getRankManager().getRank("default");
        AtmosphereShared.getInstance().getGrantManager().refresh();
        for(Grant grant : getActiveGrantsOnScope(server)){
            if(highest.getPriority() < grant.getRank().getPriority()) {
                highest = grant.getRank();
            }
        }
        return highest;
    }

    public Grant getHighestGrant(){
        Grant highest;
        try {
            highest = getActiveGrants().get(0);
            for (Grant grant : getActiveGrants()) {
                if (highest.getRank().getPriority() < grant.getRank().getPriority()) highest = grant;
            }
        }catch(Exception e){
            highest = new Grant(null, AtmosphereShared.getInstance().getRankManager().getRank("default"), null, null, null, null, 0L, 0L, 0L, null, null, Lists.newArrayList());
        }
        return highest;
    }

    public Grant getHighestGrantOnScope(Server server){
        Grant highest;
        try {
            highest = getActiveGrantsOnScope(server).get(0);
            for (Grant grant : getActiveGrantsOnScope(server)) {
                if (highest.getRank().getPriority() < grant.getRank().getPriority()) highest = grant;
            }
        }catch(Exception e){
            highest = new Grant(null, AtmosphereShared.getInstance().getRankManager().getRank("default"), null, null, null, null, 0L, 0L, 0L, null, null, Lists.newArrayList());
        }
        return highest;
    }

    public boolean hasPermission(String permission){
        if(AtmosphereShared.getInstance().getSystemType() == SystemType.BUNGEE){
            ProxiedPlayer proxiedPlayer = AtmosphereBungee.getInstance().getProxy().getPlayer(uuid);
            if(permissions.contains(permission.toLowerCase()) || proxiedPlayer.hasPermission(permission.toLowerCase())) return true;
        }
        if(AtmosphereShared.getInstance().getSystemType() == SystemType.BUKKIT){
            Player player = AtmosphereBukkit.getInstance().getServer().getPlayer(uuid);
            return permissions.contains(permission.toLowerCase()) || player.hasPermission(permission.toLowerCase());
        }
        return false;
    }

    public boolean hasPermissionPriorToLogin(String permission){
        if(getPermissions().contains(permission)) return true;
        return false;
    }

}
