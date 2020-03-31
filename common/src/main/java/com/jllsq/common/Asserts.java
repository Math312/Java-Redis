package com.jllsq.common;

public class Asserts {

    public static void isNotNull(Object object){
        if (object == null) {
            throw new NullPointerException();
        }
    }

    public static void isNotNull(Object object,String message){
        if (object == null) {
            throw new NullPointerException(message);
        }
    }

    public static void unsupportedProcession(String message) {
        throw new UnsupportedOperationException(message);
    }
}
