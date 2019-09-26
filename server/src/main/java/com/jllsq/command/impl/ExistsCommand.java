package com.jllsq.command.impl;

import com.jllsq.command.handler.impl.*;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisDb;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.basic.map.DictEntry;
import com.jllsq.common.basic.sds.SDS;
import com.jllsq.config.Shared;
import com.jllsq.command.RedisCommand;

public class ExistsCommand extends RedisCommand {

    public ExistsCommand() {
        super(new SDS("exists"), 1);
    }

    @Override
    public RedisObject process(RedisClient client) {
        RedisDb db = client.getDb();
        RedisObject result = null;
        DictEntry<RedisObject, RedisObject> entry = db.getDict().find(client.getArgv()[1]);
        if (entry != null) {
            result = Shared.getInstance().getCone();
        } else {
            result = Shared.getInstance().getCzero();
        }
        return result;
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
