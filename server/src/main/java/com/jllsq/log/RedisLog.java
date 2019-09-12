package com.jllsq.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

public class RedisLog {

    enum  RedisLogEnum{
        INSTANCE;

        RedisLogEnum() {
            redisLog = new RedisLog();
        }

        private RedisLog redisLog;
    }

    public static RedisLog getInstance() {
        return RedisLogEnum.INSTANCE.redisLog;
    }

    private String logFile;

    public static String LOG_LEVEL_DEBUG_STR = "debug";
    public static String LOG_LEVEL_VERBOSE_STR = "verbose";
    public static String LOG_LEVEL_NOTICE_STR = "notice";
    public static String LOG_LEVEL_WARNING_STR = "warning";

    public static final int LOG_LEVEL_DEBUG = 0;
    public static final int LOG_LEVEL_VERBOSE = 1;
    public static final int LOG_LEVEL_NOTICE = 2;
    public static final int LOG_LEVEL_WARNING = 3;

    public static final String LOG_FORMAT = "Time: %s - Pid: %s - [ %s ] - %s \r\n";

    private RedisLog() {
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

    public void log(int level, String ... logs) throws IOException {
        for (String log:logs) {
            String formatLog = formatLog(log,level);
            System.out.print(formatLog);
            if (logFile != null) {
                Files.write(Paths.get(logFile),formatLog.getBytes(), StandardOpenOption.APPEND);
            }
        }
    }

    public void log(int level,String logFormat, Object ... args) throws IOException {
        String data = String.format(logFormat,args);
        log(level,data);
    }

    /* todo Use the RedisServer's unixTime */
    private String getDate() {
        return new Date().toString();
    }

    private String getPid() {
        return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }

    private String getLevelString(int level) {
        switch (level) {
            case LOG_LEVEL_DEBUG:
                return LOG_LEVEL_DEBUG_STR.toUpperCase();
            case LOG_LEVEL_VERBOSE:
                return LOG_LEVEL_VERBOSE_STR.toUpperCase();
            case LOG_LEVEL_NOTICE:
                return LOG_LEVEL_NOTICE_STR.toUpperCase();
            case LOG_LEVEL_WARNING:
                return LOG_LEVEL_WARNING_STR.toUpperCase();
            default:
                throw new IllegalStateException("Unexpected value: " + level);
        }
    }

    private String formatLog(String data, int level) {
        return String.format(LOG_FORMAT,getDate(),getPid(),getLevelString(level), data);
    }

}
