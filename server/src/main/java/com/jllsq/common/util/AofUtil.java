package com.jllsq.common.util;

import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.basic.sds.SDS;

import static com.jllsq.config.Constants.*;

/**
 * @author yanlishao
 */
public class AofUtil {

    public static boolean checkCrlf(byte[] bytes,int start) {
        return bytes[start] != CR || bytes[start + 1] != LF;
    }

    public SDS[] decode(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        int index = 0;
        if (data[0] != STAR) {
            return null;
        }
        index ++;
        int length = IntegerUtil.getMaxIntegerLenInBytes(data,index);
        int len = IntegerUtil.bytesToInt(data,index,length);
        index += length;
        if (checkCrlf(data, index)) {
            return null;
        }
        index += 2;
        SDS[] result = new SDS[len];
        for (int i = 0;i < len;i ++) {
            if (data[index] != DOLLAR) {
                return null;
            }
            int innerLen = IntegerUtil.getMaxIntegerLenInBytes(data,index);;
            int commandLen = IntegerUtil.bytesToInt(data,index,innerLen);
            index += innerLen;
            if (checkCrlf(data, index)) {
                return null;
            }
            index += 2;
            byte[] bytes =new byte[innerLen];
            System.arraycopy(data,index,bytes,0,commandLen);
            SDS sds = new SDS(bytes);
            result[i] = sds;
            if (checkCrlf(data, index)) {
                return null;
            }
            index += 2;
        }
        return result;
    }

    public static byte[] encode(RedisClient client) {
        if (client == null) {
            return null;
        }
        int length = client.getArgv().length;
        int len = IntegerUtil.intToBytesLength(length);
        int lenTotal = len;
        int[] lens = new int[length];
        /* Add the length of "*" and "\r\n" */
        lenTotal = lenTotal + 3;
        for (int i = 0; i < length; i++) {
            SDS sds = ((SDS) (client.getArgv()[i].getPtr()));
            lenTotal += 1;
            int argvLen = sds.getUsed();
            lens[i] = IntegerUtil.intToBytesLength(argvLen);
            lenTotal += lens[i];
            lenTotal += 2;
            lenTotal += argvLen;
            lenTotal += 2;
        }
        byte[] result = new byte[lenTotal];
        result[0] = STAR;
        int index = 1;
        LongUtils.longToBytes(length, 0, result, 1, len);
        index = len + 1;
        result[index]=CR;
        index ++;
        result[index]=LF;
        index ++;
        for (int i = 0; i < length; i++) {
            result[index] = '$';
            index++;
            SDS sds = ((SDS) (client.getArgv()[i].getPtr()));
            LongUtils.longToBytes(sds.getUsed(), 0, result, index, lens[i]);
            index += lens[i];
            result[index] = CR;
            index ++;
            result[index] = LF;
            index ++;
            System.arraycopy(sds.getBytes(),0,result,index,sds.getUsed());
            index += sds.getUsed();
            result[index] = CR;
            index ++;
            result[index] = LF;
            index ++;
        }
        return result;
    }

}
