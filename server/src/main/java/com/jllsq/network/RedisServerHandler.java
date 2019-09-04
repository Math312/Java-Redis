package com.jllsq.network;

import com.jllsq.common.entity.RedisCommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public class RedisServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext context, Object message){
        RedisCommand redisCommand = (RedisCommand) message;
        for (int i = 0;i < redisCommand.getArgc();i ++){
            System.out.println(redisCommand.getArgv()[i].getContent());
        }
        ByteBuf byteBuf = Unpooled.copiedBuffer("*1\r\n$2\r\nOK\r\n",CharsetUtil.UTF_8);
        context.write(byteBuf);
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
