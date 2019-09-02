package com.jllsq.common.sds;

import org.junit.Before;

import static org.junit.Assert.*;

public class SDSTest {

    private SDSImpl sdsImpl1;
    private SDSImpl sdsImpl2;

    @Before
    public void init(){
        this.sdsImpl1 = new SDSImpl();
        this.sdsImpl2 = new SDSImpl("test");
    }

    @org.junit.Test
    public void setContent() {
    }

    @org.junit.Test
    public void getContent() {
        assertEquals("",this.sdsImpl1.getContent());
        assertEquals("test",this.sdsImpl2.getContent());
    }

    @org.junit.Test
    public void getLength() {
    }

    @org.junit.Test
    public void getUsed() {
    }
}