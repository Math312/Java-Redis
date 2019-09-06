package com.jllsq.common.map;

import com.jllsq.common.sds.SDS;

public class KeyptrDictType<U extends SDS,T> implements DictType<U,T> {

    @Override
    public T valueDup(T value) {
        return value;
    }

    @Override
    public void keyDestructor(U key) {

    }

    @Override
    public void valueDestructor(T value) {

    }
}
