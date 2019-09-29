package com.jllsq.common.basic.sds;

import com.jllsq.common.basic.RedisClonable;
import com.jllsq.common.basic.sds.exception.SDSMaxLengthException;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class SDS implements RedisClonable, Comparable<SDS>, Serializable {

    private static final long serialVersionUID = 7887265299375772551L;

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

    public SDS(byte[] bytes) {
        this.length = 1024;
        this.used = bytes.length;
        this.content = new byte[1024];
        System.arraycopy(bytes, 0, content, 0, this.used);
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

    private boolean expandIfNecessary(int size) throws SDSMaxLengthException {
        if (size > MAX_LENGTH) {
            throw new SDSMaxLengthException(content);
        }
        if (size > this.length) {
            int newLen = 0;
            if (this.length <= EXPAND_SPLIT) {
                newLen = Math.min(this.length * 2, EXPAND_SPLIT);
            } else {
                newLen = this.length + EXPAND_SPLIT;
            }
            if (newLen < size) {
                expandIfNecessary(size);
            } else {
                System.arraycopy(content, 0, this.content, 0, length);
            }
            return true;
        } else {
            return false;
        }
    }

    public SDS append(SDS sds) throws SDSMaxLengthException {
        int newUsed = this.used + sds.used;
        expandIfNecessary(newUsed);
        System.arraycopy(sds.content,0,this.content,this.used,sds.used);
        this.used = newUsed;
        return this;
    }

    public String setContent(String str) {
        try {
            byte[] content = str.getBytes("utf-8");
            if (content.length >= LONG_SDS) {
                System.out.println("this is a long sds: " + str);
            }
            try {
                expandIfNecessary(content.length);
                System.arraycopy(content, 0, this.content, 0, content.length);
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

    public SDS toLower() {
        byte[] bytes = new byte[this.used];
        for (int i = 0;i < bytes.length;i ++) {
            if (content[i] >= 'A' && content[i] <= 'Z') {
                bytes[i] = (byte) (content[i]+ 0x20);
            }
            else {
                bytes[i] = content[i];
            }
        }
        return new SDS(bytes);
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

    public byte[] getBytes() {
        return this.content;
    }

    public void setBytes(byte[] content) {
        this.length = content.length;
        this.content = content;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SDS)) {
            return false;
        }
        if (((SDS) obj).used != this.used) {
            return false;
        }
        for (int i = 0; i < used; i++) {
            if (content[i] != ((SDS) obj).getBytes()[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return getContent().hashCode();
    }

    @Override
    public Object cloneDeep() {
        SDS sds = new SDS();
        sds.length = this.length;
        sds.used = this.used;
        sds.content = new byte[sds.length];
        for (int i = 0; i < sds.length; i++) {
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
        if (sds.used > this.used) {
            return -1;
        } else if (sds.used < this.used) {
            return 1;
        }
        if (sds.used == this.used) {
            for (int i = 0; i < this.used; i++) {
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
