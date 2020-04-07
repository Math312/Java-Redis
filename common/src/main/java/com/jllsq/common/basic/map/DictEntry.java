package com.jllsq.common.basic.map;

public class DictEntry {

    private Object key;
    private Object value;
    private DictEntry next;

    public DictEntry(Object key, Object value) {
        this.key = key;
        this.value = value;
        this.next = null;
    }

    public DictEntry(Object key, Object value,DictEntry nextEntry) {
        this.key = key;
        this.value = value;
        this.next = nextEntry;
    }


    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public DictEntry getNext() {
        return next;
    }

    public void setNext(DictEntry next) {
        this.next = next;
    }

}
