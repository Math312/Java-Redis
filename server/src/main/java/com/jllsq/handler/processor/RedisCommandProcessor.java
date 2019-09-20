package com.jllsq.handler.processor;

import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.sds.SDS;
import com.jllsq.config.Shared;
import com.jllsq.handler.command.RedisCommandEnum;

import java.util.HashMap;
import java.util.Map;

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
