package com.jllsq.holder;

import java.util.Date;

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
    private Date unixTime;

    private RedisServerStateHolder() {
        this.dirty = 0;
        this.unixTime = new Date();
    }

    public void incrDirty() {
        dirty = dirty + 1;
    }

    public void updateUnixTime() {
        this.unixTime = new Date();
    }

    public Date getUnixTime() {
        return new Date(unixTime.getTime());
    }

    public long getUnixTimeLong() {
        return unixTime.getTime();
    }

}
