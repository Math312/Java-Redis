package com.jllsq.common.map;

import com.jllsq.common.sds.SDS;

public class Entry {

    private SDS key;
    private Object value;

    public Entry(SDS key, Object value) {
        this.key = key;
        this.value = value;
    }

    public SDS getKey() {
        return key;
    }

    public void setKey(SDS key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
