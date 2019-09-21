package com.jllsq.command.handler.impl;

import com.jllsq.command.RedisCommand;
import com.jllsq.command.RedisCommandClientHandlerChain;
import com.jllsq.command.handler.RedisCommandClientHandler;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;

public class RedisCommandParamCheckHandler implements RedisCommandClientHandler {

    @Override
    public RedisObject handle(RedisClient client, RedisCommand command, RedisCommandClientHandlerChain chain) {
        return chain.doHandle(client,command);

    }
}
