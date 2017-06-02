package common.xrecyclerview;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;

import common.android.ui.myxlistview.R;
import common.xrecyclerview.progressindicator.XLoadingIndicatorView;

public class XArrowRefreshHeader extends LinearLayout implements XBaseRefreshHeader {

    private int mState = STATE_NORMAL;

    private LinearLayout mContainer;
    private TextView mHeaderTimeView;
    private TextView mStatusTextView;
    private ImageView mArrowImageView;
    private XSimpleViewSwitcher mProgressBar;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

    private static final int ROTATE_ANIM_DURATION = 180;

    public int mMeasuredHeight;

    private String refreshNormalText;
    private String refreshDoneText;
    private String refreshReadyText;
    private String refreshLoadingText;

    private boolean showProgressBar = true;
    private boolean animationArrowImage = true;

    public XArrowRefreshHeader(Context context) {
        super(context);

        initView();
    }

    /**
     * @param context
     * @param attrs
     */
    public XArrowRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @SuppressLint("InflateParams")
    private void initView() {
        // 初始情况，设置下拉刷新view高度为0
        mContainer = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.listview_header, null);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        this.setLayoutParams(lp);
        this.setPadding(0, 0, 0, 0);

        addView(mContainer, new LayoutParams(LayoutParams.MATCH_PARENT, 0));
        setGravity(Gravity.BOTTOM);

        mArrowImageView = (ImageView) findViewById(R.id.xr_header_arrow);
        mStatusTextView = (TextView) findViewById(R.id.refresh_status_tv);

        refreshDoneText = getContext().getString(R.string.refresh_done);
        refreshReadyText = getContext().getString(R.string.listview_header_hint_ready);
        refreshNormalText = getContext().getString(R.string.listview_header_hint_normal);
        refreshLoadingText = getContext().getString(R.string.listview_header_hint_loading);
        mStatusTextView.setText(refreshNormalText);

