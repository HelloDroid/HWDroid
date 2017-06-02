package common.xrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import common.android.ui.myxlistview.R;
import common.xrecyclerview.progressindicator.XLoadingIndicatorView;

public class XLoadingMoreFooter extends LinearLayout {

    private XSimpleViewSwitcher progressCon;

    public final static int STATE_LOADING = 0;
    public final static int STATE_COMPLETE = 1;
    public final static int STATE_NOMORE = 2;

    private TextView mText;
    private String loadingHint;
    private String noMoreHint;
    private String loadingDoneHint;

    public XLoadingMoreFooter(Context context) {
        super(context);
        initView();
    }

    /**
     * @param context
     * @param attrs
     */
    public XLoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView();
    }

    public void initView() {
        setGravity(Gravity.CENTER);
        setMinimumHeight(dp2px(getContext(), 44F));
        setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        progressCon = new XSimpleViewSwitcher(getContext());
        progressCon.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        XLoadingIndicatorView progressView = new XLoadingIndicatorView(this.getContext());
        progressView.setIndicatorColor(0xffB5B5B5);
        progressView.setIndicatorId(XProgressStyle.BallSpinFadeLoader);
        progressCon.setView(progressView);

        addView(progressCon);
        mText = new TextView(getContext());
        mText.setText(R.string.load_more);

        loadingHint = (String) getContext().getText(R.string.load_more);
        noMoreHint = (String) getContext().getText(R.string.load_no_more);
        loadingDoneHint = (String) getContext().getText(R.string.loading_done);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins((int) getResources().getDimension(R.dimen.textandiconmargin), 0, 0, 0);

        mText.setLayoutParams(layoutParams);
        addView(mText);
    }

    public void setProgressStyle(int style) {
        if (style == XProgressStyle.SysProgress) {
            progressCon.setView(new ProgressBar(getContext(), null, android.R.attr.progressBarStyleSmall));
        } else {
            XLoadingIndicatorView progressView = new XLoadingIndicatorView(this.getContext());
            progressView.setIndicatorColor(0xffB5B5B5);
            progressView.setIndicatorId(style);
            progressCon.setView(progressView);
        }
    }


    public void setLoadingHint(String hint) {
        loadingHint = hint;
    }

    public void setNoMoreHint(String hint) {
        noMoreHint = hint;
    }

    public void setLoadingDoneHint(String hint) {
        loadingDoneHint = hint;
    }

    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                mText.setText(loadingHint);
                this.setVisibility(View.VISIBLE);
                progressCon.setVisibility(View.VISIBLE);
                break;

            case STATE_COMPLETE:
                mText.setText(loadingDoneHint);
                this.setVisibility(View.GONE);
                break;

            case STATE_NOMORE:
                mText.setText(noMoreHint);
                this.setVisibility(View.VISIBLE);
                progressCon.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * dp转px
     *
     * @param context
     * @param dipValue
     * @return
     */
    private int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f * (dipValue >= 0 ? 1 : -1));
    }

}
