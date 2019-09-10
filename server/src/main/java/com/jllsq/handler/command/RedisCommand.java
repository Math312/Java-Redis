package com.jllsq.handler.command;

import com.jllsq.RedisServer;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.sds.SDS;
import lombok.Data;

@Data
public abstract class RedisCommand {

    private SDS name;
    private int arity;

    public RedisCommand(SDS name, int arity) {
        this.name = name;
        this.arity = arity;
    }

    public abstract RedisObject process(RedisClient client);
}
