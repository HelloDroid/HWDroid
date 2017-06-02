/**
 * @(#)IPinnedSectionedAdapter.java 2014-3-6 Copyright 2014 .
 * All rights reserved.
 */

package common.android.ui.myxlistview.libraries;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author chenjian
 * @date 2014-3-6
 */

public interface IPinnedSectionedAdapter {

    int count();

    int getSectionForPosition(int position);

    int getPositionInSectionForPosition(int position);

    boolean isSectionHeader(int position);

    /**
     * ListView Item View Type
     * @param section
     * @param position
     * @return
     */
    int getItemViewType(int section, int position);

    int getItemViewTypeCount();

    /**
     * Pinned View Type
     */
    int getSectionHeaderViewType(int section);

    int getSectionHeaderViewTypeCount();

    Object getItem(int section, int position);

    long getItemId(int section, int position);

    Object getSectionItem(int section);

    /**
     * Pinned Header的Size
     * @return
     */
    int getSectionCount();

    /**
     * 对应Pinned Header的Size
     * @param section
     * @return
     */
    int getCountForSection(int section);

    View getItemView(int section, int positionInSection, View convertView, ViewGroup parent);

    View getSectionHeaderView(int section, View convertView, ViewGroup parent);
}
