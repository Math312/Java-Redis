package com.jllsq.command.impl;

import com.jllsq.command.handler.impl.*;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.basic.sds.SDS;
import com.jllsq.config.Shared;
import com.jllsq.command.RedisCommand;

public class PingCommand extends RedisCommand {

    public PingCommand() {
        super(new SDS("ping"),0);
    }

    @Override
    public RedisObject process(RedisClient client) {
        return Shared.getInstance().getPong();
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
