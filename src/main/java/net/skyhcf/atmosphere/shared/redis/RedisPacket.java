package net.skyhcf.atmosphere.shared.redis;

import com.google.gson.JsonObject;

public abstract class RedisPacket {
    public abstract String getIdentifier();

    public abstract JsonObject serialized();
}

