package com.leo.word.util;

import java.util.Collection;

/**
 * @author leo
 * @version 1.0.0
 * @description
 * @create 2020/8/26 15:17
 */
public class CollectionUtils {
    public static boolean isEmpty(final Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNotEmpty(final Collection<?> coll) {
        return !isEmpty(coll);
    }

}
