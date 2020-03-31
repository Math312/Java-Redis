package com.jllsq.common.v2.basic.sds.impl;

import com.jllsq.common.v2.basic.sds.SDSFactory;
import com.jllsq.common.v2.basic.sds.RSimpleDynamicString;

public class BasicSDSFactory implements SDSFactory {
    @Override
    public RSimpleDynamicString newInstance() {
        return new RSimpleDynamicStringImpl();
    }

    @Override
    public RSimpleDynamicString newInstance(String content) {
        return new RSimpleDynamicStringImpl(content);
    }

    @Override
    public RSimpleDynamicString newInstance(byte[] content) {
        return new RSimpleDynamicStringImpl(content);
    }
}
