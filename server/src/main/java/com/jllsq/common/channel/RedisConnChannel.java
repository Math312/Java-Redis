package com.jllsq.common.channel;

import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.UUID;

public class RedisConnChannel extends NioServerSocketChannel {

    private String id;

    public RedisConnChannel() {
        super();
        this.id = UUID.randomUUID().toString()+Thread.currentThread().getId();
    }

}
