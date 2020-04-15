package com.jllsq.holder.buffer;

import com.jllsq.holder.buffer.entity.BasicBuffer;

public abstract class AbstractBufferPool implements BufferPool{

    protected BasicBuffer doCreateBuffer(int size) {
        byte[] buffer = null;
        if (MIN_BUFFER_SIZE > size) {
            buffer = new byte[MIN_BUFFER_SIZE];
        } else {
            buffer = new byte[size];
        }
        return new BasicBuffer(0, buffer);
    }
}
