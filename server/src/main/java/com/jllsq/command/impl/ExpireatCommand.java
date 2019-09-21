package com.jllsq.command.impl;

import com.jllsq.command.handler.impl.*;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisDb;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.map.DictEntry;
import com.jllsq.common.sds.SDS;
import com.jllsq.config.Shared;
import com.jllsq.command.RedisCommand;
import com.jllsq.holder.RedisServerObjectHolder;
import com.jllsq.holder.RedisServerStateHolder;

import static com.jllsq.holder.RedisServerObjectHolder.REDIS_ENCODING_INT;
import static com.jllsq.holder.RedisServerObjectHolder.REDIS_STRING;

public class ExpireatCommand extends RedisCommand {

    public ExpireatCommand() {
        super(new SDS("expireat"), 2);
    }

    @Override
    public RedisObject process(RedisClient client) {
        RedisDb db = client.getDb();
        RedisObject result = null;
        DictEntry<RedisObject, RedisObject> entry = db.getDict().find(client.getArgv()[1]);
        if (entry == null) {
            result = Shared.getInstance().getNokeyerr();
        } else {
            DictEntry<RedisObject, RedisObject> expireEntry = db.getExpires().find(client.getArgv()[1]);
            RedisObject object = client.getArgv()[2];
            long expires = Long.parseLong(((SDS)(object.getPtr())).getContent());
            RedisObject expiresObject = RedisServerObjectHolder.getInstance().createObject(false,REDIS_STRING,expires,REDIS_ENCODING_INT);
            if (expireEntry == null) {
                db.getExpires().add(client.getArgv()[1],expiresObject);
            } else {
                expireEntry.setValue(expiresObject);
            }
            RedisServerStateHolder.getInstance().incrDirty();
            result = Shared.getInstance().getCone();
        }
        return result;
    }

    @Override
    public void initChain() {
        super.initChain();
        handlerChain.add(new RedisCommandInitClientHandler());
        handlerChain.add(new RedisCommandCheckParamNumsHandler());
        handlerChain.add(new CommonLongParamCheckHandler());
        handlerChain.add(new RedisCommandAofHandler());
        handlerChain.add(new RedisCommandProcessHandler());
    }
}
