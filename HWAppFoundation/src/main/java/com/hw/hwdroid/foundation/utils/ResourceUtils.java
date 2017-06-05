package com.hw.hwdroid.foundation.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.hw.hwdroid.foundation.R;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;

/**
 * 资源工具
 *
 * @author chenj
 * @date 2014-7-30
 */
public final class ResourceUtils {
    /**
     * 获取资源id
     */
    public static int getIdentifier(@NonNull Context context, String name, String type) {
        try {
            return context.getResources().getIdentifier(name, type, context.getPackageName());
        } catch (Exception e) {
            Logger.e(e);

            return 0;
        }
    }

    /**
     * 根据资源的名字获取其id值
     *
     * @param context
     * @param className 类名
     * @param name      资源名
     * @return int eg: getIdByName(getApplication(), "id", "button1") or getIdByName(getApplication(), "layout", "activity_main")
     */
    @SuppressWarnings("rawtypes")
    public static int getIdByName(@NonNull Context context, @NonNull String className, @NonNull String name) {
        if (null == context) {
            return 0;
        }

        int id = 0;
        Class rCls = null;
        String packageName = context.getPackageName();
        try {
            rCls = Class.forName(packageName + ".R");
            Class[] classes = rCls.getClasses();
            Class desireClass = null;
            for (int i = 0; i < classes.length; ++i) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];
                    break;
                }
            }

            if (desireClass != null) {
                id = desireClass.getField(name).getInt(desireClass);
            }
        } catch (ClassNotFoundException e) {
            Logger.e(e);
        } catch (IllegalArgumentException e) {
            Logger.e(e);
        } catch (SecurityException e) {
            Logger.e(e);
        } catch (IllegalAccessException e) {
            Logger.e(e);
        } catch (NoSuchFieldException e) {
            Logger.e(e);
        } catch (Exception e) {
            Logger.e(e);
        }

        return id;
    }

    /**
     * 获取资源id
     *
     * @param rCls R Class, 如: R.string.class or R.layout.class
     * @param name 资源名
     * @return
     */
    @SuppressWarnings("rawtypes")
    public final static int getResourceId(@NonNull Class rCls, @NonNull String name) {
        if (StringUtils.isNullOrWhiteSpace(name))
            return 0;

        try {
            Field f = rCls.getField(name);
            if (f == null)
                return 0;

            return f.getInt(null);
        } catch (Exception e) {
        }

        return 0;
    }

    /**
     * 获取 Layout ResourceId
     *
     * @param layoutRCls R.layout.class
     * @param name       资源名
     * @return
     */
    @SuppressWarnings("rawtypes")
    public final static int getLayoutResourceId(@NonNull Class layoutRCls, @NonNull String name) {
        return getResourceId(layoutRCls, name);
    }

    /**
     * 获取 Layout ResourceId
     *
     * @param name 资源名
     * @return
     */
    @Deprecated
    public final static int getLayoutResourceId(@NonNull String name) {
        return getLayoutResourceId(R.layout.class, name);
    }

    /**
     * 获取 String ResourceId
     *
     * @param stringRCls R.string.class
     * @param name       资源名
     * @return
     */
    @SuppressWarnings("rawtypes")
    public final static int getStringResourceId(@NonNull Class stringRCls, @NonNull String name) {
        int id = -1;
        if (StringUtils.isNullOrWhiteSpace(name))
            return id;

        try {
            Field f = stringRCls.getField(name);
            if (f == null)
                return id;

            return f.getInt(null);
        } catch (Exception e) {
        }

        return id;
    }

    /**
     * 获取 String ResourceId
     *
     * @param name 资源名
     * @return
     */
    @Deprecated
    public final static int getStringResourceId(@NonNull String name) {
        int id = -1;
        if (StringUtils.isNullOrWhiteSpace(name))
            return id;

        try {
            Field f = R.string.class.getField(name);
            if (f == null)
                return id;

            return f.getInt(null);
        } catch (Exception e) {
        }

        return id;
    }

    /**
     * 获取 Drawable资源id
     *
     * @param drawableRCls R.drawable.class
     * @param name         资源名
     * @return
     */
    @SuppressWarnings("rawtypes")
    public final static int getDrawableResourceId(@NonNull Class drawableRCls, @NonNull String name) {
        return getResourceId(drawableRCls, name);
    }

    /**
     * 获取 Drawable资源id
     *
     * @param name 资源名
     * @return
     */
    @Deprecated
    public final static int getDrawableResourceId(@NonNull String name) {
        return getDrawableResourceId(R.drawable.class, name);
    }

    /**
     * 获取 Raw资源id
     *
     * @param rawRcls R.drawable.class
     * @param name    资源名
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static int getRawIdByKey(@NonNull Class rawRcls, @NonNull String name) {
        return getResourceId(rawRcls, name);
    }

    /**
     * 获取 Raw资源id
     *
     * @param name 资源名
     * @return
     */
    @Deprecated
    public static int getRawIdByKey(@NonNull String name) {
        return getResourceId(R.raw.class, name);
    }

    /**
     * 获取字符对象
     *
     * @param context
     * @param name         资源名
     * @param defaultValue 默认值
     * @return
     */
    @Deprecated
    public static String getStringByKey(Context context, String name, String defaultValue) {
        return getStringByKey(context, R.string.class, name, defaultValue);
    }

    public static String getString(@NonNull Context context, @StringRes int resId, String defaultValue) {
        try {
            return context.getString(resId);
        } catch (Exception e) {
        }

        return defaultValue;
    }

    /**
     * 获取字符对象
     *
     * @param context
     * @param name      资源名
     * @param defaultId 默认值资源id
     * @return
     */
    @Deprecated
    public static String getStringByKey(Context context, String name, @StringRes int defaultId) {
        return getStringByKey(context, R.string.class, name, defaultId);
    }

    /**
     * 获取字符对象
     *
     * @param context
     * @param stringRCls   R.string.class
     * @param name         资源名
     * @param defaultValue 默认值
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String getStringByKey(Context context, Class stringRCls, String name, String defaultValue) {
        try {
            Field f = stringRCls.getField(name);
            if (f == null) {
                return defaultValue;
            }

            int id = f.getInt(null);
            return context.getResources().getString(id);
        } catch (Exception e) {
        }

        return defaultValue;
    }

    /**
     * 获取字符对象
     *
     * @param context
     * @param stringRCls R.string.class
     * @param name       资源名,形如R.string.name
     * @param defaultId  默认值资源id
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String getStringByKey(Context context, Class stringRCls, String name, @StringRes int defaultId) {
        String s = null != context ? context.getString(defaultId) : "";
        try {
            Field f = stringRCls.getField(name);
            if (f == null)
                return s;

            int id = f.getInt(null);
            return context.getResources().getString(id);
        } catch (Exception e) {
        }

        return s;
    }

    /**
     * 获取整型对象
     *
     * @param context
     * @param integerRCls  R.integer.class
     * @param name         资源名
     * @param defaultValue 默认值
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Integer getIntegerByKey(Context context, Class integerRCls, String name, Integer defaultValue) {
        Integer v = defaultValue;
        try {
            Field f = integerRCls.getField(name);
            if (f == null)
                return v;
            int id = f.getInt(null);
            return context.getResources().getInteger(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }

    /**
     * 获取整型对象
     *
     * @param context
     * @param name         资源名
     * @param defaultValue 默认值
     * @return
     */
    @Deprecated
    public static Integer getIntegerByKey(Context context, String name, Integer defaultValue) {
        return getIntegerByKey(context, R.integer.class, name, defaultValue);
    }

    /**
     * 获取图片
     *
     * @param context
     * @param drawableRCls R.drawable.class
     * @param name         资源名
     * @return
     */
    @SuppressWarnings("rawtypes")
    public final static Drawable getDrawable(Context context, Class drawableRCls, String name) {
        Drawable s = null;
        try {
            Field f = drawableRCls.getField(name);
            if (f == null)
                return s;
            int id = f.getInt(null);

            return context.getResources().getDrawable(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return s;
    }

    /**
     * 获取图片
     *
     * @param context
     * @param name    资源名
     * @return
     */
    @Deprecated
    public final static Drawable getDrawable(Context context, String name) {
        return getDrawable(context, R.drawable.class, name);
    }

}
