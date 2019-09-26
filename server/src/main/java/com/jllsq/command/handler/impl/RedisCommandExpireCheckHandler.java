package com.jllsq.command.handler.impl;

import com.jllsq.command.RedisCommand;
import com.jllsq.command.RedisCommandClientHandlerChain;
import com.jllsq.command.handler.RedisCommandClientHandler;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisDb;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.basic.map.DictEntry;
import com.jllsq.config.Shared;
import com.jllsq.holder.RedisServerStateHolder;

/**
 * @author yanlishao
 */
public class RedisCommandExpireCheckHandler implements RedisCommandClientHandler {

    public boolean expireIfNeed(RedisDb db, RedisObject key) {
        long time = RedisServerStateHolder.getInstance().getUnixTime();
        DictEntry<RedisObject, RedisObject> entry = db.getExpires().find(key);
        if (entry == null) {
            return false;
        } else {
            long expireTime = (long) entry.getValue().getPtr();
            if (expireTime < time) {
                DictEntry<RedisObject, RedisObject> dataEntry = db.getExpires().delete(key);
                dataEntry.getKey().destructor();
                dataEntry.getValue().destructor();
                DictEntry<RedisObject, RedisObject> expireEntry = db.getDict().delete(key);
                expireEntry.getKey().destructor();
                expireEntry.getValue().destructor();
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
