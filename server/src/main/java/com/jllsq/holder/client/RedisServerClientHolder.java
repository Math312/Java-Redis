package com.jllsq.holder.client;

import com.jllsq.common.entity.RedisClient;

import java.util.HashMap;

public class RedisServerClientHolder {

    private HashMap<String, RedisClient> clients;

    private enum RedisServerClientHolderEnum {
        INSTANCE;

        private RedisServerClientHolder redisServerClientHolder;

        RedisServerClientHolderEnum() {
            this.redisServerClientHolder = new RedisServerClientHolder();
        }
    }

    public static RedisServerClientHolder getInstance() {
        return RedisServerClientHolderEnum.INSTANCE.redisServerClientHolder;
    }

    private RedisServerClientHolder() {
        this.clients = new HashMap<>();
    }

    public void putClient(String key,RedisClient client) {
        clients.put(key, client);
    }

    public boolean removeClient(String key) {
        return clients.remove(key) == null;
    }

    public RedisClient getClient(String key) {
        return clients.get(key);
    }
}
