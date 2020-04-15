package com.jllsq.holder.buffer;

import com.jllsq.holder.buffer.entity.BasicBuffer;

public interface BufferList {

    int getSize();

    int getUsedTimes();

    BasicBuffer removeBuffer();

    void addBuffer(BasicBuffer basicBuffer);

}
