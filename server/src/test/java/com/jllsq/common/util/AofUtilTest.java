package com.jllsq.common.util;

import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.sds.SDS;
import com.jllsq.holder.RedisServerObjectHolder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AofUtilTest {

    private RedisClient client0,client1;

    @Before
    public void init() {
        client0 = null;
        client1 = new RedisClient();
        client1.setArgv(new RedisObject[]{new RedisObject(false, RedisServerObjectHolder.REDIS_STRING,
                new SDS("SET"),RedisServerObjectHolder.REDIS_ENCODING_RAW),
                new RedisObject(false, RedisServerObjectHolder.REDIS_STRING,
                        new SDS("A"),RedisServerObjectHolder.REDIS_ENCODING_RAW),
                new RedisObject(false, RedisServerObjectHolder.REDIS_STRING,
                        new SDS("123"),RedisServerObjectHolder.REDIS_ENCODING_RAW)});
    }

    @Test
    public void encode() {
        Assert.assertEquals(null,AofUtil.encode(null));
        Assert.assertEquals(new String("*3\r\n$3\r\nSET\r\n$1\r\nA\r\n$3\r\n123\r\n"),new String(AofUtil.encode(client1)));
    }

    @Test
    public void decode() {

    }
}