/**
 * @(#)ISlideListItemListener.java 2014-1-20 Copyright 2014 .
 *                                 All rights reserved.
 */

package common.android.ui.myxlistview.slideitem.libs;

import android.view.View;

/**
 * @author chenjian
 * @date 2014-1-20
 */

public interface ISlideListItemListener {

	void onActionDown(int downPosition, View itemView);

	void onActionUp(int downPosition, View itemView);

	void onActionMove(boolean isScrollInY, int downPosition, View itemView);

	void onClick4HideView(int downPosition, View itemView);
}
