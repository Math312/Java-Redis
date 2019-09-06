package com.jllsq.common.map;

import com.jllsq.common.RedisClonable;
import com.jllsq.common.sds.SDS;

public class DbDictType<U extends SDS, T extends RedisClonable> implements DictType<U, T> {

    @Override
    public U keyDup(U key) {
        SDS keySds = new SDS();
        keySds.setUsed(((SDS) key).getUsed());
        byte[] content = new byte[((SDS) key).getLength()];
        for (int i = 0; i < ((SDS) key).getLength(); i++) {
            content[i] = ((SDS) key).getBytes()[i];
        }
        keySds.setBytes(content);
        return (U) keySds;
    }

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
