package com.jllsq.codec;

import com.jllsq.common.entity.RedisClient;
import com.jllsq.holder.RedisServerObjectHolder;
import com.jllsq.log.RedisAofLog;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.Arrays;
import java.util.List;

import static com.jllsq.config.Constants.*;

public class RedisObjectDecoder extends ByteToMessageDecoder {

    private boolean isComplete = false;

    private RedisClient currentRedisClient;

    private int recordNum;

    private byte[] headBuffer;

    private boolean headReadComplete;

    private byte[] contentBuffer;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            RedisServerObjectHolder holder = RedisServerObjectHolder.getInstance();
            out.addAll(RedisAofLog.redisClients(in));
        } catch (IndexOutOfBoundsException e) {
            in.resetReaderIndex();
        }
    }

    private void createNewRedisClient() {
        this.currentRedisClient = new RedisClient();
    }

    int readLength(ByteBuf in) throws Exception {
        if (in.readByte() != STAR) {
            System.out.println("error");
        }
        byte[] head = readLine(in);
        if (head[head.length - 2] != CR || head[head.length - 1] != LF) {
            System.out.println("error");
        }
        byte[] num = Arrays.copyOf(head, head.length - 2);
        return byteArrayToInt(num);
    }

    byte[] readLine(ByteBuf in) {
        byte[] buff = new byte[128];
        byte readByte;
        int i = 0;
        do {
            readByte = in.readByte();
            buff[i] = readByte;
            i++;
        } while (readByte != '\n');
        return Arrays.copyOf(buff, i);
    }

    int byteArrayToInt(byte[] array) throws Exception {
        int result = 0;
        for (int i = 0; i < array.length; i++) {
            int num = 0;
            if (array[i] >= '0' && array[i] <= '9') {
                num = array[i] - '0';
            } else {
                throw new Exception();
            }
            result = result * 10 + num;
        }
        return result;
    }
}
