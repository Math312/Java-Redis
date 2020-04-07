package com.jllsq.holder;

public class RedisServerByteBufferHolder {

    final int DEFAULT_COMMON_BUFFER_SIZE = 1024;

    final int DEFAULT_BIG_BUFFER_SIZE = 1024 * 10;

    private byte[] commonBuffer;

    private byte[] bigBuffer;

    private RedisServerByteBufferHolder() {
        this.commonBuffer = new byte[DEFAULT_COMMON_BUFFER_SIZE];
    }

    public byte[]  getBuffer(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException();
        }
        if (size <= this.DEFAULT_COMMON_BUFFER_SIZE) {
            return commonBuffer;
        } else {
            this.bigBuffer = null;
            if (size <= this.DEFAULT_BIG_BUFFER_SIZE) {
                this.bigBuffer =new byte[this.DEFAULT_BIG_BUFFER_SIZE];
                return this.bigBuffer;
            } else {
                this.bigBuffer =new byte[size];
                return this.bigBuffer;
            }
        }
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