        //init the progress view
        mProgressBar = (XSimpleViewSwitcher) findViewById(R.id.xr_header_progressbar);
        XLoadingIndicatorView progressView = new XLoadingIndicatorView(getContext());
        progressView.setIndicatorColor(0xffB5B5B5);
        progressView.setIndicatorId(XProgressStyle.SysProgress);
        mProgressBar.setView(progressView);

        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);

        mHeaderTimeView = (TextView) findViewById(R.id.last_refresh_time);
        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = getMeasuredHeight();
    }

    public void setProgressStyle(int style) {
        if (style == XProgressStyle.SysProgress) {
            mProgressBar.setView(new ProgressBar(getContext(), null, android.R.attr.progressBarStyleSmall));
        } else {
            XLoadingIndicatorView progressView = new XLoadingIndicatorView(this.getContext());
            progressView.setIndicatorColor(0xffB5B5B5);
            progressView.setIndicatorId(style);
            mProgressBar.setView(progressView);
        }
    }

    public void setArrowImageView(@DrawableRes int resId) {
        mArrowImageView.setImageResource(resId);
    }

    public void setArrowImageView(Drawable drawable) {
        mArrowImageView.setImageDrawable(drawable);
    }

    public void setShowProgressBar(boolean showProgressBar) {
        this.showProgressBar = showProgressBar;
    }

    public void setAnimationArrowImage(boolean animationArrowImage) {
        this.animationArrowImage = animationArrowImage;
    }

    public void setState(int state) {
        if (state == mState) {
            return;
        }

        // 显示进度
        if (state == STATE_REFRESHING) {
            mArrowImageView.clearAnimation();
            if (showProgressBar) {
                mArrowImageView.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
            }
            smoothScrollTo(mMeasuredHeight);
        } else if (state == STATE_DONE) {
            if (animationArrowImage) {
                mArrowImageView.setVisibility(View.INVISIBLE);
            }
            mProgressBar.setVisibility(View.INVISIBLE);
        }
        // 显示箭头图片
        else {
            mArrowImageView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        switch (state) {
            case STATE_NORMAL:
                if (mState == STATE_RELEASE_TO_REFRESH && animationArrowImage) {
                    mArrowImageView.startAnimation(mRotateDownAnim);
                }

                if (mState == STATE_REFRESHING) {
                    mArrowImageView.clearAnimation();
                }

                mStatusTextView.setText(refreshNormalText);
                break;

            case STATE_RELEASE_TO_REFRESH:
                if (mState != STATE_RELEASE_TO_REFRESH) {
                    mArrowImageView.clearAnimation();
                    if (animationArrowImage) {
                        mArrowImageView.startAnimation(mRotateUpAnim);
                    }
                    mStatusTextView.setText(refreshReadyText);
                }
                break;

            case STATE_REFRESHING:
                mStatusTextView.setText(refreshLoadingText);
                break;

            case STATE_DONE:
                mStatusTextView.setText(refreshDoneText);
                break;
            default:
        }

        mState = state;
    }


    public int getState() {
        return mState;
    }

    @Override
    public void refreshComplete() {
        mHeaderTimeView.setText(friendlyTime(new Date()));

        setState(STATE_DONE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                reset();
            }
        }, 200);
    }

    public void setVisibleHeight(int height) {
        if (height < 0) {
            height = 0;
        }

        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisibleHeight() {
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        return lp.height;
    }

    @Override
    public void onMove(float delta) {
        if (getVisibleHeight() > 0 || delta > 0) {
            setVisibleHeight((int) delta + getVisibleHeight());

            // 未处于刷新状态，更新箭头
            if (mState <= STATE_RELEASE_TO_REFRESH) {
                if (getVisibleHeight() > mMeasuredHeight) {
                    setState(STATE_RELEASE_TO_REFRESH);
                } else {
                    setState(STATE_NORMAL);
                }
            }
        }
    }

    @Override
    public boolean releaseAction() {
        boolean isOnRefresh = false;
        int height = getVisibleHeight();

        // not visible.
        if (height == 0) {
            isOnRefresh = false;
        }

        if (getVisibleHeight() > mMeasuredHeight && mState < STATE_REFRESHING) {
            setState(STATE_REFRESHING);
            isOnRefresh = true;
        }

        // refreshing and header isn't shown fully. do nothing.
        if (mState == STATE_REFRESHING && height <= mMeasuredHeight) {
            //return;
        }

        if (mState != STATE_REFRESHING) {
            smoothScrollTo(0);
        }

        if (mState == STATE_REFRESHING) {
            int destHeight = mMeasuredHeight;
            smoothScrollTo(destHeight);
        }

        return isOnRefresh;
    }

    public void reset() {
        smoothScrollTo(0);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                setState(STATE_NORMAL);
            }
        }, 500);
    }

    private void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }


    public void setRefreshNormalText(String refreshNormalText) {
        this.refreshNormalText = refreshNormalText;
    }

    public void setRefreshDoneText(String refreshDoneText) {
        this.refreshDoneText = refreshDoneText;
    }

    public void setRefreshReadyText(String refreshReadyText) {
        this.refreshReadyText = refreshReadyText;
    }

    public void setRefreshLoadingText(String refreshLoadingText) {
        this.refreshLoadingText = refreshLoadingText;
    }

    public String friendlyTime(Date time) {
        //获取time距离当前的秒数
        int ct = (int) ((System.currentTimeMillis() - time.getTime()) / 1000);

        if (ct == 0) {
            return getContext().getString(R.string.xr_time_just);
        }

        if (ct > 0 && ct < 60) {
            return ct + getContext().getString(R.string.xr_time_beforeSeconds);
        }

        if (ct >= 60 && ct < 3600) {
            return Math.max(ct / 60, 1) + getContext().getString(R.string.xr_time_beforeMinute);
        }

        if (ct >= 3600 && ct < 86400) {
            return ct / 3600 + getContext().getString(R.string.xr_time_beforeHours);
        }

        // 86400 * 30
        if (ct >= 86400 && ct < 2592000) {
            int day = ct / 86400;
            return day + getContext().getString(R.string.xr_time_beforeDay);
        }

        // 86400 * 30
        if (ct >= 2592000 && ct < 31104000) {
            return ct / 2592000 + getContext().getString(R.string.xr_time_beforeMonth);
        }

        return ct / 31104000 + getContext().getString(R.string.xr_time_beforeYear);
    }

}