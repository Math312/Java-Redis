package com.jllsq.common.basic.list;

import lombok.Data;

@Data
public class ListNode<T> {

    ListNode<T> last;
    ListNode<T> next;
    T value;

}
