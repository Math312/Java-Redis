package com.jllsq.command.handler.impl;

import com.jllsq.command.RedisCommand;
import com.jllsq.command.RedisCommandClientHandlerChain;
import com.jllsq.command.handler.RedisCommandClientHandler;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;

/**
 * @author yanlishao
 */
public class RedisCommandProcessHandler implements RedisCommandClientHandler {

    @Override
    public RedisObject handle(RedisClient client, RedisCommand command, RedisCommandClientHandlerChain chain) {
        RedisObject response =  command.process(client);
//        command.recycleRedisObject(client);
        return response;
    }
}
