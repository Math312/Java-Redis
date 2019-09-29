package com.jllsq.command.processor;

import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;

/**
 * @author yanlishao
 */
public interface RedisCommandProcessor{

    public RedisObject process(RedisClient client);

}
