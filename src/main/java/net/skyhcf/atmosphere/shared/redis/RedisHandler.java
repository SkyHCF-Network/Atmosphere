package net.skyhcf.atmosphere.shared.redis;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisHandler {
    public static RedisHandler i;

    private Map<String, RedisPacket> packets;

    private List<RedisListener> listeners = null;

    private String channel;

    private JedisPubSub pubSub;

    private Jedis jedis;

    private RedisPublisher publisher;

    public RedisHandler(String channel) {
        i = this;
        this.listeners = new ArrayList<>();
        this.packets = new HashMap<>();
        this.channel = channel;
        startListening();
        this.publisher = new RedisPublisher(channel);
    }

    public void registerPacket(RedisPacket packet) {
        this.packets.put(packet.getIdentifier(), packet);
    }

    public void sendPacket(RedisPacket packet) {
        try {
            JsonObject object = packet.serialized();
            object.addProperty("identifier", packet.getIdentifier());
            if (object == null) {
                System.out.println("[Packet Send Failure] Packet with ID of " + packet.getIdentifier() + " failed to parse correct information.");
            } else {
                this.publisher.write(object.toString());
                System.out.println("Sent Packet: " + packet.getIdentifier() + "(" + packet.getClass().getSimpleName() + ")");
            }
        } catch (Exception var3) {
            System.out.println("[Packet Send Failure] Packet with ID of " + packet.getIdentifier() + " failed to send.");
            var3.printStackTrace();
        }
    }

    public void registerListener(RedisListener listener) {
        this.listeners.add(listener);
    }

    private void startListening() {
        try {
            this.pubSub = get();
            this.jedis = new Jedis("127.0.0.1", 6379);
            (new Thread() {
                public void run() {
                    RedisHandler.this.jedis.subscribe(RedisHandler.this.pubSub, new String[] { channel });
                }
            }).start();
        } catch (Exception var2) {
            System.out.println("Redis failed to connect with provided details.");
            var2.printStackTrace();
        }
    }

    private JedisPubSub get() {
        return new JedisPubSub() {
            public void onMessage(String channel, String message) {
                if (channel.equals(RedisHandler.i.channel)) {
                    JsonObject object = (new JsonParser()).parse(message).getAsJsonObject();
                    if (RedisHandler.this.packets.containsKey(object.get("identifier").getAsString())) {
                        RedisPacket packet = (RedisPacket)RedisHandler.this.packets.get(object.get("identifier").getAsString());
                        Iterator<RedisListener> var5 = RedisHandler.this.listeners.iterator();
                        while (var5.hasNext()) {
                            RedisListener listener = var5.next();
                            listener.receivedPacket(packet, object);
                        }
                    }
                }
            }
        };
    }

    public List<RedisListener> getListeners() {
        return this.listeners;
    }

    public RedisPublisher getPublisher() {
        return this.publisher;
    }
}