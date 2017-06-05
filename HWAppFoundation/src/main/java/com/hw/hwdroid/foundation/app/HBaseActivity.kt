package com.hw.hwdroid.foundation.app

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.hw.hwdroid.dialog.*
import com.hw.hwdroid.dialog.model.DialogType
import com.hw.hwdroid.dialog.model.exchangeModel.DialogExchangeModel
import com.hw.hwdroid.dialog.permission.HPermissionListener
import com.hw.hwdroid.dialog.permission.HPermissionUtils
import com.hw.hwdroid.dialog.permission.HPermissionsDispatcher
import com.hw.hwdroid.dialog.utils.StringUtils
import com.hw.hwdroid.foundation.R
import com.hw.hwdroid.foundation.app.annotation.*
import com.hw.hwdroid.foundation.app.model.ViewModel
import com.hw.hwdroid.foundation.app.model.ViewModelActivity
import com.hw.hwdroid.foundation.app.rx.bus.HRxBus
import com.hw.hwdroid.foundation.app.widget.HTitleBarView
import com.hw.hwdroid.foundation.utils.GUIDUtils
import com.hw.hwdroid.foundation.utils.ResourceUtils
import com.orhanobut.logger.Logger
import common.android.foundation.app.HActivityStack
import java.util.*

/**
 * 基础Activity
 * 所有activity父类
 * Created by ChenJ on 2017/2/16.
 */
open class HBaseActivity<ViewModelData : ViewModelActivity> : AppCompatActivity(), HPermissionListener, HBaseFragment.OnFragmentInteractionListener, HandleDialogFragmentEvent, SingleDialogFragmentCallBack {

    /** 用于检测回退Fragment >= 0  */
    protected var mFragmentBackStackEntryCount: Int = 0

    /** 是否已经记录了用户操作记录  */
    private var isUserRecordSaved: Boolean = false

    /** true: 使用了common_action_layout 或 common_action_layout_light */
    private var isUseParentView = false

    private var mToolbar: Toolbar? = null
    private var unBinder: Unbinder? = null
    private var addedActionBar: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        HRxBus.getInstance().init(this)
        HActivityStack.push(this)

        initPrepare()

        if (javaClass.isAnnotationPresent(HContentViewRes::class.java)) {
            val contentViewResId = javaClass.getAnnotation(HContentViewRes::class.java).value
            setContentView(contentViewResId)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        if (javaClass.isAnnotationPresent(HLoadService::class.java) && javaClass.getAnnotation(HLoadService::class.java).value) {
            loadServiceFlow(false)
        }
    }

    open protected fun initPrepare() {
    }

    /**
     * init Views
     */
    open protected fun initViews() {
    }

    /**
     * register Listener
     */
    open protected fun registerListener() {
        findViewById(R.id.contentLoading)?.setOnClickListener { setContentLoadingVisibility(false) }
    }

    /**
     * 是否需要添加TitleBar
     */
    private fun isNeedAddTitleBar(): Boolean {
        if (javaClass.isAnnotationPresent(HContentViewRes::class.java)) {
            return javaClass.getAnnotation(HContentViewRes::class.java).titleBar
        }

        return false
    }

    /**
     * set Content View
     */
    override fun setContentView(@LayoutRes layoutResID: Int) {
        if (!isNeedAddTitleBar() && layoutResID != 0 && layoutResID != View.NO_ID) {
            super.setContentView(layoutResID)
            initContentView()
            return
        }

        if (layoutResID == 0 || layoutResID == View.NO_ID) {
            setActionContentView(null, null, null)
        } else {
            setActionContentView(LayoutInflater.from(this).inflate(layoutResID, null, false), null, null)
        }
    }

    override fun setContentView(view: View?) {
        setContentView(view, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        if (!isNeedAddTitleBar() && view != null) {
            super.setContentView(view, params)
            initContentView()
            return
        }

        setActionContentView(view, params, params)
    }

    @SuppressLint("InflateParams")
    private fun setActionContentView(child: View?, childParams: ViewGroup.LayoutParams?, params: ViewGroup.LayoutParams?) {
        var themeLight = false
        if (javaClass.isAnnotationPresent(HThemeLight::class.java)) {
            themeLight = javaClass.getAnnotation(HThemeLight::class.java).value
        }

        isUseParentView = true
        val view = LayoutInflater.from(this).inflate(if (themeLight) R.layout.common_action_layout_light else R.layout.common_action_layout, null, false)
        val content = view.findViewById(R.id.content) as ViewGroup?

        content?.removeAllViews()

        if (null != child) {
            content?.addView(child, childParams ?: ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))
        }

        if (null != params) {
            super.setContentView(view, params)
        } else {
            super.setContentView(view)
        }

        initContentView()
    }

