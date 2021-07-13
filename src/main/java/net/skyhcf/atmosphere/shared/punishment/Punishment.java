package net.skyhcf.atmosphere.shared.punishment;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.server.Server;
import net.skyhcf.atmosphere.shared.utils.TimeUtil;
import org.bson.Document;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Punishment {

    private UUID uuid;
    private UUID target;
    private UUID addedBy;
    private UUID removedBy;
    private PunishmentType punishmentType;
    private Server addedServer;
    private long addedAt;
    private long duration;
    private long removedAt;
    private String reason;
    private String internalReason;
    private String removedReason;
    private List<String> proof;

    public boolean isActive(){
        return removedAt == 0L;
    }

    public String getRemainingText(){
        return (duration == Long.MAX_VALUE ? "Permanent" : (getRemainingTime() <= 0L ? "Expired" : TimeUtil.formatDuration(getRemainingTime())));
    }

    public long getActiveUntil(){
        return (this.duration == Long.MAX_VALUE) ? Long.MAX_VALUE : (this.addedAt + this.duration);
    }

    public long getRemainingTime(){
        return this.addedAt + this.duration - System.currentTimeMillis();
    }


    public void save(){
        Document filter = new Document("uuid", String.valueOf(uuid));
        Document oldDocument = AtmosphereShared.getInstance().getMongoManager().getPunishments().find(filter).first();
        Document newDocument = new Document("uuid", this.uuid.toString())
                .append("target", this.target.toString())
                .append("addedBy", this.addedBy.toString())
                .append("removedBy", (removedBy == null ? null : removedBy.toString()))
                .append("punishmentType", punishmentType.name())
                .append("addedServer", addedServer.getName())
                .append("addedAt", addedAt)
                .append("duration", duration)
                .append("removedAt", removedAt)
                .append("reason", reason)
                .append("internalReason", internalReason)
                .append("removedReason", (removedReason == null ? null : removedReason))
                .append("proof", this.proof);
        AtmosphereShared.getInstance().getMongoManager().getPunishments().replaceOne(oldDocument, newDocument);
    }

    /*         Document document = new Document("uuid", getUuid().toString())
                .append("target", getTarget().toString())
                .append("addedBy", getAddedBy().toString())
                .append("removedBy", (getRemovedBy() == null ? null : getRemovedBy()))
                .append("punishmentType", getPunishmentType().name())
                .append("addedServer", getAddedServer().getName())
                .append("addedAt", getAddedAt())
                .append("removedAt", getRemovedAt())
                .append("reason", getReason())
                .append("internalReason", getInternalReason())
                .append("removedReason", (getRemovedReason() == null ? null : getRemovedReason())); */

}
