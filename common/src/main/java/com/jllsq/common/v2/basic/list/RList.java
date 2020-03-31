package com.jllsq.common.v2.basic.list;

public interface RList<E> extends Iterable<E> {

    int length();

    E getFirst();

    E getLast();

    E getByIndex(int index);

    RList<E> dup();

    void clear();

    E addFirst(E value);

    E addLast(E value);

    E add(E value,int index);

    boolean contain(E value);

    int getFirstIndex(E value);

    int getLastIndex(E value);

    E removeFirst();

    E removeLast();

    E removeByIndex(int index);
}