    private fun initContentView() {
        setFitsSystemWindows()

        bindButterKnife()
        initToolbar()
        initTitle()
        initViews()
        registerListener()
    }

    private fun setFitsSystemWindows() {
        if (isUseParentView) return

        if (javaClass.isAnnotationPresent(HFitsSystemWindows::class.java) && javaClass.getAnnotation(HFitsSystemWindows::class.java).value) {
            val contentFrameLayout = findViewById(Window.ID_ANDROID_CONTENT) as ViewGroup?
            val parentView = contentFrameLayout?.getChildAt(0)
            if (Build.VERSION.SDK_INT < 16) {
                parentView?.fitsSystemWindows = true
            } else {
                if (parentView != null && !parentView.fitsSystemWindows) {
                    parentView.fitsSystemWindows = true
                }
            }
        }
    }

    /**
     * 绑定ButterKnife
     */
    private fun bindButterKnife() {
        if (unBinder != null) {
            return
        }

        unBinder = ButterKnife.bind(this)
    }

    /**
     * 解绑ButterKnife
     */
    private fun unbindButterKnife() {
        if (unBinder == null) {
            return
        }

        unBinder?.unbind()
        unBinder = null
    }

    /**
     * set support ActionBar
     */
    override fun setSupportActionBar(toolbar: Toolbar?) {
        if (addedActionBar) {
            return
        }

        mToolbar = toolbar
        addedActionBar = true
        super.setSupportActionBar(toolbar)
    }

    /**
     * init Toolbar
     */
    protected fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar?

        toolbar?.let {
            setSupportActionBar(toolbar)

            Logger.d("setSupportActionBar")
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        supportActionBar?.setHomeButtonEnabled(false)
        supportActionBar?.setDisplayUseLogoEnabled(false)

        if (javaClass.isAnnotationPresent(HThemeLight::class.java) && javaClass.getAnnotation(HThemeLight::class.java).value) {
            val titleBarView = findViewById(R.id.titleBar)
            if (titleBarView is HTitleBarView) {
                titleBarView.updateStyle(true)
            }
        }
    }

    /**
     * 初始化Title
     * 检查顺序: 注解类型->XML->Label
     */
    private fun initTitle() {
        var has = false
        var title: String? = null

        if (javaClass.isAnnotationPresent(HTitle::class.java)) {
            val titleResId = javaClass.getAnnotation(HTitle::class.java).value
            if (titleResId != View.NO_ID && titleResId != 0) {
                has = true
                title = ResourceUtils.getString(applicationContext, titleResId, null)
            }
        }

        if (title == null) {
            val titleBar = findViewById(R.id.titleBar) as HTitleBarView?
            title = titleBar?.titleFromAttribute
            has = titleBar != null
        }

        if (title == null) {
            val info: ActivityInfo? = packageManager?.getActivityInfo(ComponentName(this, javaClass), 0)
            if (info != null && info.labelRes != 0 && info.labelRes != View.NO_ID) {
                has = true
                title = ResourceUtils.getString(applicationContext, info.labelRes, null)
            }
        }

        if (has) {
            setTitle(StringUtils.changeNull(title))
        }
    }

    override fun setTitle(title: CharSequence) {
        super.setTitle(title)
        setToolbarTitle(title)
    }

    override fun setTitle(@StringRes titleId: Int) {
        if (titleId == 0 || titleId == View.NO_ID) {
            initTitle()
            return
        }

        try {
            title = getText(titleId)
        } catch (e: Exception) {
        }
    }

