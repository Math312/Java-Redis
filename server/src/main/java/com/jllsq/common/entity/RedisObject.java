package com.jllsq.common.entity;

import lombok.Data;

@Data
public class RedisObject implements Cloneable,Comparable<RedisObject> {

    public static final byte REDIS_ENCODING_RAW = 0;
    public static final byte REDIS_ENCODING_INT = 1;
    public static final byte REDIS_ENCODING_ZIPMAP = 2;
    public static final byte REDIS_ENCODING_HT = 3;

    public static final byte REDIS_STRING = 0;
    public static final byte REDIS_LIST = 1;
    public static final byte REDIS_SET = 2;
    public static final byte REDIS_ZSET = 3;
    public static final byte REDIS_HASH = 4;

    private boolean isShared;
    private Object ptr;
    private byte type;
    private byte encoding;
    private char storage;
    private int refCount;

    public RedisObject(){}

    public RedisObject(boolean isShared,byte type,Object ptr) {
        this.isShared = isShared;
        this.type = type;
        this.ptr = ptr;
        this.encoding = REDIS_ENCODING_RAW;
        this.refCount = 1;
    }

    public RedisObject(boolean isShared,byte type,Object ptr, byte encoding) {
        this.isShared = isShared;
        this.type = type;
        this.ptr = ptr;
        this.encoding = encoding;
        this.refCount = 1;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int hashCode(){
        return ptr.hashCode();
    }

    @Override
    public int compareTo(RedisObject redisObject) {
        if (redisObject.type != this.type && redisObject.encoding != this.encoding) {
            throw new ClassCastException();
        }
        if (ptr instanceof Comparable) {
            return ((Comparable) ptr).compareTo(redisObject.getPtr());
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof RedisObject)) {
            return false;
        }
        if (this.type != ((RedisObject) obj).type) {
            return false;
        }
        if (this.encoding != ((RedisObject) obj).encoding) {
            return false;
        }
        return ptr.equals(((RedisObject) obj).ptr);
    }
}