package com.hw.hwdroid.dialog.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenJ on 16/4/25.
 * 权限工具类
 */
public class HPermissionUtils {

    //    public static final int REQUEST_CODE_PIC = 101;
    //    public static final int REQUEST_CODE_CAMERA = 100;
    //    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    /**
     * Camera权限
     *
     * @return
     */
    public static List<String> getCameraPermissionList() {
        List<String> cameraPermissionList = new ArrayList<>();
        cameraPermissionList.add(0, Manifest.permission.CAMERA);
        cameraPermissionList.add(1, Manifest.permission.READ_EXTERNAL_STORAGE);
        cameraPermissionList.add(2, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return cameraPermissionList;
    }


    /**
     * 相册权限
     *
     * @return
     */
    public static List<String> getPicturePermissionList() {
        List<String> picturePermissionList = new ArrayList<>();
        picturePermissionList.add(0, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        picturePermissionList.add(1, Manifest.permission.READ_EXTERNAL_STORAGE);
        return picturePermissionList;
    }


    /**
     * 定位权限
     *
     * @return
     */
    public static List<String> getLocationPermissionList() {
        List<String> locationPermissionList = new ArrayList<>();
        locationPermissionList.add(0, Manifest.permission.ACCESS_FINE_LOCATION);
        return locationPermissionList;
    }


    public static void requestCameraPermission(Activity activity, Fragment fragment, int requestCodeCamera) {
        Activity context = activity != null ? activity : null;

        if (context == null && fragment != null) {
            context = fragment.getActivity();
        }

        if (Build.VERSION.SDK_INT >= 23) {
            int cameraPermission = context.checkSelfPermission(Manifest.permission.CAMERA);
            int readStoragePermission = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            int writeStoragePermission = context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (cameraPermission != PackageManager.PERMISSION_GRANTED || readStoragePermission != PackageManager.PERMISSION_GRANTED ||
                    writeStoragePermission != PackageManager.PERMISSION_GRANTED) {
                context.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCodeCamera);
                return;
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public static void handleNoRequestPermssionDialog(final Fragment fragment, final String permission, final int requestCode) {
        final Activity activity = fragment.getActivity();

        if (!activity.shouldShowRequestPermissionRationale(permission)) {
            showMessageOKCancel("You need to allow access to Contacts", fragment.getActivity(),
                    (dialog, which) -> activity.requestPermissions(new String[]{permission}, requestCode));
            return;
        }
    }

    private static void showMessageOKCancel(String message, Activity activity, DialogInterface.OnClickListener okListener) {

        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    /**
     * 检查并请求权限,返回没有授权的权限列表
     */
    public static List<String> checkAndRequestPermissions(Context context, List<String> permissions) {
        if (permissions == null || permissions.size() <= 0) {
            return new ArrayList<String>();
        }
        //还没申请权限
        List<String> noPermission = new ArrayList<String>();
        //已有权限
        List<String> hasPermission = new ArrayList<String>();
        for (String permission : permissions) {
            filterPermissions(context, permission, noPermission, hasPermission);
        }

        return noPermission;
    }

    private static void filterPermissions(Context context, String permission, List<String> noPermission, List<String> hasPermission) {
        if (MyPermissionUtils.hasSelfPermissions(context, permission)) {
            hasPermission.add(permission);
        } else {
            noPermission.add(permission);
        }
    }

    public static boolean isAllPermissionGranted(int[] grantResults) {
        boolean isGranted = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    isGranted = false;
                }
            }
        }
        return isGranted;
    }


    /**
     * 所有权限开放
     *
     * @param context
     * @param permissionList
     * @return
     */
    public static boolean isAllPermissionGrantedByList(@NonNull Context context, List<String> permissionList) {
        if (null == permissionList) {
            return false;
        }


        boolean isGranted = true;
        try {
            for (String permission : permissionList) {
                if (!(PermissionChecker.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)) {
                    isGranted = false;
                    break;
                }
            }
        } catch (Exception e) {
            isGranted = false;
        }
        return isGranted;
    }


    public static void startUpCamera(Fragment fragment, int requestCodeCamera) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> noPermissionMap = checkAndRequestPermissions(fragment.getActivity(), getCameraPermissionList());
            if (noPermissionMap.size() > 0) {
                fragment.requestPermissions(noPermissionMap.toArray(new String[noPermissionMap.size()]), requestCodeCamera);
                return;
            }
        }
    }

    public static void startUpGallary(Fragment fragment, int requestCodePic) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> noPermissionMap = checkAndRequestPermissions(fragment.getActivity(), getPicturePermissionList());
            if (noPermissionMap.size() > 0) {
                fragment.requestPermissions(noPermissionMap.toArray(new String[noPermissionMap.size()]), requestCodePic);
                return;
            }
        }
    }


    public static void startUpCamera(final Activity activity, int requestCodeAskMultiplePermissions, CoPermissionListener coPermissionListener) {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(activity, permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("GPS");
        if (!addPermission(activity, permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("Read Contacts");
        if (!addPermission(activity, permissionsList, Manifest.permission.WRITE_CONTACTS))
            permissionsNeeded.add("Write Contacts");

        if (permissionsList.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), requestCodeAskMultiplePermissions);
            }
            return;
        }

        //todo
        coPermissionListener.onStart();
    }


    public interface CoPermissionListener {
        void onStart();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static boolean addPermission(Activity activity, List<String> permissionsList, String permission) {
        if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!activity.shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }


    /**
     * 检查并请求权限（手机精确定位）
     */
    public static boolean requestPermissionsByFragment(Fragment hostFragment, int requestCode, final List<String> requestPermissions, HPermissionListener listener) {
        boolean isRequestPermissionsSuccess = false;

        List<String> miss = getMissPermissions(hostFragment.getActivity(), requestPermissions);

        if (miss != null && !miss.isEmpty()) {
            isRequestPermissionsSuccess = true;
            HPermissionsDispatcher.checkPermissionsByFragment(hostFragment, requestCode, listener, true, miss.toArray(new String[miss.size()]));
        }

        return isRequestPermissionsSuccess;
    }


    /**
     * 检查并请求权限（手机精确定位）
     */
    public static boolean requestPermissionsByActivity(Activity act, int requestCode, final List<String> requestPermissions, HPermissionListener listener) {
        boolean isRequestPermissionsSuccess = false;

        List<String> miss = getMissPermissions(act, requestPermissions);

        if (miss != null && !miss.isEmpty()) {
            isRequestPermissionsSuccess = true;
            HPermissionsDispatcher.checkPermissions(act, requestCode, listener, true, miss.toArray(new String[miss.size()]));
        }

        return isRequestPermissionsSuccess;

    }


    private static List<String> getMissPermissions(Activity act, final List<String> requestPermissions) {
        /**缺少权限*/
        List<String> miss = new ArrayList<>();

        /**   filter area that add the requested permissions here...  */
        for (String permission : requestPermissions) {
            if (!MyPermissionUtils.hasSelfPermissions(act, permission)) {
                miss.add(permission);
            }
        }

        return miss;
    }

}
