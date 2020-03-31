package com.jllsq.common.v2.basic.sds.impl;

import com.jllsq.common.v2.Ordered;
import com.jllsq.common.v2.basic.sds.RSimpleDynamicString;

public class Upper1MbExpandStrategy implements SdsExpandStrategy, Ordered {

    private static final int MB = 1024 * 1024;

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public boolean shouldExpand(RSimpleDynamicString sds) {
        return sds.length() > MB;
    }

    @Override
    public void expand(RSimpleDynamicString sds) {
        sds.expand(sds.length()+MB);
    }
}
