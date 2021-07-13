package net.skyhcf.atmosphere.shared.rank;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import org.bson.Document;

import java.util.List;

@Data
@AllArgsConstructor
public class Rank {

    private String id;
    private String displayName;
    private String prefix;
    private String color;
    private int priority;
    private boolean staff;
    private List<String> parents;
    private List<String> permissions;

    public List<Rank> getParentsAsRanks(){
        List<Rank> parents = Lists.newArrayList();
        for(String s : this.parents){
            parents.add(AtmosphereShared.getInstance().getRankManager().getRank(s));
        }
        return parents;
    }

    public void save(){
        Document filter = new Document("id", this.id);
        Document oldDocument = AtmosphereShared.getInstance().getMongoManager().getRanks().find(filter).first();
        Document newDocument = new Document("id", this.id)
                .append("displayName", this.displayName)
                .append("prefix", this.prefix)
                .append("color", this.color)
                .append("priority", this.priority)
                .append("staff", this.staff)
                .append("parents", this.parents)
                .append("permissions", this.permissions);
        AtmosphereShared.getInstance().getMongoManager().getRanks().replaceOne(oldDocument, newDocument);
    }

}
