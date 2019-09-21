package com.jllsq.command.handler.impl;

import com.jllsq.command.RedisCommand;
import com.jllsq.command.RedisCommandClientHandlerChain;
import com.jllsq.command.handler.RedisCommandClientHandler;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.holder.RedisServerStateHolder;
import com.jllsq.log.RedisAofLog;

import java.io.IOException;

public class RedisCommandAofHandler implements RedisCommandClientHandler {

    @Override
    public RedisObject handle(RedisClient client, RedisCommand command, RedisCommandClientHandlerChain chain) {
        int dirty = RedisServerStateHolder.getInstance().getDirty();
        RedisObject redisObject = chain.doHandle(client,command);
        if (RedisServerStateHolder.getInstance().getDirty() != dirty) {
            try {
                RedisAofLog.getInstance().applyAofLog(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return redisObject;
    }
}
