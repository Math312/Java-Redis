package com.jllsq.command.impl;

import com.jllsq.command.handler.impl.*;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.sds.SDS;
import com.jllsq.config.Shared;
import com.jllsq.command.RedisCommand;

public class CommandCommand extends RedisCommand {

    public CommandCommand() {
        super(new SDS("command"),0);
    }

    @Override
    public RedisObject process(RedisClient client) {
        RedisObject result = Shared.getInstance().getOk();
        return result;
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
