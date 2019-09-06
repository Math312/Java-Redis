package com.jllsq.common.entity;

import lombok.Data;

@Data
public class RedisObject implements Cloneable,Comparable {

    public static final byte REDIS_ENCODING_RAW = 0;
    public static final byte REDIS_ENCODING_INT = 1;
    public static final byte REDIS_ENCODING_ZIPMAP = 2;
    public static final byte REDIS_ENCODING_HT = 3;

    public static final byte REDIS_STRING = 0;
    public static final byte REDIS_LIST = 1;
    public static final byte REDIS_SET = 2;
    public static final byte REDIS_ZSET = 3;
    public static final byte REDIS_HASH = 4;

    private Object ptr;
    private byte type;
    private byte encoding;
    private char storage;
    private int refCount;

    public RedisObject(){}

    public RedisObject(byte type,Object ptr) {
        this.type = type;
        this.ptr = ptr;
        this.encoding = REDIS_ENCODING_RAW;
        this.refCount = 1;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
