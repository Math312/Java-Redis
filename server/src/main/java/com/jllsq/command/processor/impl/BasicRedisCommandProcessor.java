package com.jllsq.command.processor.impl;

import com.jllsq.command.RedisCommand;
import com.jllsq.command.RedisCommandClientHandlerChain;
import com.jllsq.command.processor.RedisCommandProcessor;
import com.jllsq.common.basic.sds.SDS;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.config.Shared;
import com.jllsq.holder.RedisServerCommandHolder;

public class BasicRedisCommandProcessor implements RedisCommandProcessor {

    @Override
    public RedisObject process(RedisClient client) {
        RedisCommand command = RedisServerCommandHolder.getInstance().getIgnoreCase((SDS) (client.getArgv()[0].getPtr()));
        if (command != null) {
            RedisCommandClientHandlerChain chain = command.getHandlerChain();
            chain.init();
            RedisObject result =  chain.doHandle(client,command);
//            RedisServerObjectHolder redisServerObjectHolder = RedisServerObjectHolder.getInstance();
//            for (int i = 0;i < client.getArgv().length;i ++) {
//                redisServerObjectHolder.deleteObject(client.getArgv()[i]);
//            }
            return result;
        } else {
            return Shared.getInstance().getSyntaxerr();
        }
    }
}
