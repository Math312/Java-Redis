package com.jllsq.common.entity;

import com.jllsq.common.sds.SDS;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RedisClient {

    private RedisDb db;
    private int dictId;
    private int argc;
    private RedisObject[] argv;

    public RedisClient() {

    }

}
