package com.jllsq.holder.client;

import com.jllsq.common.entity.RedisClient;
import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentLinkedDeque;

public class RedisServerClientHolder {

    private ConcurrentLinkedDeque<RedisClient> redisClients;

    private LruMap<String, Channel> lruMap;

    public void addRedisClient(RedisClient client) {

    }




}
