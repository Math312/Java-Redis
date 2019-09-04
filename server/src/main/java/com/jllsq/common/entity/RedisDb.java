package com.jllsq.common.entity;

import com.jllsq.common.map.Dict;
import com.jllsq.common.sds.SDS;

public class RedisDb {
    private Dict<SDS,Object> dict;
    private Dict<SDS,Object> expires;
    private Dict<SDS,Object> blockKeys;
    private int id;

    public Dict<SDS, Object> getDict() {
        return dict;
    }

    public void setDict(Dict<SDS, Object> dict) {
        this.dict = dict;
    }

    public Dict<SDS, Object> getExpires() {
        return expires;
    }

    public void setExpires(Dict<SDS, Object> expires) {
        this.expires = expires;
    }

    public Dict<SDS, Object> getBlockKeys() {
        return blockKeys;
    }

    public void setBlockKeys(Dict<SDS, Object> blockKeys) {
        this.blockKeys = blockKeys;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
