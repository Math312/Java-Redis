package com.jllsq.command.handler.impl;

import com.jllsq.command.RedisCommand;
import com.jllsq.command.RedisCommandClientHandlerChain;
import com.jllsq.command.handler.RedisCommandClientHandler;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.config.Shared;

/**
 * @author yanlishao
 */
public class RedisCommandCheckParamNumsHandler implements RedisCommandClientHandler {
    @Override
    public RedisObject handle(RedisClient client, RedisCommand command, RedisCommandClientHandlerChain chain) {
        if (client.getArgc() != command.getArity()+1){
            return Shared.getInstance().getSyntaxerr();
        }
        else {
            return chain.doHandle(client,command);
        }
    }
}
