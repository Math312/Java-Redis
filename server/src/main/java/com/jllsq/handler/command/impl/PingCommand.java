package com.jllsq.handler.command.impl;

import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.sds.SDS;
import com.jllsq.config.Shared;
import com.jllsq.handler.command.RedisCommand;

public class PingCommand extends RedisCommand {

    public PingCommand() {
        super(new SDS("ping"),0);
    }

    @Override
    public RedisObject process(RedisClient client) {
        return Shared.getInstance().getPong();
    }
}
