package com.jllsq.common.skiplist;

import lombok.Data;


@Data
public class SkipListNode {

    private final int MAX_LEVEL = 64;

    private double score;

    private Object object;

    private SkipListNodeLevel level[];

    private SkipListNode backward;

    public SkipListNode(int level,Object object,double score) {
        if (level <= 0) {
            throw new RuntimeException();
        }
        this.backward = null;
        this.level = new SkipListNodeLevel[level];
        for (int i = 0;i < level;i ++) {
            this.level[i] = new SkipListNodeLevel();
        }
        this.object = object;
        this.score = score;
    }

}
