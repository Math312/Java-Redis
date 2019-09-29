package com.jllsq.holder;

public class RedisServerStateHolder {

    /**
     * RedisServerStateHolder singleton implementation.
     * @author Math312
     * */
    enum RedisServerStateHolderEnum{
        /**
         * Singleton
         * */
        INSTANCE;

        RedisServerStateHolderEnum() {
            this.redisServerStateHolder = new RedisServerStateHolder();
        }

        RedisServerStateHolder redisServerStateHolder;
    }

    public static RedisServerStateHolder getInstance() {
        return RedisServerStateHolderEnum.INSTANCE.redisServerStateHolder;
    }

    /**
     * Use to record the times of changing db.
     * */
    private int dirty;

    /**
     * record the time of redis
     * */
    private long unixTime;

    private RedisServerStateHolder() {
        this.dirty = 0;
        this.unixTime = System.currentTimeMillis();
    }

    public void incrDirty() {
        dirty = dirty + 1;
    }

    public void updateUnixTime() {
        this.unixTime = System.currentTimeMillis();
    }

    public long getUnixTime() {
        return this.unixTime;
    }

    public int getDirty() {
        return dirty;
    }
}
