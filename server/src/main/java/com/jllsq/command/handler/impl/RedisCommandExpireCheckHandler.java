package com.jllsq.command.handler.impl;

import com.jllsq.command.RedisCommand;
import com.jllsq.command.RedisCommandClientHandlerChain;
import com.jllsq.command.handler.RedisCommandClientHandler;
import com.jllsq.common.basic.map.DictEntry;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisDb;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.config.Shared;
import com.jllsq.holder.RedisServerObjectHolder;
import com.jllsq.holder.RedisServerStateHolder;

/**
 * @author yanlishao
 */
public class RedisCommandExpireCheckHandler implements RedisCommandClientHandler {

    public boolean expireIfNeed(RedisDb db, RedisObject key) {
        long time = RedisServerStateHolder.getInstance().getUnixTime();
        DictEntry entry = db.getExpires().find(key);
        if (entry == null) {
            return false;
        } else {
            long expireTime = (long) entry.getValue().getPtr();
            if (expireTime < time) {
                RedisServerObjectHolder objectHolder = RedisServerObjectHolder.getInstance();
                DictEntry dataEntry = db.getExpires().delete(key);
                DictEntry expireEntry = db.getDict().delete(key);
                objectHolder.deleteObject(dataEntry.getKey());
                objectHolder.deleteObject(expireEntry.getKey());
                objectHolder.deleteObject(expireEntry.getValue());
                return true;
            }
            return false;
        }
    }

    @Override
    public RedisObject handle(RedisClient client, RedisCommand command, RedisCommandClientHandlerChain chain) {
        RedisObject result = null;
        if (expireIfNeed(client.getDb(), client.getArgv()[1])) {
            result = Shared.getInstance().getNokeyerr();
        } else {
            result = chain.doHandle(client,command);
        }
        return result;
    }
}
