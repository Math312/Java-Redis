package com.jllsq.command.impl;

import com.jllsq.command.RedisCommand;
import com.jllsq.command.handler.impl.RedisCommandAofHandler;
import com.jllsq.command.handler.impl.RedisCommandCheckParamNumsHandler;
import com.jllsq.command.handler.impl.RedisCommandInitClientHandler;
import com.jllsq.command.handler.impl.RedisCommandProcessHandler;
import com.jllsq.common.basic.sds.SDS;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.util.IntegerUtil;
import com.jllsq.config.Shared;
import com.jllsq.holder.RedisServerDbHolder;
import com.jllsq.holder.RedisServerStateHolder;

public class SelectCommand extends RedisCommand {

    public SelectCommand() {
        super(new SDS("SELECT"), 1);
    }

    @Override
    public RedisObject process(RedisClient client) {
        SDS sds = (SDS) client.getArgv()[1].getPtr();
        int index = 0;
        try {
            index = IntegerUtil.bytesToInt(sds.getContentBytes(),0,sds.getUsed());
            if (index < 0 || index >= RedisServerDbHolder.getInstance().getDbNum()) {
                throw new IllegalArgumentException();
            }
        }catch (IllegalArgumentException e) {
            return Shared.getInstance().getSyntaxerr();
        }
        RedisServerDbHolder.getInstance().setSelectedDbIndex(index);
        RedisServerStateHolder.getInstance().incrDirty();
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
