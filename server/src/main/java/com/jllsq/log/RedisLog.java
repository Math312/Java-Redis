package com.jllsq.log;

import com.jllsq.common.BasicFileWriter;
import com.jllsq.holder.buffer.RedisServerByteBufferHolder;
import com.jllsq.holder.buffer.entity.BasicBuffer;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Date;


/**
 * @author yanlishao
 */
public class RedisLog extends BasicFileWriter {

    public static String LOG_LEVEL_DEBUG_STR = "debug";
    public static String LOG_LEVEL_VERBOSE_STR = "verbose";
    public static String LOG_LEVEL_NOTICE_STR = "notice";
    public static String LOG_LEVEL_WARNING_STR = "warning";

    public static final int LOG_LEVEL_DEBUG = 0;
    public static final int LOG_LEVEL_VERBOSE = 1;
    public static final int LOG_LEVEL_NOTICE = 2;
    public static final int LOG_LEVEL_WARNING = 3;

    private static final String LOG_FORMAT = "Time: %s - Pid: %s - [ %s ] - %s \r\n";

    private RedisServerByteBufferHolder byteBufferHolder;

    enum  RedisLogEnum{
        /**
         * RedisLog singleton implementation.
         * */
        INSTANCE;

        RedisLogEnum() {
            redisLog = new RedisLog();
        }

        private RedisLog redisLog;
    }

    public static RedisLog getInstance() {
        return RedisLogEnum.INSTANCE.redisLog;
    }

    private RedisLog() {
        this.byteBufferHolder = RedisServerByteBufferHolder.getInstance();
    }

    public void log(int level, String ... logs) throws IOException {
        for (String log:logs) {
            byte[] bytes = formatLog(log,level).getBytes();
            System.out.write(bytes);
            BasicBuffer basicBuffer = new BasicBuffer(bytes.length,bytes);
            if (logFile != null) {
                BasicLog basicLog = new BasicLog();
                basicLog.setBuffer(basicBuffer);
                basicLog.setFileOutputStream(outputStream);
                LogContainer.getInstance().put(basicLog);
            }
        }
    }

    public void log(int level,String logFormat, Object ... args) throws IOException {
        String data = String.format(logFormat,args);
        log(level,data);
    }

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
