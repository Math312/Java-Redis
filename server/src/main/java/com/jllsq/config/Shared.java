package com.jllsq.config;

import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.sds.SDS;
import com.jllsq.holder.RedisServerObjectHolder;
import lombok.Data;
import lombok.Getter;

import static com.jllsq.holder.RedisServerObjectHolder.REDIS_STRING;

@Getter
public class  Shared {
    private RedisObject crlf,ok,err,emptybulk,czero,cone,pong,space,
            colon,nullbulk,nullmultibulk,queued,
            emptymultibulk,wrongtypeerr,nokeyerr,syntaxerr,sameobjecterr,
            outofrangeerr,plus,
            select0,select1,select2,select3,select4,
            select5,select6,select7,select8,select9;

    private Shared() {

    }

    private void init() {
        RedisServerObjectHolder holder = RedisServerObjectHolder.getInstance();
        this.crlf = holder.createObject(true, REDIS_STRING, new SDS("\r\n"));
        this.ok = holder.createObject(true, REDIS_STRING, new SDS("+OK\r\n"));
        this.err = holder.createObject(true, REDIS_STRING, new SDS("-ERR\r\n"));
        this.emptybulk = holder.createObject(true, REDIS_STRING, new SDS("$0\r\n\r\n"));
        this.czero = holder.createObject(true, REDIS_STRING, new SDS(":0\r\n"));
        this.cone = holder.createObject(true, REDIS_STRING, new SDS(":1\r\n"));
        this.nullbulk = holder.createObject(true, REDIS_STRING, new SDS("$-1\r\n"));
        this.nullmultibulk = holder.createObject(true, REDIS_STRING, new SDS("*-1\r\n"));
        this.emptymultibulk = holder.createObject(true, REDIS_STRING, new SDS("*0\r\n"));
        this.pong = holder.createObject(true, REDIS_STRING, new SDS("+PONG\r\n"));
        this.queued = holder.createObject(true, REDIS_STRING, new SDS("+QUEUED\r\n"));
        this.wrongtypeerr = holder.createObject(true, REDIS_STRING, new SDS("-ERR Operation against a key holding the wrong kind of value\r\n"));
        this.nokeyerr = holder.createObject(true, REDIS_STRING, new SDS("-ERR no such key\r\n"));
        this.syntaxerr = holder.createObject(true, REDIS_STRING, new SDS("-ERR syntax error\r\n"));
        this.outofrangeerr = holder.createObject(true, REDIS_STRING, new SDS("-ERR source and destination objects are the same\r\n"));
        this.sameobjecterr = holder.createObject(true,REDIS_STRING,new SDS("-ERR source and destination objects are the same\r\n"));
        this.space = holder.createObject(true, REDIS_STRING, new SDS(" "));
        this.colon = holder.createObject(true, REDIS_STRING, new SDS(":"));
        this.plus = holder.createObject(true, REDIS_STRING, new SDS("+"));
        this.select0 = holder.createObject(true, REDIS_STRING, new SDS("select 0\r\n"));
        this.select1 = holder.createObject(true, REDIS_STRING, new SDS("select 1\r\n"));
        this.select2 = holder.createObject(true, REDIS_STRING, new SDS("select 2\r\n"));
        this.select3 = holder.createObject(true, REDIS_STRING, new SDS("select 3\r\n"));
        this.select4 = holder.createObject(true, REDIS_STRING, new SDS("select 4\r\n"));
        this.select5 = holder.createObject(true, REDIS_STRING, new SDS("select 5\r\n"));
        this.select6 = holder.createObject(true, REDIS_STRING, new SDS("select 6\r\n"));
        this.select7 = holder.createObject(true, REDIS_STRING, new SDS("select 7\r\n"));
        this.select8 = holder.createObject(true, REDIS_STRING, new SDS("select 8\r\n"));
        this.select9 = holder.createObject(true, REDIS_STRING, new SDS("select 9\r\n"));

    }

    public static Shared getInstance() {
        return ShardEnum.SHARD.getInstance();
    }

    static enum ShardEnum {
        SHARD;
        private Shared shared;
        ShardEnum() {
            this.shared = new Shared();
        }

        public Shared getInstance() {
            return shared;
        }


    }


}
