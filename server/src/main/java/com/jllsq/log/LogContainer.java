package com.jllsq.log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.LinkedBlockingQueue;

public class LogContainer extends Thread{

    private LinkedBlockingQueue<BasicLog> container;

    private LogContainer() {
        container = new LinkedBlockingQueue<>();
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
                Files.write(Paths.get(basicLog.getFileName()), basicLog.getBytes(), StandardOpenOption.APPEND);
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
