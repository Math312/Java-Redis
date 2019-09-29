package com.jllsq.log;

import com.jllsq.common.BasicFileWriter;
import com.jllsq.common.basic.sds.SDS;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.util.AofUtil;
import com.jllsq.holder.RedisServerObjectHolder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.jllsq.common.util.config.ByteArrayUtil.byteArrayToInt;
import static com.jllsq.common.util.config.ByteBufUtil.readLine;
import static com.jllsq.config.Constants.*;
import static com.jllsq.holder.RedisServerObjectHolder.REDIS_ENCODING_RAW;
import static com.jllsq.holder.RedisServerObjectHolder.REDIS_STRING;

/**
 * @author yanlishao
 */
public class RedisAofLog extends BasicFileWriter {

    private RedisAofLog() {
    }

    enum RedisAofLogEnum {
        /**
         * RedisAofLog singleton implementation
         * */
        INSTANCE;

        RedisAofLogEnum() {
            redisAofLog = new RedisAofLog();
        }

        private RedisAofLog redisAofLog;
    }

    public static RedisAofLog getInstance() {
        return RedisAofLogEnum.INSTANCE.redisAofLog;
    }

    public void write(RedisClient client) throws IOException {
        byte[] bytes = AofUtil.encode(client);
        if (logFile != null) {
            Files.write(Paths.get(logFile), bytes, StandardOpenOption.APPEND);
        }
    }

    public void applyAofLog(RedisClient client) throws IOException {
        if (client.getArgv().length > 0) {
            RedisObject commandObject = client.getArgv()[0];
            SDS commandSds = (SDS)(commandObject.getPtr());
            if (commandSds.equals("expire")) {
                RedisObject object = client.getArgv()[2];
                long expires = (long) object.getPtr();
                object.setPtr(new SDS(expires+""));
                object.setShared(false);
                object.setEncoding(REDIS_ENCODING_RAW);
                commandSds = new SDS("expireat");
                commandObject.setPtr(commandSds);
                write(client);
            }else {
                write(client);
            }
        }
    }

    public List<RedisClient> readClient() throws Exception {
        byte[] bytes = Files.readAllBytes(Paths.get(logFile));
        ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
        return redisClients(byteBuf);
    }

    public static List<RedisClient> redisClients(ByteBuf in) throws Exception {
        RedisServerObjectHolder holder = RedisServerObjectHolder.getInstance();
        List<RedisClient> redisClients = new LinkedList<>();
        while (in.isReadable()) {
            RedisClient client = new RedisClient();
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
            client.setDictId(0);
            client.setArgc(length);
            client.setArgv(argv);
            redisClients.add(client);
        }
        return redisClients;
    }

}
