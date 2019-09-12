package com.jllsq.task;

public class ServerCron implements BasicTask{

    public ServerCron getInstance() {
        return ServerCronEnum.INSTANCE.serverCron;
    }

    enum ServerCronEnum {
        INSTANCE;

        private ServerCron serverCron;

        private ServerCronEnum() {
            this.serverCron = new ServerCron();
        }

    }

    @Override
    public Object start(Object ... args) {
        return null;
    }
}
