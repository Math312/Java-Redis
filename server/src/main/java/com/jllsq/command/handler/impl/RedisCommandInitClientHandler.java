package com.jllsq.command.handler.impl;

import com.jllsq.command.RedisCommand;
import com.jllsq.command.RedisCommandClientHandlerChain;
import com.jllsq.command.handler.RedisCommandClientHandler;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.holder.RedisServerDbHolder;

public class RedisCommandInitClientHandler implements RedisCommandClientHandler {
    @Override
    public RedisObject handle(RedisClient client, RedisCommand command, RedisCommandClientHandlerChain chain) {
        client.setDictId(RedisServerDbHolder.getInstance().getSelectedDbIndex());
        client.setDb(RedisServerDbHolder.getInstance().getDb()[client.getDictId()]);
        return chain.doHandle(client,command);
    }
}
