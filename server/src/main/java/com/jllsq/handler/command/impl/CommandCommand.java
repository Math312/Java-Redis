package com.jllsq.handler.command.impl;

import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.sds.SDS;
import com.jllsq.config.Shared;
import com.jllsq.handler.command.RedisCommand;

public class CommandCommand extends RedisCommand {

    public CommandCommand() {
        super(new SDS("command"),0);
    }

    @Override
    public RedisObject process(RedisClient client) {
        RedisObject result = Shared.getInstance().getOk();
        return result;
    }
}
