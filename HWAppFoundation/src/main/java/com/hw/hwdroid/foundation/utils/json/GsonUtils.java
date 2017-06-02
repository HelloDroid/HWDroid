package com.hw.hwdroid.foundation.utils.json;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by chen.jiana on 2015/9/11.
 */
public class GsonUtils {


    /**
     * to JSON String
     *
     * @param obj
     * @return
     */
    // @Nullable
    public static String toJson(Object obj) {
        try {
            return new Gson().toJson(obj);
        } catch (Exception e) {
            Logger.e(e);
            return "";
        }
    }


    /**
     * json字符串转成对象
     *
     * @param gSon
     * @param type
     * @return
     */
    // @Nullable
    public static <T> T fromJson(String gSon, Type type) {
        try {
            return new Gson().fromJson(gSon, type);
        } catch (Exception e) {
            Logger.e(e);
            return null;
        }
    }


    /**
     * gson反序列化，Gson提供了fromJson()方法来实现从Json相关对象到java实体的方法
     *
     * @param gSon
     * @param cls
     * @param <T>
     * @return
     */
    // @Nullable
    public static <T> T fromJson(String gSon, Class<T> cls) {
        try {
            return new Gson().fromJson(gSon, cls);
        } catch (Exception e) {
            Logger.e(e);
            return null;
        }
    }

    public static List<String> gsonToList(String gSon) {
        try {
            return new Gson().fromJson(gSon, new TypeToken<List<String>>() {
            }.getType());
        } catch (Exception e) {
            return null;
        }
    }

}
