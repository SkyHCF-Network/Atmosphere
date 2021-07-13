package net.skyhcf.atmosphere.shared.prefix;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import org.bson.Document;

import java.util.List;
import java.util.Locale;

public class PrefixManager {

    @Getter private List<Prefix> prefixes = Lists.newArrayList();

    public PrefixManager(){
        this.refresh();
    }

    public Prefix createPrefix(String name, String color, String prefixInput){
        Prefix prefix = new Prefix(name.toLowerCase(), name, color, prefixInput);
        Document document = new Document("id", name.toLowerCase())
                .append("name", name)
                .append("color", color)
                .append("prefix", prefixInput);
        AtmosphereShared.getInstance().getMongoManager().getPrefixes().insertOne(document);
        this.refresh();
        return prefix;
    }

    public void deletePrefix(Prefix prefix){
        Document filter = new Document("id", prefix.getId().toLowerCase());
        Document document = AtmosphereShared.getInstance().getMongoManager().getPrefixes().find(filter).first();
        AtmosphereShared.getInstance().getMongoManager().getPrefixes().deleteOne(document);
        this.refresh();
    }

    public boolean prefixExists(String id){
        for(Document document : AtmosphereShared.getInstance().getMongoManager().getPrefixes().find()){
            if(document.getString("id").equalsIgnoreCase(id)){
                return true;
            }
        }
        return false;
    }

    public Prefix getPrefix(String id){
        for(Prefix prefix : prefixes){
            if(prefix.getId().equalsIgnoreCase(id)) return prefix;
        }
        return null;
    }

    public void refresh(){
        List<Prefix> temp = Lists.newArrayList();
        for(Document document : AtmosphereShared.getInstance().getMongoManager().getPrefixes().find()){
            temp.add(new Prefix(
                    document.getString("id"),
                    document.getString("name"),
                    document.getString("color"),
                    document.getString("prefix")
            ));
        }
        this.prefixes = temp;
    }

}
