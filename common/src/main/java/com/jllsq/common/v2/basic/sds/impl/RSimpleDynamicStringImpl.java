package com.jllsq.common.v2.basic.sds.impl;

import com.jllsq.common.Asserts;
import com.jllsq.common.v2.Ordered;
import com.jllsq.common.v2.basic.sds.RSimpleDynamicString;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * SDS 基本实现
 * */
@Data
public class RSimpleDynamicStringImpl implements RSimpleDynamicString {

    private int length;

    private int used;

    private byte[] buf;

    private static List<SdsExpandStrategy> expandStrategies;

    public static final int MB = 1024 * 1024;

    static {
        expandStrategies = new LinkedList<>();
        expandStrategies.add(new Lower1MbExpandStrategy());
        expandStrategies.add(new Upper1MbExpandStrategy());
        Ordered.sortOrdered(expandStrategies);
    }

    RSimpleDynamicStringImpl() {
        this.length = 0;
        this.used = 0;
        this.buf = new byte[0];
    }

    RSimpleDynamicStringImpl(String str) {
        Asserts.isNotNull(str);
        byte[] temp = str.getBytes();
        if (temp.length <= MB) {
            this.length = temp.length * 2;
        } else {
            this.length = temp.length;
        }
        this.used = temp.length;
        this.buf = new byte[this.length];
        System.arraycopy(temp, 0, this.buf, 0, this.used);
    }

    RSimpleDynamicStringImpl(byte[] content) {
        Asserts.isNotNull(content);
        if (content.length <= MB) {
            this.length = content.length * 2;
        } else {
            this.length = content.length;
        }
        this.used = content.length;
        this.buf = new byte[this.length];
        System.arraycopy(content, 0, this.buf, 0, this.used);
    }

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public int available() {
        return this.length - this.used;
    }

    @Override
    public void clear() {
        this.used = 0;
    }

    @Override
    public int getUsedLength() {
        return this.used;
    }

    @Override
    public RSimpleDynamicString concat(RSimpleDynamicString sds) {
        Asserts.isNotNull(sds);

        return concat(sds.getUsedLength(), sds.getBuf());
    }

    @Override
    public RSimpleDynamicString concat(String str) {
        byte[] temp = str.getBytes();

        return concat(temp.length, temp);
    }

    private RSimpleDynamicString concat(int used, byte[] buf) {
        int total = this.used + used;
        if (total > this.length) {
            expand(total * 2);
        }
        System.arraycopy(buf, 0, this.buf, this.used, used);
        return this;
    }

    @Override
    public RSimpleDynamicString copy(String str) {
        byte[] temp = str.getBytes();
        this.used = temp.length;
        System.arraycopy(temp, 0, this.buf, 0, this.used);
        return this;
    }

    @Override
    public RSimpleDynamicString expand() {
        for (SdsExpandStrategy expandStrategy : expandStrategies) {
            if (expandStrategy.shouldExpand(this)) {
                expandStrategy.expand(this);
                return this;
            }
        }
        return null;
    }

    @Override
    public RSimpleDynamicString expand(int length) {
        byte[] temp = new byte[length];
        this.length = length;
        System.arraycopy(this.buf, 0, temp, 0, this.used);
        this.buf = temp;
        return this;
    }

    @Override
    public int compare(RSimpleDynamicString sds) {
        return compare(sds.getUsedLength(), sds.getBuf());
    }

    @Override
    public int compare(String str) {
        byte[] temp = str.getBytes();
        return compare(temp.length, temp);
    }

    private int compare(int used, byte[] content) {
        if (this.used != used) {
            return this.used - used;
        }
        for (int i = 0; i < this.used; i++) {
            if (this.buf[i] < content[i]) {
                return -1;
            } else if (this.buf[i] > content[i]) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return new String(buf, 0, this.used);
    }
}
