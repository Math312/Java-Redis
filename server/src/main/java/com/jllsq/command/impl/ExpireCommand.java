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
import org.apache.commons.lang3.SerializationUtils;

import static com.jllsq.holder.RedisServerObjectHolder.REDIS_ENCODING_INT;
import static com.jllsq.holder.RedisServerObjectHolder.REDIS_STRING;

public class ExpireCommand extends RedisCommand {

    public ExpireCommand() {
        super(new SDS("expire"),2);
    }

    @Override
    public RedisObject process(RedisClient client) {
        RedisDb db = client.getDb();
        RedisObject result = null;
        DictEntry entry = db.getDict().find(client.getArgv()[1]);
        if (entry == null) {
            result = Shared.getInstance().getNokeyerr();
        } else {
            DictEntry expireEntry = db.getExpires().find(client.getArgv()[1]);
            RedisObject object = client.getArgv()[2];
            long expires = Long.parseLong(((SDS)(object.getPtr())).getContent())*1000 + System.currentTimeMillis();
            object.setEncoding(REDIS_ENCODING_INT);
            object.setPtr(expires);
            RedisObject expiresObject = RedisServerObjectHolder.getInstance().createObject(false,REDIS_STRING,expires,REDIS_ENCODING_INT);
            if (expireEntry == null) {
                RedisObject key = SerializationUtils.clone(client.getArgv()[1]);
                db.getExpires().add(key,expiresObject);
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
