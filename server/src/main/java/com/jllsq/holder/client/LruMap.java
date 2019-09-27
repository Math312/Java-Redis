package com.jllsq.holder.client;

import java.util.*;

public class LruMap<U,T> extends AbstractMap<U,T> {

    public static interface LruProcess<U,T> {
        Object process(T value);
    }

    Map<U,TimeNode<U,T>> table;
    LinkedList<TimeNode<U,T>> times;

    public LruMap() {
        this.table = new HashMap<>();
        this.times = new LinkedList<>();
    }

    public T put(U key,T value) {
        TimeNode<U,T> timeNode = new TimeNode<>(key,value);
        TimeNode<U,T> oldNode = table.get(key);
        if (oldNode != null) {
            this.times.remove(oldNode);
        }
        table.put(key,timeNode);
        times.add(timeNode);
        return value;
    }

    @Override
    public Set<Entry<U, T>> entrySet() {
        Set<Entry<U, TimeNode<U, T>>> set = this.table.entrySet();
        Set<Entry<U,T>> result = new HashSet<>();
        for (Entry<U,TimeNode<U,T>> entry:set) {
            result.add(entry.getValue());
        }
        return result;
    }

    @Override
    public void clear() {
        this.times.clear();
        this.table.clear();
    }

    @Override
    public T get(Object o) {
        TimeNode<U,T> node = this.table.get(o);
        if (node != null) {
            times.remove(node);
            node.updateTime();
            times.add(node);
            return node.value;
        }else {
            return null;
        }
    }

    public void delete(U key) {
        TimeNode<U,T> removed = table.remove(key);
        if (removed != null) {
            times.remove(removed);
        }
    }

    public void process(U key,LruProcess<U,T> process) {
        if (this.times.size() > 0) {
            TimeNode<U,T> node = this.table.get(key);
            if (node != null) {
                process.process(node.value);
                this.times.remove(node);
                node.updateTime();
                this.times.add(node);
            }
        }
    }


    public T getNoRefreshTime(U key) {
        return table.get(key).value;
    }

    public long getRefreshTime(U key) {
        return table.get(key).time;
    }

    public U getLastKey() {
        if (this.times.size() > 0) {
            return this.times.getLast().key;
        }
        return null;
    }

    public void deleteLast() {
        U key = getLastKey();
        if (key != null) {
            this.table.remove(key);
            this.times.remove(key);
        }
    }

    class TimeNode<U,T> implements Entry<U,T>{
        long time;
        U key;
        T value;

        TimeNode(U key,T value) {
            this.time = System.currentTimeMillis();
            this.key = key;
            this.value = value;
        }

        void updateTime() {
            this.time = System.currentTimeMillis();
        }

        @Override
        public U getKey() {
            return key;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public T setValue(T t) {
            value = t;
            return value;
        }
    }

}
