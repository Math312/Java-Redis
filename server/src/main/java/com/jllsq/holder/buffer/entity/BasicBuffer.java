package com.jllsq.holder.buffer.entity;

public class BasicBuffer {

    private int used;

    private int size;

    private byte[] buffer;

    public BasicBuffer(int used, byte[] buffer) {
        this.buffer = buffer;
        this.size = buffer.length;
        if (used > this.size) {
            throw new IllegalArgumentException();
        }
        this.used = used;
    }

    public int getUsed() {
        return used;
    }

    public int getSize() {
        return size;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void resetBuffer() {
        this.used = 0;
        this.size = this.buffer.length;
    }

    public void setUsed(int used) {
        this.used = used;
    }
}
