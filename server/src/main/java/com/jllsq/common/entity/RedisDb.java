package com.jllsq.common.entity;

import com.jllsq.common.map.DbDictType;
import com.jllsq.common.map.Dict;
import com.jllsq.common.map.KeyListDictType;
import com.jllsq.common.map.KeyptrDictType;
import com.jllsq.common.sds.SDS;
import lombok.Data;

@Data
public class RedisDb {
    private Dict<SDS,Object> dict;
    private Dict<SDS,Object> expires;
    private Dict<SDS,Object> blockKeys;
    private int id;

    public RedisDb(int id) {
        this.id = id;
        this.dict = new Dict<SDS, Object>(new DbDictType(),null);
        this.expires = new Dict<SDS, Object>(new KeyptrDictType(),null);
        this.blockKeys = new Dict<SDS, Object>(new KeyListDictType(),null);
    }
}
