package com.hw.hwdroid.foundation.utils.json;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Json Utils
 */
public class JSONUtils {

    /**
     * get JSONObject
     *
     * @param jsonObject
     * @param key
     * @return
     */
    public static JSONObject getJSONObject(@NonNull JSONObject jsonObject, @NonNull String key) {
        try {
            if (!jsonObject.isNull(key)) {
                return jsonObject.getJSONObject(key);
            }
        } catch (Exception e) {
            Logger.e(e);
        }

        return null;
    }

    /**
     * get JSONArray
     *
     * @param jsonObject
     * @param key
     * @return
     */
    public static JSONArray getJSONArray(@NonNull JSONObject jsonObject, @NonNull String key) {
        try {
            if (!jsonObject.isNull(key)) {
                return jsonObject.getJSONArray(key);
            }
        } catch (Exception e) {
            Logger.e(e);
        }
        return null;
    }

    /**
     * return String from JSONObject
     *
     * @param jsonObject
     * @param key
     * @return
     */
    public static String getString(@NonNull JSONObject jsonObject, @NonNull String key) {
        try {
            if (!jsonObject.isNull(key)) {
                return jsonObject.getString(key);
            }
        } catch (Exception e) {
            Logger.e(e);
        }

        return "";
    }

    /**
     * return long from JSONObject
     *
     * @param jsonObject
     * @param key
     * @return
     */
    public static long getLong(@NonNull JSONObject jsonObject, @NonNull String key) {
        try {
            if (!jsonObject.isNull(key)) {
                return jsonObject.getLong(key);
            }
        } catch (Exception e) {
            Logger.e(e);
        }
        return 0;
    }

    /**
     * return integer from JSONObject
     *
     * @param jsonObject
     * @param key
     * @return
     */
    public static int getInt(@NonNull JSONObject jsonObject, @NonNull String key) {
        try {
            if (!jsonObject.isNull(key)) {
                return jsonObject.getInt(key);
            }
        } catch (Exception e) {
            Logger.e(e);
        }
        return 0;
    }

    /**
     * return Double from JSONObject
     *
     * @param jsonObject
     * @param key
     * @return
     */
    public static double getDouble(@NonNull JSONObject jsonObject, @NonNull  String key) {
        try {
            if (!jsonObject.isNull(key)) {
                return jsonObject.getDouble(key);
            }
        } catch (Exception e) {
            Logger.e(e);
        }
        return 0;
    }

    /**
     * return Boolean from JSONObject
     * 
     * @param jsonObject
     * @param key
     * @return
     */
    public static boolean getBoolean(@NonNull JSONObject jsonObject, @NonNull  String key) {
        try {
            if (!jsonObject.isNull(key)) {
                return jsonObject.getBoolean(key);
            }
        } catch (Exception e) {
            Logger.e(e);
        }
        return false;
    }
}
