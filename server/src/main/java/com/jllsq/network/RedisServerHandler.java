package com.jllsq.network;

import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisCommand;
import com.jllsq.common.entity.RedisCommandResponse;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.sds.SDS;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

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
        RedisCommand command = redisServer.getRedisCommandTable().get(client.getArgv()[0]);
        RedisCommandResponse response = command.process(client);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0;i < response.getResponse().length;i ++) {
            RedisObject redisObject = response.getResponse()[i];
            if (redisObject.getType() == RedisObject.REDIS_STRING) {
                context.write(Unpooled.copiedBuffer(((SDS)redisObject.getPtr()).getBytes()));
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
