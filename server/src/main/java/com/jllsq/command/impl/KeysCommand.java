package com.jllsq.command.impl;

import com.jllsq.command.RedisCommand;
import com.jllsq.command.handler.impl.RedisCommandAofHandler;
import com.jllsq.command.handler.impl.RedisCommandCheckParamNumsHandler;
import com.jllsq.command.handler.impl.RedisCommandInitClientHandler;
import com.jllsq.command.handler.impl.RedisCommandProcessHandler;
import com.jllsq.common.basic.list.List;
import com.jllsq.common.basic.map.DictEntry;
import com.jllsq.common.basic.sds.SDS;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisDb;
import com.jllsq.common.entity.RedisObject;
import com.jllsq.common.util.GlobUtil;
import com.jllsq.holder.RedisServerObjectHolder;

import java.util.Iterator;

import static com.jllsq.holder.RedisServerObjectHolder.REDIS_ENCODING_RAW;
import static com.jllsq.holder.RedisServerObjectHolder.REDIS_LIST;

public class KeysCommand extends RedisCommand {
    public KeysCommand() {
        super(new SDS("KEYS"), 1);
    }

    @Override
    public RedisObject process(RedisClient client) {
        RedisDb db = client.getDb();
        Iterator<DictEntry<RedisObject,RedisObject>> iterator =  db.getDict().iterator();
        SDS patternSds = ((SDS)(client.getArgv()[1].getPtr()));
        List<SDS> list = new List<>();
        while (iterator.hasNext()) {
            DictEntry<RedisObject,RedisObject> entry = iterator.next();
            SDS stringSds = ((SDS)entry.getKey().getPtr());
            if (GlobUtil.match(stringSds.getBytes(),stringSds.getUsed(),patternSds.getBytes(),patternSds.getUsed())) {
                list.addLast((SDS) entry.getKey().getPtr());
            }

//            if (((SDS)(entry.getKey().getPtr())).getContent().matches(pattern) && expireIfNeed(db,entry.getKey())) {
//                list.addLast((SDS) entry.getKey().getPtr());
//            }
        }
        RedisObject result = new RedisObject(false, REDIS_LIST,list,REDIS_ENCODING_RAW);
        return result;
    }

    @Override
    public void initChain() {
        super.initChain();
        handlerChain.add(new RedisCommandInitClientHandler());
        handlerChain.add(new RedisCommandCheckParamNumsHandler());
        handlerChain.add(new RedisCommandAofHandler());
        handlerChain.add(new RedisCommandProcessHandler());
    }
}
