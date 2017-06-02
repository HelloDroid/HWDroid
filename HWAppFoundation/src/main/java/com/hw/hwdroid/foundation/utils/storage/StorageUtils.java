package com.hw.hwdroid.foundation.utils.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.SharedPreferencesCompat;
import android.text.TextUtils;

import com.hw.hwdroid.foundation.utils.StringUtils;
import com.hw.hwdroid.foundation.utils.json.GsonUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.Map;

/**
 * 本地数据缓存工具
 */
public class StorageUtils {

    /**
     * 删除文件
     *
     * @param context
     * @param fileName
     */
    public static void delFile(Context context, String fileName) {
        try {
            File file = new File("/data/data/" + context.getPackageName() + "/shared_prefs", fileName);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
        }
    }

    /**
     * 清除所有数据
     *
     * @param context
     * @param fileName
     */
    public static void clear(@NonNull Context context, @NonNull String fileName) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    /**
     * return Editor
     *
     * @param context
     * @param file
     * @return
     */
    public static SharedPreferences.Editor getEditor(@NonNull Context context, String file) {
        try {
            return context.getSharedPreferences(file, Context.MODE_PRIVATE).edit();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * remove key
     *
     * @param context
     * @param file
     * @param key
     */
    public static void remove(@NonNull Context context, @NonNull String file, @NonNull String key) {
        try {
            if (null == context || TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
                return;
            }

            SharedPreferences.Editor editor = context.getSharedPreferences(file, Context.MODE_PRIVATE).edit();

            editor.remove(key);
            editor.commit();
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    /**
     * put values
     *
     * @param context
     * @param file
     * @param values
     */
    public static void setStorage(@NonNull Context context, @NonNull String file, Bundle values) {
        try {
            if (null == context || TextUtils.isEmpty(file)) {
                return;
            }


            SharedPreferences.Editor editor = context.getSharedPreferences(file, Context.MODE_PRIVATE).edit();


            for (String key : values.keySet()) {
                Object value = values.get(key);

                if (value == null) {
                    editor.remove(key);
                } else {
                    if (value instanceof Integer || value instanceof Short) {
                        editor.putInt(key, (Integer) value);
                    } else if (value instanceof Long) {
                        editor.putLong(key, (Long) value);
                    } else if (value instanceof Float || value instanceof Double) {
                        editor.putFloat(key, (Float) value);
                    } else if (value instanceof Boolean) {
                        editor.putBoolean(key, (Boolean) value);
                    } else if (value instanceof String) {
                        editor.putString(key, (String) value);
                    } else if (value instanceof Object) {
                        editor.putString(key, GsonUtils.toJson(value));
                    }
                }
            }
            editor.commit();
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    /**
     * put values
     *
     * @param context
     * @param file
     * @param values
     */
    public static void setStorage(@NonNull Context context, @NonNull String file, Map<String, Object> values) {
        try {
            if (null == context || TextUtils.isEmpty(file)) {
                return;
            }


            SharedPreferences.Editor editor = context.getSharedPreferences(file, Context.MODE_PRIVATE).edit();


            for (String key : values.keySet()) {
                Object value = values.get(key);

                if (value == null) {
                    editor.remove(key);
                } else {
                    if (value instanceof Integer || value instanceof Short) {
                        editor.putInt(key, (Integer) value);
                    } else if (value instanceof Long) {
                        editor.putLong(key, (Long) value);
                    } else if (value instanceof Float || value instanceof Double) {
                        editor.putFloat(key, (Float) value);
                    } else if (value instanceof Boolean) {
                        editor.putBoolean(key, (Boolean) value);
                    } else if (value instanceof String) {
                        editor.putString(key, (String) value);
                    } else if (value instanceof Object) {
                        editor.putString(key, GsonUtils.toJson(value));
                    }
                }
            }
            editor.commit();
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    /**
     * put value
     *
     * @param context
     * @param file
     * @param key
     * @param value
     */
    public static void setStorage(@NonNull Context context, @NonNull String file, @NonNull String key, Object value) {
        try {
            if (null == context || TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
                return;
            }

            SharedPreferences.Editor editor = context.getSharedPreferences(file, Context.MODE_PRIVATE).edit();

            if (value == null) {
                editor.remove(key);
            } else {
                if (value instanceof Integer || value instanceof Short) {
                    editor.putInt(key, (Integer) value);
                } else if (value instanceof Long) {
                    editor.putLong(key, (Long) value);
                } else if (value instanceof Float || value instanceof Double) {
                    editor.putFloat(key, (Float) value);
                } else if (value instanceof Boolean) {
                    editor.putBoolean(key, (Boolean) value);
                } else if (value instanceof String) {
                    editor.putString(key, (String) value);
                } else if (value instanceof Object) {
                    editor.putString(key, GsonUtils.toJson(value));
                }
            }

            editor.commit();
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    public static <T> T getData(@NonNull Context context, @NonNull String file, @NonNull String key, Class<T> cls) {
        String v = getString(context, file, key, "");

        if (StringUtils.isNullOrWhiteSpace(v)) {
            return null;
        }

        return new GsonUtils().fromJson(v, cls);
    }


    /**
     * put String
     *
     * @param context
     * @param file
     * @param key
     * @param value
     */
    public static void setString(@NonNull Context context, @NonNull String file, @NonNull String key, String value) {
        try {
            if (null == context || TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
                return;
            }

            SharedPreferences.Editor editor = context.getSharedPreferences(file, Context.MODE_PRIVATE).edit();
            //            editor.remove(key);

            if (TextUtils.isEmpty(value)) {
                editor.remove(key);
            } else {
                editor.putString(key, value);
            }

            editor.commit();
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    /**
     * get String
     *
     * @param context
     * @param file
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(@NonNull Context context, @NonNull String file, @NonNull String key, String defValue) {
        try {
            if (null == defValue) {
                defValue = "";
            }

            if (null == context || TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
                return defValue;
            }

            SharedPreferences sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);

            if (!sp.contains(key)) {
                return defValue;
            }

            return sp.getString(key, defValue);
        } catch (Exception e) {
            Logger.e(e);
        }

        return defValue;
    }

    /**
     * put int
     *
     * @param context
     * @param file
     * @param key
     * @param value
     */
    public static void setInt(@NonNull Context context, @NonNull String file, @NonNull String key, int value) {
        try {
            if (null == context || TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
                return;
            }

            SharedPreferences.Editor editor = context.getSharedPreferences(file, Context.MODE_PRIVATE).edit();
            editor.putInt(key, value);
            editor.commit();
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    /**
     * get int value
     *
     * @param context
     * @param file
     * @param key
     * @param defValue
     * @return
     */
    public static int getInt(@NonNull Context context, @NonNull String file, @NonNull String key, int defValue) {
        try {
            if (null == context || TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
                return defValue;
            }

            SharedPreferences sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);

            if (!sp.contains(key)) {
                return defValue;
            }

            return sp.getInt(key, defValue);
        } catch (Exception e) {
            Logger.e(e);
        }

        return defValue;
    }

    /**
     * put long
     *
     * @param context
     * @param file
     * @param key
     * @param value
     */
    public static void setLong(@NonNull Context context, @NonNull String file, @NonNull String key, long value) {
        try {
            if (null == context || TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
                return;
            }

            SharedPreferences.Editor editor = context.getSharedPreferences(file, Context.MODE_PRIVATE).edit();
            editor.putLong(key, value);
            editor.commit();
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    /**
     * get long value
     *
     * @param context
     * @param file
     * @param key
     * @param defValue
     * @return
     */
    public static long getLong(@NonNull Context context, @NonNull String file, @NonNull String key, long defValue) {
        try {
            if (null == context || TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
                return defValue;
            }

            SharedPreferences sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);

            if (!sp.contains(key)) {
                return defValue;
            }

            return sp.getLong(key, defValue);
        } catch (Exception e) {
            Logger.e(e);
        }

        return defValue;
    }

    /**
     * put float
     *
     * @param context
     * @param file
     * @param key
     * @param value
     */
    public static void setFloat(@NonNull Context context, @NonNull String file, @NonNull String key, float value) {
        try {
            if (null == context || TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
                return;
            }

            SharedPreferences.Editor editor = context.getSharedPreferences(file, Context.MODE_PRIVATE).edit();
            editor.putFloat(key, value);
            editor.commit();
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    /**
     * get float value
     *
     * @param context
     * @param file
     * @param key
     * @param defValue
     * @return
     */
    public static float getFloat(@NonNull Context context, @NonNull String file, @NonNull String key, float defValue) {
        try {
            if (null == context || TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
                return defValue;
            }

            SharedPreferences sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);

            if (!sp.contains(key)) {
                return defValue;
            }

            return sp.getFloat(key, defValue);
        } catch (Exception e) {
            Logger.e(e);
        }

        return defValue;
    }


    /**
     * put boolean
     *
     * @param context
     * @param file
     * @param key
     * @param value
     */
    public static void setBoolean(@NonNull Context context, @NonNull String file, @NonNull String key, boolean value) {
        try {
            if (null == context || TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
                return;
            }

            SharedPreferences.Editor editor = context.getSharedPreferences(file, Context.MODE_PRIVATE).edit();
            editor.putBoolean(key, value);
            editor.commit();
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    /**
     * return boolean
     *
     * @param context
     * @param file
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getBoolean(@NonNull Context context, @NonNull String file, @NonNull String key, boolean defValue) {
        try {
            if (null == context || TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
                return defValue;
            }

            SharedPreferences sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);

            if (!sp.contains(key))
                return defValue;

            return sp.getBoolean(key, defValue);
        } catch (Exception e) {
            Logger.e(e);
        }
        return defValue;
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @param fileName
     * @return
     */
    public static Map<String, ?> getAll(@NonNull Context context, @NonNull String fileName) {
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }
}
