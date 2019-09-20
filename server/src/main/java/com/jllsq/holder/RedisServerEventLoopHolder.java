package com.jllsq.holder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;

import java.util.concurrent.ScheduledFuture;

public class RedisServerEventLoopHolder {

    private ServerBootstrap bootstrap;
    private EventLoopGroup eventLoopGroup;
    private ScheduledFuture scheduledFuture;
    private ChannelFuture channelFuture;


    public ServerBootstrap getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(ServerBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public EventLoopGroup getEventLoopGroup() {
        return eventLoopGroup;
    }

    public void setEventLoopGroup(EventLoopGroup eventLoopGroup) {
        this.eventLoopGroup = eventLoopGroup;
    }

    public ScheduledFuture getScheduledFuture() {
        return scheduledFuture;
    }

    public void setScheduledFuture(ScheduledFuture scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    public void setChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

    private RedisServerEventLoopHolder() {

    }

    public static RedisServerEventLoopHolder getInstance() {
        return RedisServerEventLoopHolderEnum.INSTANCE.redisServerEventLoopHolder;
    }

    enum RedisServerEventLoopHolderEnum {
        INSTANCE;

        private RedisServerEventLoopHolder redisServerEventLoopHolder;

        RedisServerEventLoopHolderEnum() {
            this.redisServerEventLoopHolder = new RedisServerEventLoopHolder();
        }
    }

}
