package com.jllsq.common.v2.basic.list.impl;

import com.jllsq.common.Asserts;
import com.jllsq.common.v2.basic.Cloneable;
import com.jllsq.common.v2.basic.Freeable;
import com.jllsq.common.v2.basic.list.RListNode;

public class RBasicListNode<E> implements RListNode<E> {

    E value;

    RBasicListNode<E> pre;

    RBasicListNode<E> next;


    @Override
    public Object clone() {
        if (this.value instanceof Cloneable) {
            return ((Cloneable) this.value).clone();
        }
        Asserts.unsupportedProcession("数据不允许克隆");
        return null;
    }

    @Override
    public int compareTo(E o) {
        if (this.value instanceof Comparable){
            return ((Comparable) this.value).compareTo(o);
        }
        Asserts.unsupportedProcession("数据不允许克隆");
        return 0;
    }

    @Override
    public Object free() {
        if (this.value instanceof Freeable) {
            return ((Freeable) this.value).free();
        }
        return null;
    }
}
