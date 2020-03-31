package com.jllsq.common.v2.basic.list.impl;

import com.jllsq.common.Asserts;
import com.jllsq.common.v2.basic.Cloneable;
import com.jllsq.common.v2.basic.list.RList;

import java.util.Iterator;

public class RBasicList<E> implements RList<E> {

    private RBasicListNode<E> head;

    private RBasicListNode<E> tail;

    private int length;

    public RBasicList() {
        this.head = null;
        this.tail = null;
        this.length = 0;
    }

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public E getFirst() {
        if (head != null) {
            return head.value;
        }
        return null;
    }

    @Override
    public E getLast() {
        if (tail != null) {
            return tail.value;
        }
        return null;
    }

    @Override
    public E getByIndex(int index) {
        if (index < 0) {
            Asserts.unsupportedProcession("index 必须大于等于0");
        }
        if (index >= this.length) {
            Asserts.unsupportedProcession("index 必须小于等于链表长度");
        }
        int i = 0;
        RBasicListNode<E> temp = head;
        while (i < index) {
            temp = temp.next;
            i++;
        }
        return temp.value;
    }

    @Override
    public RList<E> dup() {
        RList<E> list = new RBasicList<>();
        RBasicListNode<E> temp = head;
        while (temp != null) {
            E value = temp.value;
            if (value instanceof Cloneable) {
                list.addFirst((E) ((Cloneable) value).clone());
            } else {
                list.addFirst(value);
            }
            temp = temp.next;
        }
        return list;
    }

    @Override
    public void clear() {
        this.length = 0;
        this.head = null;
        this.tail = null;
    }

    @Override
    public E addFirst(E value) {
        RBasicListNode<E> node = new RBasicListNode<>();
        node.value = value;
        if (head == null) {
            head = node;
            tail = node;
        } else {
            node.next = head;
            head.pre = node;
            head = node;
        }
        length++;
        return this.head.value;
    }

    @Override
    public E addLast(E value) {
        RBasicListNode<E> node = new RBasicListNode<>();
        node.value = value;
        if (tail == null) {
            tail = node;
            head = node;
        } else {
            node.pre = tail;
            tail.next = node;
            tail = node;
        }
        length++;
        return this.tail.value;
    }

    @Override
    public E add(E value, int index) {
        if (index > length || index < 0) {
            Asserts.unsupportedProcession("index 越界");
        }
        if (index == 0 || length == 0) {
            return addFirst(value);
        } else if (index == length) {
            return addLast(value);
        } else {
            RBasicListNode<E> node = new RBasicListNode<>(), temp;
            node.value = value;
            if (index > this.length / 2) {
                temp = tail;
                int i = length - 1;
                while (i > index + 1) {
                    temp = temp.pre;
                    i--;
                }
                node.next = temp;
                node.pre = temp.pre;
                temp.pre.next = node;
                temp.pre = node;
            } else {
                temp = head;
                int i = 0;
                while (i < index - 1) {
                    temp = temp.next;
                    i++;
                }
                node.pre = temp;
                node.next = temp.next;
                temp.next.pre = node;
                temp.next = node;
            }
            return temp.value;
        }
    }

    @Override
    public boolean contain(E value) {
        RBasicListNode<E> temp = head;
        while (temp != null) {
            if (temp.value.equals(value)) {
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    @Override
    public int getFirstIndex(E value) {
        RBasicListNode<E> temp = head;
        int i = 0;
        while (temp != null) {
            if (temp.value.equals(value)) {
                return i;
            }
            temp = temp.next;
            i++;
        }
        return -1;
    }

    @Override
    public int getLastIndex(E value) {
        RBasicListNode<E> temp = tail;
        int i = length - 1;
        while (temp != null) {
            if (temp.value.equals(value)) {
                return i;
            }
            temp = temp.pre;
            i--;
        }
        return -1;
    }

    @Override
    public E removeFirst() {
        if (head == null) {
            return null;
        }
        RBasicListNode<E> temp = head;
        if (length == 1) {
            this.head = null;
            this.tail = null;
            return temp.value;
        }
        head = head.next;
        head.pre = null;
        return temp.value;
    }

    @Override
    public E removeLast() {
        if (tail == null) {
            return null;
        }
        RBasicListNode<E> temp = tail;
        if (length == 1) {
            this.head = null;
            this.tail = null;
            return temp.value;
        }
        tail = tail.pre;
        tail.next = null;
        return temp.value;
    }

    @Override
    public E removeByIndex(int index) {
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private int step = 0;
            private RBasicListNode<E> temp = head;

            @Override
            public boolean hasNext() {
                return step < length;
            }

            @Override
            public E next() {
                step++;
                E result = temp.value;
                temp = temp.next;
                return result;
            }
        };
    }

}
