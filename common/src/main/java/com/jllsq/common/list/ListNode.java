package com.jllsq.common.list;

import lombok.Data;

@Data
public class ListNode<T> {

    ListNode<T> last;
    ListNode<T> next;
    T value;

}
