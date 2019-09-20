package com.jllsq.common;

import java.io.File;
import java.io.IOException;

/**
 * @author yanlishao
 */
public class BasicFileWriter {

    protected String logFile;

    public boolean init(String logFile) throws IOException {
        File file = new File(logFile);
        if (!file.exists()) {
            if (file.createNewFile()) {
                this.logFile = logFile;
                return true;
            }
            return false;
        }
        if (file.canWrite() && file.canRead()) {
            this.logFile = logFile;
            return true;
        }
        return false;
    }
}
