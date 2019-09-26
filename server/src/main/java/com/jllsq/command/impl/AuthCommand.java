package com.jllsq.command.impl;

import com.jllsq.command.handler.impl.*;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.basic.sds.SDS;
import com.jllsq.command.RedisCommand;

public class AuthCommand extends RedisCommand {

    public AuthCommand() {
        super(new SDS("auth"),1);
    }

    @Override
    public RedisObject process(RedisClient client) {
        return null;
    }

    @Override
    public void initChain() {
        super.initChain();
        handlerChain.add(new RedisCommandInitClientHandler());
        handlerChain.add(new RedisCommandCheckParamNumsHandler());
        handlerChain.add(new RedisCommandAofHandler());
        handlerChain.add(new RedisCommandProcessHandler());
    }
}
