/**
 * @(#)IRListViewListener.java 2013-10-23 Copyright 2013 .
 * All rights reserved.
 */

package common.android.ui.myxlistview.libraries;

/**
 * implements this interface to get refresh/load more event.
 *
 * @author chenjian
 * @date 2013-10-23
 */

public interface IRListViewListener {

    void onRefresh();

    void onLoadMore();
}
