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
        long time = RedisServerStateHolder.getInstance().getUnixTimeLong();
        DictEntry<RedisObject,RedisObject> entry = db.getExpires().find(key);
        if (entry == null) {
            return false;
        } else {
            long expireTime = Long.parseLong(((SDS)(entry.getValue().getPtr())).getContent());
            if (expireTime < time) {
                db.getExpires().delete(key);
                db.getDict().delete(key);
                return true;
            }
            return false;
        }
    }
}
