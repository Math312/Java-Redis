package com.jllsq.command;

import com.jllsq.command.handler.RedisCommandClientHandler;
import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.entity.RedisObject;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RedisCommandClientHandlerChain {

    private List<RedisCommandClientHandler> handlers;
    private Iterator<RedisCommandClientHandler> iterator;

    public RedisCommandClientHandlerChain(){
        this.handlers = new LinkedList<>();
    }

    public RedisCommandClientHandlerChain(RedisCommandClientHandler... handlers) {
        this.handlers = new LinkedList<>();
        if (handlers == null) {
            throw new IllegalArgumentException("Handler must not be null!");
        }
        this.handlers.addAll(Arrays.asList(handlers));
        this.iterator = this.handlers.iterator();
    }

    public void init(){
        this.iterator = this.handlers.iterator();
    }

    public RedisObject doHandle(RedisClient client, RedisCommand command) {
        if (iterator.hasNext()){
            RedisCommandClientHandler handler = iterator.next();
            return handler.handle(client, command,this);
        }
        return null;
    }

    public boolean isComplete() {
        return iterator.hasNext();
    }

    public void add(RedisCommandClientHandler handler) {
        this.handlers.add(handler);
    }

}
