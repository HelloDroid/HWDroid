package com.hw.hwdroid.foundation.app;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.hw.hwdroid.dialog.BaseDialogFragment;
import com.hw.hwdroid.dialog.DialogCallBackContainer;
import com.hw.hwdroid.dialog.HFragmentExchangeController;
import com.hw.hwdroid.dialog.HWDialogManager;
import com.hw.hwdroid.dialog.HandleDialogFragmentEvent;
import com.hw.hwdroid.dialog.SingleDialogFragmentCallBack;
import com.hw.hwdroid.dialog.model.DialogType;
import com.hw.hwdroid.dialog.model.exchangeModel.DialogExchangeModel;
import com.hw.hwdroid.dialog.permission.HPermissionListener;
import com.hw.hwdroid.dialog.permission.HPermissionUtils;
import com.hw.hwdroid.dialog.permission.HPermissionsDispatcher;
import com.hw.hwdroid.dialog.utils.StringUtils;
import com.hw.hwdroid.foundation.R;
import com.hw.hwdroid.foundation.app.annotation.HContentViewRes;
import com.hw.hwdroid.foundation.app.model.ViewModel;
import com.hw.hwdroid.foundation.app.model.ViewModelActivity;
import com.hw.hwdroid.foundation.app.rx.bus.HRxBus;
import com.hw.hwdroid.foundation.app.widget.HTitleBarView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import common.android.foundation.app.HActivityStack;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * 基础Activity
 * 所有activity父类
 * Created by ChenJ on 2017/2/16.
 */
//@HUseRxBus
public class HBaseActivity<ViewModelData extends ViewModelActivity> extends AppCompatActivity implements HPermissionListener, HBaseFragment.OnFragmentInteractionListener, HandleDialogFragmentEvent, SingleDialogFragmentCallBack {
    // public class BaseActivity extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener {


    /** 用于检测回退Fragment >= 0 */
    protected int mFragmentBackStackEntryCount = 0;

    /** 是否已经记录了用户操作记录 */
    private boolean isUserRecordSaved = false;

    /** model data */
    private ViewModelData mData;

    private Toolbar mToolbar;
    private Unbinder unbinder;
    private boolean addedActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HRxBus.getInstance().init(this);
        HActivityStack.INSTANCE.push(this);

        initPrepare();

        if (getClass().isAnnotationPresent(HContentViewRes.class)) {
            int contentViewResId = getClass().getAnnotation(HContentViewRes.class).value();
            setContentView(contentViewResId);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (getClass().isAnnotationPresent(HLoadService.class) && getClass().getAnnotation(HLoadService.class).value()) {
            loadServiceFlow(false);
        }
    }

    protected void initPrepare() {
        setData((ViewModelData) new ViewModelActivity());
    }

    protected void initViews() {
    }

    protected void registerListener() {
    }

    private boolean isNeedAddTitleBar() {
        boolean needAddTitleBar = false;
        if (getClass().isAnnotationPresent(HAddTitleBar.class)) {
            needAddTitleBar = getClass().getAnnotation(HAddTitleBar.class).value();
        }

        return needAddTitleBar;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (!isNeedAddTitleBar() && layoutResID != 0 && layoutResID != View.NO_ID) {
            super.setContentView(layoutResID);
            initContentView();
            return;
        }

        setActionContentView((layoutResID == 0 || layoutResID == View.NO_ID) ? null : LayoutInflater.from(this).inflate(layoutResID, null, false), null, null);
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (!isNeedAddTitleBar() && null != view) {
            super.setContentView(view, params);
            initContentView();
            return;
        }

        setActionContentView(view, params, params);
    }

    @SuppressLint("InflateParams")
    private void setActionContentView(View child, ViewGroup.LayoutParams childParams, ViewGroup.LayoutParams params) {
        boolean themeLight = false;
        if (getClass().isAnnotationPresent(HThemeLight.class)) {
            themeLight = getClass().getAnnotation(HThemeLight.class).value();
        }

        View view = LayoutInflater.from(this).inflate(themeLight ? R.layout.common_action_layout_light : R.layout.common_action_layout, null, false);
        ViewGroup content = (ViewGroup) view.findViewById(R.id.content);
        content.removeAllViews();
        if (null != child) {
            content.addView(child, null == childParams ? new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT) : childParams);
        }

        if (null != params) {
            super.setContentView(view, params);
        } else {
            super.setContentView(view);
        }

