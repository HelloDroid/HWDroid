package com.hw.hwdroid.foundation.app

import android.os.Bundle
import android.view.View
import com.hw.hwdroid.foundation.app.model.ViewModelFragment

/**
 * Created by ChenJ on 2017/4/21.
 */
abstract class HWLazyFragment<ViewModelData : ViewModelFragment> : HWBaseFragment<ViewModelData>() {

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lazyLoad()
    }


    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged
     * 若是初始就show的Fragment为了触发该事件 需要先hide再show
     *
     * @param hidden hidden True if the fragment is now hidden, false if it is not visible.
     */
    override fun onHiddenChanged(hidden: Boolean) {
        if (isShowing == !hidden) {
            return
        }

        super.onHiddenChanged(hidden)

        if (!hidden) {
            onVisible()
        } else {
            onInvisible()
        }
    }


    /**
     * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
     *
     * @param isVisibleToUser 是否显示出来了
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        // Logger.d("setUserVisibleHint=%b", isVisibleToUser);
        if (isShowing == userVisibleHint) {
            return
        }

        super.setUserVisibleHint(isVisibleToUser)

        isShowing = userVisibleHint

        if (isShowing) {
            onVisible()
        } else {
            onInvisible()
        }
    }

    protected fun onVisible() {
        isShowing = true

        if (!isPrepared) {
            return
        }

        isFirstLoad = false
        lazyLoad()
    }

    protected abstract fun lazyLoad()

    protected fun onInvisible() {
        isShowing = false
    }

}
