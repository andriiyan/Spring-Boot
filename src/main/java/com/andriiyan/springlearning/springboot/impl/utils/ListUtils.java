package com.andriiyan.springlearning.springboot.impl.utils;

import java.util.Iterator;

public final class ListUtils {

    private ListUtils() {}

    public static String joinToString(Iterable<String> items, char separator) {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> iterator = items.iterator();
        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next());
            if (iterator.hasNext()) {
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString();
    }

}
