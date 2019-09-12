package com.jllsq.handler.command.impl;

import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisDb;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.map.DictEntry;
import com.jllsq.common.sds.SDS;
import com.jllsq.config.Shared;
import com.jllsq.handler.command.RedisCommand;
import com.jllsq.holder.RedisServerStateHolder;

import java.util.Date;

public class ExpireCommand extends RedisCommand {

    public ExpireCommand() {
        super(new SDS("expire"),1);
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
            long expires = Long.parseLong(((SDS)(object.getPtr())).getContent())*1000 + System.currentTimeMillis();
            object.setEncoding(RedisObject.REDIS_ENCODING_INT);
            object.setPtr(expires);
            if (expireEntry == null) {
                db.getExpires().add(client.getArgv()[1],client.getArgv()[2]);
            } else {
                expireEntry.setValue(client.getArgv()[2]);
            }
            RedisServerStateHolder.getInstance().incrDirty();
            result = Shared.getInstance().getCone();
        }
        return result;
    }
}
