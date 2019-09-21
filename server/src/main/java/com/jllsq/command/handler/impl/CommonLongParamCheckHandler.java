package com.jllsq.command.handler.impl;

import com.jllsq.command.RedisCommand;
import com.jllsq.command.RedisCommandClientHandlerChain;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.sds.SDS;
import com.jllsq.common.util.ByteUtil;
import com.jllsq.config.Shared;

/**
 * @author yanlishao
 */
public class CommonLongParamCheckHandler extends RedisCommandParamCheckHandler {

    @Override
    public RedisObject handle(RedisClient client, RedisCommand command, RedisCommandClientHandlerChain chain) {
        SDS sds = (SDS) client.getArgv()[2].getPtr();
        if (ByteUtil.bytesIsLong(sds.getBytes())) {
            return super.handle(client, command, chain);
        }else {
            return Shared.getInstance().getSyntaxerr();
        }
    }
}
