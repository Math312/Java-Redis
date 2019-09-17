package com.jllsq.common.entity;

import com.jllsq.holder.RedisServerObjectHolder;
import lombok.Data;

@Data
public class RedisObject implements Cloneable,Comparable<RedisObject> {
    private boolean isShared;
    private Object ptr;
    private byte type;
    private byte encoding;
    private char storage;
    private int refCount;

    public RedisObject(){}

    public RedisObject(boolean isShared,byte type,Object ptr, byte encoding) {
        this.isShared = isShared;
        this.type = type;
        this.ptr = ptr;
        this.encoding = encoding;
        this.refCount = 1;
    }

    public void destructor() {
        RedisServerObjectHolder.getInstance().deleteObject(this);
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
