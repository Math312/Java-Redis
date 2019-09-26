package com.jllsq.common.basic.skiplist;

public interface SkipList {

    boolean zslFree();

    SkipListNode zslCreateNode(int level, Object value, double score);

    boolean zslFreeNode(SkipListNode node);

    boolean zslInsert(Object value, double score);


}
