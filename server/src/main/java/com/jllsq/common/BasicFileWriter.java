package com.jllsq.common;

import java.io.*;

/**
 * @author yanlishao
 */
public class BasicFileWriter {

    protected String logFile;

    protected OutputStream outputStream;

    public boolean init(String logFile) throws IOException {
        File file = new File(logFile);
        if (!file.exists()) {
            if (file.createNewFile()) {
                this.logFile = logFile;
                this.outputStream = new FileOutputStream(this.logFile,true);
                return true;
            }
            return false;
        }
        if (file.canWrite() && file.canRead()) {
            this.logFile = logFile;
            this.outputStream = new FileOutputStream(this.logFile,true);
            return true;
        }
        return false;
    }
}
