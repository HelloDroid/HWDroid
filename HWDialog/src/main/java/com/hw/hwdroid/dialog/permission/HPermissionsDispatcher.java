package com.hw.hwdroid.dialog.permission;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限管理工具类
 */
public final class HPermissionsDispatcher {
    private static boolean mIsShowDialog = true;
    private static HPermissionSettingDialog mPermissionSettingDialog;

    public static void dismissPermissionSettingDialog() {
        if (mPermissionSettingDialog == null) {
            return;
        }

        mPermissionSettingDialog.dismiss();
        mPermissionSettingDialog = null;
    }

    public static void setPermissionSettingDialog(HPermissionSettingDialog permissionSettingDialog) {
        mPermissionSettingDialog = permissionSettingDialog;
    }

    private HPermissionsDispatcher() {
    }

    public static void checkPermissions(final Activity act, int requestCode, HPermissionListener listener, boolean isShowDialog, String... permissions) {
        mIsShowDialog = isShowDialog;
        checkPermissions(act, requestCode, listener, permissions);
    }

    /**
     * check permissions are whether granted or not
     *
     * @param act
     * @param requestCode
     * @param listener
     * @param permissions
     */
    public static void checkPermissions(final Activity act, int requestCode, HPermissionListener listener, String... permissions) {

        if (act == null) {
            if (listener != null) {
                listener.onPermissionsError(requestCode, null, "checkPermissions()-->param act :the activity is null", permissions);
            }
            return;
        }
        if (permissions == null || permissions.length < 1) {
            if (listener != null) {
                listener.onPermissionsError(requestCode, null, "checkPermissions()-->param permissions: is null or length is 0", permissions);
            }
            return;
        }

        /*****
         * check permissions are granted ?
         */
        MyPermissionUtils.sortGrantedAndDeniedPermissions(act, permissions);

        if (MyPermissionUtils.getGrantedPermissions().size() > 0) {
            List<String> grantedPermissionsList = MyPermissionUtils.getGrantedPermissions();
            String[] grantedPermissionsArr = grantedPermissionsList.toArray(new String[grantedPermissionsList.size()]);

            if (listener != null) {
                listener.onPermissionsGranted(requestCode, null, grantedPermissionsArr);
            }
        }

        if (MyPermissionUtils.getDeniedPermissions().size() > 0) {
            List<String> deniedPermissionsList = MyPermissionUtils.getDeniedPermissions();
            String[] deniedPermissionsArr = deniedPermissionsList.toArray(new String[deniedPermissionsList.size()]);
            if (deniedPermissionsArr.length > 0) {
                MyPermissionUtils.sortUnShowPermission(act, deniedPermissionsArr);
            }
        }

        if (MyPermissionUtils.getUnShowedPermissions().size() > 0) {
            List<String> unShowPermissionsList = MyPermissionUtils.getUnShowedPermissions();
            String[] unShowPermissionsArr = unShowPermissionsList.toArray(new String[unShowPermissionsList.size()]);
            if (listener != null) {
                if (true == mIsShowDialog) {
                    StringBuilder message = getUnShowPermissionsMessage(unShowPermissionsList);
                    showMessage_GotoSetting(act, message.toString());
                }
                listener.onShowRequestPermissionRationale(requestCode, false, unShowPermissionsArr);
            }
        }

        mIsShowDialog = true;

        if (MyPermissionUtils.getNeedRequestPermissions().size() > 0) {//true 表示允许弹申请权限框
            List<String> needRequestPermissionsList = MyPermissionUtils.getNeedRequestPermissions();
            String[] needRequestPermissionsArr = needRequestPermissionsList.toArray(new String[needRequestPermissionsList.size()]);
            if (listener != null) {
                listener.onShowRequestPermissionRationale(requestCode, true, needRequestPermissionsArr);
            }
        }
    }

