package com.hw.hwdroid.foundation.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hw.hwdroid.dialog.BaseDialogFragment;
import com.hw.hwdroid.dialog.DialogCallBackContainer;
import com.hw.hwdroid.dialog.HWDialogManager;
import com.hw.hwdroid.dialog.model.DialogType;
import com.hw.hwdroid.dialog.model.exchangeModel.DialogExchangeModel;
import com.hw.hwdroid.dialog.permission.HPermissionListener;
import com.hw.hwdroid.dialog.permission.HPermissionUtils;
import com.hw.hwdroid.dialog.permission.HPermissionsDispatcher;
import com.hw.hwdroid.foundation.app.annotation.HContentViewAttachToRoot;
import com.hw.hwdroid.foundation.app.annotation.HContentViewRes;
import com.hw.hwdroid.foundation.app.annotation.HLoadService;
import com.hw.hwdroid.foundation.app.model.ViewModel;
import com.hw.hwdroid.foundation.app.rx.bus.HRxBus;
import com.hw.hwdroid.foundation.utils.StringUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Base Fragment
 * <p>
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JBaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JBaseFragment<ViewModelData extends ViewModel> extends Fragment implements HPermissionListener {
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(ViewModel data);
    }

    /** 标志位，View已经初始化完成 */
    protected boolean isPrepared;


    /** 是否可见状态 为了避免和{@link JBaseFragment#isVisible()}冲突 */
    protected boolean isVisible;

    /** 是否第一次加载 */
    protected boolean isFirstLoad = true;


    /** 是否已经记录了用户操作记录 */
    private boolean isUserRecordSaved = false;
    /** 界面最后停留时间 毫秒 */
    public long takeAWalkLastTime;

    /** model dataList */
    private ViewModelData mData;

    private Unbinder unbinder;
    private OnFragmentInteractionListener mListener;

    public static JBaseFragment newInstance(Bundle args) {
        JBaseFragment fragment = new JBaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public JBaseFragment() {
        super();
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BaseFragment.
     * <p>
     * public static BaseFragment newInstance() {
     * BaseFragment fragment = new BaseFragment();
     * Bundle args = new Bundle();
     * fragment.setArguments(args);
     * return fragment;
     * }
     */


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setData((ViewModelData) new ViewModel());

        try {
            if (context instanceof OnFragmentInteractionListener) {
                mListener = (OnFragmentInteractionListener) context;
            }
        } catch (ClassCastException e) {
            // throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public Bundle getArgument() {
        Bundle arg = getArguments();

        if (null == arg) {
            arg = new Bundle();
        }

        return arg;
    }

    protected String getPageName() {
        Class clazz = this.getClass();
        return clazz.getSimpleName();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HRxBus.getInstance().init(this);
        getActivity();
    }

    public HWBaseActivity getBaseActivity() {
        Activity activity = getActivity();

        if (activity instanceof HWBaseActivity) {
            return (HWBaseActivity) activity;
        }

        return null;
    }

    @Override
    public final Context getContext() {
        Context context = super.getContext();

        if (null == context) {
            context = getActivity().getApplicationContext();
        }

        return context;
    }

    /**
     * // Inflate the layout for this fragment
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;

        isFirstLoad = true;
        boolean attachToRoot = false;
        if (getClass().isAnnotationPresent(HContentViewAttachToRoot.class)) {
            HContentViewAttachToRoot annotation = getClass().getAnnotation(HContentViewAttachToRoot.class);
            attachToRoot = annotation.value();
        }

        if (getClass().isAnnotationPresent(HContentViewRes.class)) {
            HContentViewRes annotation = getClass().getAnnotation(HContentViewRes.class);
            if (annotation.value() != View.NO_ID) {
                view = inflater.inflate(annotation.value(), attachToRoot ? container : null);
            }
        }

        bindButterKnife(view);
        return null != view ? view : super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 是否第一次加载
     *
     * @return
     */
    public boolean isFirstLoad() {
        return isFirstLoad;
    }

    /**
     * View已经初始化完成
     *
     * @return
     */
    public boolean isPrepared() {
        return isPrepared;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        bindButterKnife(view);
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        if (getClass().isAnnotationPresent(HLoadService.class) && getClass().getAnnotation(HLoadService.class).value()) {
            loadServiceFlow(false);
        }
    }

    protected void initViews(@NonNull View view) {
    }

    private void bindButterKnife(View view) {
        if (null != unbinder || null == view) {
            return;
        }

        isPrepared = true;
        unbinder = ButterKnife.bind(this, view);
    }

    private void unbindButterKnife() {
        if (null == unbinder) {
            return;
        }

        unbinder.unbind();
        unbinder = null;
    }

    @Override
    public void onResume() {
        isUserRecordSaved = false;
        super.onResume();
        takeAWalkLastTime = System.currentTimeMillis();
    }

    public void onInteraction() {
        if (mListener != null) {
            mListener.onFragmentInteraction(getData());
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isVisible = !hidden;

        if (hidden) {
            long dTakeAWalkLastTime = System.currentTimeMillis() - takeAWalkLastTime;
        }

        takeAWalkLastTime = System.currentTimeMillis();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
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
     * 是否正在加载
     *
     * @return
     */
    protected boolean isLoading() {
        return getData().isLoadingStatus();
    }

    /**
     * 检测是否可加载数据的状态
     *
     * @return
     */
    public boolean checkLoadingStatus(final boolean more) {
        if (null == getActivity()) {
            return false;
        }

        if (isLoading()) {
            return false;
        }

        if (getActivity().isFinishing()) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && getActivity().isDestroyed()) {
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

    /**
     * 各页面实现记录用户信息的方法, 并通过返回值由子类控制实际是否记录
     *
     * @return true - 子类不需要记录, false - 子类需要记录
     */
    public boolean onSaveUserRecord() {
        return isUserRecordSaved;
    }

    /**
     * 当Activity直接退出时调用记录用户信息
     */
    public void saveUserRecordFromActivity() {
        if (isUserRecordSaved) {
            return;
        }

        try {
            onSaveUserRecord();
        } catch (Exception e) {
            e.printStackTrace();
        }

        isUserRecordSaved = true;
    }

    @Override
    public void onDestroyView() {
        isPrepared = false;
        unbindButterKnife();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *
     public interface OnFragmentInteractionListener {
     void onFragmentInteraction(Uri uri);
     }
     */

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
        return showDialog(null, DialogType.SINGLE, tag, buttonText, "", title, contentText, isBackAble, isSpaceAble);
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
        return showDialog(null, type, tag, negativeText, positiveText, title, contentText, false, false);
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
        return showDialog(null, type, tag, negativeText, positiveText, title, contentText, true, true);
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
        return showDialog(null, type, tag, negativeText, positiveText, title, contentText, isBackAble, isSpaceAble);
    }

    /**
     * show Dialog
     *
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
    public BaseDialogFragment showDialog(DialogCallBackContainer callBackContainer,
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

        return HWDialogManager.showDialogFragment(getFragmentManager(), builder.create(), callBackContainer, this, getActivity());
    }

    /**
     * DimensionPixelOffset
     *
     * @param id
     * @return
     */
    public int getDimensionPixelOffset(@DimenRes int id) {
        try {
            return getResources().getDimensionPixelOffset(id);
        } catch (Exception e) {
            Logger.e(e);
            return 0;
        }
    }

    /**
     * DimensionPixelSize
     *
     * @param id
     * @return
     */
    public int getDimensionPixelSize(@DimenRes int id) {
        try {
            return getResources().getDimensionPixelSize(id);
        } catch (Exception e) {
            Logger.e(e);
            return 0;
        }
    }


    /**
     * 返回DisplayMetrics
     *
     * @return
     */
    public DisplayMetrics getDisplayMetrics() {
        return getResources().getDisplayMetrics();
    }

    /**
     * 根据dimension返回px
     *
     * @param id dimentResId
     * @return
     */
    public float applyDimension(@DimenRes int id) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(id), getDisplayMetrics());
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

        return HPermissionUtils.requestPermissionsByFragment(this, requestCode, requestPermissions, this);
    }

    /**
     * 所有权限是否开放
     *
     * @param requestCode
     * @return
     */
    public boolean isAllPermissionGranted(int requestCode) {
        return HPermissionUtils.isAllPermissionGrantedByList(getActivity(), getData().requestPermissionMap.get(requestCode));
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
        getData().requestPermissionMap.remove(requestCode);
    }

    @Override
    public void onPermissionsDenied(int requestCode, int[] grantResults, String... permissions) {
    }

    @Override
    public void onShowRequestPermissionRationale(int requestCode, boolean isShowRationale, String... permissions) {
        if (permissions != null && permissions.length > 0) {
            HPermissionsDispatcher.requestPermissionsByFragment(this, requestCode, permissions);
        }
    }

    @Override
    public void onPermissionsError(int requestCode, int[] grantResults, String errorMsg, String... permissions) {
        getData().requestPermissionMap.remove(requestCode);
    }

}
