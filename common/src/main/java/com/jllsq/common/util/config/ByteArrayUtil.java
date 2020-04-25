package com.jllsq.common.util.config;

public class ByteArrayUtil {

    public static int byteArrayToInt(byte[] array,int start,int end) throws Exception {
        int result = 0;
        for (int i = start;i < end;i ++){
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