    /**
     * set toolbar title
     * @param title
     */
    open fun setToolbarTitle(title: CharSequence?) {
        runOnUiThread {
            val titleBar = findViewById(R.id.titleBar) as HTitleBarView?
            titleBar?.setTitle(StringUtils.changeNull(title))

            if (titleBar == null && getToolbar() != null) {
                val toolbarTitle = getToolbar()?.findViewById(R.id.titleBarTitleTv) as TextView?
                toolbarTitle?.text = StringUtils.changeNull(title)
            }
        }
    }

    /**
     * 返回Data
     *
     * @return
     */
    var data: ViewModelData = ViewModelActivity() as ViewModelData
        get() = field
        set(value) {
            field = value
        }

    /**
     * AppBarLayout
     */
    open fun getAppBarLayout(): AppBarLayout? {
        return findViewById(R.id.appBar) as AppBarLayout?
    }

    /**
     * Toolbar
     */
    open fun getToolbar(): Toolbar? {
        if (mToolbar == null) {
            mToolbar = findViewById(R.id.toolbar) as Toolbar?
        }

        return mToolbar
    }

    /**
     * HTitleBarView
     */
    open fun getTitleBar(): HTitleBarView? {
        return findViewById(R.id.titleBar) as HTitleBarView?
    }

    override fun onFragmentInteraction(data: ViewModel) {
    }

    /**
     * get Intent
     */
    override fun getIntent(): Intent {
        var intent: Intent? = super.getIntent()

        if (intent == null) {
            intent = Intent()
            setIntent(intent)
        }

        return intent
    }

    /**
     * Bundle
     *
     * @return
     */
    fun getExtras(): Bundle {
        var extras: Bundle? = intent.extras

        if (extras == null) {
            extras = Bundle()
            intent.putExtras(extras)
        }

        return extras
    }

    fun getActivity(): HBaseActivity<ViewModelData> {
        return this
    }

    override fun onResume() {
        isUserRecordSaved = false
        super.onResume()
    }

