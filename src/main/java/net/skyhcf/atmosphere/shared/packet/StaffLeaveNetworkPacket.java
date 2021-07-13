package net.skyhcf.atmosphere.shared.packet;

import com.google.gson.JsonObject;
import net.skyhcf.atmosphere.shared.redis.RedisPacket;

public class StaffLeaveNetworkPacket extends RedisPacket {

    private final String sender;
    private final String server;

    public StaffLeaveNetworkPacket(){
        this.sender = null;
        this.server = null;
    }

    public StaffLeaveNetworkPacket(String sender, String server){
        this.sender = sender;
        this.server = server;
    }

    @Override
    public String getIdentifier() {
        return "staff-leave-packet";
    }

    @Override
    public JsonObject serialized() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sender", this.sender);
        jsonObject.addProperty("server", this.server);
        return jsonObject;
    }


}
