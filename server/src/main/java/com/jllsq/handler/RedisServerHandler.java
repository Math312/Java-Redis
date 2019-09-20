package com.jllsq.handler;

import com.jllsq.RedisServer;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.sds.SDS;
import com.jllsq.handler.command.RedisCommand;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.config.Shared;
import com.jllsq.handler.command.RedisCommandEnum;
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
        int dirty = RedisServerStateHolder.getInstance().getDirty();
        client.setDb(RedisServerDbHolder.getInstance().getDb()[client.getDictId()]);
        RedisCommand command = RedisCommandEnum.getCommandByKey((SDS) (client.getArgv()[0].getPtr())).getCommand();
        RedisObject response = null;
        if (command != null) {
            response = command.process(client);
        } else {
            response = Shared.getInstance().getSyntaxerr();
        }
        if (RedisServerStateHolder.getInstance().getDirty() != dirty) {
            try {
                RedisAofLog.getInstance().applyAofLog(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
