package com.jllsq.command.impl;

import com.jllsq.command.RedisCommand;
import com.jllsq.command.handler.impl.RedisCommandAofHandler;
import com.jllsq.command.handler.impl.RedisCommandCheckParamNumsHandler;
import com.jllsq.command.handler.impl.RedisCommandInitClientHandler;
import com.jllsq.command.handler.impl.RedisCommandProcessHandler;
import com.jllsq.common.basic.map.DictEntry;
import com.jllsq.common.basic.sds.SDS;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.util.ByteUtil;
import com.jllsq.common.util.LongUtils;
import com.jllsq.config.Shared;

import static com.jllsq.holder.RedisServerObjectHolder.*;
import static com.jllsq.holder.RedisServerObjectHolder.REDIS_STRING;

public class DecrCommand extends RedisCommand {

    public DecrCommand() {
        super(new SDS("DECR"),1);
    }

    @Override
    public RedisObject process(RedisClient client) {
        DictEntry<RedisObject,RedisObject> value = client.getDb().getDict().find(client.getArgv()[1]);
        RedisObject result = null;
        if (value == null) {
            result = Shared.getInstance().getNokeyerr();
        }else {
            RedisObject object =  value.getValue();
            if (object.getEncoding() == REDIS_ENCODING_INT) {
                long num = (long)object.getPtr()-1;
                object.setPtr(num);
                result = new RedisObject();
                result.setEncoding(REDIS_ENCODING_INT);
                result.setType(REDIS_STRING);
                result.setPtr(num);
            }else if (object.getEncoding() == REDIS_ENCODING_RAW) {
                if (object.getType() == REDIS_STRING) {
                    SDS sds = ((SDS)object.getPtr());
                    try {
                        long num = ByteUtil.byteToLong(sds.getBytes(),sds.getUsed());
                        num -= 1;
                        sds.setBytes(LongUtils.longToBytes(num));
                        result = new RedisObject();
                        result.setEncoding(REDIS_ENCODING_INT);
                        result.setType(REDIS_STRING);
                        result.setPtr(num);
                    } catch (IllegalArgumentException e) {
                        result = Shared.getInstance().getWrongtypeerr();
                    }
                }
            }

        }
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
