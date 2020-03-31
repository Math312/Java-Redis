package com.jllsq.common.v2.basic.sds;

public interface RSimpleDynamicString {

    int length();

    int available();

    void clear();

    int getUsedLength();

    RSimpleDynamicString concat(RSimpleDynamicString sds);

    RSimpleDynamicString concat(String str);

    RSimpleDynamicString copy(String str);

    RSimpleDynamicString expand();

    RSimpleDynamicString expand(int length);

    byte[] getBuf();

    int compare(RSimpleDynamicString sds);

    int compare(String str);
}
