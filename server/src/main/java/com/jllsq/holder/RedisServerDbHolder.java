package com.jllsq.holder;

import com.jllsq.common.entity.RedisDb;
import com.jllsq.common.list.List;
import org.apache.commons.lang3.SerializationUtils;

public class RedisServerDbHolder {

    public static final int REDIS_DEFAULT_DBNUM = 16;

    private RedisDb db[];
    private int dbNum;
    private List objFreeList;

    public RedisDb[] getDb() {
        return db;
    }

    public int getDbNum() {
        return dbNum;
    }

    public void setDbNum(int dbNum) {
        this.dbNum = dbNum;
        init();
    }

    private RedisServerDbHolder(){
        this.dbNum = REDIS_DEFAULT_DBNUM;
        init();
    }

    private void init() {
        this.db = new RedisDb[this.dbNum];
        for (int i = 0; i < this.dbNum; i++) {
            this.db[i] = new RedisDb(i);
        }
    }

    public static RedisServerDbHolder getInstance() {
        return RedisServerDbHolderEnum.INSTANCE.redisServerDbHolder;
    }

    public  RedisDb[] snapshot() {
        return SerializationUtils.clone(this.db);
    }

    enum RedisServerDbHolderEnum {
        INSTANCE;

        private RedisServerDbHolder redisServerDbHolder;

        RedisServerDbHolderEnum() {
            this.redisServerDbHolder = new RedisServerDbHolder();
        }
    }

    public void rdbSave(String fileName) {

    }
}
