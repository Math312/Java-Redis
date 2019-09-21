package com.jllsq.command.processor;

import com.jllsq.common.entity.RedisClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yanlishao
 */
public abstract class RedisCommandProcessor{

    public abstract boolean preHandle(RedisClient client, Map<String,Object> shared);

    public Object process(RedisClient client) {
        Map<String,Object> shared = new HashMap<>();
        preHandle(client,shared);
        processing(client,shared);
        return afterComplete(client,shared);

    }

    public abstract Object afterComplete(RedisClient client,Map<String, Object> shared);

    public abstract boolean processing(RedisClient client, Map<String, Object> shared);

}
