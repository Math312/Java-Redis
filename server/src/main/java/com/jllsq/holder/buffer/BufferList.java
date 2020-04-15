package com.jllsq.holder.buffer.entity;

public interface BufferList {

    int getSize();

    int getUsedTimes();

    BasicBuffer removeBuffer();

    void addBuffer(BasicBuffer basicBuffer);

}
