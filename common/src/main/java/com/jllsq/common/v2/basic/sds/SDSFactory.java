package com.jllsq.common.v2.basic.sds;

public interface SDSFactory {

    RSimpleDynamicString newInstance();

    RSimpleDynamicString newInstance(String content);

    RSimpleDynamicString newInstance(byte[] content);

}
