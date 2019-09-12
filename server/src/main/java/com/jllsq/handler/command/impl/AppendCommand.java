package com.jllsq.handler.command.impl;

import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisDb;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.map.DictEntry;
import com.jllsq.common.sds.SDS;
import com.jllsq.common.sds.exception.SDSMaxLengthException;
import com.jllsq.config.Shared;
import com.jllsq.handler.command.RedisCommand;
import com.jllsq.holder.RedisServerStateHolder;

import static com.jllsq.common.entity.RedisObject.REDIS_STRING;

public class AppendCommand extends RedisCommand {

    public AppendCommand() {
        super(new SDS("append"),2);
    }

    @Override
    public RedisObject process(RedisClient client) {
        RedisDb db = client.getDb();
        RedisObject result = null;
        if (expireIfNeed(db,client.getArgv()[1])) {
            result = Shared.getInstance().getNokeyerr();
            return result;
        }
        DictEntry<RedisObject, RedisObject> entry = db.getDict().find(client.getArgv()[1]);
        if (entry == null) {
            boolean addResult = db.getDict().add(client.getArgv()[1], client.getArgv()[2]);
            if (addResult) {
                RedisServerStateHolder.getInstance().incrDirty();
                return db.getDict().find(client.getArgv()[1]).getValue();
            } else {
                return Shared.getInstance().getCzero();
            }
        } else {
            RedisObject value = entry.getValue();
            if (value.getType() != REDIS_STRING) {
                return Shared.getInstance().getWrongtypeerr();
            } else {
                try {
                    value.setPtr(((SDS) (entry.getValue().getPtr())).append(((SDS) (client.getArgv()[2].getPtr()))));
                    RedisServerStateHolder.getInstance().incrDirty();
                } catch (SDSMaxLengthException e) {
                    e.printStackTrace();
                    return Shared.getInstance().getErr();
                }
                return entry.getValue();
            }
        }
    }
}