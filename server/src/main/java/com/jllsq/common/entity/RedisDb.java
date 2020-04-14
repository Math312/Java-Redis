package com.jllsq.common.entity;

import com.jllsq.common.basic.map.Dict;
import lombok.Data;

@Data
public class RedisDb {
    private Dict dict;
    private Dict expires;
    private Dict blockKeys;
    private int id;

    public RedisDb(int id) {
        this.id = id;
        this.dict = new Dict();
        this.expires = new Dict();
        this.blockKeys = new Dict();
    }
}
