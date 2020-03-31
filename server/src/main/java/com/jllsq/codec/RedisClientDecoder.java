package com.jllsq.codec;

import com.jllsq.common.entity.RedisClient;
import io.netty.buffer.ByteBuf;

import java.util.List;

public interface RedisClientDecoder {

    List<RedisClient> decodeToRedisClientList(ByteBuf byteBuf) throws Exception;
}
