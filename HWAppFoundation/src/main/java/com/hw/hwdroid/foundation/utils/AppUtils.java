package com.hw.hwdroid.foundation.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Browser;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneNumberUtils;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES;


/**
 * 系统操作方法
 *
 * @author chenj
 * @date 2014-7-30
 */
public class AppUtils {

    /**
     * 版本号
     *
     * @param context
     * @param defVersionCode
     * @return
     */
    public static int getVersionCode(Context context, int defVersionCode) {
        try {
            PackageManager packageMgr = context.getPackageManager();
            String packageName = context.getPackageName();
            if (packageMgr == null || TextUtils.isEmpty(packageName)) {
                return defVersionCode;
            }

            PackageInfo packageInfo = packageMgr.getPackageInfo(packageName, 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            return defVersionCode;
        }
    }

    /**
     * 版本号code
     */
    public static int getVersionCode(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            Logger.e(e);
        }
        return -1;
    }

    /**
     * 版本名
     *
     * @param context
     * @param defVersionName
     * @return
     */
    public static String getVersionName(Context context, String defVersionName) {
        try {
            PackageManager packageMgr = context.getPackageManager();
            String packageName = context.getPackageName();
            if (packageMgr == null || TextUtils.isEmpty(packageName)) {
                return defVersionName;
            }

            PackageInfo packageInfo = packageMgr.getPackageInfo(packageName, 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            return defVersionName;
        }
    }

    private static final String VERSIONNAMEADDEDFORSAMSUNG = "ctch1";

    /**
     * 版本号name
     */
    public static String getVersionName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            Logger.e(e);
        }
        return "";
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName2(@NonNull Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String versionName = pi.versionName.endsWith(VERSIONNAMEADDEDFORSAMSUNG) ? pi.versionName.replace(VERSIONNAMEADDEDFORSAMSUNG,
                    "") : pi.versionName;
            return versionName;
        } catch (Exception e) {
            Logger.e(e);
            return "";
        }
    }

    /**
     * 应用名
     *
     * @return
     */
    public static String getAppName(Context context) {
        String name = "";
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            name = context.getString(pi.applicationInfo.labelRes);
        } catch (NameNotFoundException e) {
            Logger.e(e);
        }

        return name;
    }

    /**
     * 应用名
     */
    public static String getAppName2(Context context) {
        if (context == null) {
            return "";
        }

        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.applicationInfo.loadLabel(context.getPackageManager()).toString();
        } catch (Exception e) {
            Logger.e(e);
        }
        return "";
    }

    /**
     * 是否是系统软件或者是系统软件的更新软件
     *
     * @return
     */
    public static boolean isSystemApp(@NonNull Context context) {
        try {
            return ((getPackageInfo(context).applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
        } catch (Exception e) {
            Logger.e(e);

            return false;
        }
    }

    /**
     * 系统软件 OR 系统软件更新
     *
     * @param context
     * @return
     */
    public static boolean isSystemUpdateApp(@NonNull Context context) {
        try {
            return ((getPackageInfo(context).applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
        } catch (Exception e) {
            Logger.e(e);

            return false;
        }
    }

    /**
     * 用户类型软件
     *
     * @param context
     * @return
     */
    public static boolean isUserApp(@NonNull Context context) {
        return (!isSystemApp(context) && !isSystemUpdateApp(context));
    }

    /**
     * 返回包详细
     *
     * @param context
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageManager packageMgr = context.getPackageManager();
        String packageName = context.getPackageName();
        if (packageMgr == null || TextUtils.isEmpty(packageName)) {
            return null;
        }

        try {
            return packageMgr.getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    /**
     * 读取Manifest中的meta-data字段的Bundle
     *
     * @param context
     * @return
     */
    public static Bundle getMetaData(@NonNull Context context) {
        if (context == null) {
            return null;
        }

        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager
                    .GET_META_DATA);
            return null != appInfo ? appInfo.metaData : null;
        } catch (NameNotFoundException e) {
            Logger.e(e);
            return null;
        }
    }

    /**
     * 读取Manifest中的meta-data字段
     *
     * @param context
     * @param metaKey
     * @return
     */
    public static String getMetaValue(@NonNull Context context, @NonNull String metaKey) {
        if (context == null || metaKey == null) {
            return "";
        }

        String value = "";

        Bundle metaData = getMetaData(context);
        if (null == metaData) {
            return "";
        }

        try {
            value = metaData.getString(metaKey);
        } catch (Exception e) {
            Logger.e(e);
        }

        try {
            if (TextUtils.isEmpty(value) && null != metaData.get(metaKey)) {
                value = metaData.get(metaKey).toString();
            }
        } catch (Exception e) {
            Logger.e(e);
        }

        if (null == value) {
            value = "";
        }

        return value;
    }

    /**
     * app是否运行在前台
     *
     * @param context
     * @return
     */
    public static boolean appIsForeground(Context context) {
        if (context == null)
            return false;

        PackageInfo packageInfo = AppUtils.getPackageInfo(context);
        if (null == packageInfo)
            return false;
        String packageName = packageInfo.packageName;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(2);

        if (tasksInfo != null && !tasksInfo.isEmpty()) {
            int len = tasksInfo.size();
            String topActivityName = tasksInfo.get(0).topActivity.getPackageName();

            // if (android.os.Build.MODEL.equals("MEIZU MX") && len >= 2) { //
            // 魅族
            // if(TTUtil.isScreenLocked()) {
            // topActivityName = tasksInfo.get(1).topActivity.getPackageName();
            // } else {
            // topActivityName = tasksInfo.get(0).topActivity.getPackageName();
            // }
            // }

            // 应用程序位于堆栈的顶层
            if (!StringUtils.isNullOrWhiteSpace(topActivityName) && packageName.equals(topActivityName)) {
                return true;
            }
        }

        // if (tasksInfo.size() > 0) {
        // String topActivityName =
        // tasksInfo.get(0).topActivity.getPackageName();
        // Log.v("toast", "topActivityName is:" + topActivityName);
        //
        // // 应用程序位于堆栈的顶层
        // if (packageName.equals(topActivityName)) {
        // return true;
        // }
        // }

        return false;
    }


    /**
     * Returns the country code for this locale
     *
     * @param context
     * @return 当前语言环境地区的国家代码
     */
    public static String countryCode(Context context) {
        if (null == context)
            return "";

        return context.getResources().getConfiguration().locale.getCountry();
    }

    /**
     * 比较国家代码
     *
     * @param context
     * @param countryCode 国家代码
     * @return true
     */
    public static boolean equalsCountryCode(Context context, String countryCode) {
        return StringUtils.equals(countryCode, countryCode(context));
    }

    /**
     * 比较国家代码
     *
     * @param context
     * @param countryCode Locale
     * @return
     */
    public static boolean equalsCountryCode(Context context, Locale countryCode) {
        return StringUtils.equals(countryCode.getCountry(), countryCode(context));
    }

    /**
     * 是否为简体中文
     *
     * @param context
     * @return
     */
    public static boolean isChineseCountryCode(Context context) {
        return equalsCountryCode(context, Locale.CHINA.getCountry());
    }

    /**
     * 设置语言
     *
     * @param context
     * @param locale  Locale
     */
    public static void updateConfiguration4Language(Context context, Locale locale) {
        if (null == context || null == locale) {
            return;
        }

        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();      // 屏幕
        Configuration config = resources.getConfiguration();    // 配置

        config.locale = locale;
        resources.updateConfiguration(config, dm);


        //        DisplayMetrics dm = new DisplayMetrics();
        //        Configuration config = context.getResources().getConfiguration();
        //        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //        wm.getDefaultDisplay().getMetrics(dm);
        //
        //        config.locale = locale;
        //        context.getResources().updateConfiguration(config, dm);
    }


    /**
     * 返回ApplicationInfo
     *
     * @param context
     * @return
     */
    public static ApplicationInfo applicationInfo(Context context) {
        try {
            PackageManager packageMgr = context.getPackageManager();
            String packageName = context.getPackageName();
            if (packageMgr == null || TextUtils.isEmpty(packageName)) {
                return null;
            }

            PackageInfo packageInfo = packageMgr.getPackageInfo(packageName, 0);
            return packageInfo.applicationInfo;
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    /**
     * 发送邮件
     *
     * @param context
     * @param eMail   eMail地址
     */
    public static void sendeMile(Context context, String eMail) {
        if (StringUtils.isNullOrWhiteSpace(eMail))
            return;

        try {
            Uri emailUri = Uri.parse("mailto:" + eMail);
            // 建立Intent 对象
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, emailUri);
            emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            Log.e("Customer service phone", "ActivityNotFoundException : ", e);
        }
    }

    /**
     * 发送邮件
     *
     * @param context
     * @param eMail      eMail地址
     * @param ccEmailArr 抄送地址
     * @param subject    主题
     * @param body       内容
     */
    public static void sendeMile(Context context, String eMail, String[] ccEmailArr, String subject, String body) {
        if (StringUtils.isNullOrWhiteSpace(eMail) || context == null) {
            return;
        }

        try {
            Uri emailUri = Uri.parse("mailto:" + eMail);
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, emailUri);
            emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (ccEmailArr != null && ccEmailArr.length > 0) {
                emailIntent.putExtra(Intent.EXTRA_CC, ccEmailArr);
            }

            if (!StringUtils.isNullOrWhiteSpace(subject)) {
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            } else {
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "The email subject text");
            }
            if (!StringUtils.isNullOrWhiteSpace(body)) {
                emailIntent.putExtra(Intent.EXTRA_TEXT, body);
            } else {
                emailIntent.putExtra(Intent.EXTRA_TEXT, "The email body text");
            }

            context.startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            Log.e("Customer service phone", "ActivityNotFoundException : ", e);
        }
    }

    /**
     * 选择邮件客户端发送邮件
     *
     * @param context
     * @param eMail   eMail地址
     * @param ccEmail 抄送地址
     * @param subject 主题
     * @param body    内容
     */
    public static void sendeMile4EmailClients(Context context, String eMail, String ccEmail, String subject, String body) {
        if (StringUtils.isNullOrWhiteSpace(eMail) || context == null) {
            return;
        }

        try {
            Uri emailUri = Uri.parse("mailto:" + eMail);
            Intent it = new Intent(Intent.ACTION_SEND, emailUri);
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String[] tos = {
                    eMail
            };
            it.putExtra(Intent.EXTRA_EMAIL, tos);
            if (!StringUtils.isNullOrWhiteSpace(ccEmail)) {
                String[] ccs = {
                        ccEmail
                };
                it.putExtra(Intent.EXTRA_CC, ccs);
            }
            if (!StringUtils.isNullOrWhiteSpace(subject)) {
                it.putExtra(Intent.EXTRA_TEXT, subject);
            } else {
                it.putExtra(Intent.EXTRA_SUBJECT, "The email subject text");
            }
            if (!StringUtils.isNullOrWhiteSpace(body)) {
                it.putExtra(Intent.EXTRA_TEXT, body);
            } else {
                it.putExtra(Intent.EXTRA_TEXT, "The email body text");
            }
            it.setType("message/rfc822");

            context.startActivity(Intent.createChooser(it, "Choose Email Client"));
        } catch (ActivityNotFoundException e) {
            Log.e("Customer service phone", "ActivityNotFoundException : ", e);
        }
    }

    /**
     * 呼叫电话
     * <p>
     * <pre>
     * 注意：需要权限<uses-permission android:name="android.permission.CALL_PHONE" />
     *
     * @param context
     * @param phoneNum 电话号码
     */
    public static void call(Context context, String phoneNum) {
        if (StringUtils.isNullOrWhiteSpace(phoneNum))
            return;

        try {
            Intent pIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + PhoneNumberUtils.formatNumber(phoneNum)));
            pIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 权限检测
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            context.startActivity(pIntent);
        } catch (ActivityNotFoundException e) {
            Log.e("Customer service phone", "ActivityNotFoundException : ", e);
        }
    }

    /**
     * 打开拨号界面
     *
     * @param context
     * @param phoneNum 电话号码
     */
    public static void toDial(Context context, String phoneNum) {
        if (StringUtils.isNullOrWhiteSpace(phoneNum))
            return;

        try {
            Intent pIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + PhoneNumberUtils.formatNumber(phoneNum)));
            pIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(pIntent);
        } catch (ActivityNotFoundException e) {
            Log.e("Customer service phone", "ActivityNotFoundException : ", e);
        }
    }

    /**
     * 将文本复制到剪贴板
     *
     * @param context
     * @param content 复制内容
     */
    @SuppressWarnings("deprecation")
    public static void copyToClipboard(Context context, String content) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setText(content);
        }
    }

    /**
     * 取得剪贴板的文本
     *
     * @param context
     */
    @SuppressWarnings("deprecation")
    public static String getText4Clipboard(Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null && cm.getText() != null) {
            return cm.getText().toString();
        }
        return "";
    }

    /**
     * wireless settings
     *
     * @param context
     */
    public static void wirelessSettings(Context context) {
        if (context == null)
            return;

        try {
            context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
        } catch (ActivityNotFoundException e) {
        } catch (Exception e) {
        }
    }

    /**
     * wifi settings
     *
     * @param context
     */
    public static void wifiSettings(Context context) {
        if (context == null)
            return;

        try {
            context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        } catch (ActivityNotFoundException e) {
        } catch (Exception e) {
        }
    }

    /**
     * 打开设置界面
     *
     * @param context
     */
    public static void settings(Context context) {
        if (context == null)
            return;

        try {
            context.startActivity(new Intent(Settings.ACTION_SETTINGS));
        } catch (ActivityNotFoundException e) {
        } catch (Exception e) {
        }
    }

    /**
     * 设置旋转方式
     *
     * @param context
     * @param autoRotation 自动旋转
     */
    public static void setRotation(Context context, boolean autoRotation) {
        if (context == null)
            return;

        // 当前屏幕旋转状态，1：自动旋转，
        if (autoRotation) {
            Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);
        } else {
            Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        }
    }

    /**
     * 设置禁止手机自动锁屏
     *
     * @param activity
     * @param setkeepScreenOn 自动旋转
     */
    public static void setKeepScreenOn(Activity activity, boolean setkeepScreenOn) {
        if (null == activity)
            return;

        // activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
        // WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 调用系统拍照功能，将拍照图片存储在 默认媒体库下
     *
     * @param context
     * @param requestCode 返回码
     */
    public static void takePicture(Context context, int requestCode) {
        try {
            if (context == null) {
                return;
            }

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, requestCode);
            } else {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } catch (ActivityNotFoundException e) {
        } catch (Exception e) {
        }
    }

    /**
     * 调用系统拍照功能，将拍照图片存储在 sdcard/.carmerTemp目录下
     *
     * @param context
     * @param fileName    照片存储Name
     * @param imagePath   存储点击拍照后的图片路径
     * @param requestCode
     * @Description
     */
    public static void takePicture(Context context, String fileName, String imagePath, int requestCode) {
        takePicture(context, SDCardUtils.getExternalStorageDirectory() + File.separator + ".carmerTemp", fileName, imagePath, requestCode);
    }

    /**
     * 调用系统拍照功能，将拍照图片存储在dir目录下
     *
     * @param context
     * @param dir         照片存储目录,如果目录为空,默认为 sdcard/.carmerTemp
     * @param fileName    照片存储Name
     * @param imagePath   存储点击拍照后的图片路径
     * @param requestCode 返回码
     * @Description
     */
    public static void takePicture(Context context, String dir, String fileName, String imagePath, int requestCode) {
        try {
            if (context == null) {
                return;
            }

            if (StringUtils.isNullOrWhiteSpace(dir)) {
                dir = SDCardUtils.getExternalStorageDirectory() + File.separator + ".carmerTemp";
            }

            File fDir = new File(dir);
            if (!fDir.exists() || !fDir.isDirectory()) {
                fDir.mkdirs();
            }
            if (StringUtils.isNullOrWhiteSpace(fileName)) {
                fileName = System.currentTimeMillis() + ".jpg";
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile = new File(dir, fileName);
            if (photoFile != null) {
                imagePath = photoFile.getAbsolutePath();
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, requestCode);
            } else {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } catch (ActivityNotFoundException e) {
        } catch (Exception e) {
        }
    }

    /**
     * 选择图片资源
     *
     * @param context
     * @param requestCode 返回码
     */
    public static void chooserPics(Context context, int requestCode) {
        if (context == null) {
            return;
        }

        try {
            Intent localIntent = new Intent();
            localIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            localIntent.setType("image/*");
            localIntent.setAction(Intent.ACTION_GET_CONTENT);
            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(Intent.createChooser(localIntent, "Select Picture"), requestCode);
            } else {
                localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(Intent.createChooser(localIntent, "Select Picture"));
            }
        } catch (Exception e) {
        }
    }

    /**
     * 选择系统相册
     *
     * @param context
     * @param requestCode 返回码
     * @Description
     */
    public static void chooserSysPics(Context context, int requestCode) {
        if (context == null) {
            return;
        }

        try {
            Intent localIntent = new Intent();
            localIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            localIntent.setType("image/*");
            // Intent.ACTION_GET_CONTENT
            localIntent.setAction("android.intent.action.GET_CONTENT");
            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(localIntent, requestCode);
            } else {
                localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(localIntent);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 保存图片
     *
     * @param context
     * @param absDir  相对路径
     * @param bmp
     */
    public static void saveImageToGallery(Context context, String absDir, String fileName, File bmp) {
        // 首先保存图片
        File appDir = new File(absDir);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + appDir)));
    }

    /**
     * 保存图片
     *
     * @param context
     * @param absDir  相对路径
     * @param bmp
     */
    public static void saveImageToGallery(Context context, String absDir, String fileName, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(absDir);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + appDir)));
    }

    /**
     * 保存图片
     *
     * @param context
     * @param dir     相对路径
     * @param bmp
     */
    public static void saveImageToGallery(Context context, String dir, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), dir);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + appDir)));
    }

    /**
     * 在应用商店打开
     *
     * @param context
     * @param packageName
     */
    public static void openApp4AppStore(Context context, String packageName) {
        if (null == context)
            return;

        try {
            if (StringUtils.isNullOrWhiteSpace(packageName))
                return;
            if (isPlayStoreInstalled(context)) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
            } else {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" +
                        packageName)));
            }
        } catch (Exception e) {
        }
    }

    /**
     * 是否安装了App Store
     *
     * @param context
     * @return
     */
    public static boolean isPlayStoreInstalled(Context context) {
        if (null == context)
            return false;

        try {

            Intent market = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=dummy"));
            PackageManager manager = context.getPackageManager();
            List<ResolveInfo> list = manager.queryIntentActivities(market, 0);

            return list.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检测是否安装app
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean checkApkExistTryCatch(@NonNull Context context, String packageName) {
        try {
            return checkApkExist(context, packageName);
        } catch (Exception e) {
            Logger.e(e);
            return false;
        }
    }

    /**
     * 检测是否安装app
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean checkApkExist(@NonNull Context context, String packageName) throws Exception {
        if (StringUtils.isNullOrWhiteSpace(packageName)) {
            return false;
        }

        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        // .getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
        return (packageInfo != null);
    }


    /**
     * 评论App
     *
     * @param context
     */
    public static void toScores(Context context) {
        try {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }

    /**
     * 在浏览器中打开Url
     *
     * @param context
     * @param url
     */
    public static void openView(Context context, String url) {
        try {
            Intent intentUri = new Intent(Intent.ACTION_VIEW);
            intentUri.setData(Uri.parse(url));
            intentUri.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentUri);
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    /**
     * 打开一个url链接
     *
     * @param context
     * @param url     url地址
     */
    public static void linkURL(Context context, String url) {
        if (StringUtils.isNullOrWhiteSpace(url) || context == null) {
            return;
        }

        if (url.toLowerCase().startsWith("www.")) {
            url = "http://" + url;
        }

        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Logger.e(e);
        }
    }

    /**
     * 打开app
     *
     * @param context
     * @param packageName
     */
    public static void openAppTryCatch(Context context, String packageName) {
        try {
            openApp(context, packageName);
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    /**
     * 打开app
     *
     * @param context
     * @param packageName
     */
    public static void openApp(Context context, String packageName) {
        if (null == context || StringUtils.isNullOrWhiteSpace(packageName)) {
            return;
        }

        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            context.startActivity(intent);
        }
    }

    /**
     * 启动APP
     *
     * @param context
     * @throws NameNotFoundException
     * @throws Exception
     */
    public static void runApp(Context context) throws NameNotFoundException, Exception {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo appinfo = packageManager.getPackageInfo(context.getPackageName(), GET_UNINSTALLED_PACKAGES | PackageManager
                .GET_ACTIVITIES);

        ActivityInfo[] activitys = appinfo.activities;
        if (activitys.length <= 0) {
            return;
        }

        ActivityInfo firstactivity = activitys[0];
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(context.getPackageName(), firstactivity.name);
        context.startActivity(intent);
    }

    /**
     * 激活Activity
     *
     * @param context
     * @param cls     被激活的Activity
     */
    public static void openActivity(Context context, Class<Activity> cls) {
        if (null == context || null == cls)
            return;

        Intent i = new Intent(context, cls);
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(i);
    }

    /**
     * 卸载
     *
     * @param context
     * @param packageName app 报名
     */
    @SuppressLint("InlinedApi")
    public static void uninstallApk(Context context, String packageName) {
        if (null == context)
            return;
        if (StringUtils.isNullOrWhiteSpace(packageName))
            return;

        try {
            Uri packageUri = Uri.parse("package:" + packageName);
            Intent uninstallIntent;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                uninstallIntent = new Intent(Intent.ACTION_DELETE, packageUri);
            } else {
                uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri);
            }
            context.startActivity(uninstallIntent);
        } catch (Exception e) {
        }
    }

    /**
     * 卸载程序
     *
     * @param context
     * @param packageName 包名
     * @see AppUtils#uninstallApk(Context, String)
     */
    @Deprecated
    public static void uninstallApk2(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
    }

    /**
     * 打开并安装文件
     *
     * @param context
     * @param file    apk文件路径
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 应用是否在运行
     *
     * @param ctx
     * @return
     */
    public static boolean isRunning(Context ctx) {
        if (null == ctx) {
            return false;
        }

        String packageName = ctx.getPackageName();
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        if (StringUtils.isNullOrWhiteSpace(packageName) || null == am) {
            return false;
        }

        boolean isAppRunning = false;
        List<RunningTaskInfo> list = am.getRunningTasks(50);
        if (null == list || list.isEmpty()) {
            return false;
        }

        for (RunningTaskInfo info : list) {
            if (null == info)
                continue;

            if ((info.topActivity != null && packageName.equals(info.topActivity.getPackageName()))
                    || (info.baseActivity != null && packageName.equals(info.baseActivity.getPackageName()))) {
                isAppRunning = true;
                break;
            }
        }

        return isAppRunning;
    }

    /**
     * 判断服务是否运行
     *
     * @param ctx
     * @param className 判断的服务名字 "com.xxx.xx..XXXService"
     * @return true 运行中
     */
    public static boolean isServiceRunning(Context ctx, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> servicesList = activityManager.getRunningServices(100);
        Iterator<RunningServiceInfo> l = servicesList.iterator();
        while (l.hasNext()) {
            RunningServiceInfo si = l.next();
            if (className.equals(si.service.getClassName())) {
                isRunning = true;
            }
        }

        return isRunning;
    }

    /**
     * 判断服务是否运行
     *
     * @param ctx
     * @param className 判断的服务名字 "com.xxx.xx..XXXService"
     * @return true 运行中
     */
    public static boolean hasServiceRunning(Context ctx, String... className) {
        List<String> classNames;
        try {
            classNames = Arrays.asList(className);

            if (null == classNames || classNames.isEmpty()) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        boolean isRunning = false;
        try {
            int maxNum = Integer.MAX_VALUE;
            maxNum = 100;
            ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningServiceInfo> servicesList = activityManager.getRunningServices(maxNum);
            Iterator<RunningServiceInfo> l = servicesList.iterator();
            while (l.hasNext()) {
                RunningServiceInfo si = l.next();
                if (classNames.contains(si.service.getClassName())) {
                    isRunning = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return isRunning;
    }

    /**
     * 停止服务
     *
     * @param ctx
     * @param className 服务名字 "com.xxx.xx..XXXService"
     * @return true, if successful
     */
    public static boolean stopRunningService(Context ctx, String className) {
        Intent intent_service = null;
        boolean ret = false;
        try {
            intent_service = new Intent(ctx, Class.forName(className));
        } catch (Exception e) {
        }
        if (intent_service != null) {
            ret = ctx.stopService(intent_service);
        }
        return ret;
    }

    /**
     * 获取动画设置
     *
     * @param context
     * @return
     */
    public static boolean getAnimationSetting(@NonNull Context context) {
        ContentResolver cv = context.getContentResolver();
        String animation = Settings.System.getString(cv, Settings.System.TRANSITION_ANIMATION_SCALE);
        return NumberUtils.parseDouble(animation) > 0;
    }

}
