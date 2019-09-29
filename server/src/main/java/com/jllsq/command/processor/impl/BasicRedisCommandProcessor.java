package com.jllsq.command.processor.impl;

import com.jllsq.command.RedisCommand;
import com.jllsq.command.RedisCommandClientHandlerChain;
import com.jllsq.command.RedisCommandEnum;
import com.jllsq.command.processor.RedisCommandProcessor;
import com.jllsq.common.basic.sds.SDS;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.config.Shared;

public class BasicRedisCommandProcessor implements RedisCommandProcessor {

    @Override
    public RedisObject process(RedisClient client) {
        RedisCommandEnum commandEnum = RedisCommandEnum.getCommandByKey((SDS) (client.getArgv()[0].getPtr()));
        if (commandEnum != null) {
            RedisCommand command = commandEnum.getCommand();
            RedisCommandClientHandlerChain chain = command.getHandlerChain();
            chain.init();
            RedisObject result =  chain.doHandle(client,command);
            for (int i = 0;i < client.getArgv().length;i ++) {
                client.getArgv()[i].destructor();
            }
            return result;
        } else {
            return Shared.getInstance().getSyntaxerr();
        }
    }
}
