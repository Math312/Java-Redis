package com.jllsq.log;

import com.jllsq.common.entity.RedisClient;
import com.jllsq.common.util.AofUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class RedisAofLog {

    private String logFile;

    private RedisAofLog(){}

    enum  RedisAofLogEnum{
        INSTANCE;

        RedisAofLogEnum() {
            redisAofLog = new RedisAofLog();
        }

        private RedisAofLog redisAofLog;
    }

    public static RedisAofLog getInstance() {
        return RedisAofLogEnum.INSTANCE.redisAofLog;
    }

    public void init(String logFile) throws IOException {
        try {
            FileOutputStream output = new FileOutputStream(logFile);
            output.close();
            this.logFile = logFile;
        } catch (FileNotFoundException e) {
            File file = new File(logFile);
            try {
                if (file.createNewFile()){

                } else {
                    throw new IOException();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new IOException();
            }
        }
    }

    public void write(RedisClient client) throws IOException {
        byte[] bytes = AofUtil.encode(client);
        if (logFile != null) {
            Files.write(Paths.get(logFile),bytes, StandardOpenOption.APPEND);
        }
    }

}
