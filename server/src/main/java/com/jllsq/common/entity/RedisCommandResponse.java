package com.jllsq.common.entity;

import com.jllsq.common.sds.SDS;
import lombok.Data;

@Data
public class RedisCommandResponse {

    private RedisObject[] response;

}
