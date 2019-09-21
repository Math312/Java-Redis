package com.jllsq.command.processor.impl;

import com.jllsq.command.RedisCommand;
import com.jllsq.command.RedisCommandClientHandlerChain;
import com.jllsq.command.RedisCommandEnum;
import com.jllsq.command.processor.RedisCommandProcessor;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.sds.SDS;
import com.jllsq.config.Shared;
import com.jllsq.holder.RedisServerDbHolder;
import com.jllsq.holder.RedisServerStateHolder;
import com.jllsq.log.RedisAofLog;

import java.io.IOException;
import java.util.Map;

public class BasicRedisCommandProcessor implements RedisCommandProcessor {

    @Override
    public RedisObject process(RedisClient client) {
        RedisCommandEnum commandEnum = RedisCommandEnum.getCommandByKey((SDS) (client.getArgv()[0].getPtr()));
        if (commandEnum != null) {
            RedisCommand command = commandEnum.getCommand();
            RedisCommandClientHandlerChain chain = command.getHandlerChain();
            chain.init();
            return chain.doHandle(client,command);
        } else {
            return Shared.getInstance().getSyntaxerr();
        }
    }
}
