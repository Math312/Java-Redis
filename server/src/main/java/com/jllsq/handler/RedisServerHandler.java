package com.jllsq.handler;

import com.jllsq.RedisServer;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.sds.SDS;
import com.jllsq.handler.command.RedisCommand;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.config.Shared;
import com.jllsq.handler.command.RedisCommandEnum;
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
        client.setDb(redisServer.getDb()[client.getDictId()]);
        RedisCommand command = RedisCommandEnum.getCommandByKey((SDS) (client.getArgv()[0].getPtr())).getCommand();
        RedisObject response = null;
        if (command != null) {
            response = command.process(client);
        } else {
            response = Shared.getInstance().getSyntaxerr();
        }
        context.writeAndFlush(response)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
