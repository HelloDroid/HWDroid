/**
 * @(#)FileLastModifSort.java 2014-5-16 Copyright 2014 . All
 *                            rights reserved.
 */

package com.hw.hwdroid.foundation.common.lang;

import java.io.File;
import java.util.Comparator;

/**
 * 根据文件的最后修改时间进行排序
 *
 * @author chenjian
 * @date 2014-5-16
 */

public class FileLastModifySort implements Comparator<File> {

    /**
     * @see Comparator#compare(Object, Object)
     */
    @Override
    public int compare(File lhs, File rhs) {
        if (lhs.lastModified() > rhs.lastModified()) {
            return 1;
        } else if (lhs.lastModified() == rhs.lastModified()) {
            return 0;
        } else {
            return -1;
        }
    }

}
