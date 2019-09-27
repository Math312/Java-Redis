package com.jllsq.common.basic.list;

import java.util.Iterator;

public class List<T> implements Iterable<T> {

    private int length;
    private ListNode<T> head;
    private ListNode<T> tail;

    public int length(){
        return this.length;
    }

    public List() {
        this.head = this.tail = null;
        this.length = 0;
    }

    public void addLast(T data){
        ListNode<T> node = new ListNode<>();
        node.value = data;
        if (this.tail == null) {
            node.next = null;
            node.last = null;
            this.head = node;
            this.tail = node;
        } else {
            node.next = null;
            node.last = this.tail;
            this.tail.next = node;
            this.tail = node;
        }
        this.length ++;
    }

    public void addFirst(T data) {
        ListNode<T> node = new ListNode<>();
        node.value = data;
        if (this.head == null) {
            node.next = null;
            node.last = null;
            this.head = node;
            this.tail = node;
        } else {
            node.next = this.head;
            node.last = null;
            this.head.last = node;
            this.head = node;
        }
        this.length ++;
    }

    public void add(T data,int index) {
        if (index < 0 || index > length) {
            throw new IllegalArgumentException("Index must be between 0 and "+this.length);
        }
        if (index == 0) {
            addFirst(data);
        }else if (index == length) {
            addLast(data);
        } else {
            ListNode<T> node = getNode(index);
            ListNode<T> newNode = new ListNode<>();
            newNode.last = node.last;
            newNode.next = node;
            node.last.next = newNode;
            node.last = newNode;
            this.length ++;
        }
    }

    private ListNode<T> getNode(int index) {
        if (this.length < 0) {
            throw new IllegalArgumentException("input is "+ index+", index must be >= 0");
        }
        if (this.length < index) {
            ListNode<T> tmp = this.head;
            int i = 0;
            while (tmp != null && i < index) {
                tmp = tmp.next;
                i ++;
            }
            return tmp;
        }else {
            throw new IllegalArgumentException("The size of list is "+this.length+",the index must be between 0 and "+(this.length - 1));
        }
    }

    private T get(int index) {
        ListNode<T> node = getNode(index);
        if (node != null) {
            return node.value;
        }
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return new ListIterator<T>();
    }

    private class ListIterator<T> implements Iterator<T> {

        private int index;
        private ListNode<T> node;
        ListIterator() {
            index = 0;
            this.node = (ListNode<T>) head;
        }

        @Override
        public boolean hasNext() {
            return index < length;
        }

        @Override
        public T next() {
            ListNode<T> tmp = node;
            node = node.next;
            index ++;
            return tmp.value;
        }
    }
}
