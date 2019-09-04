package com.jllsq.common.sds;

import com.jllsq.common.sds.exception.SDSMaxLengthException;

import java.io.UnsupportedEncodingException;

public class SDS{

    public static int INIT_SIZE = 1024;
    public static int EXPAND_SPLIT = 1024 * 1024;
    public static int MAX_LENGTH = 512 * 1024 * 1024;
    public static int LONG_SDS = 1024 * 1024;
    private int length;

    private int used;

    private byte[] content;

    public SDS() {
        this.length = 1024;
        this.used = 0;
        this.content = new byte[1024];
    }

    public SDS(byte[] bytes){
        this.length = 1024;
        this.used = bytes.length;
        this.content = new byte[1024];
        System.arraycopy(bytes,0,content,0,this.used);
    }

    public SDS(String str) {
        try {
            byte[] content = str.getBytes("utf-8");
            if (content.length >= LONG_SDS) {
                System.out.println("this is a long sds: " + str);
            }
            int len = content.length;
            if (len > INIT_SIZE) {
                this.length = INIT_SIZE;
            } else {
                this.length = len;
            }
            this.used = len;
            this.content = content;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private boolean expandIfNecessary(byte[] content) throws SDSMaxLengthException {
        int length = content.length;
        if (length > MAX_LENGTH) {
            throw new SDSMaxLengthException(content);
        }
        if (length > this.length) {
            int newLen = 0;
            if (this.length <= EXPAND_SPLIT) {
                newLen = Math.min(this.length * 2, EXPAND_SPLIT);
            } else {
                newLen = this.length + EXPAND_SPLIT;
            }
            if (newLen < length) {
                expandIfNecessary(content);
            } else {
                System.arraycopy(content, 0, this.content, 0, length);
            }
            return true;
        } else {
            return false;
        }
    }

    public String setContent(String str) {
        try {
            byte[] content = str.getBytes("utf-8");
            if (content.length >= LONG_SDS) {
                System.out.println("this is a long sds: " + str);
            }
            try {
                expandIfNecessary(content);
                this.used = content.length;
            } catch (SDSMaxLengthException e) {
                e.printStackTrace();
            }
            return getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public SDS sdsDup() {
        SDS sds = new SDS();
        sds.length = this.length;
        sds.used = this.used;
        sds.content = new byte[sds.length];
        System.arraycopy(this.content, 0, sds.content, 0, this.used);
        return sds;
    }


    public void sdsEmpty() {
        this.used = 0;
        this.length = 1024;
        this.content = new byte[1024];
    }

    public void sdsClear() {
        this.used = 0;
    }

    public void sdsToLower() {
        String str = getContent();
        str = str.toLowerCase();
        byte[] bytes = str.getBytes();
        System.arraycopy(bytes, 0, this.content, 0, bytes.length);
    }

    public void sdsToUpper() {
        String str = getContent();
        str = str.toUpperCase();
        byte[] bytes = str.getBytes();
        System.arraycopy(bytes, 0, this.content, 0, bytes.length);
    }

    public int getLength() {
        return length;
    }

    public int getUsed() {
        return used;
    }

    public String getContent() {
        return new String(this.content, 0, this.used);
    }
}
