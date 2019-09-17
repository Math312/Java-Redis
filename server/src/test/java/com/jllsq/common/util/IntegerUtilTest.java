package com.jllsq.common.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class IntegerUtilTest {

    @Test
    public void byteIsInt() {
        byte[] num = new byte[]{'1','3','2'};
        byte[] num2 = new byte[]{'-','1','3','2'};
        byte[] num3 = new byte[]{'-'};
        byte[] num4 = new byte[]{};
        byte[] num5 = new byte[]{'+'};
        byte[] num6 = null;
        assertTrue(IntegerUtil.byteIsInt(num));
        assertTrue(IntegerUtil.byteIsInt(num2));
        assertFalse(IntegerUtil.byteIsInt(num3));
        assertFalse(IntegerUtil.byteIsInt(num4));
        assertFalse(IntegerUtil.byteIsInt(num5));
        assertFalse(IntegerUtil.byteIsInt(num6));

    }

    @Test
    public void intToBytesLength() {
        int num = 0;
        int num1 = -1;
        int num2 = 89757;
        int num3 = -89757;
        assertEquals(1,IntegerUtil.intToBytesLength(num));
        assertEquals(2,IntegerUtil.intToBytesLength(num1));
        assertEquals(5,IntegerUtil.intToBytesLength(num2));
        assertEquals(6,IntegerUtil.intToBytesLength(num3));
    }

    @Test
    public void numToByteArray() {
        int num = 0;
        int num1 = -1;
        int num2 = 89757;
        int num3 = -89757;
        assertArrayEquals(new byte[]{'0'},IntegerUtil.numToByteArray(num));
        assertArrayEquals(new byte[]{'-','1'},IntegerUtil.numToByteArray(num1));
        assertArrayEquals(new byte[]{'8','9','7','5','7'},IntegerUtil.numToByteArray(num2));
        assertArrayEquals(new byte[]{'-','8','9','7','5','7'},IntegerUtil.numToByteArray(num3));

    }
}