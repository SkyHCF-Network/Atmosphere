package net.skyhcf.atmosphere.shared.redis;

import redis.clients.jedis.Jedis;

public class RedisPublisher {
    private Jedis jedis;

    private String channel;

    public RedisPublisher(String channel) {
        try {
            this.channel = channel;
            this.jedis = new Jedis("127.0.0.1", 6379);
        } catch (Exception var6) {
            System.out.println("Redis Publisher failed to connect with provided details.");
        }
    }

    public void write(String msg) {
        this.jedis.publish(this.channel, msg);
    }
}