    private static StringBuilder getUnShowPermissionsMessage(List<String> list) {
        StringBuilder message = new StringBuilder("您已关闭了");
        String permisson;
        boolean hasCALENDAR = false;
        boolean hasCAMERA = false;
        boolean hasCONTACTS = false;
        boolean hasLOCATION = false;
        boolean hasMICROPHONE = false;
        boolean hasPHONE = false;
        boolean hasSENSORS = false;
        boolean hasSMS = false;
        boolean hasSTORAGE = false;

        if (list.size() == 1) {
            permisson = list.get(0);
            if (permisson.contains("CALENDAR")) {
                message.append("日历 ");
            } else if (permisson.contains("CAMERA")) {
                message.append("相机 ");

            } else if (permisson.contains("CONTACTS") || permisson.equals("android.permission.GET_ACCOUNTS")) {
                message.append("通讯录 ");

            } else if (permisson.contains("LOCATION")) {
                message.append("定位 ");

            } else if (permisson.equals("android.permission.RECORD_AUDIO")) {
                message.append("耳麦 ");

            } else if (permisson.contains("PHONE")
                    || permisson.contains("CALL_LOG")
                    || permisson.contains("ADD_VOICEMAIL")
                    || permisson.contains("USE_SIP")
                    || permisson.contains("PROCESS_OUTGOING_CALLS")) {
                message.append("电话 ");

            } else if (permisson.contains("BODY_SENSORS")) {
                message.append("身体传感 ");

            } else if (permisson.contains("SMS")
                    || permisson.contains("RECEIVE_WAP_PUSH")
                    || permisson.contains("RECEIVE_MMS")
                    || permisson.contains("READ_CELL_BROADCASTS")) {
                message.append("短信 ");
            } else if (permisson.contains("STORAGE")) {
                message.append("手机存储 ");
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                permisson = list.get(i);
                if (permisson.contains("CALENDAR") && hasCALENDAR == false) {
                    message.append("日历");
                    hasCALENDAR = true;
                } else if (permisson.contains("CAMERA") && hasCAMERA == false) {
                    message.append("相机");
                    hasCAMERA = true;
                } else if (permisson.contains("CONTACTS")
                        || permisson.equals("android.permission.GET_ACCOUNTS")
                        && hasCONTACTS == false) {
                    message.append("通讯录");
                    hasCONTACTS = true;
                } else if (permisson.contains("LOCATION") && hasLOCATION == false) {
                    message.append("定位");
                    hasLOCATION = true;
                } else if (permisson.equals("android.permission.RECORD_AUDIO") && hasMICROPHONE == false) {
                    message.append("耳麦");
                    hasMICROPHONE = true;
                } else if (permisson.contains("PHONE")
                        || permisson.contains("CALL_LOG")
                        || permisson.contains("ADD_VOICEMAIL")
                        || permisson.contains("USE_SIP")
                        || permisson.contains("PROCESS_OUTGOING_CALLS") && hasPHONE == false) {
                    message.append("电话");
                    hasPHONE = true;
                } else if (permisson.contains("BODY_SENSORS") && hasSENSORS == false) {
                    message.append("身体传感");
                    hasSENSORS = true;
                } else if (permisson.contains("SMS")
                        || permisson.contains("RECEIVE_WAP_PUSH")
                        || permisson.contains("RECEIVE_MMS")
                        || permisson.contains("READ_CELL_BROADCASTS") && hasSMS == false) {
                    message.append("短信");
                    hasSMS = true;
                } else if (permisson.contains("STORAGE") && hasSTORAGE == false) {
                    message.append("手机存储");
                    hasSTORAGE = true;
                }
                if (i < list.size() - 1) {
                    message.append(",");
                }
            }
        }

        message.append("访问权限，为了保证功能的正常使用，请前往系统设置页面开启");
        return message;
    }

    private static void gotoPermissionSetting(Activity act) {
        Uri packageURI = Uri.parse("package:" + act.getPackageName());
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        act.startActivity(intent);
    }

    private static void showMessage_GotoSetting(@NonNull final Activity act, final String message) {
        if (act == null) {
            return;
        }

        act.runOnUiThread(() -> {
            HPermissionSettingDialog.Builder builder = new HPermissionSettingDialog.Builder(act);
            builder.setMessage(message);
            builder.setPositiveButton("取消", (dialog, which) -> {
                dialog.dismiss();
                dismissPermissionSettingDialog();
            });

            builder.setNegativeButton("设置", (dialog, which) -> {
                dialog.dismiss();
                gotoPermissionSetting(act);
                dismissPermissionSettingDialog();
            });

            builder.create().show();
        });

    }

    /**
     * request permissions to be granted
     *
     * @param act
     * @param requestCode
     * @param permissions
     */
    public static void requestPermissions(Activity act, int requestCode, String... permissions) {
        ActivityCompat.requestPermissions(act, permissions, requestCode);
    }

