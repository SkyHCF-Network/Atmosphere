package net.skyhcf.atmosphere.shared.grant;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.profile.Profile;
import net.skyhcf.atmosphere.shared.punishment.PunishmentType;
import org.bson.Document;

import java.util.List;
import java.util.UUID;

public class GrantManager {

    @Getter private List<Grant> grants = Lists.newArrayList();

    public GrantManager(){
        this.refresh();
    }

    public Grant createGrant(Grant grant){
        Document document = new Document("uuid", String.valueOf(grant.getUuid()))
                .append("rank", grant.getRank().getId())
                .append("target", String.valueOf(grant.getTarget()))
                .append("addedBy", String.valueOf(grant.getAddedBy()))
                .append("removedBy", grant.getRemovedBy())
                .append("server", grant.getAddedServer().getId())
                .append("addedAt", grant.getAddedAt())
                .append("duration", grant.getDuration())
                .append("removedAt", grant.getRemovedAt())
                .append("reason", grant.getReason())
                .append("removedReason", grant.getRemovedReason())
                .append("scopes", grant.getScopes());
        AtmosphereShared.getInstance().getMongoManager().getGrants().insertOne(document);
        this.refresh();
        return grant;
    }

    public Grant removeGrant(Grant grant, UUID removedBy, String removedReason){
        grant.setRemovedAt(System.currentTimeMillis());
        grant.setRemovedBy(removedBy);
        grant.setRemovedReason(removedReason);
        grant.save();
        return grant;
    }

    public void removeGrantFromDB(Grant grant){
        Document filter = new Document("uuid", grant.getUuid().toString());
        Document document = AtmosphereShared.getInstance().getMongoManager().getGrants().find(filter).first();
        AtmosphereShared.getInstance().getMongoManager().getGrants().deleteOne(document);
    }

    public void refresh(){
        List<Grant> grants = Lists.newArrayList();
        for(Document document : AtmosphereShared.getInstance().getMongoManager().getGrants().find()){
            grants.add(new Grant(
                    UUID.fromString(document.getString("uuid")),
                    AtmosphereShared.getInstance().getRankManager().getRank(document.getString("rank")),
                    UUID.fromString(document.getString("target")),
                    UUID.fromString(document.getString("addedBy")),
                    (document.getString("removedBy") == null ? null : UUID.fromString(document.getString("removedBy"))),
                    AtmosphereShared.getInstance().getServerManager().getServer(document.getString("server")),
                    document.getLong("addedAt"),
                    document.getLong("duration"),
                    (document.getLong("removedAt") == null ? 0L : document.getLong("removedAt")),
                    document.getString("reason"),
                    document.getString("removedReason"),
                    (List<String>) document.get("scopes")
            ));
        }
        for(Grant grant : grants){
            if(grant.getRemainingTime() <= 0L && grant.isActive()){
                grant.setRemovedAt(System.currentTimeMillis());
                grant.setRemovedBy(Profile.getConsoleUUID());
                grant.setRemovedReason("Grant Expired");
                grant.save();
            }
        }
        this.grants = grants;
    }



}
