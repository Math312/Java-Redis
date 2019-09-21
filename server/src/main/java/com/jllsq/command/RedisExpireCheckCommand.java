package com.jllsq.command;

import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.sds.SDS;
import com.jllsq.config.Shared;
import com.jllsq.command.RedisCommand;

public abstract class RedisExpireCheckCommand extends RedisCommand {

    public RedisExpireCheckCommand(SDS name, int arity) {
        super(name, arity);
    }

    @Override
    public RedisObject beforeProcessing(RedisClient client) {
        RedisObject result = null;
        if ((result = super.beforeProcessing(client)) != null){
            return result;
        } else {
            if (expireIfNeed(client.getDb(),getExpireKey(client))) {
                result = Shared.getInstance().getNokeyerr();
            } else {
                result = null;
            }
        }
        return result;
    }

    public abstract RedisObject getExpireKey(RedisClient client);
}
