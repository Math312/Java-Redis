package com.jllsq.common.basic.map;

import com.jllsq.common.entity.RedisObject;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Random;

public class Dict implements Iterable<DictEntry>, Serializable {

    private static final long serialVersionUID = 7887265299375772551L;

    private DictEntry[] table;
//    private DictEntry[] table2;
//    private volatile boolean expanding = false;
    private int size;
    private int sizeMask;
    private int used;
    private  static final int INIT_SIZE = 2;

    public Dict() {
        table = new DictEntry[INIT_SIZE];
        size = INIT_SIZE;
        sizeMask = size - 1;
        used = 0;
    }

    @Override
    public Iterator<DictEntry> iterator() {
        return new DictEntryIterator(this);
    }

    public Dict expand(int size) {
        DictEntry[] temp = new DictEntry[size];
        DictEntry[] dict = this.table;
        int sizeMask = size - 1;
        for (int i = 0;i < dict.length;i ++) {
            DictEntry head = dict[i];
            if (head != null) {
                DictEntry tempEntry = head;
                while (tempEntry != null) {
                    DictEntry nexEntry = tempEntry.getNext();
                    int hash = hashFunction(tempEntry.getKey()) & sizeMask;
                    DictEntry existedEntry = temp[hash];
                    if (existedEntry == null) {
                        tempEntry.setNext(null);
                        temp[hash] = tempEntry;
                    } else {
                        tempEntry.setNext(existedEntry);
                        temp[hash] = tempEntry;
                    }
                    tempEntry = nexEntry;
                }
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

    public boolean add(RedisObject key, RedisObject value) {
        if (this.size == Integer.MAX_VALUE) {
            return false;
        }
        if (find(key) != null){
            replace(key,value);
            return true;
        }
        expandIfNecessary();
//        System.out.println("Ready to Expand");
        int hash = hashFunction(key) & sizeMask;
        DictEntry entry = table[hash];
        DictEntry newEntry = null;
//        System.out.println("Ready to Add To map");
        if (entry == null){
            newEntry = new DictEntry(key,value);
        }else {
            newEntry = new DictEntry(key,value,entry);
        }
        table[hash] = newEntry;
        this.used ++;
        return true;
    }

    public boolean replace(RedisObject key, RedisObject value) {
        DictEntry dictEntry = find(key);
        if (dictEntry != null) {
            dictEntry.setValue(value);
            return true;
        }
        return false;
    }

    public DictEntry delete(Object key) {
        int hash = hashFunction(key);
        DictEntry entry = table[hash];
        DictEntry last = null;
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

    public DictEntry find(Object key) {
        int hash = hashFunction(key) & sizeMask;
        DictEntry entry = table[hash];
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


    public int hashFunction(Object key) {
        return key.hashCode() & sizeMask;
    }

    public int getUsed() {
        return used;
    }

    public int getSize() {
        return size;
    }



    public class DictEntryIterator implements Iterator<DictEntry> {

        private int index = -1;
        private int walked = 0;
        private DictEntry entry = null;
        private Dict dict;
        public DictEntryIterator(Dict dict){
            this.dict = dict;
        }

        @Override
        public boolean hasNext() {
            return walked < this.dict.used;
        }

        @Override
        public DictEntry next() {
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

    public DictEntry dictGetRandomKey() {
        DictEntry result = null;
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
