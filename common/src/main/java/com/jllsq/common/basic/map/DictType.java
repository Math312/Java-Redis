package com.jllsq.common.basic.map;

import com.jllsq.common.basic.RedisClonable;
import com.jllsq.common.basic.sds.SDS;

public interface DictType<U extends RedisClonable & Comparable, T> {

     default int hashFunction(U key) {
         byte[] buf = null;
         if (key instanceof SDS){
             buf = ((SDS) key).getBytes();
         }else {
             buf = new SDS(key.toString()).getBytes();
         }
         int hash = 5381;
         for (int i = 0;i < buf.length;i ++) {
             hash = ((hash << 5) + hash) + buf[i];
         }
         return hash;
     }

    default U keyDup(U key) {
         return (U)key.cloneDeep();
    }

    T valueDup(T value);

    default int keyCompare(U key1, U key2) {
         return key1.compareTo(key2);
    }

    void keyDestructor(U key);

    void valueDestructor(T value);

}
