package com.jllsq.common.util.config;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;

public class ByteBufUtil {

    public static byte[] readLine(ByteBuf in){
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

    public static int readLineToExistedBuffer(ByteBuf in,byte[] buff){
        byte readByte;
        int i = 0;
        do {
            readByte = in.readByte();
            buff[i] = readByte;
            i ++;
        }while (readByte != '\n');
        return i;
    }
}
