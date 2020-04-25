package com.jllsq.holder.buffer;

import com.jllsq.holder.buffer.entity.BasicBuffer;

public interface BufferPool {

    public static final int MIN_BUFFER_SIZE = 128;

    BasicBuffer createBuffer(int size);

    boolean needScheduleReleaseBuffer();

    void scheduleReleaseBuffer();

    void recycleBuffer(BasicBuffer basicBuffer);
}