    public static void checkPermissionsByFragment(Fragment fragment, int requestCode, HPermissionListener listener, boolean isShowDialog, String... permissions) {
        mIsShowDialog = isShowDialog;
        checkPermissionsByFragment(fragment, requestCode, listener, permissions);
    }

    /**
     * check permissions are whether granted or not for fragment
     *
     * @param fragment
     * @param requestCode
     * @param listener
     * @param permissions
     */
    public static void checkPermissionsByFragment(Fragment fragment, int requestCode, HPermissionListener listener, String... permissions) {
        if (fragment == null) {
            if (listener != null) {
                listener.onPermissionsError(requestCode, null, "checkPermissions()-->param act :the activity is null", permissions);
            }
            return;
        }

        if (permissions == null || permissions.length < 1) {
            if (listener != null) {
                listener.onPermissionsError(requestCode, null, "checkPermissions()-->param permissions: is null or length is 0", permissions);
            }
            return;
        }

        MyPermissionUtils.sortGrantedAndDeniedPermissions(fragment.getContext(), permissions);

        if (MyPermissionUtils.getGrantedPermissions().size() > 0) {
            List<String> grantedPermissionsList = MyPermissionUtils.getGrantedPermissions();
            String[] grantedPermissionsArr = grantedPermissionsList.toArray(new String[grantedPermissionsList.size()]);

            if (listener != null) {
                listener.onPermissionsGranted(requestCode, null, grantedPermissionsArr);
            }
        }

        if (MyPermissionUtils.getDeniedPermissions().size() > 0) {
            List<String> deniedPermissionsList = MyPermissionUtils.getDeniedPermissions();
            String[] deniedPermissionsArr = deniedPermissionsList.toArray(new String[deniedPermissionsList.size()]);
            if (deniedPermissionsArr.length > 0) {
                MyPermissionUtils.sortUnShowPermissionByFragment(fragment, deniedPermissionsArr);
            }
        }

        if (MyPermissionUtils.getUnShowedPermissions().size() > 0) {
            List<String> unShowPermissionsList = MyPermissionUtils.getUnShowedPermissions();
            String[] unShowPermissionsArr = unShowPermissionsList.toArray(new String[unShowPermissionsList.size()]);
            if (listener != null) {
                if (true == mIsShowDialog) {
                    StringBuilder message = getUnShowPermissionsMessage(unShowPermissionsList);
                    showMessage_GotoSetting(fragment.getActivity(), message.toString());
                }

                listener.onShowRequestPermissionRationale(requestCode, false, unShowPermissionsArr);
            }
        }

        mIsShowDialog = true;

        if (MyPermissionUtils.getNeedRequestPermissions().size() > 0) {//true 表示允许弹申请权限框
            List<String> needRequestPermissionsList = MyPermissionUtils.getNeedRequestPermissions();
            String[] needRequestPermissionsArr = needRequestPermissionsList.toArray(new String[needRequestPermissionsList.size()]);
            if (listener != null) {
                listener.onShowRequestPermissionRationale(requestCode, true, needRequestPermissionsArr);
            }
        }
    }

    /**
     * request permissions to be granted for fragment
     *
     * @param fragment
     * @param requestCode
     * @param permissions
     */
    public static void requestPermissionsByFragment(Fragment fragment, int requestCode, String... permissions) {
        fragment.requestPermissions(permissions, requestCode);
    }


    /**
     * do their permissions results for fragment
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param listener
     */
    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults, HPermissionListener listener) {
        List<String> grantedPermissions = new ArrayList<>();
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            } else {
                grantedPermissions.add(permissions[i]);
            }
        }
        if (grantedPermissions.size() > 0) {
            String[] grantedPermissionsArr = grantedPermissions.toArray(new String[grantedPermissions.size()]);

            if (listener != null) {
                listener.onPermissionsGranted(requestCode, null, grantedPermissionsArr);
            }
        }
        if (deniedPermissions.size() > 0) {
            String[] deniedPermissionsArr = deniedPermissions.toArray(new String[deniedPermissions.size()]);

            if (listener != null) {
                listener.onPermissionsDenied(requestCode, null, deniedPermissionsArr);
            }
        }
    }

}

