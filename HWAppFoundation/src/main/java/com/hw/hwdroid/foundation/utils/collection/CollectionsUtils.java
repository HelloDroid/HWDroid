package com.hw.hwdroid.foundation.utils.collection;


import com.hw.hwdroid.foundation.common.lang.NullCollection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author chenjian
 * @date 2014-4-23
 */

public final class CollectionsUtils {

    //    /**
    //     * 排序
    //     *
    //     * @param firstStr
    //     * @return
    //     */
    //    public static Comparator<String> sortStr(final String firstStr) {
    //
    //        return new Comparator<String>() {
    //
    //            @Override
    //            public int compare(String lhs, String rhs) {
    //                if (lhs == null || rhs == null) {
    //                    return -1;
    //                }
    //
    //                if (rhs == null || lhs == null) {
    //                    return -1;
    //                }
    //
    //                // firstStr排序到第一位
    //                if (!StringUtils.isNullOrWhiteSpace(firstStr)) {
    //                    if (StringUtils.equals(lhs, firstStr)) {
    //                        return -1;
    //                    }
    //                    if (StringUtils.equals(rhs, firstStr)) {
    //                        return 1;
    //                    }
    //                }
    //
    //                try {
    //                    return new PinyinComparator(false).compare(lhs, rhs);
    //                } catch (Exception e) {
    //                }
    //
    //                return 0;
    //            }
    //        };
    //    }

    /**
     * 去除重复元素
     *
     * @param list
     */
    public static void removeDuplicate(List<Object> list) {
        if (null == list || list.isEmpty()) {
            return;
        }

        Set<Object> set = new HashSet<Object>(list);
        list.clear();
        list.addAll(set);
    }

    public static void removeDuplicate4String(List<String> list) {
        if (null == list || list.isEmpty()) {
            return;
        }

        Set<String> set = new HashSet<String>(list);
        list.clear();
        list.addAll(set);
    }

    /**
     * 删除空数据
     *
     * @param list
     */
    public static void removeNullCollection(List<?> list) {
        if (null == list || list.isEmpty()) {
            return;
        }

        list.removeAll(new NullCollection());
    }

}
