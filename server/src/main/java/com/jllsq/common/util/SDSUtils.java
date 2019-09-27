package com.jllsq.common.util;

import com.jllsq.common.basic.sds.SDS;

/**
 * @author yanlishao
 */
public class SDSUtils {

    public static boolean sdsIsLong(SDS sds) {
        if (sds == null) {
            return false;
        }
        if (sds.getUsed() <= 0) {
            return false;
        }
        byte[] bytes = sds.getBytes();
        return ByteUtil.bytesIsLong(bytes,sds.getUsed());
    }

}
