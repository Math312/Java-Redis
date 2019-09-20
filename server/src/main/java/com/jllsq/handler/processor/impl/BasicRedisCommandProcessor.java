package com.jllsq.handler.processor.impl;

import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.sds.SDS;
import com.jllsq.config.Shared;
import com.jllsq.handler.command.RedisCommand;
import com.jllsq.handler.command.RedisCommandEnum;
import com.jllsq.handler.processor.RedisCommandProcessor;
import com.jllsq.holder.RedisServerDbHolder;
import com.jllsq.holder.RedisServerStateHolder;
import com.jllsq.log.RedisAofLog;

import java.io.IOException;
import java.util.Map;

public class BasicRedisCommandProcessor extends RedisCommandProcessor {

    @Override
    public boolean preHandle(RedisClient client, Map<String, Object> shared) {
        int dirty = RedisServerStateHolder.getInstance().getDirty();
        client.setDb(RedisServerDbHolder.getInstance().getDb()[client.getDictId()]);
        shared.put("dirty",dirty);
        return true;
    }

    @Override
    public Object afterComplete(RedisClient client, Map<String, Object> shared) {
        int dirty = (int) shared.get("dirty");
        if (RedisServerStateHolder.getInstance().getDirty() != dirty) {
            try {
                RedisAofLog.getInstance().applyAofLog(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return shared.get("response");
    }

    @Override
    public boolean processing(RedisClient client, Map<String, Object> shared) {
        RedisCommandEnum commandEnum = RedisCommandEnum.getCommandByKey((SDS) (client.getArgv()[0].getPtr()));
        RedisObject response = null;
        if (commandEnum != null) {
            if (checkCommandArgsNum(client,commandEnum.getCommand())) {
                response = commandEnum.getCommand().process(client);
            } else {
                response = Shared.getInstance().getSyntaxerr();
            }
        } else {
            response = Shared.getInstance().getSyntaxerr();
        }
        shared.put("response",response);
        return true;
    }

    private boolean checkCommandArgsNum(RedisClient client, RedisCommand command) {
        if (client.getArgc() == command.getArity()+1){
            return true;
        }
        return false;
    }
}
