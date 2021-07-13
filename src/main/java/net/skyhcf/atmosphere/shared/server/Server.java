package net.skyhcf.atmosphere.shared.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import org.bson.Document;

@Data
@AllArgsConstructor
public class Server {

    private String id;
    private String name;
    private int playerCount;
    private int maxPlayerCount;
    private double tps;
    private boolean online;
    private boolean whitelisted;

    public void save(){
        Document filter = new Document("id", this.id);
        Document oldDoc = AtmosphereShared.getInstance().getMongoManager().getServers().find(filter).first();
        Document newDoc = new Document("id", this.id)
                .append("name", this.name)
                .append("playerCount", this.playerCount)
                .append("maxPlayerCount", this.maxPlayerCount)
                .append("tps", this.tps)
                .append("online", this.online)
                .append("whitelisted", this.whitelisted);
        AtmosphereShared.getInstance().getMongoManager().getServers().replaceOne(oldDoc, newDoc);
    }

}
