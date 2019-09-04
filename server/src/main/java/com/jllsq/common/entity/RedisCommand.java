package com.jllsq.common.entity;

import com.jllsq.common.sds.SDS;

public class RedisCommand {

    private int argc;
    private SDS[] argv;

    public int getArgc() {
        return argc;
    }

    public void setArgc(int argc) {
        this.argc = argc;
    }

    public SDS[] getArgv() {
        return argv;
    }

    public void setArgv(SDS[] argv) {
        this.argv = argv;
    }
}
