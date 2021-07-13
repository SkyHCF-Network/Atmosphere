package net.skyhcf.atmosphere.shared.packet;

import com.google.gson.JsonObject;
import net.skyhcf.atmosphere.shared.redis.RedisPacket;

public class StaffSwitchServerPacket extends RedisPacket {

    private final String sender;
    private final String from;
    private final String server;

    public StaffSwitchServerPacket(){
        this.sender = null;
        this.from = null;
        this.server = null;
    }

    public StaffSwitchServerPacket(String sender, String from, String server){
        this.sender = sender;
        this.from = from;
        this.server = server;
    }

    @Override
    public String getIdentifier() {
        return "staff-switch-packet";
    }

    @Override
    public JsonObject serialized() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sender", this.sender);
        jsonObject.addProperty("from", this.from);
        jsonObject.addProperty("server", this.server);
        return jsonObject;
    }


}
