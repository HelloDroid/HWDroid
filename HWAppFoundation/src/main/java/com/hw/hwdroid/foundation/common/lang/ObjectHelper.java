package com.hw.hwdroid.foundation.common.lang;

/**
 * Created by ChenJ on 2017/1/20.
 */

public class ObjectHelper {

    /** Utility class. */
    private ObjectHelper() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> T requireNonNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }

        return object;
    }

    public static void requireTrue(boolean flag, String message) {
        if (flag) {
            return;
        }

        throw new IllegalStateException(message);
    }

    public static void requireFalse(boolean flag, String message) {
        if (!flag) {
            return;
        }

        throw new IllegalStateException(message);
    }

    public static boolean equals(Object o1, Object o2) {
        return o1 == o2 || (o1 != null && o1.equals(o2));
    }

    public static int hashCode(Object o) {
        return o != null ? o.hashCode() : 0;
    }

    public static int compare(long v1, long v2) {
        return v1 < v2 ? -1 : (v1 > v2 ? 1 : 0);
    }

}
