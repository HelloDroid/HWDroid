package com.hw.hwdroid.foundation.app

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.DimenRes
import android.support.v4.app.Fragment
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.hw.hwdroid.dialog.permission.HPermissionListener
import com.hw.hwdroid.dialog.permission.HPermissionUtils
import com.hw.hwdroid.dialog.permission.HPermissionsDispatcher
import com.hw.hwdroid.foundation.app.annotation.HContentViewAttachToRoot
import com.hw.hwdroid.foundation.app.annotation.HContentViewRes
import com.hw.hwdroid.foundation.app.annotation.HLoadService
import com.hw.hwdroid.foundation.app.model.ViewModel
import com.hw.hwdroid.foundation.app.model.ViewModelFragment
import com.hw.hwdroid.foundation.app.rx.bus.HRxBus
import com.hw.hwdroid.foundation.utils.StringUtils
import com.orhanobut.logger.Logger

/**
 * Base Fragment
 */
open class HWBaseFragment<ViewModelData : ViewModelFragment> : Fragment(), HPermissionListener {

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(data: ViewModel)
    }

    /**
     * View已经初始化完成
     * @return
     */
    var isPrepared: Boolean = false
        protected set


    /** 是否可见状态 为了避免和[HWBaseFragment.isVisible]冲突  */
    protected var isShowing: Boolean = false

    /**
     * 是否第一次加载
     * @return
     */
    var isFirstLoad = true
        protected set

    /** 是否已经记录了用户操作记录  */
    private var isUserRecordSaved = false

    /** 界面最后停留时间 毫秒  */
    var takeAWalkLastTime: Long = 0

    private var unbinder: Unbinder? = null
    private var listener: OnFragmentInteractionListener? = null


    override fun onAttach(context: Context?) {
        super.onAttach(context)

        data = (ViewModelFragment() as ViewModelData)

        try {
            if (context is OnFragmentInteractionListener) {
                listener = context as OnFragmentInteractionListener?
            }
        } catch (e: ClassCastException) {
        }
    }

    val argument: Bundle
        get() {
            return arguments ?: Bundle()
        }

    protected val pageName: String
        get() = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HRxBus.getInstance().init(this)
        activity
    }

    val baseActivity: HWBaseActivity<*>?
        get() = activity as HWBaseActivity<*>?


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View? = null

        isFirstLoad = true
        var attachToRoot = false
        if (javaClass.isAnnotationPresent(HContentViewAttachToRoot::class.java)) {
            val annotation = javaClass.getAnnotation(HContentViewAttachToRoot::class.java)
            attachToRoot = annotation.value
        }

        if (javaClass.isAnnotationPresent(HContentViewRes::class.java)) {
            val annotation = javaClass.getAnnotation(HContentViewRes::class.java)
            if (annotation.value != View.NO_ID) {
                view = inflater!!.inflate(annotation.value, if (attachToRoot) container else null)
            }
        }

        bindButterKnife(view)
        return if (null != view) view else super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        bindButterKnife(view)
        super.onViewCreated(view, savedInstanceState)

        view?.let { initViews(view) }

        if (javaClass.isAnnotationPresent(HLoadService::class.java) && javaClass.getAnnotation(HLoadService::class.java).value) {
            loadServiceFlow(false)
        }
    }

    protected fun initViews(view: View) {}

    private fun bindButterKnife(view: View?) {
        if (unbinder != null) {
            return
        }

        view?.let {
            isPrepared = true
            unbinder = ButterKnife.bind(this, view)
        }
    }

    private fun unbindButterKnife() {
        if (unbinder == null) {
            return
        }

        unbinder?.unbind()
        unbinder = null
    }

    override fun onResume() {
        super.onResume()
        isUserRecordSaved = false
        takeAWalkLastTime = System.currentTimeMillis()
    }

    fun onInteraction() {
        listener?.onFragmentInteraction(data)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        isShowing = !hidden

        if (hidden) {
            val dTakeAWalkLastTime = System.currentTimeMillis() - takeAWalkLastTime
        }

        takeAWalkLastTime = System.currentTimeMillis()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isShowing = isVisibleToUser
    }

    /**
     * 返回Data
     * @return
     */
    var data: ViewModelData = ViewModelFragment() as ViewModelData
        get() = field
        set(value) {
            field = value
        }

    /**
     * 加载数据

     * @param more 加载更多
     */
    fun loadServiceFlow(more: Boolean = false) {
        if (!checkLoadingStatus(more)) {
            return
        }

        loadService(more)
    }

    /**
     * 加载数据
     * @param more 加载更多(下一页)
     */
    fun loadService(more: Boolean = false) {
        data.isLoadingStatus = true
        setPageIndex(more)
    }

    /**
     * 是否正在加载
     * @return
     */
    protected val isLoading: Boolean
        get() = data.isLoadingStatus

    /**
     * 检测是否可加载数据的状态
     * @return
     */
    fun checkLoadingStatus(more: Boolean): Boolean {
        if (activity == null) {
            return false
        }

        if (isLoading) {
            return false
        }

        if (activity.isFinishing) {
            return false
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed) {
            return false
        }

        if (more) {
            if (!hasNextPage()) {
                return false
            }
        }

        return true
    }

    /**
     * 是否有下一页
     * @return
     */
    fun hasNextPage(): Boolean {
        return data.hasNextPage()
    }

    /**
     * 设置分页功能的pageIndex
     * @param more
     */
    fun setPageIndex(more: Boolean = false) {
        if (more) {
            data.pageIdx++
        } else {
            data.pageIdx = data.pageIdxStart
        }
    }

    /**
     * 各页面实现记录用户信息的方法, 并通过返回值由子类控制实际是否记录
     * @return true - 子类不需要记录, false - 子类需要记录
     */
    fun onSaveUserRecord(): Boolean {
        return isUserRecordSaved
    }

    /**
     * 当Activity直接退出时调用记录用户信息
     */
    fun saveUserRecordFromActivity() {
        if (isUserRecordSaved) {
            return
        }

        try {
            onSaveUserRecord()
        } catch (e: Exception) {
            Logger.e(e)
        }

        isUserRecordSaved = true
    }

    override fun onDestroyView() {
        isPrepared = false
        unbindButterKnife()
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * 获取事件执行时间
     * @param action
     * @return
     */
    @JvmOverloads fun getEvenTime(action: String, remove: Boolean = true): Long {
        if (StringUtils.isNullOrWhiteSpace(action)) {
            return 0
        }

        val time = data.eventMap[action]

        if (remove) {
            removeEven(action)
        }

        return time ?: 0L
    }

    /**
     * 删除事件执行时间
     * @param action
     */
    fun removeEven(action: String) {
        if (StringUtils.isNullOrWhiteSpace(action)) {
            return
        }

        data.eventMap.remove(action)
    }

    /**
     * DimensionPixelOffset
     * @param id
     * @return
     */
    fun getDimensionPixelOffset(@DimenRes id: Int): Int {
        try {
            return resources.getDimensionPixelOffset(id)
        } catch (e: Exception) {
            Logger.e(e)
            return 0
        }

    }

    /**
     * DimensionPixelSize
     * @param id
     * @return
     */
    fun getDimensionPixelSize(@DimenRes id: Int): Int {
        try {
            return resources.getDimensionPixelSize(id)
        } catch (e: Exception) {
            Logger.e(e)
            return 0
        }
    }


    /**
     * 返回DisplayMetrics
     * @return
     */
    val displayMetrics: DisplayMetrics
        get() = resources.displayMetrics

    /**
     * 根据dimension返回px
     * @param id dimentResId
     * @return
     */
    fun applyDimension(@DimenRes id: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, resources.getDimension(id), displayMetrics)
    }

    fun getRequestPermission(key: Int): List<String>? {
        return data.requestPermissionMap[key]
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (data.superOnRequestPermissionsResult) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

        if (data.requestPermissionMap.containsKey(requestCode)) {
            HPermissionsDispatcher.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
        }
    }

    /**
     * 检查并请求权限
     * 注意：需要重写 onRequestPermissionsResult()
     * @param requestCode
     * @param superOnRequestPermissionsResult 推荐：false
     * @param requestPermissions
     * @return
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissions(requestCode: Int, superOnRequestPermissionsResult: Boolean, requestPermissions: List<String>?): Boolean {
        //        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        //            return false;
        //        }
        data.superOnRequestPermissionsResult = superOnRequestPermissionsResult

        if (requestPermissions != null && !requestPermissions.isEmpty()) {
            data.requestPermissionMap.put(requestCode, requestPermissions)
        } else {
            data.requestPermissionMap.remove(requestCode)
        }

        return HPermissionUtils.requestPermissionsByFragment(this, requestCode, requestPermissions, this)
    }

    /**
     * 所有权限是否开放
     * @param requestCode
     * @return
     */
    fun isAllPermissionGranted(requestCode: Int): Boolean {
        return HPermissionUtils.isAllPermissionGrantedByList(activity, data.requestPermissionMap[requestCode])
    }

    override fun onPermissionsGranted(requestCode: Int, grantResults: IntArray?, vararg permissions: String?) {
        data.requestPermissionMap.remove(requestCode)
    }

    override fun onPermissionsDenied(requestCode: Int, grantResults: IntArray?, vararg permissions: String?) {
    }

    override fun onShowRequestPermissionRationale(requestCode: Int, isShowRationale: Boolean, vararg permissions: String?) {
        if (permissions.isNotEmpty()) {
            HPermissionsDispatcher.requestPermissionsByFragment(this, requestCode, *permissions)
        }
    }

    override fun onPermissionsError(requestCode: Int, grantResults: IntArray?, errorMsg: String?, vararg permissions: String?) {
        data.requestPermissionMap.remove(requestCode)
    }

}
