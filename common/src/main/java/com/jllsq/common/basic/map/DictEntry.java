package com.jllsq.common.basic.map;


public class DictEntry<U,T> implements Cloneable {

    private U key;
    private T value;
    private DictEntry<U,T> next;

    public DictEntry(U key, T value) {
        this.key = key;
        this.value = value;
        this.next = null;
    }

    public DictEntry(U key, T value,DictEntry<U,T> nextEntry) {
        this.key = key;
        this.value = value;
        this.next = nextEntry;
    }


    public U getKey() {
        return key;
    }

    public void setKey(U key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public DictEntry<U, T> getNext() {
        return next;
    }

    public void setNext(DictEntry<U, T> next) {
        this.next = next;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        DictEntry<U,T> dictEntry = new DictEntry(this.getKey(),this.getValue(),this.next);
        return dictEntry;
    }
}
