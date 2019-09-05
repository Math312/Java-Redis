package com.jllsq.common.entity;

import lombok.Data;

@Data
public class SaveParam {

    public SaveParam(long seconds, long changes){
        this.seconds = seconds;
        this.changes = changes;
    }

    private long seconds;
    private long changes;

}
