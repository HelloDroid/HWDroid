package com.hw.hwdroid.foundation.utils.widget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import com.orhanobut.logger.Logger;

/**
 * 桌面徽章（未读消息数）
 *
 * @author chenj
 * @date 2015-5-15
 */
public class BadgeUtils {

    public enum Platform {
        samsung, lg, htc, mi, sony
    }

    ;

    public static void setBadgeCount(Context context, int count) {
        if (count < 0) {
            count = 0;
        }

        if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
            BadgeUtils.setBadgeCount(context, count, Platform.mi);
        } else if (Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
            BadgeUtils.setBadgeCount(context, count, Platform.samsung);
        } else if (Build.MANUFACTURER.equalsIgnoreCase("htc")) {
            BadgeUtils.setBadgeCount(context, count, Platform.htc);
        } else if (Build.MANUFACTURER.equalsIgnoreCase("lg")) {
            BadgeUtils.setBadgeCount(context, count, Platform.lg);
        } else if (Build.MANUFACTURER.equalsIgnoreCase("sony")) {
            BadgeUtils.setBadgeCount(context, count, Platform.sony);
        }
    }

    public static void setBadgeCount(Context context, int count, Platform platform) {
        if (count < 0)
            count = 0;
        Intent badgeIntent = null;

        if (platform.equals(Platform.samsung)) {
            Logger.d("samsung....");

            badgeIntent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            badgeIntent.putExtra("badge_count", count);
            badgeIntent.putExtra("badge_count_package_name", context.getPackageName());
            badgeIntent.putExtra("badge_count_class_name", getLauncherClassName(context));
        }

        if (platform.equals(Platform.mi)) {
            Logger.d("xiaoMiShortCut....");

            badgeIntent = new Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE");
            badgeIntent.putExtra("android.intent.extra.update_application_component_name", getLauncherClassName(context));
            badgeIntent.putExtra("android.intent.extra.update_application_message_text", count);
            context.sendBroadcast(badgeIntent);
        }

        if (platform.equals(Platform.sony)) {
            Logger.d("sony....");

            badgeIntent = new Intent();
            badgeIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", true);
            badgeIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
            badgeIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", getLauncherClassName(context));
            badgeIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", count);
            badgeIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.getPackageName());
        }

        if (platform.equals(Platform.htc)) {
            Logger.d("htc....");

            badgeIntent = new Intent("com.htc.launcher.action.UPDATE_SHORTCUT");
            badgeIntent.putExtra("packagename", getLauncherClassName(context));
            badgeIntent.putExtra("count", count);
        }

        if (platform.equals(Platform.lg)) {
            Logger.d("lg....");

            badgeIntent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            badgeIntent.putExtra("badge_count_package_name", context.getPackageName());
            badgeIntent.putExtra("badge_count_class_name", getLauncherClassName(context));
            badgeIntent.putExtra("badge_count", count);
        }
        context.sendBroadcast(badgeIntent);
    }

    /**
     * reset Badge count
     *
     * @param context
     */
    public static void resetBadgeCount(Context context) {
        Platform platform = getPlatform();
        if (null == platform) {
            return;
        }

        setBadgeCount(context, 0, platform);
    }

    /**
     * reset Badge count
     *
     * @param context
     * @param platform
     */
    public static void resetBadgeCount(Context context, Platform platform) {
        setBadgeCount(context, 0, platform);
    }

    /**
     * get platform
     *
     * @return
     */
    public static Platform getPlatform() {
        if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
            return Platform.mi;
        }

        if (Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
            return Platform.samsung;
        }

        if (Build.MANUFACTURER.equalsIgnoreCase("htc")) {
            return Platform.htc;
        }

        if (Build.MANUFACTURER.equalsIgnoreCase("lg")) {
            return Platform.lg;
        }

        if (Build.MANUFACTURER.equalsIgnoreCase("sony")) {
            return Platform.sony;
        }

        return null;
    }

    private static String getLauncherClassName(Context context) {
        PackageManager packageManager = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        // To limit the components this Intent will resolve to, by setting an
        // explicit package name.
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        // All Application must have 1 Activity at least.
        // Launcher activity must be found!
        ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        // get a ResolveInfo containing ACTION_MAIN, CATEGORY_LAUNCHER
        // if there is no Activity which has filtered by CATEGORY_DEFAULT
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }

        return info.activityInfo.name;
    }

}
