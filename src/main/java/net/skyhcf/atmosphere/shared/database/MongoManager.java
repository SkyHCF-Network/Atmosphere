package net.skyhcf.atmosphere.shared.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import net.skyhcf.atmosphere.shared.utils.SystemLogger;
import org.bson.Document;

@Getter
public class MongoManager {

    /*private final String host;
    private final int port;
    private final String databaseName;*/

    private final MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> ranks;
    private MongoCollection<Document> profiles;
    private MongoCollection<Document> punishments;
    private MongoCollection<Document> prefixes;
    private MongoCollection<Document> servers;
    private MongoCollection<Document> grants;
    private MongoCollection<Document> disguises;
    private MongoCollection<Document> skins;

    public MongoManager(/*String host, int port, String databaseName*/){
/*        this.host = host;
        this.port = port;
        this.databaseName = databaseName;*/

        this.mongoClient = MongoClients.create("mongodb://localhost:27017");
        try {
            this.database = mongoClient.getDatabase("Atmosphere");
            this.ranks = database.getCollection("ranks");
            this.profiles = database.getCollection("profiles");
            this.punishments = database.getCollection("punishments");
            this.prefixes = database.getCollection("prefixes");
            this.servers = database.getCollection("servers");
            this.grants = database.getCollection("grants");
            this.disguises = database.getCollection("disguises");
            this.skins = database.getCollection("skins");
            SystemLogger.log("Successfully connected to Mongo Database System.");
        }catch(Exception e){
            SystemLogger.log("Unable to connect to Mongo Database System.");
        }
    }

}
