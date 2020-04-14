package com.jllsq.common.basic.map;

import com.jllsq.common.entity.RedisObject;

public class DictEntry {

    private RedisObject key;
    private RedisObject value;
    private DictEntry next;

    public DictEntry(RedisObject key, RedisObject value) {
        this.key = key;
        this.value = value;
        this.next = null;
    }

    public DictEntry(RedisObject key, RedisObject value,DictEntry nextEntry) {
        this.key = key;
        this.value = value;
        this.next = nextEntry;
    }


    public RedisObject getKey() {
        return key;
    }

    public void setKey(RedisObject key) {
        this.key = key;
    }

    public RedisObject getValue() {
        return value;
    }

    public void setValue(RedisObject value) {
        this.value = value;
    }

    public DictEntry getNext() {
        return next;
    }

    public void setNext(DictEntry next) {
        this.next = next;
    }

}
