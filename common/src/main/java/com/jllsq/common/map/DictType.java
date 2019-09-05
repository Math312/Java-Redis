package com.jllsq.common.map;

import com.jllsq.common.sds.SDS;

public interface DictType<U, T> {

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

    U keyDup(U key);

    T valueDup(T value);

    int keyCompare(U key1, U key2);

    void keyDestructor(U key);

    void valueDestructor(T value);

}
