package com.jllsq.common.util;

import static com.jllsq.config.Constants.*;

public class ByteUtil {

    public static boolean bytesIsLong(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return false;
        }
        int start = 0;
        if (bytes[0] == SUB) {
            start = 1;
        }
        for (int i = start;i < bytes.length;i ++) {
            if (bytes[i] > NINE_CHAR && bytes[i] < ZERO_CHAR){
                return false;
            }
        }
        return true;
    }
}
