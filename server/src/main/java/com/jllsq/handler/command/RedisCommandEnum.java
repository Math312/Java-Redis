package com.jllsq.handler.command;

import com.jllsq.common.sds.SDS;
import com.jllsq.handler.command.impl.*;

public enum RedisCommandEnum {

    GET_COMMAND(new SDS("get"),new GetCommand()),
    SET_COMMAND(new SDS("set"),new SetCommand()),
    EXPIRE_COMMAND(new SDS("expire"),new ExpireCommand()),
    EXISTS_COMMAND(new SDS("exists"),new ExistsCommand()),
    COMMAND_COMMAND(new SDS("command"),new CommandCommand()),
    APPEND_COMMAND(new SDS("append"),new AppendCommand()),
    TTL_COMMAND(new SDS("ttl"),new TtlCommand());

    private SDS key;
    private RedisCommand command;

    RedisCommandEnum(SDS key, RedisCommand command){
        this.key = key;
        this.command = command;
    }

    public static RedisCommandEnum getCommandByKey(SDS sds) {
        for (RedisCommandEnum commandEnum:RedisCommandEnum.values()) {
            if (commandEnum.key.getContent().equalsIgnoreCase(sds.getContent())) {
                return commandEnum;
            }
        }
        return null;
    }

    public RedisCommand getCommand() {
        return command;
    }
}
