package com.jllsq.handler;

import com.jllsq.RedisServer;
import com.jllsq.command.processor.RedisCommandProcessor;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.command.processor.impl.BasicRedisCommandProcessor;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

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
//        for (int i = 0;i < client.getArgv().length;i ++) {
//            client.getArgv()[i].destructor();
//            client.getArgv()[i] = null;
//        }
        context.writeAndFlush(response)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
