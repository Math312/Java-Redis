package com.jllsq.codec.impl;

import com.jllsq.codec.RedisClientDecoder;
import com.jllsq.common.basic.sds.SDS;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.holder.RedisServerObjectHolder;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.jllsq.common.util.config.ByteArrayUtil.byteArrayToInt;
import static com.jllsq.common.util.config.ByteBufUtil.readLine;
import static com.jllsq.config.Constants.*;
import static com.jllsq.holder.RedisServerObjectHolder.REDIS_STRING;

public class RedisClientDecoderImpl implements RedisClientDecoder {

    @Override
    public List<RedisClient> decodeToRedisClientList(ByteBuf byteBuf) throws Exception {
        RedisServerObjectHolder holder = RedisServerObjectHolder.getInstance();
        List<RedisClient> redisClients = new LinkedList<>();
        while (byteBuf.isReadable()) {
            RedisClient client = new RedisClient();
            if (byteBuf.readByte() != STAR) {
                System.out.println("error");
            }
            byte[] head = readLine(byteBuf);
            if (head[head.length - 2] != CR || head[head.length - 1] != LF) {
                System.out.println("error");
            }
            byte[] num = Arrays.copyOf(head, head.length - 2);
            int length = byteArrayToInt(num);
            RedisObject[] argv = new RedisObject[length];
            for (int i = 0; i < length; i++) {
                byte dollar = byteBuf.readByte();
                if (dollar != '$') {
                    System.out.println("error");
                }
                byte[] commandArgv = readLine(byteBuf);
                if (commandArgv[commandArgv.length - 2] != CR || commandArgv[commandArgv.length - 1] != LF) {
                    System.out.println("error");
                }
                num = Arrays.copyOf(commandArgv, commandArgv.length - 2);
                int commandLen = byteArrayToInt(num);
                ByteBuf command = byteBuf.readBytes(commandLen);
                byte[] buff = new byte[commandLen];
                command.getBytes(0, buff);
                argv[i] = holder.createObject(false, REDIS_STRING, new SDS(buff));
                ByteBuf nextLine = byteBuf.readBytes(2);
                if (nextLine.getByte(0) != CR || nextLine.getByte(1) != LF) {
                    System.out.println("error");
                }
            }
            client.setArgc(length);
            client.setArgv(argv);
            redisClients.add(client);
        }
        return redisClients;
    }
}