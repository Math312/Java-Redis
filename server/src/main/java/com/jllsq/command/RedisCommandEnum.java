package com.jllsq.command;

//public enum RedisCommandEnum {
//
//    GET_COMMAND(new SDS("get"),new GetCommand()),
//    SET_COMMAND(new SDS("set"),new SetCommand()),
//    DEL_COMMAND(new SDS("del"),new DelCommand()),
//    EXPIRE_COMMAND(new SDS("expire"),new ExpireCommand()),
//    EXPIREAT_COMMAND(new SDS("expireat"),new ExpireatCommand()),
//    EXISTS_COMMAND(new SDS("exists"),new ExistsCommand()),
//    COMMAND_COMMAND(new SDS("command"),new CommandCommand()),
//    APPEND_COMMAND(new SDS("append"),new AppendCommand()),
//    TTL_COMMAND(new SDS("ttl"),new TtlCommand()),
//    PING_COMMAND(new SDS("ping"),new PingCommand()),
//    AUTH_COMMAND(new SDS("auth"),new AuthCommand()),
//    KEYS_COMMAND(new SDS("keys"),new KeysCommand()),
//    INCR_COMMAND(new SDS("incr"),new IncrCommand());
//
//    private SDS key;
//    private RedisCommand command;
//
//    RedisCommandEnum(SDS key, RedisCommand command){
//        this.key = key;
//        this.command = command;
//    }
//
//    public static RedisCommandEnum getCommandByKey(SDS sds) {
//        for (RedisCommandEnum commandEnum:RedisCommandEnum.values()) {
//            if (commandEnum.key.getContent().equalsIgnoreCase(sds.getContent())) {
//                return commandEnum;
//            }
//        }
//        return null;
//    }
//
//    public RedisCommand getCommand() {
//        return command;
//    }
//}
