package com.jllsq.command;

import com.jllsq.common.basic.sds.SDS;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.holder.RedisServerObjectHolder;
import lombok.Data;

import java.io.Serializable;

@Data
public abstract class RedisCommand implements Serializable {

    private static final long serialVersionUID = -3642545448143222676L;
    protected RedisCommandClientHandlerChain handlerChain;
    protected RedisServerObjectHolder redisServerObjectHolder;

    private SDS name;
    private int arity;

    public RedisCommand(SDS name, int arity) {
        this.name = name;
        this.arity = arity;
        this.redisServerObjectHolder = RedisServerObjectHolder.getInstance();
        initChain();
    }

    public abstract RedisObject process(RedisClient client);

    public void recycleRedisObject(RedisClient client) {
        redisServerObjectHolder.deleteObject(client.getArgv()[0]);
    }

    public void initChain() {
        this.handlerChain = new RedisCommandClientHandlerChain();
    }
}
