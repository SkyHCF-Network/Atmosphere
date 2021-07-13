package net.skyhcf.atmosphere.shared.rank;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.grant.Grant;
import org.bson.Document;

import java.util.List;
import java.util.Locale;

public class RankManager {

    @Getter private List<Rank> ranks = Lists.newArrayList();

    public RankManager(){
        if(!rankExists("Default")) createRank("default", "Default");
        refresh();
    }

    public List<Rank> getRanksSorted(){
        List<Rank> ranks = this.ranks;
        ranks.sort((o1, o2) -> o2.getPriority() - o1.getPriority());
        return ranks;
    }

    public Rank createRank(String id, String name){
        Rank rank = new Rank(id.toLowerCase(), name, "&f", "&f", 1, false, Lists.newArrayList(), Lists.newArrayList());
        Document document = new Document("id", rank.getId())
                .append("displayName", rank.getDisplayName())
                .append("prefix", rank.getPrefix())
                .append("color", rank.getColor())
                .append("priority", rank.getPriority())
                .append("staff", rank.isStaff())
                .append("parents", rank.getParents())
                .append("permissions", rank.getPermissions());
        AtmosphereShared.getInstance().getMongoManager().getRanks().insertOne(document);
        this.refresh();
        return rank;
    }

    public void deleteRank(Rank rank){
        Document filter = new Document("id", rank.getId().toLowerCase());
        Document document = AtmosphereShared.getInstance().getMongoManager().getRanks().find(filter).first();
        AtmosphereShared.getInstance().getMongoManager().getRanks().deleteOne(document);
        for(Grant grant : AtmosphereShared.getInstance().getGrantManager().getGrants()){
            if(grant.getRank().getId().equalsIgnoreCase(rank.getId())) AtmosphereShared.getInstance().getGrantManager().removeGrantFromDB(grant);
        }
        this.refresh();
    }

    public Rank getRank(String id){
        for(Rank rank : ranks){
            if(rank.getId().equalsIgnoreCase(id)) return rank;
        }
        return null;
    }

    public boolean rankExists(String id){
        for(Document document : AtmosphereShared.getInstance().getMongoManager().getRanks().find()){
            if(document.getString("id").equalsIgnoreCase(id)){
                return true;
            }
        }
        return false;
    }

    public void refresh(){
        List<Rank> ranks = Lists.newArrayList();
        for(Document document : AtmosphereShared.getInstance().getMongoManager().getRanks().find()){
            ranks.add(new Rank(
                    document.getString("id"),
                    document.getString("displayName"),
                    document.getString("prefix"),
                    document.getString("color"),
                    document.getInteger("priority"),
                    document.getBoolean("staff"),
                    (List<String>) document.get("parents"),
                    (List<String>) document.get("permissions")
            ));
        }
        this.ranks = ranks;
    }

}
