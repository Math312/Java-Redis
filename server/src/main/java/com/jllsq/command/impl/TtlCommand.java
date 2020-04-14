package com.jllsq.command.impl;

import com.jllsq.command.RedisCommand;
import com.jllsq.command.handler.impl.*;
import com.jllsq.common.basic.map.DictEntry;
import com.jllsq.common.basic.sds.SDS;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisDb;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.config.Shared;
import com.jllsq.holder.RedisServerObjectHolder;
import com.jllsq.holder.RedisServerStateHolder;

import static com.jllsq.holder.RedisServerObjectHolder.REDIS_ENCODING_INT;
import static com.jllsq.holder.RedisServerObjectHolder.REDIS_STRING;

public class TtlCommand  extends RedisCommand {

    private RedisServerObjectHolder redisServerObjectHolder;

    public TtlCommand() {
        super(new SDS("ttl"),1);
        redisServerObjectHolder = RedisServerObjectHolder.getInstance();
    }

    @Override
    public RedisObject process(RedisClient client) {
        RedisDb db = client.getDb();
        RedisObject result = null;
        DictEntry entry = db.getDict().find(client.getArgv()[1]);
        if (entry == null) {
            result = Shared.getInstance().getNokeyerr();
        } else {
            entry = db.getExpires().find(client.getArgv()[1]);
            if (entry == null) {
                result = Shared.getInstance().getNullbulk();
            }else {
                long ttl = ((long)(entry.getValue().getPtr()) - RedisServerStateHolder.getInstance().getUnixTime()) / 1000;
                result = new RedisObject(false,REDIS_STRING,ttl,REDIS_ENCODING_INT);
            }
        }
        return result;
    }

    @Override
    public void recycleRedisObject(RedisClient client) {
        this.redisServerObjectHolder.deleteObject(client.getArgv()[0]);
    }

    @Override
    public void initChain() {
        super.initChain();
        handlerChain.add(new RedisCommandInitClientHandler());
        handlerChain.add(new RedisCommandCheckParamNumsHandler());
        handlerChain.add(new RedisCommandExpireCheckHandler());
        handlerChain.add(new RedisCommandAofHandler());
        handlerChain.add(new RedisCommandProcessHandler());
    }
}
