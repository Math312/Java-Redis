package com.jllsq.config;

import com.jllsq.common.entity.RedisObject;
import lombok.Data;

@Data
public class  Shared {
    private RedisObject crlf,ok,err,emptybulk,czero,cone,pong,space,
            colon,nullbulk,nullmultibulk,queued,
            emptymultibulk,wrongtypeerr,nokeyerr,syntaxerr,sameobjecterr,
            outofrangeerr,plus,
            select0,select1,select2,select3,select4,
            select5,select6,select7,select8,select9;

    private Shared() {

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
