package com.jllsq.command.impl;

import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisDb;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.map.DictEntry;
import com.jllsq.common.sds.SDS;
import com.jllsq.config.Shared;
import com.jllsq.command.RedisCommand;
import com.jllsq.command.RedisExpireCheckCommand;
import com.jllsq.holder.RedisServerStateHolder;

import static com.jllsq.holder.RedisServerObjectHolder.REDIS_ENCODING_INT;
import static com.jllsq.holder.RedisServerObjectHolder.REDIS_STRING;

public class TtlCommand  extends RedisExpireCheckCommand {


    public TtlCommand() {
        super(new SDS("ttl"),1);
    }

    @Override
    public RedisObject processing(RedisClient client) {
        RedisDb db = client.getDb();
        RedisObject result = null;
        DictEntry<RedisObject, RedisObject> entry = db.getDict().find(client.getArgv()[1]);
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
    public RedisObject getExpireKey(RedisClient client) {
        return client.getArgv()[1];
    }
}
