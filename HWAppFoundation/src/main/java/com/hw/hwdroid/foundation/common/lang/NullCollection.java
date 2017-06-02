package com.hw.hwdroid.foundation.common.lang;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.RandomAccess;

/**
 * @author chenjian
 * @date 2014-4-16
 */

public class NullCollection extends AbstractList<Object> implements RandomAccess, Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7671651320563840668L;

    /**
     * @see AbstractList#get(int)
     */
    @Override
    public Object get(int location) {
        return null;
    }

    /**
     * @see java.util.AbstractCollection#size()
     */
    @Override
    public int size() {
        return 1;
    }

    public boolean contains(Object obj) {
        return null == obj;
    }

    private Object readResolve() {
        return null;
    }

}
