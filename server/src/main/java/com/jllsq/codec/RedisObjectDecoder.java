package com.jllsq.codec;

import com.jllsq.holder.RedisServerObjectHolder;
import com.jllsq.log.RedisAofLog;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.Arrays;
import java.util.List;

public class RedisObjectDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        RedisServerObjectHolder holder = RedisServerObjectHolder.getInstance();
        out.addAll(RedisAofLog.redisClients(in));
    }

    byte[] readLine(ByteBuf in){
        byte[] buff = new byte[128];
        byte readByte;
        int i = 0;
        do {
            readByte = in.readByte();
            buff[i] = readByte;
            i ++;
        }while (readByte != '\n');
        return Arrays.copyOf(buff,i);
    }

    int byteArrayToInt(byte[] array) throws Exception {
        int result = 0;
        for (int i = 0;i < array.length;i ++){
            int num = 0;
            if (array[i]>='0' && array[i] <= '9'){
                num = array[i]-'0';
            }else {
                throw new Exception();
            }
            result = result*10+num;
        }
        return result;
    }
}
