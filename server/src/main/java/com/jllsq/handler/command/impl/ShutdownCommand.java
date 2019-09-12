package com.jllsq.handler.command.impl;

import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.sds.SDS;
import com.jllsq.handler.command.RedisCommand;

public class ShutdownCommand extends RedisCommand {

    public ShutdownCommand() {
        super(new SDS("shutdown"),0);

    }

    @Override
    public RedisObject process(RedisClient client) {
        System.exit(0);
        return null;
    }
}
