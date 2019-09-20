package com.jllsq.handler;

import com.jllsq.RedisServer;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.sds.SDS;
import com.jllsq.handler.command.RedisCommand;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.config.Shared;
import com.jllsq.handler.command.RedisCommandEnum;
import com.jllsq.handler.processor.RedisCommandProcessor;
import com.jllsq.handler.processor.impl.BasicRedisCommandProcessor;
import com.jllsq.holder.RedisServerConfigHolder;
import com.jllsq.holder.RedisServerDbHolder;
import com.jllsq.holder.RedisServerStateHolder;
import com.jllsq.log.RedisAofLog;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;

@ChannelHandler.Sharable
public class RedisServerHandler extends ChannelInboundHandlerAdapter {

    private RedisServer redisServer;

    public RedisServerHandler(RedisServer redisServer) {
        super();
        this.redisServer = redisServer;
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object message){
        RedisClient client = (RedisClient) message;
        RedisCommandProcessor processor = new BasicRedisCommandProcessor();
        RedisObject response = (RedisObject) processor.process(client);
        context.writeAndFlush(response)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
