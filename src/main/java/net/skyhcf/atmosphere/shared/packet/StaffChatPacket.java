package net.skyhcf.atmosphere.shared.packet;

import com.google.gson.JsonObject;
import net.skyhcf.atmosphere.shared.redis.RedisPacket;

public class StaffChatPacket extends RedisPacket {

    private final String sender;
    private final String server;
    private final String message;

    public StaffChatPacket(){
        this.sender = null;
        this.server = null;
        this.message = null;
    }

    public StaffChatPacket(String sender, String server, String message){
        this.sender = sender;
        this.server = server;
        this.message = message;
    }

    @Override
    public String getIdentifier() {
        return "staffchat-packet";
    }

    @Override
    public JsonObject serialized() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sender", this.sender);
        jsonObject.addProperty("server", this.server);
        jsonObject.addProperty("message", this.message);
        return jsonObject;
    }
}
