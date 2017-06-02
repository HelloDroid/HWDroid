package common.xrecyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.ArrayList;
import java.util.List;

public class XRecyclerView extends RecyclerView {

    // 设置一个很大的数字,尽可能避免和用户的adapter冲突
    // 下面的ItemViewType是保留值(ReservedItemViewType),如果用户的adapter与它们重复将会强制抛出异常。不过为了简化,我们检测到重复时对用户的提示是ItemViewType必须小于10000
    private static final int TYPE_FOOTER = 10001;
    private static final int HEADER_INIT_INDEX = 10002;
    private static final int TYPE_REFRESH_HEADER = 10000;

    // 每个header必须有不同的type,不然滚动的时候顺序会变化
    // private static List<Integer> sHeaderTypes = new ArrayList<>(5);
    private List<Integer> sHeaderTypes = new ArrayList<>(5);

    private boolean isNoMore = false;
    private boolean isLoadingData = false;

    private int mRefreshProgressStyle = XProgressStyle.SysProgress;
    private int mLoadingMoreProgressStyle = XProgressStyle.SysProgress;
    private ArrayList<View> mHeaderViews = new ArrayList<>();
    private WrapAdapter mWrapAdapter;
    private float mLastY = -1;
    private static final float DRAG_RATE = 3;
    private LoadingListener mLoadingListener;
    private LoadingListener mLoadingMoreListener;
    private XArrowRefreshHeader mRefreshHeader;
    private boolean pullRefreshEnabled = true;
    private boolean loadingMoreEnabled = true;

    private int mPageCount = 0;

    // adapter没有数据的时候显示,类似于listView的emptyView
    private View mEmptyView;
    private View mFootView;

    private final RecyclerView.AdapterDataObserver mDataObserver = new DataObserver();
    private XAppBarStateChangeListener.State appbarState = XAppBarStateChangeListener.State.EXPANDED;

