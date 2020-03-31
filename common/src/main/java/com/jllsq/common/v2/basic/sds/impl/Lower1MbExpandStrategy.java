package com.jllsq.common.v2.basic.sds.impl;

import com.jllsq.common.v2.basic.sds.RSimpleDynamicString;

public class Lower1MbExpandStrategy implements SdsExpandStrategy {

    private static final int MB = 1024 * 1024;

    @Override
    public boolean shouldExpand(RSimpleDynamicString sds) {
        return sds.length() <= MB;
    }

    @Override
    public void expand(RSimpleDynamicString sds) {
        sds.expand(sds.length() * 2);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
