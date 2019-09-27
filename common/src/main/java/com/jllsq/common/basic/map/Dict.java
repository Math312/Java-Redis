package com.jllsq.common.basic.map;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Random;

public class Dict<U,T> implements Iterable<DictEntry<U,T>>, Serializable {

    private static final long serialVersionUID = 7887265299375772551L;

    private DictEntry<U,T>[] table;
    private int size;
    private int sizeMask;
    private int used;
    private Object privateData;
    private  static final int INIT_SIZE = 4;

    public <U,T> Dict(Object privateData){
        this.table = new DictEntry[4];
        this.size = 4;
        this.sizeMask = this.size - 1;
        this.used = 0;
        this.privateData = privateData;
    }

    @Override
    public Iterator<DictEntry<U,T>> iterator() {
        return new DictEntryIterator<>(this);
    }

    public Dict expand(int size) {
        DictEntry[] temp = new DictEntry[size];
        Dict<U,T> dict = SerializationUtils.clone(this);
        int sizeMask = size - 1;
        Iterator<DictEntry<U,T>> iterator = dict.iterator();
        int hash;
        while (iterator.hasNext()){
            DictEntry<U,T> dictEntry = iterator.next();
            try {
                DictEntry<U,T> newEntry = (DictEntry<U,T>)dictEntry.clone();
                int hash2 = hashFunction(dictEntry.getKey()) & sizeMask;
                if (temp[hash2] == null) {
                    temp[hash2] = newEntry;
                } else {
                    DictEntry<U,T> tempEntry = temp[hash2];
                    temp[hash2] = ((DictEntry) (newEntry));
                    temp[hash2].setNext(tempEntry);
                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        this.table = temp;
        this.sizeMask = sizeMask;
        this.size = size;
        return this;
    }

    public boolean expandIfNecessary() {
        if (this.size == Integer.MAX_VALUE) {
            return false;
        }
        if (this.size == 0) {
            expand(INIT_SIZE);
        } else if (this.size == this.used) {
            if (size > Integer.MAX_VALUE / 2){
                expand(Integer.MAX_VALUE);
            }else {
                expand(this.size * 2);
            }
        }
        return true;
    }

    public boolean add(U key, T value) {
        if (this.size == Integer.MAX_VALUE) {
            return false;
        }
        if (find(key) != null){
            replace(key,value);
            return true;
        }
        expandIfNecessary();
        int hash = hashFunction(key) & sizeMask;
        DictEntry<U,T> entry = table[hash];
        DictEntry<U,T> newEntry = null;
        if (entry == null){
            newEntry = new DictEntry<>(key,value);
        }else {
            newEntry = new DictEntry<>(key,value,entry);
        }
        table[hash] = newEntry;
        this.used ++;
        return true;
    }

    public boolean replace(U key, T value) {
        DictEntry<U,T> dictEntry = find(key);
        if (dictEntry != null) {
            dictEntry.setValue(value);
            return true;
        }
        return false;
    }

    public DictEntry<U,T> delete(U key) {
        int hash = hashFunction(key);
        DictEntry<U,T> entry = table[hash];
        DictEntry<U,T> last = null;
        while (entry != null) {
            if (key.equals(entry.getKey())){
                if (last == null) {
                    table[hash] = null;
                } else {
                    last.setNext(entry.getNext());
                }
                this.used --;
                return entry;
            }else {
                last = entry;
                entry = entry.getNext();
            }
        }
        return null;
    }

    public DictEntry<U, T> find(U key) {
        int hash = hashFunction(key) & sizeMask;
        DictEntry<U,T> entry = table[hash];
        while (entry != null) {
            if (key.equals(entry.getKey())){
                return entry;
            }else {
                entry = entry.getNext();
            }
        }
        return null;
    }

    public void clear() {
        for (int i = 0;i < this.size;i ++) {
            this.table[i] = null;
        }
    }


    public int hashFunction(U key) {
        return key.hashCode() & sizeMask;
    }

    public int getUsed() {
        return used;
    }

    public int getSize() {
        return size;
    }



    public class DictEntryIterator<U,T> implements Iterator<DictEntry<U,T>> {

        private int index = -1;
        private int walked = 0;
        private DictEntry<U,T> entry = null;
        private Dict<U,T> dict;
        public <U,T> DictEntryIterator(Dict dict){
            this.dict = dict;
        }

        @Override
        public boolean hasNext() {
            return walked < this.dict.used;
        }

        @Override
        public DictEntry<U, T> next() {
            if (entry != null && entry.getNext() != null) {
                entry = entry.getNext();
                walked ++;
            }
            else {
                index ++;
                if (this.dict.table[index] != null) {
                    entry = this.dict.table[index];
                    walked ++;
                } else {
                    entry = next();
                }
            }
            return entry;
        }
    }

    public DictEntry<U,T> dictGetRandomKey() {
        DictEntry<U,T> result = null;
        if (this.used == 0) {
            return null;
        }
        int randomIndex = 0;
        Random random = new Random();
        do {
            randomIndex = random.nextInt(this.size);
            result = table[randomIndex];
        } while (result == null);
        int length = 0;
        while (result != null){
            result = result.getNext();
            length ++;
        }
        result =table[randomIndex];
        randomIndex = random.nextInt(length);
        while (randomIndex > 0){
            result = result.getNext();
        }
        return result;
    }

//    public int dictGenHashFunction(byte[] buf) {
//        int hash = 5381;
//        for (int i = 0;i < buf.length;i ++) {
//            hash = ((hash << 5) + hash) + buf[i];
//        }
//        return hash;
//    }

}
