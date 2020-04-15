package com.jllsq.holder.buffer;

import com.jllsq.holder.buffer.entity.BasicBuffer;

import java.util.HashMap;

public class HashMapBufferPool extends AbstractBufferPool{

    private final int bufferListMaxSize = 10;

    private HashMap<Integer, BufferList> bufferPool;

    public HashMapBufferPool() {
        this.bufferPool = new HashMap<>();
    }

    @Override
    public BasicBuffer createBuffer(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException();
        }
        BufferList bufferList = bufferPool.get(size);
        if (bufferList == null) {
            return doCreateBuffer(size);
        } else {
            if (bufferList.getSize() > 0) {
                return bufferList.removeBuffer();
            } else {
                return doCreateBuffer(size);
            }
        }
    }

    @Override
    public boolean needScheduleReleaseBuffer() {
        return false;
    }

    @Override
    public void scheduleReleaseBuffer() {

    }

    @Override
    public void recycleBuffer(BasicBuffer basicBuffer) {
        basicBuffer.resetBuffer();
        BufferList bufferList = bufferPool.get(basicBuffer.getSize());
        if (bufferList == null) {
            bufferList  = new LinkedBufferList();
            bufferPool.put(basicBuffer.getSize(),bufferList);
        }
        if (bufferList.getSize() < bufferListMaxSize) {
            bufferList.addBuffer(basicBuffer);
        }
    }
}
