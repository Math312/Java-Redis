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
        RedisServerByteBufferHolder byteBufferHolder = RedisServerByteBufferHolder.getInstance();
        if (in.isReadable()) {
            BasicBuffer basicBuffer = byteBufferHolder.getRedisClientBufferFromPool(in.writerIndex());
            in.readBytes(basicBuffer.getBuffer(),0,in.writerIndex());
            basicBuffer.setUsed(in.writerIndex());
            if (redisClient == null) {
                rs = new RedisClient();
            } else {
                rs = redisClient;
            }
            rs.clearArgs();
            byte[] buffer = basicBuffer.getBuffer();
            if (buffer[0] != STAR) {
                System.out.println("error");
            }
            int index = 1;
            int used = basicBuffer.getUsed();
            int endIndex = ByteBufUtil.readLineFromBuffer(basicBuffer.getBuffer(),index,basicBuffer.getUsed());
            if (buffer[endIndex - 2] != CR || buffer[endIndex - 1] != LF) {
                System.out.println("error");
            }
            int length = byteArrayToInt(buffer,index,endIndex-2);
            RedisObject[] argv = new RedisObject[length];
            index = endIndex;
            for (int i = 0; i < length; i++) {
                byte dollar = buffer[index];
                if (dollar != '$') {
                    System.out.println("error");
                }
                index ++;
                endIndex = ByteBufUtil.readLineFromBuffer(buffer,index,used);
                if (buffer[endIndex - 2] != CR || buffer[endIndex - 1] != LF) {
                    System.out.println("error");
                }
                int commandLen = byteArrayToInt(buffer,index,endIndex-2);
                index = endIndex;
                byte[] buff = new byte[commandLen];
                System.arraycopy(buffer,index,buff,0,commandLen);
                argv[i] = holder.createObject(false, REDIS_STRING, new SDS(buff));
                index += commandLen;
                if (buffer[index] != CR || (index != used-1 && buffer[index+1] != LF)) {
                    System.out.println("error");
                }
                index += 2;
            }
            rs.setBuffer(basicBuffer);
            rs.setArgc(length);
            rs.setArgv(argv);
        }
//        System.out.println(redisClient);
        return rs;
    }

    public static RedisClient readProtocolToNewRedisClient(ByteBuf in) throws Exception {
        return readProtocolToExistedRedisClient(in,null);
    }

}
