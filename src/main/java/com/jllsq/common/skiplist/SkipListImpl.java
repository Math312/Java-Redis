//package com.jllsq.common.skiplist;
//
//import lombok.Data;
//
//import java.util.HashMap;
//import java.util.concurrent.ThreadLocalRandom;
//
//@Data
//public class SkipListImpl implements SkipList{
//
//    private SkipListNode head;
//
//    private SkipListNode tail;
//
//    private int length;
//
//    private byte level;
//
//    private byte randomLevel() {
//        ThreadLocalRandom random = ThreadLocalRandom.current();
//        int level = random.nextInt(64);
//        return (byte)level;
//    }
//
//    public SkipListImpl() {
//        this.head = null;
//        this.tail = null;
//        this.length = 0;
//        this.level = 0;
//    }
//
//    @Override
//    public boolean zslFree() {
//        return false;
//    }
//
//    @Override
//    public SkipListNode zslCreateNode(int level, Object value, double score) {
//        SkipListNode node = new SkipListNode(level,value,score);
//        return node;
//    }
//
//    @Override
//    public boolean zslFreeNode(SkipListNode node) {
//        return false;
//    }
//
//    @Override
//    public boolean zslInsert(Object value, double score) {
//        byte level = randomLevel();
//        SkipListNode node = zslCreateNode(level,value,score);
//        if (level > this.level) {
//            this.level = level;
//        }
//        HashMap<Integer,SkipListNode> map = new HashMap<>();
//        SkipListNode temp = head;
//        double tempScore;
//        for (int i = 0;i < level;i ++) {
//            tempScore = temp.getScore();
//            if (tempScore > score) {
//                i ++;
//            } else {
//
//            }
//
//        }
//        return false;
//    }
//}
