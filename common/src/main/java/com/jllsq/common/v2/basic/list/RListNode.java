package com.jllsq.common.v2.basic.list;

public interface RListNode<E> {

    Object clone();

    int compareTo(E o);

    Object free();

}


