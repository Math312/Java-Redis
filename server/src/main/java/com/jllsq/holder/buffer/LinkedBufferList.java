package com.jllsq.holder.buffer;

import com.jllsq.holder.buffer.entity.BasicBuffer;

import java.util.LinkedList;

public class LinkedBufferList implements BufferList {

    private LinkedList<BasicBuffer> list;

    private int used;

    public LinkedBufferList() {
        this.list = new LinkedList<>();
        this.used = 0;
    }

    @Override
    public int getSize() {
        return this.list.size();
    }

    @Override
    public int getUsedTimes() {
        return this.used;
    }

    @Override
    public BasicBuffer removeBuffer() {
        this.used += 1;
        return this.list.removeFirst();
    }

    @Override
    public void addBuffer(BasicBuffer basicBuffer) {
        this.list.add(basicBuffer);
    }
}
