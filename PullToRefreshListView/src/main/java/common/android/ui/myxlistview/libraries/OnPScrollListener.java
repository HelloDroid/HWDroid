/**
 * @(#)OnPScrollListener.java 2014-2-21 Copyright 2014 . All
 * rights reserved.
 */

package common.android.ui.myxlistview.libraries;

import android.view.View;

/**
 * you can listen ListView.OnScrollListener or this one. it will invoke
 * onXScrolling when header/footer scroll back.
 * @author chenjian
 * @date 2014-2-21
 */

public interface OnPScrollListener {

    void onPScrolling(View view);
}
