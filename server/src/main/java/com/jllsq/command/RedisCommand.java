package com.jllsq.command;

import com.jllsq.common.basic.sds.SDS;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import lombok.Data;

import java.io.Serializable;

@Data
public abstract class RedisCommand implements Serializable {

    private static final long serialVersionUID = -3642545448143222676L;
    protected RedisCommandClientHandlerChain handlerChain;

    private SDS name;
    private int arity;

    public RedisCommand(SDS name, int arity) {
        this.name = name;
        this.arity = arity;
        initChain();
    }

    public abstract RedisObject process(RedisClient client);

    public abstract void recycleRedisObject(RedisClient client);

    public void initChain() {
        this.handlerChain = new RedisCommandClientHandlerChain();
    }
}
