package com.jllsq.command.impl;

import com.jllsq.command.RedisCommand;
import com.jllsq.command.handler.impl.*;
import com.jllsq.common.basic.map.DictEntry;
import com.jllsq.common.basic.sds.SDS;
import com.jllsq.common.basic.sds.exception.SDSMaxLengthException;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisDb;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.config.Shared;
import com.jllsq.holder.RedisServerStateHolder;

import static com.jllsq.holder.RedisServerObjectHolder.REDIS_STRING;

public class AppendCommand extends RedisCommand {

    public AppendCommand() {
        super(new SDS("append"),2);
    }

    private int processResultLabel = 0;

    @Override
    public RedisObject process(RedisClient client) {
        RedisDb db = client.getDb();
        DictEntry entry = db.getDict().find(client.getArgv()[1]);
        if (entry == null) {
            RedisObject key = client.getArgv()[1];
            RedisObject value = client.getArgv()[2];
            boolean addResult = db.getDict().add(key, value);
            if (addResult) {
                processResultLabel = 0;
                RedisServerStateHolder.getInstance().incrDirty();
                return value;
            } else {
                processResultLabel = 1;
                return Shared.getInstance().getCzero();
            }
        } else {
            RedisObject value = entry.getValue();
            if (value.getType() != REDIS_STRING) {
                processResultLabel = -1;
                return Shared.getInstance().getWrongtypeerr();
            } else {
                try {
                    value.setPtr(((SDS) (entry.getValue().getPtr())).append(((SDS) (client.getArgv()[2].getPtr()))));
                    RedisServerStateHolder.getInstance().incrDirty();
                    processResultLabel = 2;
                } catch (SDSMaxLengthException e) {
                    e.printStackTrace();
                    processResultLabel = -1;
                    return Shared.getInstance().getErr();
                }
                return entry.getValue();
            }
        }
    }

    @Override
    public void recycleRedisObject(RedisClient client) {
        redisServerObjectHolder.deleteObject(client.getArgv()[0]);
        switch (processResultLabel) {
            case 0:
                break;
            default:
                redisServerObjectHolder.deleteObject(client.getArgv()[1]);
                redisServerObjectHolder.deleteObject(client.getArgv()[2]);
        }
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