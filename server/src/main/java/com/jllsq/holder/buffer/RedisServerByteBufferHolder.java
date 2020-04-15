package com.jllsq.holder.buffer;

import com.jllsq.holder.buffer.entity.BasicBuffer;

public class RedisServerByteBufferHolder {

    private BufferPool bufferPool;

    private byte[] readLineBuffer = new byte[128];

    private RedisServerByteBufferHolder() {
        this.bufferPool = new HashMapBufferPool();
    }

    public byte[] getReadLineBuffer() {
        return readLineBuffer;
    }


    public BasicBuffer getRedisClientBufferFromPool(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException();
        }
        return bufferPool.createBuffer(size);
    }

    public void recycleBuffer(BasicBuffer basicBuffer) {
        bufferPool.recycleBuffer(basicBuffer);
    }

    public static RedisServerByteBufferHolder getInstance() {
        return RedisServerByteBufferHolderEnum.INSTANCE.getInstance();
    }

    enum RedisServerByteBufferHolderEnum {
        INSTANCE;

        private RedisServerByteBufferHolder redisServerByteBufferHolder;

        RedisServerByteBufferHolderEnum() {
            this.redisServerByteBufferHolder = new RedisServerByteBufferHolder();
        }

        public RedisServerByteBufferHolder getInstance() {
            return this.redisServerByteBufferHolder;
        }
    }

}