    override fun onPostResume() {
        super.onPostResume()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsMenuClosed(menu: Menu) {
        super.onOptionsMenuClosed(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (KeyEvent.KEYCODE_BACK != keyCode) {
            return super.onKeyDown(keyCode, event)
        }

        var currDialogFragment: Fragment? = null
        val size = data.dialogFragmentTags.size

        if (size > 0) {
            for (index in size - 1 downTo 0) {
                val tag = data.dialogFragmentTags[index]
                currDialogFragment = supportFragmentManager.findFragmentByTag(tag)

                if (currDialogFragment != null) {
                    break
                }
            }
        }

        if (currDialogFragment != null) {
            // dialog fragment
            if (currDialogFragment is BaseDialogFragment) {
                if (currDialogFragment.isBackable) {
                    HFragmentExchangeController.removeFragment(supportFragmentManager, currDialogFragment)
                }

                return true
            }
        }

        return popBackStackImmediate() || super.onKeyDown(keyCode, event)

        //        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
        //            // finishCurrentActivity();
        //            // return true;
        //            return super.onKeyDown(keyCode, event);
        //        }
        //
        //        try {
        //            boolean pop = getSupportFragmentManager().popBackStackImmediate();
        //            return pop ? true : super.onKeyDown(keyCode, event);
        //        } catch (Exception e) {
        //            return super.onKeyDown(keyCode, event);
        //        }
    }

    /**
     * pop Fragment
     *
     * @return
     */
    open protected fun popBackStackImmediate(): Boolean {
        if (mFragmentBackStackEntryCount < 0) {
            return false
        }

        if (supportFragmentManager.backStackEntryCount <= mFragmentBackStackEntryCount) {
            // finishCurrentActivity();
            // return true;
            return false
        }

        try {
            val pop = supportFragmentManager.popBackStackImmediate()
            if (pop) {
                updateView4PopBackStack()
            }
            return pop
        } catch (e: Exception) {
            return false
        }

    }


    /**
     * Pop Fragment update View
     */
    open protected fun updateView4PopBackStack() {}

    /**
     * 结束当前Activity内容 在[.onKeyDown]中调用
     */
    fun finishCurrentActivity() {
        saveUserRecord()
        finish()
    }


    /**
     * 保存用户操作记录, 真正实现在子类(Fragment)中
     */
    open fun saveUserRecord() {
        if (isUserRecordSaved) {
            return
        }

        val fragments = HFragmentExchangeController.getAllFragments(this) ?: return

        val size = fragments.size
        for (i in size - 1 downTo 0) {
            val fragment = fragments[i]
            if (fragment is HBaseFragment<*>) {
                fragment.saveUserRecordFromActivity()
                isUserRecordSaved = true
            }
        }
    }

    /**
     * 加载数据
     *
     * @param more 加载更多
     */
    fun loadServiceFlow(more: Boolean) {
        if (!checkLoadingStatus(more)) {
            return
        }

        loadService(more)
    }

    /**
     * 加载数据
     *
     * @param more 加载更多(下一页)
     */
    open fun loadService(more: Boolean) {
        data.isLoadingStatus = true

        setContentLoadingVisibility(true)
        setPageIndex(more)
    }

    /**
     * 加载完成
     */
    open fun loadCompleted() {
        data.isLoadingStatus = false
        setContentLoadingVisibility(false)
    }

    /**
     * 检测是否可加载数据的状态
     * @return
     */
    open fun checkLoadingStatus(more: Boolean): Boolean {
        if (data.isLoadingStatus) {
            return false
        }

        if (isFinishing) {
            return false
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed) {
            return false
        }

        if (more) {
            if (!hasNextPage()) {
                data.isLoadingStatus = false
                return false
            }
        }

        return true
    }

    /**
     * 是否有下一页
     * @return
     */
    open fun hasNextPage(): Boolean {
        return data.hasNextPage()
    }

    /**
     * 设置分页功能的pageIndex
     * @param more
     */
    open fun setPageIndex(more: Boolean) {
        if (more) {
            data.pageIdx++
        } else {
            data.pageIdx = data.pageIdxStart
        }
    }

    /**
     * 设置LoadingContent是否显示
     */
    open fun setContentLoadingVisibility(show: Boolean) {
        val contentLoading: View? = findViewById(R.id.contentLoading)
        val contentProgressView: View? = findViewById(R.id.contentProgressView)

        if (show) {
            contentLoading?.visibility = View.VISIBLE
            contentProgressView?.visibility = View.VISIBLE
        } else {
            contentLoading?.visibility = View.GONE
            contentProgressView?.visibility = View.GONE
        }
    }

    override fun finish() {
        saveUserRecord()
        HActivityStack.remove(this)
        super.finish()
    }

    override fun onDestroy() {
        try {
            unbindButterKnife()
            HRxBus.getInstance().unRegister(this)
            HActivityStack.pop(this, false)
        } catch (e: Exception) {
            Logger.e(e)
        }

        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    /**
     * back Activity or Fragment
     */
    open fun goBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            onBackPressed()
            return
        }

        try {
            supportFragmentManager.popBackStackImmediate()
        } catch (e: Exception) {
            onBackPressed()
        }

    }

    open fun startActivity(cls: Class<HBaseActivity<*>>) {
        super.startActivity(Intent(this, cls))
    }

    /**
     * 获取事件执行时间
     * @param action
     * @return
     */
    fun getEvenTime(action: String, remove: Boolean = true): Long {
        val def: Long = 0L
        if (StringUtils.isNullOrWhiteSpace(action)) {
            return def
        }

        val time = data.eventMap[action]

        if (remove) {
            removeEven(action)
        }

        return time ?: def
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        Logger.d("onActivityResult requestCode: %d ,resultCode: %d", requestCode, resultCode)
    }

    open fun getPageName(): String {
        return javaClass.simpleName
    }

    open fun getRequestPermission(key: Int): List<String> {
        return data.requestPermissionMap[key] ?: ArrayList<String>()
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
     *
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

        if (requestPermissions != null && requestPermissions.isNotEmpty()) {
            data.requestPermissionMap.put(requestCode, requestPermissions)
        } else {
            data.requestPermissionMap.remove(requestCode)
        }

        return HPermissionUtils.requestPermissionsByActivity(this, requestCode, requestPermissions, this)
    }

    /**
     * 所有权限是否开放
     *
     * @param requestCode
     * @return
     */
    open fun isAllPermissionGranted(requestCode: Int): Boolean {
        return HPermissionUtils.isAllPermissionGrantedByList(this, data.requestPermissionMap[requestCode])
    }

    /**
     * 权限
     *
     * @param requestCode
     * @param grantResults
     * @param permissions
     */
    override fun onPermissionsGranted(requestCode: Int, grantResults: IntArray?, vararg permissions: String?) {
        onPermissionsGranted2(requestCode, grantResults, *permissions)
        data.requestPermissionMap.remove(requestCode)
    }

    open fun onPermissionsGranted2(requestCode: Int, grantResults: IntArray?, vararg permissions: String?) {
    }

    override fun onPermissionsDenied(requestCode: Int, grantResults: IntArray?, vararg permissions: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onShowRequestPermissionRationale(requestCode: Int, isShowRationale: Boolean, vararg permissions: String?) {
        if (permissions.isNotEmpty()) {
            HPermissionsDispatcher.requestPermissions(this, requestCode, *permissions)
        }
    }

    override fun onPermissionsError(requestCode: Int, grantResults: IntArray?, errorMsg: String?, vararg permissions: String?) {
        data.requestPermissionMap.remove(requestCode)
    }

    /**
     * 单按钮弹出框
     *
     * @param tag                   tag
     * @param positive              按钮文案
     * @param title                 标题
     * @param content               提示文案
     * @param cancelForBack         返回可点击
     * @param cancelForClickSpace   空白可点击
     * @return
     */
    fun showSingleDialog(tag: String = GUIDUtils.guid(), positive: CharSequence = String(), title: CharSequence = String(),
                         content: CharSequence = String(), cancelForBack: Boolean = true, cancelForClickSpace: Boolean = false): BaseDialogFragment {
        return showDialog(DialogType.SINGLE, tag, String(), positive, title, content, cancelForBack, cancelForClickSpace)
    }

    /**
     * show Dialog
     * 返回和空白可点击
     * @param type              类型
     * @param tag               tag
     * @param negative          取消文案
     * @param positive          确定文案
     * @param title             标题
     * @param content           提示文案
     * @return
     */
    fun showDialogBackAble(type: DialogType, tag: String = GUIDUtils.guid(), negative: CharSequence = String(),
                           positive: CharSequence = String(), title: CharSequence = String(), content: CharSequence = String()): BaseDialogFragment {
        return showDialog(type, tag, negative, positive, title, content, true, true)
    }

    /**
     * show Dialog
     *
     * @param fragment
     * @param callBack
     * @param type                      类型
     * @param tag                       tag
     * @param negative                  取消文案
     * @param positive                  确定文案
     * @param title                     标题
     * @param content                   提示文案
     * @param cancelForBack             返回可点击
     * @param cancelForClickSpace       空白可点击
     * @return
     */
    fun showDialog(type: DialogType, tag: String = GUIDUtils.guid(),
                   negative: CharSequence = String(), positive: CharSequence, title: CharSequence = String(), content: CharSequence = String(),
                   cancelForBack: Boolean = true, cancelForClickSpace: Boolean = false, fragment: Fragment? = null, callBack: DialogCallBackContainer? = null): BaseDialogFragment {
        val builder = DialogExchangeModel.DialogExchangeModelBuilder(type, tag)

        // 双按钮框
        if (type == DialogType.EXCUTE) {
            builder.setPositiveText(positive).setNegativeText(negative)
        }
        // 单按钮框
        else if (type == DialogType.SINGLE) {
            builder.setSingleText(negative)
        }

        // 文本消息
        builder.setDialogContext(content)

        // 标题，back可点击，空白可点击
        builder.setDialogTitle(title).setBackable(cancelForBack).setSpaceable(cancelForClickSpace).setHasTitle(!StringUtils.isEmptyOrNull(title))
        return HWDialogManager.showDialogFragment(supportFragmentManager, builder.create(), callBack, fragment, this)
    }

    /**
     * 确认点击回调(Dialog)
     *
     * @param tag
     */
    override fun onPositiveBtnClick(tag: String) {
    }

    /**
     * 取消点击回调(Dialog)
     *
     * @param tag
     */
    override fun onNegativeBtnClick(tag: String) {
    }

    /**
     * 单个按键点击回调(Dialog)
     *
     * @param tag
     */
    override fun onSingleBtnClick(tag: String) {
    }

}
