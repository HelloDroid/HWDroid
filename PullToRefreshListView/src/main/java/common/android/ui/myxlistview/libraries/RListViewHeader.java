/**
 * @file XListViewHeader.java
 * @create Apr 18, 2012 5:22:27 PM
 * @author Maxwin
 * @description XListView's header
 */
package common.android.ui.myxlistview.libraries;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import common.android.ui.myxlistview.R;


public class RListViewHeader extends LinearLayout {

    private LinearLayout mContainer;
    private ImageView mArrowImageView;
    private ProgressBar mProgressBar;
    private TextView mHintTextView;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

    private int mState = STATE_NORMAL;

    private final int ROTATE_ANIM_DURATION = 180;

    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;

    // 是否启用简单风格:只有文字
    private boolean mIsSingleStyle = false;

    public RListViewHeader(Context context) {
        super(context);

        mIsSingleStyle = false;

        initView(context);
    }

    public RListViewHeader(Context context, boolean isSingleStyle) {
        super(context);

        mIsSingleStyle = isSingleStyle;

        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public RListViewHeader(Context context, boolean isSingleStyle, AttributeSet attrs) {
        super(context, attrs);
        mIsSingleStyle = isSingleStyle;
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public RListViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(context);
    }

    /**
     * @return the mIsSingleStyle
     */
    public boolean isSingleStyle() {
        return mIsSingleStyle;
    }

    @SuppressLint("InflateParams")
    private void initView(Context context) {
        // 初始情况，设置下拉刷新view高度为0
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.rlistview_header, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);

        mHintTextView = (TextView) findViewById(R.id.headerText);
        mArrowImageView = (ImageView) findViewById(R.id.header_arrow);
        mProgressBar = (ProgressBar) findViewById(R.id.header_progressbar);

        if (mIsSingleStyle) {
            mArrowImageView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            findViewById(R.id.subHeaderText).setVisibility(View.GONE);
        } else {
            mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
            mRotateUpAnim.setFillAfter(true);
            mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
            mRotateDownAnim.setFillAfter(true);
        }
    }

    public void setState(int state) {
        if (state == mState)
            return;

        if (!mIsSingleStyle) {
            // 显示进度
            if (state == STATE_REFRESHING) {
                mArrowImageView.clearAnimation();
                mArrowImageView.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
            }
            // 显示箭头图片
            else {
                mArrowImageView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }

        switch (state) {
            case STATE_NORMAL:
                if (!mIsSingleStyle) {
                    if (mState == STATE_READY) {
                        mArrowImageView.startAnimation(mRotateDownAnim);
                    }
                    if (mState == STATE_REFRESHING) {
                        mArrowImageView.clearAnimation();
                    }
                }
                mHintTextView.setText(R.string.listview_header_hint_normal);
                break;
            case STATE_READY:
                if (mState != STATE_READY) {
                    if (!mIsSingleStyle) {
                        mArrowImageView.clearAnimation();
                        mArrowImageView.startAnimation(mRotateUpAnim);
                    }
                    mHintTextView.setText(R.string.listview_header_hint_ready);
                }
                break;
            case STATE_REFRESHING:
                mHintTextView.setText(R.string.listview_header_hint_loading);
                break;
            default:
        }

        mState = state;
    }

    public void setVisiableHeight(int height) {
        if (height < 0)
            height = 0;
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisiableHeight() {
        return mContainer.getHeight();
    }

}
