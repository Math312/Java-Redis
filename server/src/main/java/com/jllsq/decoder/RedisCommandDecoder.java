package com.jllsq.decoder;

import com.jllsq.common.entity.RedisCommand;
import com.jllsq.common.sds.SDS;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.Arrays;
import java.util.List;

public class RedisCommandDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.isReadable()){
            RedisCommand redisCommand = new RedisCommand();
            if (in.readByte() != '*') {
                System.out.println("error");
            }
            byte[] head = readLine(in);
            if (head[head.length-2] !='\r' || head[head.length-1] !='\n') {
                System.out.println("error");
            }
            byte[] num = Arrays.copyOf(head,head.length-2);
            int length = byteArrayToInt(num);
            SDS[] argv = new SDS[length];
            for (int i = 0;i < length;i ++) {
                byte dollar = in.readByte();
                if (dollar != '$') {
                    System.out.println("error");
                }
                byte[] commandArgv = readLine(in);
                if (head[head.length-2] !='\r' || head[head.length-1] !='\n') {
                    System.out.println("error");
                }
                num = Arrays.copyOf(commandArgv,head.length-2);
                int commandLen = byteArrayToInt(num);
                ByteBuf command = in.readBytes(commandLen);
                byte[] buff = new byte[commandLen];
                command.getBytes(0,buff);
                argv[i] = new SDS(buff);
                System.out.println(argv[i].getContent());
                ByteBuf nextLine = in.readBytes(2);
            }
            out.add(redisCommand);
        }
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
