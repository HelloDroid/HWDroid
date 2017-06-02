package com.hw.hwdroid.dialog.ActionSheet;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hw.hwdroid.dialog.R;
import com.hw.hwdroid.dialog.utils.StringUtils;
import com.hw.hwdroid.dialog.utils.Util;

import java.util.List;


/**
 * 显示操作菜单列表
 * <p>
 * Created by ChenJ on 2017/5/4.
 */

public class ActionSheetDialog extends Dialog {
    private RecyclerView mRecyclerView;

    private List<String> actions;
    private onClickItem onClickItem;

    private View view;
    private TextView mTitleTv;

    private int selectedColor;
    private boolean useSelectedColoe;
    private ColorStateList colorStateList;

    private int selectedIndex;
    private boolean useThemStyle;

    public ActionSheetDialog(@NonNull Context context, String title, @NonNull List<String> actions) {
        this(context, true, title, actions, -1, View.NO_ID);
    }

    public ActionSheetDialog(@NonNull Context context, boolean useThemStyle, String title, @NonNull List<String> actions, int selectedIndex, @ColorRes int selectedColorResId) {
        super(context, R.style.DialogTheme_Full);

        this.actions = actions;
        this.useThemStyle = useThemStyle;
        this.selectedIndex = selectedIndex;

        setCancelable(true);
        setCanceledOnTouchOutside(true);
        view = LayoutInflater.from(context).inflate(R.layout.action_sheet_layout, null);
        setContentView(view);


        setSelectedColor(selectedColorResId);
        setColorStateList(useThemStyle ? R.color.action_sheet_textcolor_selector_theme : R.color.action_sheet_textcolor_selector);

        mTitleTv = (TextView) view.findViewById(R.id.title_action);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.a_rv);

        if (this.actions.size() > 6) {
            mRecyclerView.getLayoutParams().height = Util.dp2px(context, 45F) * 6 + 5;
        }


        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setWindowAnimations(R.style.Slide_FromBottom);

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.alpha = 0.9876f;
        layoutParams.dimAmount = 0.6789f;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

        mTitleTv.setText(StringUtils.changeNullOrWhiteSpace(title));
        mTitleTv.setVisibility(StringUtils.isNullOrWhiteSpace(title) ? View.GONE : View.VISIBLE);

        view.findViewById(R.id.cancel_action).setOnClickListener(v -> dismiss());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new ActionSheetDecoration(context));
        mRecyclerView.setAdapter(new ActionSheetAdapter());

        if (selectedIndex > 0) {
            view.postDelayed(() -> mRecyclerView.smoothScrollToPosition(selectedIndex), 500);
        }
    }

    public void smoothScrollToPosition(final int pos) {
        if (null == view || selectedIndex < 0) {
            return;
        }

        view.postDelayed(() -> mRecyclerView.smoothScrollToPosition(pos), 500);
    }

    public void smoothScrollToSelectedPosition() {
        smoothScrollToPosition(selectedIndex);
    }

    public void setSelectedIndex(final int selectedIndex) {
        this.selectedIndex = selectedIndex;
        smoothScrollToSelectedPosition();
    }

    public void setSelectedColor(@ColorRes int id) {
        if (id < 0 || id == View.NO_ID) {
            this.useSelectedColoe = false;
            return;
        }

        this.useSelectedColoe = true;
        this.selectedColor = getContext().getResources().getColor(id);
    }

    public void setOnClickItem(ActionSheetDialog.onClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public void setColorStateList(@ColorRes int id) {
        if (id < 0 || id == View.NO_ID) {
            return;
        }

        colorStateList = getContext().getResources().getColorStateList(id);
    }

    class ActionSheetAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder holder = new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.action_sheet_item, parent, false));

            if (useThemStyle && null != holder.itemTextView) {
                holder.itemTextView.setTextColor(colorStateList);
            }

            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final String item = StringUtils.changeNullOrWhiteSpace(actions.get(position));
            holder.itemTextView.setText(item);

            if (useSelectedColoe) {
                if (selectedIndex == position) {
                    holder.itemTextView.setTextColor(selectedColor);
                } else {
                    holder.itemTextView.setTextColor(colorStateList);
                }
            }


            holder.itemTextView.setOnClickListener(v -> {
                selectedIndex = position;

                if (useSelectedColoe) {
                    notifyDataSetChanged();
                }

                dismiss();

                if (null != onClickItem) {
                    onClickItem.onClick(position, item);
                }
            });
        }

        @Override
        public int getItemCount() {
            return null == actions ? 0 : actions.size();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemTextView;

        ViewHolder(View itemView) {
            super(itemView);

            itemTextView = (TextView) itemView.findViewById(R.id.item_tv);
        }

    }


    class ActionSheetDecoration extends RecyclerView.ItemDecoration {

        private Context mContext;
        private Drawable mDivider;
        private int mDividerHeight = 1; //分割线高度，默认为1px

        private int mOrientation;
        public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
        public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

        //我们通过获取系统属性中的listDivider来添加，在系统中的AppTheme中设置
        final int[] ATRRS = new int[]{
                android.R.attr.listDivider
        };

        ActionSheetDecoration(Context context) {
            this.mContext = context;
            final TypedArray ta = context.obtainStyledAttributes(ATRRS);
            mDivider = ta.getDrawable(0);
            ta.recycle();

            // mDividerHeight = mDivider.getIntrinsicHeight();
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(0, 0, 0, mDividerHeight);
        }

        @Override
        public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();
            final int childCount = parent.getChildCount();

            for (int i = 0; i < childCount - 1; i++) {
                final View child = parent.getChildAt(i);

                // 获得child的布局信息
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDividerHeight;

                if (mDivider != null) {
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(canvas);
                }
            }
        }
    }

    public interface onClickItem {
        void onClick(int index, String item);
    }

}
