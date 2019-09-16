package com.jllsq.common.util;

import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.sds.SDS;
import org.apache.commons.lang3.Conversion;
import org.apache.commons.lang3.SerializationUtils;

public class AofUtil {

//    public SDS[] decode(byte[] data) {
//
//    }

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
        result[0] = '*';
        int index = 1;
        Conversion.intToByteArray(length, 0, result, 1, len);
        for (int j = 0;j < len;j ++) {
            result[j+index]+='0';
        }
        index = len + 1;
        result[index]='\r';
        index ++;
        result[index]='\n';
        index ++;
        for (int i = 0; i < length; i++) {
            result[index] = '$';
            index++;
            SDS sds = ((SDS) (client.getArgv()[i].getPtr()));
            Conversion.intToByteArray(sds.getUsed(), 0, result, index, lens[i]);
            for (int j = 0;j < lens[i];j ++) {
                result[j+index]+='0';
            }
            index += lens[i];
            result[index] = '\r';
            index ++;
            result[index] = '\n';
            index ++;
            System.arraycopy(sds.getBytes(),0,result,index,sds.getUsed());
            index += sds.getUsed();
            result[index] = '\r';
            index ++;
            result[index] = '\n';
            index ++;
        }
        return result;
    }

}
