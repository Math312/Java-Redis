package com.jllsq.common.map;

import com.jllsq.common.RedisClonable;
import com.jllsq.common.sds.SDS;

public class KeyListDictType<U extends SDS,T extends RedisClonable> implements DictType<U,T> {

    @Override
    public T valueDup(T value) {
        return (T)value.cloneDeep();
    }

    @Override
    public void keyDestructor(U key) {

    }

    @Override
    public void valueDestructor(T value) {

    }
}
