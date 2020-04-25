package com.jllsq.common.entity;

import com.jllsq.holder.buffer.entity.BasicBuffer;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;


@Data
@AllArgsConstructor
@ToString
public class RedisClient {

    public static final int INIT_RAW_REQUEST_SIZE = 8096;
    public static final int REFRESH_BUFFER_TIMES = 256;
    private Channel channel;
    private RedisDb db;
    private int dictId;
    private int argc;
    private RedisObject[] argv;
    private BasicBuffer buffer;

    public RedisClient() {
    }

    public void clearArgs() {
        this.argc = 0;
        this.argv = null;
    }

}
