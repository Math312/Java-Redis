package com.jllsq.command.impl;

import com.jllsq.command.RedisCommand;
import com.jllsq.command.handler.impl.RedisCommandAofHandler;
import com.jllsq.command.handler.impl.RedisCommandCheckParamNumsHandler;
import com.jllsq.command.handler.impl.RedisCommandInitClientHandler;
import com.jllsq.command.handler.impl.RedisCommandProcessHandler;
import com.jllsq.common.basic.map.DictEntry;
import com.jllsq.common.basic.sds.SDS;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisDb;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.config.Shared;
import com.jllsq.holder.RedisServerStateHolder;

public class DelCommand extends RedisCommand {

    public DelCommand() {
        super(new SDS("DEL"),1);
    }

    @Override
    public RedisObject process(RedisClient client) {
        RedisDb db = client.getDb();
        DictEntry<RedisObject,RedisObject> dataEntry = null;
        if ((dataEntry = db.getDict().delete(client.getArgv()[1]))!= null ) {
            dataEntry.getValue().destructor();
            dataEntry.getKey().destructor();
            dataEntry = db.getExpires().delete(client.getArgv()[1]);
            if (dataEntry != null) {
                dataEntry.getValue().destructor();
                dataEntry.getKey().destructor();
            }
            RedisServerStateHolder.getInstance().incrDirty();
        } else {
            return Shared.getInstance().getNokeyerr();
        }
        return Shared.getInstance().getOk();
    }

    @Override
    public void initChain() {
        super.initChain();
        handlerChain.add(new RedisCommandInitClientHandler());
        handlerChain.add(new RedisCommandCheckParamNumsHandler());
        handlerChain.add(new RedisCommandAofHandler());
        handlerChain.add(new RedisCommandProcessHandler());
    }
}
