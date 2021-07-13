package net.skyhcf.atmosphere.shared.server;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.utils.SystemLogger;
import org.bson.Document;

import java.util.List;
import java.util.Locale;

public class ServerManager {

    @Getter private List<Server> servers = Lists.newArrayList();

    public ServerManager(){
        this.refresh();
    }

    public void createServer(String name){
        Server server = new Server(name.toLowerCase(), name, 0, 0, 0.0,false, false);
        Document document = new Document("id", server.getId())
                .append("name", server.getName())
                .append("playerCount", server.getPlayerCount())
                .append("maxPlayerCount", server.getMaxPlayerCount())
                .append("tps", server.getTps())
                .append("online", server.isOnline())
                .append("whitelisted", server.isWhitelisted());
        AtmosphereShared.getInstance().getMongoManager().getServers().insertOne(document);
        this.refresh();
    }

    public void deleteServer(String id){
        Document filter = new Document("id", id.toLowerCase());
        Document document = AtmosphereShared.getInstance().getMongoManager().getServers().find(filter).first();
        AtmosphereShared.getInstance().getMongoManager().getServers().deleteOne(document);
        this.refresh();
    }

    public boolean serverExists(String id){
        return getServer(id) != null;
    }

    public Server getServer(String id){
        for(Server server : servers){
            if(server.getId().equalsIgnoreCase(id)) return server;
        }
        return null;
    }

    public void refresh(){
        List<Server> servers = Lists.newArrayList();
        for(Document document : AtmosphereShared.getInstance().getMongoManager().getServers().find()){
            servers.add(new Server(
                    document.getString("id"),
                    document.getString("name"),
                    document.getInteger("playerCount"),
                    document.getInteger("maxPlayerCount"),
                    document.getDouble("tps"),
                    document.getBoolean("online"),
                    document.getBoolean("whitelisted")
            ));
        }
        this.servers = servers;
    }

}
