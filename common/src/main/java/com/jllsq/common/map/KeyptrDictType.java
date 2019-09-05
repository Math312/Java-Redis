package com.jllsq.common.map;

public class KeyptrDictType<U,T> implements DictType<U,T> {
    @Override
    public U keyDup(U key) {
        return null;
    }

    @Override
    public T valueDup(T value) {
        return null;
    }

    @Override
    public int keyCompare(U key1, U key2) {
        return 0;
    }

    @Override
    public void keyDestructor(U key) {

    }

    @Override
    public void valueDestructor(T value) {

    }
}
