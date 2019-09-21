package com.jllsq.command.impl;

import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.sds.SDS;
import com.jllsq.command.RedisCommand;

public class AuthCommand extends RedisCommand {

    public AuthCommand() {
        super(new SDS("auth"),1);
    }

    @Override
    public RedisObject processing(RedisClient client) {
        return null;
    }
}
