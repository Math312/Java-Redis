package com.jllsq.common.basic.sds;

import com.jllsq.common.basic.RedisClonable;
import com.jllsq.common.basic.sds.exception.SDSMaxLengthException;
import com.jllsq.common.util.IntegerUtil;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class SDS implements RedisClonable, Comparable<SDS>, Serializable {

    private static final long serialVersionUID = 7887265299375772551L;

    public static int INIT_SIZE = 32;
    public static int EXPAND_SPLIT = 1024 * 1024;
    public static int MAX_LENGTH = 512 * 1024 * 1024;
    public static int LONG_SDS = 1024 * 1024;
    private byte[] content;

    public SDS() {
        this.content = new byte[1032];
    }

    private void setLength(int length) {
        this.content[0] = (byte) ((length >> 24) & 0xFF);
        this.content[1] = (byte) ((length >> 16) & 0xFF);
        this.content[2] = (byte) ((length >> 8) & 0xFF);
        this.content[3] = (byte) ((length) & 0xFF);
    }

    public void setUsed(int used) {
        this.content[4] = (byte) ((used >> 24) & 0xFF);
        this.content[5] = (byte) ((used >> 16) & 0xFF);
        this.content[6] = (byte) ((used >> 8) & 0xFF);
        this.content[7] = (byte) ((used) & 0xFF);
    }

    public SDS(byte[] bytes) {
        if (bytes.length <= INIT_SIZE) {
            this.content = new byte[INIT_SIZE+8];
            setLength(INIT_SIZE);
            setUsed(bytes.length);
            System.arraycopy(bytes, 0, content, 8, bytes.length);
        } else {
            int length = bytes.length;
            setUsed(length);
            setLength(length);
            this.content = new byte[length + 8];
            System.arraycopy(bytes, 0, content, 8, length);
        }
    }

    public SDS(String str) {
        try {
            byte[] content = str.getBytes("utf-8");
            if (content.length >= LONG_SDS) {
                System.out.println("this is a long sds: " + str);
            }
            int len = content.length;
            byte[] bytes  = new byte[content.length+8];
            System.arraycopy(content,0,bytes,8,content.length);
            this.content = bytes;
            setLength(Math.max(len, INIT_SIZE));
            setUsed(len);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private boolean expandIfNecessary(int size) throws SDSMaxLengthException {
        if (size > MAX_LENGTH) {
            throw new SDSMaxLengthException(content);
        }
        int length = getLength();
        if (size > length) {
            int newLen = 0;
            if (length <= EXPAND_SPLIT) {
                newLen = Math.min(length * 2, EXPAND_SPLIT);
            } else {
                newLen = length + EXPAND_SPLIT;
            }
            if (newLen < size) {
                expandIfNecessary(size);
            } else {
                content = new byte[newLen+8];
                System.arraycopy(content, 0, this.content, 8, length);
            }
            return true;
        } else {
            return false;
        }
    }

    public SDS append(SDS sds) throws SDSMaxLengthException {
        int sdsUsed = sds.getUsed();
        int used = getUsed();
        int newUsed = used + sdsUsed;
        expandIfNecessary(newUsed);
        System.arraycopy(sds.getContentBytes(), 0, this.content, used + 8, sdsUsed);
        this.setUsed(newUsed);
        return this;
    }

    public void sdsClear() {
        setUsed(0);
    }

    public void sdsToLower() {
        String str = getContent();
        str = str.toLowerCase();
        byte[] bytes = str.getBytes();
        System.arraycopy(bytes, 0, this.content, 0, bytes.length);
    }

    public SDS toLower() {
        int used = getUsed();
        byte[] bytes = new byte[used + 8];
        for (int i = 8; i < bytes.length; i++) {
            if (content[i] >= 'A' && content[i] <= 'Z') {
                bytes[i] = (byte) (content[i] + 0x20);
            } else {
                bytes[i] = content[i];
            }
        }
        System.arraycopy(content,0,bytes,0,8);
        SDS sds = new SDS();
        sds.setBytes(bytes);
        return sds;
    }

    public void sdsToUpper() {
        String str = getContent();
        str = str.toUpperCase();
        byte[] bytes = str.getBytes();
        System.arraycopy(bytes, 0, this.content, 0, bytes.length);
    }

    public int getLength() {
        byte[] buffer = new byte[4];
        System.arraycopy(content,0,buffer,0,4);
        return IntegerUtil.complementArrayToInt(buffer);
    }

    public int getUsed() {
        byte[] buffer = new byte[4];
        System.arraycopy(content,4,buffer,0,4);
        return IntegerUtil.complementArrayToInt(buffer);
    }

    public String getContent() {
        return new String(this.content, 8, getUsed());
    }

    public byte[] getBytes() {
        return this.content;
    }

    public void setBytes(byte[] content) {
        this.content = content;
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SDS)) {
            return false;
        }
        if (((SDS) obj).getUsed() != this.getUsed()) {
            return false;
        }
        for (int i = 0; i < getUsed(); i++) {
            if (content[i+8] != ((SDS) obj).getBytes()[i+8]) {
                return false;
            }
        }
        return true;
    }

    public byte[] getContentBytes() {
        int used = getUsed();
        byte[] bytes = new  byte[used];
        System.arraycopy(content,8,bytes,0,used);
        return bytes;
    }

    @Override
    public int hashCode() {
        return getContent().hashCode();
    }

    @Override
    public Object cloneDeep() {
        SDS sds = new SDS();
        sds.setLength(getLength());
        sds.setUsed(getUsed());
        sds.content = new byte[sds.getLength()];
        for (int i = 0; i < sds.getLength(); i++) {
            sds.content[i] = this.content[i];
        }
        return sds;
    }

    @Override
    public int compareTo(SDS sds) {
        if (sds == null) {
            return -1;
        }
        if (sds.content == null) {
            return -1;
        }
        if (sds.getUsed() > this.getUsed()) {
            return -1;
        } else if (sds.getUsed() < this.getUsed()) {
            return 1;
        }
        if (sds.getUsed() == this.getUsed()) {
            for (int i = 0; i < this.getUsed(); i++) {
                if (sds.content[i] != this.content[i]) {
                    if (this.content[i] > sds.content[i]) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return getContent();
    }
}
