package com.jllsq.handler.command;

import com.jllsq.RedisServer;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisDb;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.map.DictEntry;
import com.jllsq.common.sds.SDS;
import com.jllsq.holder.RedisServerStateHolder;
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

    public boolean expireIfNeed(RedisDb db,RedisObject key) {
        long time = RedisServerStateHolder.getInstance().getUnixTime();
        DictEntry<RedisObject,RedisObject> entry = db.getExpires().find(key);
        if (entry == null) {
            return false;
        } else {
            long expireTime = (long) entry.getValue().getPtr();
            if (expireTime < time) {
                DictEntry<RedisObject,RedisObject> dataEntry = db.getExpires().delete(key);
                dataEntry.getKey().destructor();
                dataEntry.getValue().destructor();
                DictEntry<RedisObject,RedisObject> expireEntry = db.getDict().delete(key);
                expireEntry.getKey().destructor();
                expireEntry.getValue().destructor();
                return true;
            }
            return false;
        }
    }
}
