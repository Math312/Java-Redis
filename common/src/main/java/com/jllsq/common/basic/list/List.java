package com.jllsq.common.basic.list;

public class List<T> {

    private int length;
    private ListNode<T> head;
    private ListNode<T> tail;

    public int length(){
        return 0;
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

        }


    }


}