    public XRecyclerView(Context context) {
        this(context, null);
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init() {
        if (pullRefreshEnabled) {
            mRefreshHeader = new XArrowRefreshHeader(getContext());
            mRefreshHeader.setProgressStyle(mRefreshProgressStyle);
        }

        XLoadingMoreFooter footView = new XLoadingMoreFooter(getContext());
        footView.setProgressStyle(mLoadingMoreProgressStyle);
        mFootView = footView;
        mFootView.setVisibility(GONE);
    }

    public void setFootViewText(String loading, String noMore) {
        if (mFootView instanceof XLoadingMoreFooter) {
            ((XLoadingMoreFooter) mFootView).setLoadingHint(loading);
            ((XLoadingMoreFooter) mFootView).setNoMoreHint(noMore);
        }
    }

    public void setHeaderViewText(String refreshNormalText, String refreshReadyText, String refreshLoadingText, String refreshDoneText) {
        if (null == mRefreshHeader) {
            return;
        }
        mRefreshHeader.setRefreshNormalText(refreshNormalText);
        mRefreshHeader.setRefreshReadyText(refreshReadyText);
        mRefreshHeader.setRefreshLoadingText(refreshLoadingText);
        mRefreshHeader.setRefreshDoneText(refreshDoneText);
    }

    public void addHeaderView(View view) {
        sHeaderTypes.add(HEADER_INIT_INDEX + mHeaderViews.size());
        mHeaderViews.add(view);
        if (mWrapAdapter != null) {
            mWrapAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 根据Header的ViewType判断是哪个header
     *
     * @param itemType
     * @return
     */
    private View getHeaderViewByType(int itemType) {
        if (!isHeaderType(itemType)) {
            return null;
        }

        return mHeaderViews.get(itemType - HEADER_INIT_INDEX);
    }

    /**
     * 判断一个type是否为HeaderType
     *
     * @param itemViewType
     * @return
     */
    private boolean isHeaderType(int itemViewType) {
        return mHeaderViews.size() > 0 && sHeaderTypes.contains(itemViewType);
    }

    /**
     * 判断是否是XRecyclerView保留的itemViewType
     *
     * @param itemViewType
     * @return
     */
    private boolean isReservedItemViewType(int itemViewType) {
        return itemViewType == TYPE_REFRESH_HEADER || itemViewType == TYPE_FOOTER || sHeaderTypes.contains(itemViewType);
    }

    public void setFootView(final View view) {
        mFootView = view;
    }

    public void loadMoreComplete() {
        isLoadingData = false;
        if (mFootView instanceof XLoadingMoreFooter) {
            ((XLoadingMoreFooter) mFootView).setState(XLoadingMoreFooter.STATE_COMPLETE);
        } else {
            mFootView.setVisibility(View.GONE);
        }
    }

    public void setNoMore(boolean noMore) {
        isNoMore = noMore;
        isLoadingData = false;

        if (mFootView instanceof XLoadingMoreFooter) {
            ((XLoadingMoreFooter) mFootView).setState(isNoMore ? XLoadingMoreFooter.STATE_NOMORE : XLoadingMoreFooter.STATE_COMPLETE);
        } else {
            mFootView.setVisibility(View.GONE);
        }
    }

    public void refresh() {
        if (pullRefreshEnabled && mLoadingListener != null) {
            mRefreshHeader.setState(XArrowRefreshHeader.STATE_REFRESHING);
            mLoadingListener.onLoading();
        }
    }

    public void reset() {
        setNoMore(false);
        loadMoreComplete();
        refreshComplete();
    }

    public void refreshComplete() {
        mRefreshHeader.refreshComplete();
        setNoMore(false);
    }

    public void setRefreshHeader(XArrowRefreshHeader refreshHeader) {
        mRefreshHeader = refreshHeader;
    }

    /**
     * 设置下拉刷新是否可用
     *
     * @param enabled
     */
    public void setPullRefreshEnabled(boolean enabled) {
        pullRefreshEnabled = enabled;
    }

    /**
     * 设置加载更多是否可用
     *
     * @param enabled true: 可用
     */
    public void setLoadingMoreEnabled(boolean enabled) {
        loadingMoreEnabled = enabled;

        if (!enabled) {
            if (mFootView instanceof XLoadingMoreFooter) {
                ((XLoadingMoreFooter) mFootView).setState(XLoadingMoreFooter.STATE_COMPLETE);
            }
        }
    }

    public void setRefreshProgressStyle(int style) {
        mRefreshProgressStyle = style;
        if (mRefreshHeader != null) {
            mRefreshHeader.setProgressStyle(style);
        }
    }

    public void setLoadingMoreProgressStyle(int style) {
        mLoadingMoreProgressStyle = style;
        if (mFootView instanceof XLoadingMoreFooter) {
            ((XLoadingMoreFooter) mFootView).setProgressStyle(style);
        }
    }

    public void setArrowImageView(int resId) {
        if (mRefreshHeader != null) {
            mRefreshHeader.setArrowImageView(resId);
        }
    }

    public void setArrowImageView(Drawable drawable) {
        if (mRefreshHeader != null) {
            mRefreshHeader.setArrowImageView(drawable);
        }
    }

    public void setHeaderShowProgressBar(boolean showProgressBar) {
        if (mRefreshHeader != null) {
            mRefreshHeader.setShowProgressBar(showProgressBar);
        }
    }

    public void setHeaderAnimationArrowImage(boolean animationArrowImage) {
        if (mRefreshHeader != null) {
            mRefreshHeader.setAnimationArrowImage(animationArrowImage);
        }
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
        mDataObserver.onChanged();
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);

        adapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }

    /**
     * 避免用户自己调用getAdapter() 引起的ClassCastException
     *
     * @return
     */
    @Override
    public Adapter getAdapter() {
        if (mWrapAdapter != null) {
            return mWrapAdapter.getOriginalAdapter();
        } else {
            return null;
        }
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);

        if (mWrapAdapter != null) {
            if (layout instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) layout);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (mWrapAdapter.isHeader(position) || mWrapAdapter.isFooter(position) || mWrapAdapter.isRefreshHeader(position)) ? gridManager.getSpanCount() : 1;
                    }
                });
            }
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadingMoreListener != null && !isLoadingData && loadingMoreEnabled) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;

            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }

            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1 && layoutManager.getItemCount() > layoutManager.getChildCount() && !isNoMore && mRefreshHeader.getState() < XArrowRefreshHeader.STATE_REFRESHING) {
                isLoadingData = true;
                if (mFootView instanceof XLoadingMoreFooter) {
                    ((XLoadingMoreFooter) mFootView).setState(XLoadingMoreFooter.STATE_LOADING);
                } else {
                    mFootView.setVisibility(View.VISIBLE);
                }
                mLoadingMoreListener.onLoading();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (isOnTop() && pullRefreshEnabled && appbarState == XAppBarStateChangeListener.State.EXPANDED) {
                    mRefreshHeader.onMove(deltaY / DRAG_RATE);
                    if (mRefreshHeader.getVisibleHeight() > 0 && mRefreshHeader.getState() < XArrowRefreshHeader.STATE_REFRESHING) {
                        return false;
                    }
                }
                break;

            default:
                mLastY = -1; // reset
                if (isOnTop() && pullRefreshEnabled && appbarState == XAppBarStateChangeListener.State.EXPANDED) {
                    if (mRefreshHeader.releaseAction()) {
                        if (mLoadingListener != null) {
                            mLoadingListener.onLoading();
                        }
                    }
                }
                break;
        }

        return super.onTouchEvent(ev);
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private boolean isOnTop() {
        return mRefreshHeader.getParent() != null;
    }

    private class DataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }

            if (mWrapAdapter != null && mEmptyView != null) {
                int emptyCount = 1 + mWrapAdapter.getHeadersCount();
                if (loadingMoreEnabled) {
                    emptyCount++;
                }
                if (mWrapAdapter.getItemCount() == emptyCount) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    XRecyclerView.this.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    XRecyclerView.this.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    }


    private class WrapAdapter extends RecyclerView.Adapter<ViewHolder> {

        private RecyclerView.Adapter adapter;

        public WrapAdapter(RecyclerView.Adapter adapter) {
            this.adapter = adapter;
        }

        public RecyclerView.Adapter getOriginalAdapter() {
            return this.adapter;
        }

        public boolean isHeader(int position) {
            return position >= 1 && position < mHeaderViews.size() + 1;
        }

        public boolean isFooter(int position) {
            return loadingMoreEnabled && position == getItemCount() - 1;
        }

        public boolean isRefreshHeader(int position) {
            return position == 0;
        }

        public int getHeadersCount() {
            return mHeaderViews.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_REFRESH_HEADER) {
                return new SimpleViewHolder(mRefreshHeader);
            } else if (isHeaderType(viewType)) {
                return new SimpleViewHolder(getHeaderViewByType(viewType));
            } else if (viewType == TYPE_FOOTER) {
                return new SimpleViewHolder(mFootView);
            }

            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (isHeader(position) || isRefreshHeader(position)) {
                return;
            }

            int adjPosition = position - (getHeadersCount() + 1);
            int adapterCount;

            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    adapter.onBindViewHolder(holder, adjPosition);
                }
            }
        }

        // some times we need to override this
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
            if (isHeader(position) || isRefreshHeader(position)) {
                return;
            }

            int adjPosition = position - (getHeadersCount() + 1);
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    if (payloads.isEmpty()) {
                        adapter.onBindViewHolder(holder, adjPosition);
                    } else {
                        adapter.onBindViewHolder(holder, adjPosition, payloads);
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            if (loadingMoreEnabled) {
                if (adapter != null) {
                    return getHeadersCount() + adapter.getItemCount() + 2;
                } else {
                    return getHeadersCount() + 2;
                }
            } else {
                if (adapter != null) {
                    return getHeadersCount() + adapter.getItemCount() + 1;
                } else {
                    return getHeadersCount() + 1;
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            int adjPosition = position - (getHeadersCount() + 1);
            if (isRefreshHeader(position)) {
                return TYPE_REFRESH_HEADER;
            }
            if (isHeader(position)) {
                position = position - 1;
                return sHeaderTypes.get(position);
            }

            if (isFooter(position)) {
                return TYPE_FOOTER;
            }

            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    int type = adapter.getItemViewType(adjPosition);
                    if (isReservedItemViewType(type)) {
                        throw new IllegalStateException("XRecyclerView require itemViewType in adapter should be less than 10000 ");
                    }
                    return type;
                }
            }
            return 0;
        }

        @Override
        public long getItemId(int position) {
            if (adapter != null && position >= getHeadersCount() + 1) {
                int adjPosition = position - (getHeadersCount() + 1);
                if (adjPosition < adapter.getItemCount()) {
                    return adapter.getItemId(adjPosition);
                }
            }
            return -1;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (isHeader(position) || isFooter(position) || isRefreshHeader(position)) ? gridManager.getSpanCount() : 1;
                    }
                });
            }
            adapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            adapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && (isHeader(holder.getLayoutPosition()) || isRefreshHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition()))) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }

            if (null != adapter) {
                adapter.onViewAttachedToWindow(holder);
            }
        }

        @Override
        public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
            if (null != adapter) {
                adapter.onViewDetachedFromWindow(holder);
            }
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            if (null != adapter) {
                adapter.onViewRecycled(holder);
            }
        }

        @Override
        public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
            return adapter.onFailedToRecycleView(holder);
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            adapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            if (null != adapter) {
                adapter.registerAdapterDataObserver(observer);
            }
        }

        private class SimpleViewHolder extends RecyclerView.ViewHolder {
            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    /**
     * 设置 Refresh Loading Listener
     *
     * @param listener
     */
    public void setLoadingListener(LoadingListener listener) {
        mLoadingListener = listener;
    }

    /**
     * 设置 Loading More Listener
     *
     * @param listener
     */
    public void setLoadingMoreListener(LoadingListener listener) {
        mLoadingMoreListener = listener;
    }

    public interface LoadingListener {

        void onLoading();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 解决和CollapsingToolbarLayout冲突的问题
        AppBarLayout appBarLayout = null;
        ViewParent p = getParent();

        while (p != null) {
            if (p instanceof CoordinatorLayout) {
                break;
            }
            p = p.getParent();
        }

        if (p instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) p;
            final int childCount = coordinatorLayout.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = coordinatorLayout.getChildAt(i);
                if (child instanceof AppBarLayout) {
                    appBarLayout = (AppBarLayout) child;
                    break;
                }
            }
            if (appBarLayout != null) {
                appBarLayout.addOnOffsetChangedListener(new XAppBarStateChangeListener() {
                    @Override
                    public void onStateChanged(AppBarLayout appBarLayout, State state) {
                        appbarState = state;
                    }
                });
            }
        }
    }

    public class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private Drawable mDivider;
        private int mOrientation;

        // private boolean isRemoveBottomDivider = true;

        private boolean showHeaderDivider = true;

        public DividerItemDecoration(Drawable divider, boolean showHeaderDivider) {
            mDivider = divider;
            this.showHeaderDivider = showHeaderDivider;
        }

        /**
         * Sole constructor. Takes in a {@link Drawable} to be used as the interior divider.
         *
         * @param divider A divider {@code Drawable} to be drawn on the RecyclerView
         */
        public DividerItemDecoration(Drawable divider) {
            this(divider, true);
        }

        /**
         * Draws horizontal or vertical dividers onto the parent RecyclerView.
         *
         * @param canvas The {@link Canvas} onto which dividers will be drawn
         * @param parent The RecyclerView onto which dividers are being added
         * @param state  The current RecyclerView.State of the RecyclerView
         */
        @Override
        public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
            if (mOrientation == LinearLayoutManager.HORIZONTAL) {
                drawHorizontalDividers(canvas, parent);
            } else if (mOrientation == LinearLayoutManager.VERTICAL) {
                drawVerticalDividers(canvas, parent);
            }
        }

        /**
         * Determines the size and location of offsets between items in the parent
         * RecyclerView.
         *
         * @param outRect The {@link Rect} of offsets to be added around the child
         *                view
         * @param view    The child view to be decorated with an offset
         * @param parent  The RecyclerView onto which dividers are being added
         * @param state   The current RecyclerView.State of the RecyclerView
         */
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            // if (parent.getChildAdapterPosition(view) <= mWrapAdapter.getHeadersCount() + 1) {
            if (parent.getChildAdapterPosition(view) <= (mWrapAdapter.getHeadersCount() + (showHeaderDivider ? 1 : 0))) {
                return;
            }

            mOrientation = ((LinearLayoutManager) parent.getLayoutManager()).getOrientation();
            if (mOrientation == LinearLayoutManager.HORIZONTAL) {
                outRect.left = mDivider.getIntrinsicWidth();
            } else if (mOrientation == LinearLayoutManager.VERTICAL) {
                outRect.top = mDivider.getIntrinsicHeight();
            }
        }

        /**
         * Adds dividers to a RecyclerView with a LinearLayoutManager or its subclass oriented horizontally.
         *
         * @param canvas The {@link Canvas} onto which horizontal dividers will be drawn
         * @param parent The RecyclerView onto which horizontal dividers are being added
         */
        private void drawHorizontalDividers(Canvas canvas, RecyclerView parent) {
            int parentTop = parent.getPaddingTop();
            int parentBottom = parent.getHeight() - parent.getPaddingBottom();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int parentLeft = child.getRight() + params.rightMargin;
                int parentRight = parentLeft + mDivider.getIntrinsicWidth();

                mDivider.setBounds(parentLeft, parentTop, parentRight, parentBottom);
                mDivider.draw(canvas);
            }
        }

        /**
         * Adds dividers to a RecyclerView with a LinearLayoutManager or its
         * subclass oriented vertically.
         *
         * @param canvas The {@link Canvas} onto which vertical dividers will be drawn
         * @param parent The RecyclerView onto which vertical dividers are being added
         */
        private void drawVerticalDividers(Canvas canvas, RecyclerView parent) {
            final int parentLeft = parent.getPaddingLeft();
            final int parentRight = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount() - 1;
            // final int size = isRemoveBottomDivider ? childCount - mWrapAdapter.getHeadersCount() - 1 : childCount;

            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int parentTop = child.getBottom() + params.bottomMargin;
                int parentBottom = parentTop + mDivider.getIntrinsicHeight();

                mDivider.setBounds(parentLeft, parentTop, parentRight, parentBottom);
                mDivider.draw(canvas);
            }
        }
    }

    public class GridLayoutItemDecorationSpace extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int horizonSpan;
        private int verticalSpan;
        private int orientation;

        public GridLayoutItemDecorationSpace(int count) {
            spanCount = count;
            setDivideParams(2, 2);
            orientation = GridLayoutManager.VERTICAL;
        }

        public GridLayoutItemDecorationSpace(int count, int orientation) {
            spanCount = count;
            setDivideParams(2, 2);
            setOrientation(orientation);
        }

        public GridLayoutItemDecorationSpace(int count, int orientation, int horizonSpan, int verticalSpan) {
            this(count);

            setOrientation(orientation);
            setDivideParams(horizonSpan, verticalSpan);
        }

        public void setOrientation(int orientation) {
            if (orientation != LinearLayoutManager.HORIZONTAL && orientation != LinearLayoutManager.VERTICAL) {
                throw new IllegalArgumentException("invalid orientation");
            }

            this.orientation = orientation;
        }


        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            final int position = parent.getChildAdapterPosition(view) - 1;
            final int totalCount = parent.getAdapter().getItemCount();
            int left = (position % spanCount == 0) ? 0 : horizonSpan;
            int bottom = ((position + 1) % spanCount == 0) ? 0 : verticalSpan;

            if (isVertical(parent)) {
                if (!isLastRaw(parent, position, spanCount, totalCount)) {
                    outRect.set(left, 0, 0, verticalSpan);
                } else {
                    outRect.set(left, 0, 0, 0);
                }
            } else {
                if (!isLastColumn(parent, position, spanCount, totalCount)) {
                    outRect.set(0, 0, horizonSpan, bottom);
                } else {
                    outRect.set(0, 0, 0, bottom);
                }
            }
        }

        public boolean isVertical(RecyclerView parent) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                int orientation = ((GridLayoutManager) layoutManager).getOrientation();
                return orientation == StaggeredGridLayoutManager.VERTICAL;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
                return orientation == StaggeredGridLayoutManager.VERTICAL;
            }
            return false;
        }

        public void setDivideParams(int horizon, int vertical) {
            horizonSpan = horizon;
            verticalSpan = vertical;
        }

        /**
         * span[2]
         *
         * @return
         */
        public int[] getDivideSpan() {
            return new int[]{horizonSpan, verticalSpan};
        }

        private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
            if (isVertical(parent)) {
                if ((pos - pos % spanCount) + spanCount >= childCount)
                    return true;
            } else {
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            }
            return false;
        }


        public boolean isLastColumn(RecyclerView parent, int pos, int spanCount, int childCount) {
            if (isVertical(parent)) {
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)
                    return true;
            }

            return false;
        }
    }
}