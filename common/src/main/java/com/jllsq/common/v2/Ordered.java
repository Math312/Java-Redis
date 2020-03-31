package com.jllsq.common.v2;

import java.util.Comparator;
import java.util.List;

public interface Ordered {

    public static Ordered HIGHEST = new Ordered() {
        @Override
        public int getOrder() {
            return 0;
        }
    };

    public static Ordered LOWEST = new Ordered() {
        @Override
        public int getOrder() {
            return Integer.MAX_VALUE;
        }
    };

    public static void sortOrdered(List<? extends Ordered> list) {
        list.sort(new Comparator<Ordered>() {
            @Override
            public int compare(Ordered o1, Ordered o2) {
                return o2.getOrder() - o1.getOrder();
            }
        });
    };

    public int getOrder();

}
