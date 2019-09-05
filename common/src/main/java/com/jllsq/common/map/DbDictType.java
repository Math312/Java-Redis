package com.jllsq.common.map;

import com.jllsq.common.sds.SDS;

public class DbDictType<U,T> implements DictType<U,T> {

    @Override
    public U keyDup(U key) {
        SDS keySds = new SDS();
        if (key instanceof SDS) {
            keySds.setUsed(((SDS) key).getUsed());
            byte[] content = new byte[((SDS) key).getLength()];
            for (int i = 0;i < ((SDS) key).getLength();i ++) {
                content[i] = ((SDS) key).getBytes()[i];
            }
            keySds.setBytes(content);
        } else {
            keySds = new SDS(key.toString());
        }

        return (U) keySds;
    }

    @Override
    public T valueDup(T value) {
        SDS keySds = new SDS();
        if (value instanceof SDS) {
            keySds.setUsed(((SDS) value).getUsed());
            byte[] content = new byte[((SDS) value).getLength()];
            for (int i = 0;i < ((SDS) value).getLength();i ++) {
                content[i] = ((SDS) value).getBytes()[i];
            }
            keySds.setBytes(content);
        } else {
            keySds = new SDS(value.toString());
        }

        return (T) keySds;
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
