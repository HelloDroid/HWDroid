package com.hw.hwdroid.foundation.utils.storage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.hw.hwdroid.foundation.utils.StringUtils;
import com.hw.hwdroid.foundation.utils.json.GsonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 本地数据缓存
 *
 * @author chenjian
 * @date 2013-9-5
 */

public abstract class BaseStorage {

    protected Editor mEditor;
    protected Context mContext;
    protected SharedPreferences mPreferences;

    public BaseStorage(Context context) {
        this(context, "");
    }

    // Context.MODE_PRIVATE
    public BaseStorage(Context context, String fileName, int mode) {
        mContext = context;
        initEditor(mContext, fileName, mode);
    }

    public BaseStorage(Context context, String fileName) {
        mContext = context;
        initEditor(mContext, fileName);
    }

    public void initEditor(Context context, String fileName) {
        if (null == context || null == fileName || 0 == fileName.length()) {
            return;
        }

        // Context.MODE_WORLD_WRITEABLE
        mPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public void initEditor(Context context, String fileName, int mode) {
        if (null == context || null == fileName || 0 == fileName.length()) {
            return;
        }

        mPreferences = context.getSharedPreferences(fileName, mode);
    }

    /**
     * put value
     *
     * @param key
     * @param obj
     */
    public void setStorage(String key, Object obj) {
        if (obj == null) {
            return;
        }

        openEditor();

        try {
            if (obj instanceof Integer || obj instanceof Short) {
                setInt(key, (Integer) obj);
            } else if (obj instanceof Long) {
                setLong(key, (Long) obj);
            } else if (obj instanceof Float || obj instanceof Double) {
                setFloat(key, (Float) obj);
            } else if (obj instanceof Boolean) {
                setBoolean(key, (Boolean) obj);
            } else if (obj instanceof String) {
                setString(key, (String) obj);
            } else if (obj instanceof Object) {
                setString(key, GsonUtils.toJson(obj));
            }
        } catch (Exception e) {
        }

        commit();
    }

    /**
     * opent editor
     */
    public void openEditor() {
        if (null == mPreferences) {
            return;
        }

        if (mEditor == null) {
            mEditor = mPreferences.edit();
        }
    }

    /**
     * commit modify
     */
    public void commit() {
        if (mEditor == null) {
            return;
        }

        mEditor.commit();
    }

    /**
     * close editor
     */
    public void closeEditor() {
        if (mEditor == null) {
            return;
        }

        mEditor.commit();
        mEditor = null;
    }

    public boolean isClosed() {
        if (mEditor != null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * release the instance
     */
    public void release() {
        if (!isClosed()) {
            closeEditor();
        }
    }

    public void setInt(String key, int value) {
        if (StringUtils.isNullOrWhiteSpace(key) || null == mPreferences) {
            return;
        }

        if (null == mEditor) {
            mEditor = mPreferences.edit();
        }

        mEditor.putInt(key, value);
    }

    public void setLong(String key, long value) {
        if (StringUtils.isNullOrWhiteSpace(key) || null == mPreferences) {
            return;
        }

        if (null == mEditor) {
            mEditor = mPreferences.edit();
        }

        mEditor.putLong(key, value);
    }

    public void setFloat(String key, float value) {
        if (StringUtils.isNullOrWhiteSpace(key) || null == mPreferences) {
            return;
        }

        if (null == mEditor) {
            mEditor = mPreferences.edit();
        }

        mEditor.putFloat(key, value);
    }

    public void setString(String key, String value) {
        if (StringUtils.isNullOrWhiteSpace(key) || null == mPreferences) {
            return;
        }

        if (null == mEditor) {
            mEditor = mPreferences.edit();
        }

        mEditor.putString(key, value);
    }

    public void setBoolean(String key, boolean value) {
        if (StringUtils.isNullOrWhiteSpace(key) || null == mPreferences) {
            return;
        }

        if (null == mEditor) {
            mEditor = mPreferences.edit();
        }

        mEditor.putBoolean(key, value);
    }

    public boolean contains(String key) {
        if (StringUtils.isNullOrWhiteSpace(key) || null == mPreferences) {
            return false;
        }

        if (null == mPreferences) {
            return false;
        }

        return mPreferences.contains(key);
    }

    public void remove(String key) {
        if (StringUtils.isNullOrWhiteSpace(key) || null == mPreferences) {
            return;
        }

        if (null == mEditor) {
            mEditor = mPreferences.edit();
        }

        mEditor.remove(key);
    }

    public String getString(String key, String defValue) {
        String value = defValue;
        if (StringUtils.isNullOrWhiteSpace(key) || mPreferences == null) {
            return value;
        }

        if (mPreferences.contains(key)) {
            value = mPreferences.getString(key, defValue);
        }

        return value;
    }

    public int getInt(String key, int defValue) {
        int value = defValue;
        if (StringUtils.isNullOrWhiteSpace(key) || mPreferences == null)
            return value;

        if (mPreferences.contains(key)) {
            value = mPreferences.getInt(key, defValue);
        }

        return value;
    }

    public long getLong(String key, long defValue) {
        long value = defValue;
        if (StringUtils.isNullOrWhiteSpace(key) || mPreferences == null)
            return value;

        if (mPreferences.contains(key)) {
            value = mPreferences.getLong(key, defValue);
        }

        return value;
    }

    public float getFloat(String key, float defValue) {
        float value = defValue;
        if (StringUtils.isNullOrWhiteSpace(key) || mPreferences == null) {
            return value;
        }

        if (mPreferences.contains(key)) {
            value = mPreferences.getFloat(key, defValue);
        }

        return value;
    }

    public boolean getBoolean(String key, boolean defValue) {
        boolean value = defValue;
        if (StringUtils.isNullOrWhiteSpace(key) || mPreferences == null)
            return value;

        if (mPreferences.contains(key)) {
            value = mPreferences.getBoolean(key, defValue);
        }

        return value;
    }

    /**
     * 批量添加
     *
     * @param map
     */
    public void batchValues(Map<String, Object> map) {
        if (null == map || map.isEmpty() || null == mPreferences) {
            return;
        }

        Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        if (null == it)
            return;

        if (mEditor == null) {
            mEditor = mPreferences.edit();
        }

        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            if (null == entry) {
                continue;
            }

            String key = entry.getKey();
            Object obj = entry.getValue();

            if (null == key || key.length() == 0 || null == obj) {
                continue;
            }

            try {
                if (obj instanceof Integer) {
                    mEditor.putInt(key, (Integer) obj);
                } else if (obj instanceof Long) {
                    mEditor.putLong(key, (Long) obj);
                } else if (obj instanceof Float) {
                    mEditor.putFloat(key, (Float) obj);
                } else if (obj instanceof Boolean) {
                    mEditor.putBoolean(key, (Boolean) obj);
                } else if (obj instanceof String) {
                    mEditor.putString(key, (String) obj);
                } else if (obj instanceof Object) {
                    mEditor.putString(key, GsonUtils.toJson(obj));
                }
            } catch (Exception e) {
            }
        }

        mEditor.commit();
    }

    public Map<String, ?> getAll() {
        if (null == mPreferences) {
            return null;
        }

        return mPreferences.getAll();
    }

    @SuppressWarnings("unchecked")
    public List<Object> getList() {
        Map<String, Object> map = (Map<String, Object>) getAll();
        if (null == map || map.isEmpty()) {
            return null;
        }

        List<Object> list = new ArrayList<Object>();
        Iterator<Entry<String, Object>> itMap = map.entrySet().iterator();
        if (itMap == null) {
            return null;
        }

        while (itMap.hasNext()) {
            Entry<String, Object> entry = itMap.next();
            if (null == entry)
                continue;
            list.add(entry.getValue());
        }

        return list;
    }

    public synchronized static void setStorage(Editor editor, String key, Object obj) {
        if (obj == null || editor == null || StringUtils.isNullOrWhiteSpace(key)) {
            return;
        }

        try {
            if (obj instanceof Integer || obj instanceof Short) {
                editor.putInt(key, (Integer) obj);
            } else if (obj instanceof Long) {
                editor.putLong(key, (Long) obj);
            } else if (obj instanceof Float || obj instanceof Double) {
                editor.putFloat(key, (Float) obj);
            } else if (obj instanceof Boolean) {
                editor.putBoolean(key, (Boolean) obj);
            } else if (obj instanceof String) {
                editor.putString(key, (String) obj);
            } else if (obj instanceof Object) {
                editor.putString(key, GsonUtils.toJson(obj));
            }
        } catch (Exception e) {
        }

        editor.commit();
    }

    /**
     * 存储登录信息
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setStorage(Context context, String key, Object value) {
        if (StringUtils.isNullOrWhiteSpace(key)) {
            return;
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        setStorage(sp.edit(), key, value);
    }

    public <T> T getData(@NonNull String key, Class<T> cls) {
        String v = getString(key, "");

        if (StringUtils.isNullOrWhiteSpace(v)) {
            return null;
        }

        return new GsonUtils().fromJson(v, cls);
    }

    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(key, defValue);
    }

    public static long getLong(Context context, String key, long defValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getLong(key, defValue);
    }

    public static String getLong(Context context, String key, String defValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key, defValue);
    }

    public static float getFloat(Context context, String key, float defValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getFloat(key, defValue);
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(key, defValue);
    }

    /**
     * 删除SharedPrefs文件
     *
     * @param fileName
     * @Description
     */
    @SuppressLint("SdCardPath")
    public static void delFile(Context context, String fileName) {
        try {
            File file = new File("/data/data/" + context.getPackageName() + "/shared_prefs", fileName);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
        }
    }

}
