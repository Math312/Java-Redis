package com.jllsq.common.entity;

import com.jllsq.common.basic.map.Dict;
import lombok.Data;

@Data
public class RedisDb {
    private Dict<RedisObject,RedisObject> dict;
    private Dict<RedisObject,RedisObject> expires;
    private Dict<RedisObject,RedisObject> blockKeys;
    private int id;

    public RedisDb(int id) {
        this.id = id;
        this.dict = new Dict<>(null);
        this.expires = new Dict<>(null);
        this.blockKeys = new Dict<>(null);
    }
}
