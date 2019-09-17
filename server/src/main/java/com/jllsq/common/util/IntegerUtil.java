package com.jllsq.common.util;

public class IntegerUtil {

    public static boolean byteIsInt(byte[] bytes) {
        for (int i = 0;i < bytes.length;i ++) {
            if (bytes[i] >= '9' || bytes[i] <= '0'){
                return false;
            }
        }
        return true;
    }

    public static int intToBytesLength(int num) {
        int index = 1;
        while (num / 10 != 0) {
            index ++;
            num = num % 10;
        }
        return index;
    }

}
