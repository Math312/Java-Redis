package com.jllsq.common.map;

import com.jllsq.common.sds.SDS;

public interface DictType<U, T> {

    int hashFunction(U key);

    U keyDup(U key);

    T valueDup(T value);

    int keyCompare(U key1, U key2);

    void keyDestructor(U key);

    void valueDestructor(T value);

}
