package com.jllsq.common.map;
import java.util.Iterator;

public class Dict<U,T> implements Iterable<DictEntry<U,T>>{
    private DictEntry<U,T>[] table;
    private DictType<U,T> type;
    private int size;
    private int sizeMask;
    private int used;
    private Object privateData;
    private  static final int INIT_SIZE = 4;

    public <U,T> Dict(DictType type, Object privateData){
        this.table = new DictEntry[4];
        this.size = 4;
        this.sizeMask = this.size - 1;
        this.used = 0;
        this.privateData = privateData;
        this.type = type;
    }



    @Override
    public Iterator<DictEntry<U,T>> iterator() {
        return new DictEntryIterator<>(this);
    }

    public Dict expand(int size) {
        this.table = new DictEntry[size];
        this.sizeMask = this.size - 1;
        Iterator<DictEntry<U,T>> iterator = this.iterator();
        int hash;
        while (iterator.hasNext()){
            DictEntry<U,T> dictEntry = iterator.next();
            this.add(this.type.keyDup(dictEntry.getKey()),this.type.valueDup(dictEntry.getValue()));
        }
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
        expandIfNecessary();
        int hash = hashFunction(key) & sizeMask;
        DictEntry<U,T> entry = table[hash];
        DictEntry<U,T> newEntry = null;
        if (entry == null){
            newEntry = new DictEntry<>(this.type.keyDup(key),this.type.valueDup(value));
        }else {
            newEntry = new DictEntry<>(this.type.keyDup(key),this.type.valueDup(value),entry);
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

    public boolean delete(U key) {
        int hash = hashFunction(key);
        DictEntry<U,T> entry = table[hash];
        DictEntry<U,T> last = null;
        while (entry != null) {
            if (this.type.keyCompare(key,entry.getKey()) == 0){
                if (last == null) {
                    table[hash] = null;
                    this.type.keyDestructor(entry.getKey());
                    this.type.valueDestructor(entry.getValue());
                } else {
                    last.setNext(entry.getNext());
                    this.type.keyDestructor(entry.getKey());
                    this.type.valueDestructor(entry.getValue());
                }
                return true;
            }else {
                last = entry;
                entry = entry.getNext();
            }
        }
        return false;
    }

    public DictEntry<U, T> find(U key) {
        int hash = hashFunction(key);
        DictEntry<U,T> entry = table[hash];
        while (entry != null) {
            if (this.type.keyCompare(key,entry.getKey()) == 0){
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
        return this.type.hashFunction(key);
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
            int label = 0;
            if (entry == null) {
                label = 1;
            }
            while(entry == null) {
                index ++;
                entry = this.dict.table[index];
            }
            walked ++;
            if (label == 0) {
                entry = entry.getNext();
                return entry;
            }
            return entry;
        }
    }

    public int dictGenHashFunction(byte[] buf) {
        int hash = 5381;
        for (int i = 0;i < buf.length;i ++) {
            hash = ((hash << 5) + hash) + buf[i];
        }
        return hash;
    }

}
