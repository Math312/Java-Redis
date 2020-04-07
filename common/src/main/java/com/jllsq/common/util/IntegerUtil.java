package com.jllsq.common.util;

import java.nio.ByteBuffer;

import static com.jllsq.config.Constants.*;

/**
 * @author Yanli Shao
 */
public class IntegerUtil {


    public byte[] intToComplementArray(int num) {
        byte[] result = new byte[4];
        result[0] = (byte) ((num >> 24) & 0xFF);
        result[1] = (byte) ((num >> 16) & 0xFF);
        result[2] = (byte) ((num >> 8) & 0xFF);
        result[3] = (byte) (num & 0xFF);
        return result;
    }

    public static int complementArrayToInt(byte[] byteArray) {
        return ByteBuffer.wrap(byteArray).getInt();
    }


    public static boolean byteIsInt(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return false;
        }
        int start = 0;
        if (bytes[0] == SUB){
            if (bytes.length == 1) {
                return false;
            }
            start = 1;
        }

        for (int i = start;i < bytes.length;i ++) {
            if (bytes[i] >= NINE_CHAR || bytes[i] <= ZERO_CHAR){
                return false;
            }
        }
        return true;
    }

    public static boolean byteIsInt(byte[] bytes,int start, int length) {
        if (bytes == null || bytes.length == 0) {
            return false;
        }
        if (bytes[start] == SUB){
            if (length == 1) {
                return false;
            }
            start = 1;
        }
        for (int i = start;i < length;i ++) {
            if (bytes[i] > NINE_CHAR || bytes[i] < ZERO_CHAR){
                return false;
            }
        }
        return true;
    }

    public static int intToBytesLength(int num) {
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

    public static byte[] numToByteArray(int num) {
        int len = intToBytesLength(num);
        byte[] result = new byte[len];
        int start = 0;
        if (num < 0) {
            start ++;
            result[0] = SUB;
            num = -num;
        }
        for (int i = len;i > start;i --) {
            int temp = num % TEN;
            result[i-1] = (byte)(temp+ZERO_CHAR);
            num = num / TEN;
        }
        return result;
    }

    public static int bytesToInt(byte[] bytes,int start,int length) {
        if (byteIsInt(bytes,start,length)) {
            int sum = 0;
            for (int i = 0;i < length;i ++) {
                sum *= 10;
                sum += (bytes[start+i]-ZERO_CHAR);
            }
            return sum;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static int getMaxIntegerLenInBytes(byte[] bytes,int start) {
        int length = 0;
        if (start < 0) {
            throw new IllegalArgumentException();
        }
        while(start+length < bytes.length && bytes[start + length] <= NINE_CHAR && bytes[start + length] >= ZERO_CHAR) {
            length ++;
        }
        return length;
    }





}