        initContentView();
    }

    private void initContentView() {
        setFitsSystemWindows();

        bindButterKnife();
        initToolbar();
        initTitle();
        initViews();
        registerListener();
    }

    private void setFitsSystemWindows() {
        boolean fitsSystemWindows = true;
        if (getClass().isAnnotationPresent(HAddTitleBar.class)) {
            HAddTitleBar titleBar = getClass().getAnnotation(HAddTitleBar.class);
            if (titleBar.value()) {
                fitsSystemWindows = titleBar.setFitsSystemWindows();
            }
        }

        if (fitsSystemWindows && Build.VERSION.SDK_INT >= 14) {
            ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
            View parentView = contentFrameLayout.getChildAt(0);
            if (Build.VERSION.SDK_INT < 16) {
                if (parentView != null) {
                    parentView.setFitsSystemWindows(true);
                }
            } else {
                if (parentView != null && !parentView.getFitsSystemWindows()) {
                    parentView.setFitsSystemWindows(true);
                }
            }
        }
    }

    private void bindButterKnife() {
        if (null != unbinder) {
            return;
        }

        unbinder = ButterKnife.bind(this);
    }

    private void unbindButterKnife() {
        if (null == unbinder) {
            return;
        }

        unbinder.unbind();
        unbinder = null;
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        if (addedActionBar) {
            return;
        }

        mToolbar = toolbar;
        addedActionBar = true;
        super.setSupportActionBar(toolbar);
    }

    /**
     * init Toolbar
     */
    protected void initToolbar() {
        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (null != toolbar && getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
        }

        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);

            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
        }

        if (getClass().isAnnotationPresent(HThemeLight.class) && getClass().getAnnotation(HThemeLight.class).value()) {
            View titleBarView = findViewById(R.id.titleBar);
            if (titleBarView instanceof HTitleBarView) {
                ((HTitleBarView) titleBarView).updateStyle(true);
            }
        }
    }

    /**
     * 初始化Title
     * <p>
     * 检查顺序: 注解类型->XML->Label
     */
    private void initTitle() {
        String title = "";

        if (getClass().isAnnotationPresent(HTitle.class)) {
            int titleResId = getClass().getAnnotation(HTitle.class).value();
            if (titleResId != View.NO_ID && titleResId != 0) {
                try {
                    title = getString(titleResId);
                } catch (Exception e) {
                    Logger.e(e);
                    title = "";
                }
            }
        }

        if (StringUtils.isNullOrWhiteSpace(title)) {
            HTitleBarView titleBar = (HTitleBarView) findViewById(R.id.titleBar);
            if (null != titleBar) {
                title = titleBar.getTitleFromAttribute();
            }
        }


        if (StringUtils.isNullOrWhiteSpace(title)) {
            try {
                PackageManager pm = getPackageManager();
                ActivityInfo info = pm.getActivityInfo(new ComponentName(this, getClass()), 0);
                if (info.labelRes != 0 && info.labelRes != View.NO_ID) {
                    String labelStr = getString(info.labelRes);
                    if (!StringUtils.isNullOrWhiteSpace(labelStr)) {
                        title = labelStr;
                    }
                }
            } catch (Exception e) {
                Logger.e(e);
            }
        }

        setTitle(title);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        setToolbarTitle(title);
    }

    @Override
    public void setTitle(@StringRes int titleId) {
        if (titleId == 0 || titleId == View.NO_ID) {
            initTitle();
            return;
        }

        setTitle(getText(titleId));
    }

    /**
     * set toolbar title
     *
     * @param title
     */
    private void setToolbarTitle(CharSequence title) {
        if (null == title) {
            title = "";
        }

        HTitleBarView titleBar = (HTitleBarView) findViewById(R.id.titleBar);

        if (null != titleBar) {
            titleBar.setTitle(title);
            return;
        }

        if (null != mToolbar) {
            TextView tvToolbarTitle = (TextView) mToolbar.findViewById(R.id.titleBar_title_tv);
            if (null != tvToolbarTitle) {
                tvToolbarTitle.setText(title);
            }
        }
    }

    /**
     * 返回Data
     *
     * @return
     */
    public ViewModelData getData() {
        if (null == mData) {
            mData = (ViewModelData) new ViewModel();
        }

        return mData;
    }

    /**
     * 设置Data
     *
     * @param data
     */
    public ViewModelData setData(ViewModelData data) {
        mData = data;

        return mData;
    }

    public AppBarLayout getAppBarLayout() {
        return (AppBarLayout) findViewById(R.id.appBar);
    }

    public Toolbar getToolbar() {
        if (null == mToolbar) {
            View toolbar = findViewById(R.id.toolbar);
            if (toolbar instanceof Toolbar) {
                mToolbar = (Toolbar) toolbar;
            }
        }

        return mToolbar;
    }

    public HTitleBarView getTitleBar() {
        View titleBar = findViewById(R.id.titleBar);

        if (titleBar instanceof HTitleBarView) {
            return (HTitleBarView) titleBar;
        }

        return null;
    }

    @Override
    public void onFragmentInteraction(ViewModel data) {
    }

    @Override
    public Intent getIntent() {
        Intent intent = super.getIntent();

        if (null == intent) {
            intent = new Intent();
            setIntent(intent);
        }

        return intent;
    }

    /**
     * Bundle
     *
     * @return
     */
    public Bundle getExtras() {
        Bundle extras = getIntent().getExtras();

        if (null == extras) {
            extras = new Bundle();
            getIntent().putExtras(extras);
        }

        return extras;
    }

    public HBaseActivity<ViewModelData> getActivity() {
        return this;
    }

    @Override
    protected void onResume() {
        isUserRecordSaved = false;
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //    @Override
    //    public void onFragmentInteraction(Uri uri) {
    //    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // return super.onKeyDown(keyCode, event);

        if (KeyEvent.KEYCODE_BACK != keyCode) {
            return super.onKeyDown(keyCode, event);
        }

        Fragment currDialogFragment = null;
        int size = getData().dialogFragmentTags.size();

        if (size > 0) {
            for (int index = size - 1; index >= 0; index--) {
                String tag = getData().dialogFragmentTags.get(index);
                currDialogFragment = getSupportFragmentManager().findFragmentByTag(tag);

                if (currDialogFragment != null) {
                    break;
                }
            }
        }

        if (currDialogFragment != null) {
            // dialog fragment
            if (currDialogFragment instanceof BaseDialogFragment) {
                BaseDialogFragment baseDialogFragment = (BaseDialogFragment) currDialogFragment;
                if (baseDialogFragment.isBackable) {
                    HFragmentExchangeController.removeFragment(getSupportFragmentManager(), currDialogFragment);
                }

                return true;
            }
        }

        return popBackStackImmediate() || super.onKeyDown(keyCode, event);

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
    protected boolean popBackStackImmediate() {
        if (mFragmentBackStackEntryCount < 0) {
            return false;
        }

        if (getSupportFragmentManager().getBackStackEntryCount() <= mFragmentBackStackEntryCount) {
            // finishCurrentActivity();
            // return true;
            return false;
        }

        try {
            boolean pop = getSupportFragmentManager().popBackStackImmediate();
            if (pop) {
                updateView4PopBackStack();
            }
            return pop;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Pop Fragment update View
     */
    protected void updateView4PopBackStack() {
    }

    /**
     * 结束当前Activity内容 在{@link #onKeyDown}中调用
     */
    public void finishCurrentActivity() {
        saveUserRecord();
        finish();
    }


    /**
     * 保存用户操作记录, 真正实现在子类(Fragment)中
     */
    public void saveUserRecord() {
        if (isUserRecordSaved) {
            return;
        }

        ArrayList<Fragment> fragments = HFragmentExchangeController.getAllFragments(this);
        if (fragments == null) {
            return;
        }

        final int size = fragments.size();
        for (int i = size - 1; i >= 0; i--) {
            Fragment fragment = fragments.get(i);
            if (fragment instanceof HBaseFragment) {
                ((HBaseFragment) fragment).saveUserRecordFromActivity();
                isUserRecordSaved = true;
            }
        }
    }

    /**
     * 加载数据
     *
     * @param more 加载更多
     */
    public void loadServiceFlow(final boolean more) {
        if (!checkLoadingStatus(more)) {
            return;
        }

        loadService(more);
    }

    /**
     * 加载数据
     *
     * @param more 加载更多(下一页)
     */
    public void loadService(final boolean more) {
        getData().setLoadingStatus(true);
        setPageIndex(more);
    }

    /**
     * 检测是否可加载数据的状态
     *
     * @return
     */
    public boolean checkLoadingStatus(final boolean more) {
        if (getData().isLoadingStatus()) {
            return false;
        }

        if (isFinishing()) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed()) {
            return false;
        }

        if (more) {
            if (!hasNextPage()) {
                return false;
            }
        }

        return true;

    }

    /**
     * 是否有下一页
     *
     * @return
     */
    public boolean hasNextPage() {
        return getData().hasNextPage();
    }

    /**
     * 设置分页功能的pageIndex
     *
     * @param more
     */
    public void setPageIndex(final boolean more) {
        if (more) {
            getData().pageIdx++;
        } else {
            getData().pageIdx = getData().pageIdxStart;
        }
    }

    @Override
    public void finish() {
        saveUserRecord();
        HActivityStack.INSTANCE.remove(this);
        super.finish();
    }

    @Override
    protected void onDestroy() {
        unbindButterKnife();
        HRxBus.getInstance().unRegister(this);
        HActivityStack.INSTANCE.pop(this, false);

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * back Activity or Fragment
     */
    public void goBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            onBackPressed();
            return;
        }

        try {
            getSupportFragmentManager().popBackStackImmediate();
        } catch (Exception e) {
            onBackPressed();
        }
    }

    public void startActivity(Class<HBaseActivity> cls) {
        super.startActivity(new Intent(this, cls));
    }

    /**
     * 获取事件执行时间
     *
     * @param action
     * @return
     */
    public long getEvenTime(String action, boolean remove) {
        if (StringUtils.isNullOrWhiteSpace(action)) {
            return 0;
        }

        long time = getData().eventMap.get(action);

        if (remove) {
            removeEven(action);
        }

        return time;
    }

    /**
     * 获取事件执行时间
     *
     * @param action
     * @return
     */
    public long getEvenTime(String action) {
        return getEvenTime(action, true);
    }

    /**
     * 删除事件执行时间
     *
     * @param action
     */
    public void removeEven(String action) {
        if (StringUtils.isNullOrWhiteSpace(action)) {
            return;
        }

        getData().eventMap.remove(action);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Logger.d("onActivityResult requestCode: %d ,resultCode: %d", requestCode, resultCode);
    }

    private String getPageName() {
        Class clazz = this.getClass();
        return clazz.getSimpleName();
    }

    public List<String> getRequestPermission(int key) {
        return getData().requestPermissionMap.get(key);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (getData().superOnRequestPermissionsResult) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        if (getData().requestPermissionMap.containsKey(requestCode)) {
            HPermissionsDispatcher.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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
    public boolean requestPermissions(int requestCode, boolean superOnRequestPermissionsResult, final List<String> requestPermissions) {
        //        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        //            return false;
        //        }
        getData().superOnRequestPermissionsResult = superOnRequestPermissionsResult;

        if (requestPermissions != null && !requestPermissions.isEmpty()) {
            getData().requestPermissionMap.put(requestCode, requestPermissions);
        } else {
            getData().requestPermissionMap.remove(requestCode);
        }

        return HPermissionUtils.requestPermissionsByActivity(this, requestCode, requestPermissions, this);
    }

    /**
     * 所有权限是否开放
     *
     * @param requestCode
     * @return
     */
    public boolean isAllPermissionGranted(int requestCode) {
        return HPermissionUtils.isAllPermissionGrantedByList(this, getData().requestPermissionMap.get(requestCode));
    }

    /**
     * 权限
     *
     * @param requestCode
     * @param grantResults
     * @param permissions
     */
    @Override
    public void onPermissionsGranted(int requestCode, int[] grantResults, String... permissions) {
        onPermissionsGranted2(requestCode, grantResults, permissions);
        getData().requestPermissionMap.remove(requestCode);
    }

    public void onPermissionsGranted2(int requestCode, int[] grantResults, String... permissions) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, int[] grantResults, String... permissions) {
    }

    @Override
    public void onShowRequestPermissionRationale(int requestCode, boolean isShowRationale, String... permissions) {
        if (permissions != null && permissions.length > 0) {
            HPermissionsDispatcher.requestPermissions(this, requestCode, permissions);
        }
    }

    @Override
    public void onPermissionsError(int requestCode, int[] grantResults, String errorMsg, String... permissions) {
        getData().requestPermissionMap.remove(requestCode);
    }

    /**
     * 单按钮弹出框
     *
     * @param tag         tag
     * @param buttonText  按钮文案
     * @param title       标题
     * @param contentText 提示文案
     * @param isBackAble  返回可点击
     * @param isSpaceAble 空白可点击
     * @return
     */
    public BaseDialogFragment showSingleDialog(String tag, CharSequence buttonText, CharSequence title,
                                               CharSequence contentText, boolean isBackAble, boolean isSpaceAble) {
        return showDialog(null, null, DialogType.SINGLE, tag, buttonText, "", title, contentText, isBackAble, isSpaceAble);
    }

    /**
     * show Dialog
     * 返回和空白不可点击
     *
     * @param type         类型
     * @param tag          tag
     * @param negativeText 取消文案
     * @param positiveText 确定文案
     * @param title        标题
     * @param contentText  提示文案
     * @return
     */
    public BaseDialogFragment showDialog(DialogType type, String tag, CharSequence negativeText, CharSequence positiveText,
                                         CharSequence title, CharSequence contentText) {
        return showDialog(null, null, type, tag, negativeText, positiveText, title, contentText, false, false);
    }

    /**
     * show Dialog
     * 返回和空白可点击
     *
     * @param type         类型
     * @param tag          tag
     * @param negativeText 取消文案
     * @param positiveText 确定文案
     * @param title        标题
     * @param contentText  提示文案
     * @return
     */
    public BaseDialogFragment showDialogBackable(DialogType type, String tag, CharSequence negativeText, CharSequence positiveText,
                                                 CharSequence title, CharSequence contentText) {
        return showDialog(null, null, type, tag, negativeText, positiveText, title, contentText, true, true);
    }

    /**
     * show Dialog
     *
     * @param type         类型
     * @param tag          tag
     * @param negativeText 取消文案
     * @param positiveText 确定文案
     * @param title        标题
     * @param contentText  提示文案
     * @param isBackAble   返回可点击
     * @param isSpaceAble  空白可点击
     * @return
     */
    public BaseDialogFragment showDialog(DialogType type, String tag, CharSequence negativeText, CharSequence positiveText,
                                         CharSequence title, CharSequence contentText, boolean isBackAble, boolean isSpaceAble) {
        return showDialog(null, null, type, tag, negativeText, positiveText, title, contentText, isBackAble, isSpaceAble);
    }

    /**
     * show Dialog
     *
     * @param fragment
     * @param type         类型
     * @param tag          tag
     * @param negativeText 取消文案
     * @param positiveText 确定文案
     * @param title        标题
     * @param contentText  提示文案
     * @param isBackAble   返回可点击
     * @param isSpaceAble  空白可点击
     * @return
     */
    public BaseDialogFragment showDialog(Fragment fragment,
                                         DialogType type, String tag, CharSequence negativeText, CharSequence positiveText,
                                         CharSequence title, CharSequence contentText, boolean isBackAble, boolean isSpaceAble) {
        return showDialog(fragment, null, type, tag, negativeText, positiveText, title, contentText, isBackAble, isSpaceAble);
    }

    /**
     * show Dialog
     *
     * @param fragment
     * @param callBackContainer
     * @param type              类型
     * @param tag               tag
     * @param negativeText      取消文案
     * @param positiveText      确定文案
     * @param title             标题
     * @param contentText       提示文案
     * @param isBackAble        返回可点击
     * @param isSpaceAble       空白可点击
     * @return
     */
    public BaseDialogFragment showDialog(Fragment fragment, DialogCallBackContainer callBackContainer,
                                         DialogType type, String tag, CharSequence negativeText, CharSequence positiveText,
                                         CharSequence title, CharSequence contentText, boolean isBackAble, boolean isSpaceAble) {
        DialogExchangeModel.DialogExchangeModelBuilder builder = new DialogExchangeModel.DialogExchangeModelBuilder(type, tag);

        // 双按钮框
        if (type == DialogType.EXCUTE) {
            builder
                    .setPositiveText(positiveText)
                    .setNegativeText(negativeText);
        }
        // 单按钮框
        else if (type == DialogType.SINGLE) {
            builder.setSingleText(negativeText);
        }

        // 文本消息
        builder.setDialogContext(contentText);

        // 标题，back可点击，空白可点击
        builder
                .setDialogTitle(title)
                .setBackable(isBackAble)
                .setSpaceable(isSpaceAble)
                .setHasTitle(!StringUtils.isEmptyOrNull(title));

        return HWDialogManager.showDialogFragment(getSupportFragmentManager(), builder.create(), callBackContainer, fragment, this);
    }

    /**
     * 确认点击回调
     *
     * @param tag
     */
    @Override
    public void onPositiveBtnClick(String tag) {
    }

    /**
     * 取消点击回调
     *
     * @param tag
     */
    @Override
    public void onNegtiveBtnClick(String tag) {
    }

    /**
     * 单个按键点击回调
     *
     * @param tag
     */
    @Override
    public void onSingleBtnClick(String tag) {
    }


}
