package com.jllsq.holder;

import com.jllsq.common.entity.RedisObject;

import java.util.LinkedList;
import java.util.List;

public class RedisServerObjectHolder {

    public static final byte REDIS_ENCODING_RAW = 0;
    public static final byte REDIS_ENCODING_INT = 1;
    public static final byte REDIS_ENCODING_ZIPMAP = 2;
    public static final byte REDIS_ENCODING_HT = 3;

    public static final byte REDIS_STRING = 0;
    public static final byte REDIS_LIST = 1;
    public static final byte REDIS_SET = 2;
    public static final byte REDIS_ZSET = 3;
    public static final byte REDIS_HASH = 4;

    private List<RedisObject> freeList;

    private RedisServerObjectHolder() {
        this.freeList = new LinkedList<>();
    }

    public RedisObject createObject(boolean isShared, byte type, Object ptr) {
        RedisObject result = null;
        if (freeList.size() > 0) {
            result = freeList.get(0);
            result.setEncoding(REDIS_ENCODING_RAW);
            result.setPtr(ptr);
            result.setType(type);
            result.setShared(isShared);
            result.setRefCount(0);
            freeList.remove(0);
        } else {
            result = new RedisObject(isShared,type,ptr,REDIS_ENCODING_RAW);
        }
        return result;
    }

    public boolean contains(RedisObject object) {
        return freeList.contains(object);
    }


    public RedisObject createObject(boolean isShared, byte type, Object ptr,byte encoding) {
        RedisObject result = null;
        if (freeList.size() > 0) {
            result = freeList.get(0);
            result.setEncoding(encoding);
            result.setPtr(ptr);
            result.setType(type);
            result.setShared(isShared);
            result.setRefCount(0);
            freeList.remove(0);
        } else {
            result = new RedisObject(isShared,type,ptr,encoding);
        }
        return result;
    }

    public int getFreeListSize() {
        return freeList.size();
    }

    public void deleteObject(RedisObject object) {
        if (!freeList.contains(object)) {
            this.freeList.add(object);
        }
    }

    public static RedisServerObjectHolder getInstance() {
        return RedisServerObjectHolderEnum.INSTANCE.redisServerObjectHolder;
    }

    enum RedisServerObjectHolderEnum {
        INSTANCE;

        private RedisServerObjectHolder redisServerObjectHolder;

        RedisServerObjectHolderEnum() {
            this.redisServerObjectHolder = new RedisServerObjectHolder();
        }
    }


}
