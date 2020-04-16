package com.jllsq.log;

import com.jllsq.holder.buffer.entity.BasicBuffer;
import lombok.Data;

import java.io.OutputStream;

@Data
public class BasicLog {

//    private boolean needConsolePrinted;
    private OutputStream fileOutputStream;
    private BasicBuffer buffer;

}
