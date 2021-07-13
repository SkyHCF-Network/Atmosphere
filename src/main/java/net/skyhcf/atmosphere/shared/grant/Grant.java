package net.skyhcf.atmosphere.shared.grant;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.punishment.PunishmentType;
import net.skyhcf.atmosphere.shared.rank.Rank;
import net.skyhcf.atmosphere.shared.server.Server;
import net.skyhcf.atmosphere.shared.utils.EncryptionHelper;
import net.skyhcf.atmosphere.shared.utils.TimeUtil;
import org.bson.Document;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Grant {

    private UUID uuid;
    private Rank rank;
    private UUID target;
    private UUID addedBy;
    private UUID removedBy;
    private Server addedServer;
    private long addedAt;
    private long duration;
    private long removedAt;
    private String reason;
    private String removedReason;
    private List<String> scopes;

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
        Document oldDocument = AtmosphereShared.getInstance().getMongoManager().getGrants().find(filter).first();
        Document newDocument = new Document("uuid", String.valueOf(this.uuid))
                .append("rank", this.rank.getId())
                .append("target", (this.target == null ? null : this.target.toString()))
                .append("addedBy", (this.addedBy == null ? null : this.addedBy.toString()))
                .append("removedBy", (this.removedBy == null ? null : this.removedBy.toString()))
                .append("server", addedServer.getName())
                .append("addedAt", this.addedAt)
                .append("duration", this.duration)
                .append("removedAt", this.removedAt)
                .append("reason", this.reason)
                .append("removedReason", this.removedReason)
                .append("scopes", this.scopes);
        AtmosphereShared.getInstance().getMongoManager().getGrants().replaceOne(oldDocument, newDocument);
    }

}
