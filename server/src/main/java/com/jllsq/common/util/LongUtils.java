package com.jllsq.common.util;

import static com.jllsq.config.Constants.*;

public class LongUtils {

    public static int longToBytesLength(long num) {
        int index = 1;
        if (num < 0) {
            index ++;
        }
        while (num / TEN != 0) {
            index ++;
            num = num / TEN;
        }
        return index;
    }

    public static byte[] longToBytes(long num) {
        int len = longToBytesLength(num);
        byte[] result = new byte[len];
        int start = 0;
        if (num < 0) {
            start ++;
            result[0] = SUB;
            num = -num;
        }
        for (int i = len;i > start;i --) {
            long temp = num % TEN;
            result[i-1] = (byte)(temp+ZERO_CHAR);
            num = num / TEN;
        }
        return result;
    }

    public static void longToBytes(long num,int numStartIndex,byte[] target,int index,int len) {
        byte[] longByteArray = longToBytes(num);
        System.arraycopy(longByteArray,numStartIndex,target,index,len);
    }

}
