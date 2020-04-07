package com.jllsq.codec.protocol;

import com.jllsq.common.basic.sds.SDS;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.holder.RedisServerObjectHolder;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;

import static com.jllsq.common.util.config.ByteArrayUtil.byteArrayToInt;
import static com.jllsq.common.util.config.ByteBufUtil.readLine;
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
            byte[] head = readLine(in);
            if (head[head.length - 2] != CR || head[head.length - 1] != LF) {
                System.out.println("error");
            }
            byte[] num = Arrays.copyOf(head, head.length - 2);
            int length = byteArrayToInt(num);
            RedisObject[] argv = new RedisObject[length];
            for (int i = 0; i < length; i++) {
                byte dollar = in.readByte();
                if (dollar != '$') {
                    System.out.println("error");
                }
                byte[] commandArgv = readLine(in);
                if (commandArgv[commandArgv.length - 2] != CR || commandArgv[commandArgv.length - 1] != LF) {
                    System.out.println("error");
                }
                num = Arrays.copyOf(commandArgv, commandArgv.length - 2);
                int commandLen = byteArrayToInt(num);
                ByteBuf command = in.readBytes(commandLen);
                byte[] buff = new byte[commandLen];
                command.getBytes(0, buff);
                argv[i] = holder.createObject(false, REDIS_STRING, new SDS(buff));
                ByteBuf nextLine = in.readBytes(2);
                if (nextLine.getByte(0) != CR || nextLine.getByte(1) != LF) {
                    System.out.println("error");
                }
            }
            rs.setArgc(length);
            rs.setArgv(argv);
        }
        return rs;
    }

    public static RedisClient readProtocolToNewRedisClient(ByteBuf in) throws Exception {
        return readProtocolToExistedRedisClient(in,null);
    }

}
