package com.jllsq.log;

import com.jllsq.holder.buffer.RedisServerByteBufferHolder;
import com.jllsq.holder.buffer.entity.BasicBuffer;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class LogContainer extends Thread{

    private LinkedBlockingQueue<BasicLog> container;

    private RedisServerByteBufferHolder byteBufferHolder;

    private LogContainer() {
        container = new LinkedBlockingQueue<>();
        this.byteBufferHolder = RedisServerByteBufferHolder.getInstance();
    }

    enum LogContainerEnum {
        INSTANCE;

        LogContainer logContainer;

        LogContainerEnum() {
            this.logContainer = new LogContainer();
            this.logContainer.start();
        }
    }

    public static LogContainer getInstance() {
        return LogContainerEnum.INSTANCE.logContainer;
    }


    @Override
    public void run() {
        try {
            while(true) {
                BasicLog basicLog = container.take();
                BasicBuffer basicBuffer = basicLog.getBuffer();
                basicLog.getFileOutputStream().write(basicBuffer.getBuffer(),0,basicBuffer.getUsed());
                byteBufferHolder.recycleBuffer(basicBuffer);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public void put(BasicLog log) {
        try {
            if (log != null) {
                container.put(log);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
