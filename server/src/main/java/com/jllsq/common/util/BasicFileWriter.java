package com.jllsq.common.util;

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
            return file.createNewFile();
        }
        if (file.canWrite() && file.canRead()) {
            this.logFile = logFile;
            return true;
        }
        return false;
    }
}
