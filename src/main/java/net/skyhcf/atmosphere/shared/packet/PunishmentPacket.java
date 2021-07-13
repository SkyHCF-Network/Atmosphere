package net.skyhcf.atmosphere.shared.packet;

import com.google.gson.JsonObject;
import net.skyhcf.atmosphere.shared.punishment.Punishment;
import net.skyhcf.atmosphere.shared.punishment.PunishmentType;
import net.skyhcf.atmosphere.shared.redis.RedisPacket;

import java.util.UUID;

public class PunishmentPacket extends RedisPacket {

    private final Punishment punishment;
    private final boolean silent;

    public PunishmentPacket(){
        this.punishment = null;
        this.silent = false;
    }

    public PunishmentPacket(Punishment punishment, boolean silent){
        this.punishment = punishment;
        this.silent = silent;
    }

    @Override
    public String getIdentifier() {
        return "punishment-packet";
    }

    @Override
    public JsonObject serialized() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uuid", punishment.getUuid().toString());
        jsonObject.addProperty("punishmentType", punishment.getPunishmentType().name());
        jsonObject.addProperty("target", punishment.getTarget().toString());
        jsonObject.addProperty("addedBy", punishment.getAddedBy().toString());
        jsonObject.addProperty("server", punishment.getAddedServer().getName());
        jsonObject.addProperty("reason", punishment.getReason());
        if(punishment.getInternalReason() != null) jsonObject.addProperty("internalReason", punishment.getInternalReason());
        jsonObject.addProperty("duration", punishment.getDuration());
        jsonObject.addProperty("silent", this.silent);
        return jsonObject;
    }
}
