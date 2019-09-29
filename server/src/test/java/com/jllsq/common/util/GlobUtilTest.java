package com.jllsq.common.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class GlobUtilTest {

    @Test
    public void match() {

        String string = "";
        String pattern = "*";
        assertEquals(true,GlobUtil.match(string.getBytes(),0,pattern.getBytes(),1));
        string = "*";
        assertEquals(true,GlobUtil.match(string.getBytes(),1,pattern.getBytes(),1));
        string = "aaa";
        pattern = "a*";
        assertEquals(true,GlobUtil.match(string.getBytes(),3,pattern.getBytes(),2));
        pattern = "*a";
        assertEquals(true,GlobUtil.match(string.getBytes(),3,pattern.getBytes(),2));
        pattern = "?a?";
        assertEquals(true,GlobUtil.match(string.getBytes(),3,pattern.getBytes(),3));
        string = "adf";
        pattern = "a[bcdf]f";
        assertEquals(true,GlobUtil.match(string.getBytes(),3,pattern.getBytes(),8));
        pattern = "ad[bcdf]";
        assertEquals(true,GlobUtil.match(string.getBytes(),3,pattern.getBytes(),8));
        pattern = "[a]d[bcdf]";
        assertEquals(true,GlobUtil.match(string.getBytes(),3,pattern.getBytes(),10));
        pattern = "a[d[fg]b]";
        assertEquals(false,GlobUtil.match(string.getBytes(),3,pattern.getBytes(),8));
        pattern = "a[\\[\\]]";
        string = "a[";
        assertEquals(true,GlobUtil.match(string.getBytes(),2,pattern.getBytes(),7));
    }

    @Test(expected = IllegalArgumentException.class)
    public void matchIlleagal() {
        String string = "[";
        String pattern = "[";
        GlobUtil.match(string.getBytes(),1,pattern.getBytes(),1);
    }
}