package com.jllsq.common.util.config;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;

public class ByteArrayUtil {



    public static int byteArrayToInt(byte[] array) throws Exception {
        int result = 0;
        for (int i = 0;i < array.length;i ++){
            int num = 0;
            if (array[i]>='0' && array[i] <= '9'){
                num = array[i]-'0';
            }else {
                System.out.println(array[i]);
                throw new Exception();
            }
            result = result*10+num;
        }
        return result;
    }
}
