package com.jllsq.command.handler;

import com.jllsq.command.RedisCommand;
import com.jllsq.command.RedisCommandClientHandlerChain;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;

public interface RedisCommandClientHandler {

    RedisObject handle(RedisClient client, RedisCommand command,RedisCommandClientHandlerChain chain);
}
