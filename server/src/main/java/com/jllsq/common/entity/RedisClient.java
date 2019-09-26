package com.jllsq.common.entity;

import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class RedisClient {

    private Channel channel;
    private RedisDb db;
    private int dictId;
    private int argc;
    private RedisObject[] argv;

    public RedisClient() {

    }

}
