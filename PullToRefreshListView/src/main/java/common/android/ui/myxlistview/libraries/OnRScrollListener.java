/**
 * @(#)OnRScrollListener.java 2013-10-23 Copyright 2013 . All
 * rights reserved.
 */

package common.android.ui.myxlistview.libraries;

import android.view.View;

/**
 * you can listen ListView.OnScrollListener or this one. it will invoke
 * onScrolling when header/footer scroll back.
 *
 * @author chenjian
 * @date 2013-10-23
 */

public interface OnRScrollListener {

    void onRScrolling(View view);
}
