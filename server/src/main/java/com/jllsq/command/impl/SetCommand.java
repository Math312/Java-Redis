package com.jllsq.command.impl;

import com.jllsq.command.handler.impl.RedisCommandAofHandler;
import com.jllsq.command.handler.impl.RedisCommandCheckParamNumsHandler;
import com.jllsq.command.handler.impl.RedisCommandInitClientHandler;
import com.jllsq.command.handler.impl.RedisCommandProcessHandler;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisDb;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.sds.SDS;
import com.jllsq.config.Shared;
import com.jllsq.command.RedisCommand;
import com.jllsq.holder.RedisServerStateHolder;

public class SetCommand extends RedisCommand {
    public SetCommand() {
        super(new SDS("SET"), 2);
    }

    @Override
    public RedisObject process(RedisClient client) {
        RedisDb db = client.getDb();
        RedisObject result = null;
        if (db.getDict().add(client.getArgv()[1], client.getArgv()[2])) {
            db.getExpires().delete(client.getArgv()[1]);
            result = Shared.getInstance().getCone();
        } else {
            result = Shared.getInstance().getCzero();
        }
        RedisServerStateHolder.getInstance().incrDirty();
        return result;
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
