package com.jllsq.common.util;

import com.jllsq.config.Constants;

public class ByteUtil {

    public static boolean bytesIsLong(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return false;
        }
        int start = 0;
        if (bytes[0] == Constants.SUB) {
            start = 1;
        }
        for (int i = start;i < bytes.length;i ++) {
            if (bytes[i] > Constants.NINE_CHAR || bytes[i] < Constants.ZERO_CHAR){
                return false;
            }
        }
        return true;
    }

    public static boolean bytesIsLong(byte[] bytes,int length) {
        if (bytes == null || bytes.length == 0 || length == 0) {
            return false;
        }
        int start = 0;
        if (bytes[0] == Constants.SUB) {
            start = 1;
        }
        for (int i = start;i < length;i ++) {
            if (bytes[i] > Constants.NINE_CHAR || bytes[i] < Constants.ZERO_CHAR){
                return false;
            }
        }
        return true;
    }

    public static long byteToLong(byte[] bytes,int length) {
        if (!bytesIsLong(bytes,length)) {
            throw new IllegalArgumentException();
        }
        long result = 0;
        int start = 0;
        if (bytes[0] == Constants.SUB) {
            start = 1;
        }
        for (int i = start;i < length;i ++) {
            result *= 10;
            result += bytes[i] - '0';
        }
        if (start == 1) {
            result = -result;
        }
        return result;
    }
}
