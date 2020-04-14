package com.jllsq.common.entity;

import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;


@Data
@AllArgsConstructor
@ToString
public class RedisClient {

    private Channel channel;
    private RedisDb db;
    private int dictId;
    private int argc;
    private RedisObject[] argv;

    public RedisClient() {

    }

    public void clearArgs() {
        this.argc = 0;
        this.argv = null;
    }

}
