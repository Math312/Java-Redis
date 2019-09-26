package com.jllsq.common.basic.skiplist;

import lombok.Data;

@Data
public class SkipListNodeLevel {

    private SkipListNode nextNode;

    private int span;

}
