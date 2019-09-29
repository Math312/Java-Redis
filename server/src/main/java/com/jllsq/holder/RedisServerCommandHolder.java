package com.jllsq.holder;

import com.jllsq.command.RedisCommand;
import com.jllsq.command.impl.*;
import com.jllsq.common.basic.sds.SDS;

import java.util.HashMap;
import java.util.Map;

public class RedisServerCommandHolder  {

    private Map<SDS, RedisCommand> commandMap;

    private RedisServerCommandHolder() {
        commandMap = new HashMap<>();
        commandMap.put(new SDS("get"),new GetCommand());
        commandMap.put(new SDS("set"),new SetCommand());
        commandMap.put(new SDS("del"),new DelCommand());
        commandMap.put(new SDS("expire"),new ExpireCommand());
        commandMap.put(new SDS("expireat"),new ExpireatCommand());
        commandMap.put(new SDS("exists"),new ExistsCommand());
        commandMap.put(new SDS("command"),new CommandCommand());
        commandMap.put(new SDS("append"),new AppendCommand());
        commandMap.put(new SDS("ttl"),new TtlCommand());
        commandMap.put(new SDS("ping"),new PingCommand());
        commandMap.put(new SDS("auth"),new AuthCommand());
        commandMap.put(new SDS("keys"),new KeysCommand());
        commandMap.put(new SDS("incr"),new IncrCommand());
        commandMap.put(new SDS("decr"),new DecrCommand());
    }


    enum RedisServerCommandHolderEnum {
        INSTANCE;

        private RedisServerCommandHolder redisServerCommandHolder;

        RedisServerCommandHolderEnum() {
            this.redisServerCommandHolder = new RedisServerCommandHolder();
        }
    }

    public static RedisServerCommandHolder getInstance() {
        return RedisServerCommandHolderEnum.INSTANCE.redisServerCommandHolder;
    }

    public RedisCommand getIgnoreCase(SDS key) {
        return commandMap.get(key.toLower());
    }


}
