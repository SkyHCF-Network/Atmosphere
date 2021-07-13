package net.skyhcf.atmosphere.shared.prefix;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import org.bson.Document;

@Data
@AllArgsConstructor
public class Prefix {

    private String id;
    private String name;
    private String color;
    private String prefix;

    public void save(){
        Document filter = new Document("id", this.id);
        Document oldDoc = AtmosphereShared.getInstance().getMongoManager().getPrefixes().find(filter).first();
        Document newDoc = new Document("id", this.id)
                .append("name", this.name)
                .append("color", this.color)
                .append("prefix", this.prefix);
        AtmosphereShared.getInstance().getMongoManager().getPrefixes().replaceOne(oldDoc, newDoc);
    }

}
