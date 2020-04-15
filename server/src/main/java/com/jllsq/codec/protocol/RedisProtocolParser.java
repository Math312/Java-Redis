package com.jllsq.codec.protocol;

import com.jllsq.common.basic.sds.SDS;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.util.config.ByteBufUtil;
import com.jllsq.holder.RedisServerObjectHolder;
import com.jllsq.holder.buffer.RedisServerByteBufferHolder;
import com.jllsq.holder.buffer.entity.BasicBuffer;
import io.netty.buffer.ByteBuf;

import static com.jllsq.common.util.config.ByteArrayUtil.byteArrayToInt;
import static com.jllsq.config.Constants.*;
import static com.jllsq.holder.RedisServerObjectHolder.REDIS_STRING;

public class RedisProtocolParser {

    public static RedisClient readProtocolToExistedRedisClient(ByteBuf in,RedisClient redisClient) throws Exception {
        RedisServerObjectHolder holder = RedisServerObjectHolder.getInstance();
        RedisClient rs = null;
        if (in.isReadable()) {
            if (redisClient == null) {
                rs = new RedisClient();
            } else {
                rs = redisClient;
            }
            rs.clearArgs();
            if (in.readByte() != STAR) {
                System.out.println("error");
            }
            byte[] readLineBuffer = RedisServerByteBufferHolder.getInstance().getReadLineBuffer();
            int readLineBufferLength = ByteBufUtil.readLineToExistedBuffer(in,readLineBuffer);
            if (readLineBuffer[readLineBufferLength - 2] != CR || readLineBuffer[readLineBufferLength - 1] != LF) {
                System.out.println("error");
            }
            int length = byteArrayToInt(readLineBuffer,0,readLineBufferLength-2);
            RedisObject[] argv = new RedisObject[length];
            for (int i = 0; i < length; i++) {
                byte dollar = in.readByte();
                if (dollar != '$') {
                    System.out.println("error");
                }
                readLineBufferLength = ByteBufUtil.readLineToExistedBuffer(in,readLineBuffer);
                if (readLineBuffer[readLineBufferLength - 2] != CR || readLineBuffer[readLineBufferLength - 1] != LF) {
                    System.out.println("error");
                }
                int commandLen = byteArrayToInt(readLineBuffer,0,readLineBufferLength-2);
                ByteBuf command = in.readBytes(commandLen);
                byte[] buff = new byte[commandLen];
                command.getBytes(0, buff);
                argv[i] = holder.createObject(false, REDIS_STRING, new SDS(buff));
                ByteBuf nextLine = in.readBytes(2);
                if (nextLine.getByte(0) != CR || nextLine.getByte(1) != LF) {
                    System.out.println("error");
                }
            }
            in.resetReaderIndex();
            RedisServerByteBufferHolder byteBufferHolder = RedisServerByteBufferHolder.getInstance();
            BasicBuffer basicBuffer = byteBufferHolder.getRedisClientBufferFromPool(in.writerIndex());
            in.readBytes(basicBuffer.getBuffer(),0,in.writerIndex());
            basicBuffer.setUsed(in.writerIndex());
            rs.setBuffer(basicBuffer);
            rs.setArgc(length);
            rs.setArgv(argv);
        }
        return rs;
    }

    public static RedisClient readProtocolToNewRedisClient(ByteBuf in) throws Exception {
        return readProtocolToExistedRedisClient(in,null);
    }

}
