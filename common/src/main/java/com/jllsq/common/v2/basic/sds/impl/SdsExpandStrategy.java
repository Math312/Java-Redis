package com.jllsq.common.v2.basic.sds.impl;

import com.jllsq.common.v2.Ordered;
import com.jllsq.common.v2.basic.sds.RSimpleDynamicString;

public interface SdsExpandStrategy extends Ordered {

    public boolean shouldExpand(RSimpleDynamicString sds);

    public void expand(RSimpleDynamicString sds);

}
