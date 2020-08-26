package com.leo.word.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leo
 * @version 1.0.0
 * @description
 * @create 2020/8/25 15:27
 */
public class ListUtils {

    public static <T> List<T> subList(List<T> list, int start, int end) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        if (list.size() >= end) {
            return list.subList(start, end);
        }
        if (list.size() > start) {
            return list.subList(start, list.size());
        }
        return new ArrayList<>();
    }
}
