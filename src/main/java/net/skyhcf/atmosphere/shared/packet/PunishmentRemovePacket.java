package net.skyhcf.atmosphere.shared.packet;

import com.google.gson.JsonObject;
import net.skyhcf.atmosphere.shared.punishment.Punishment;
import net.skyhcf.atmosphere.shared.redis.RedisPacket;

import java.util.UUID;

public class PunishmentRemovePacket extends RedisPacket {

    private final Punishment punishment;
    private final boolean silent;

    public PunishmentRemovePacket(){
        this.punishment = null;
        this.silent = false;
    }

    public PunishmentRemovePacket(Punishment punishment, boolean silent){
        this.punishment = punishment;
        this.silent = silent;
    }

    @Override
    public String getIdentifier() {
        return "punishment-remove-packet";
    }

    @Override
    public JsonObject serialized() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("reason", punishment.getRemovedReason());
        jsonObject.addProperty("target", punishment.getTarget().toString());
        jsonObject.addProperty("removedBy", punishment.getRemovedBy().toString());
        jsonObject.addProperty("punishmentType", punishment.getPunishmentType().name());
        jsonObject.addProperty("silent", this.silent);
        return jsonObject;
    }
}
