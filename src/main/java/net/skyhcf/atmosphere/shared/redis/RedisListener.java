package net.skyhcf.atmosphere.shared.redis;

import com.google.gson.JsonObject;
import java.lang.reflect.Method;

public abstract class RedisListener {
    public final void receivedPacket(RedisPacket packet, JsonObject object) {
        try {
            Method[] var3 = getClass().getDeclaredMethods();
            int var4 = var3.length;
            for (int var5 = 0; var5 < var4; var5++) {
                Method method = var3[var5];
                if (method.isAnnotationPresent((Class)PacketListener.class)) {
                    PacketListener annotation = method.<PacketListener>getAnnotation(PacketListener.class);
                    if (annotation.identifier().equalsIgnoreCase(packet.getIdentifier()))
                        method.invoke(this, new Object[] { object });
                }
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }
    }
}
