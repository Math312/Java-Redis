package com.jllsq.command.impl;

import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisDb;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.map.DictEntry;
import com.jllsq.common.sds.SDS;
import com.jllsq.config.Shared;
import com.jllsq.command.RedisCommand;
import com.jllsq.command.RedisExpireCheckCommand;

public class ExistsCommand extends RedisExpireCheckCommand {

    public ExistsCommand() {
        super(new SDS("exists"), 1);
    }

    @Override
    public RedisObject processing(RedisClient client) {
        RedisDb db = client.getDb();
        RedisObject result = null;
        DictEntry<RedisObject, RedisObject> entry = db.getDict().find(client.getArgv()[1]);
        if (entry != null) {
            result = Shared.getInstance().getCone();
        } else {
            result = Shared.getInstance().getCzero();
        }
        return result;
    }

    @Override
    public RedisObject getExpireKey(RedisClient client) {
        return client.getArgv()[1];
    }
}
