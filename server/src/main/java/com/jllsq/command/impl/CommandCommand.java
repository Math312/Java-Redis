package com.jllsq.command.impl;

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
    public RedisObject processing(RedisClient client) {
        RedisObject result = Shared.getInstance().getOk();
        return result;
    }
}
