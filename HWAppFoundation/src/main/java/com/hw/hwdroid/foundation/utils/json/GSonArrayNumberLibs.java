/**
 * @(#)GSonArrayNumberList.java 2014-4-8 Copyright 2014 . All
 * rights reserved.
 */

package com.hw.hwdroid.foundation.utils.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * @author chenjian
 * @date 2014-4-8
 */

public class GSonArrayNumberLibs {

    /**
     * to json
     *
     * @param arr
     * @return
     */
    public static String toGson(Number[] arr) {
        try {
            return new Gson().toJson(arr);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * to array
     *
     * @param gson
     * @return
     */
    public static Number[] toArray(String gson) {
        try {
            return new Gson().fromJson(gson, new TypeToken<Number[]>() {
            }.getType());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param gson
     * @return
     */
    public static List<? extends Number> toList(String gson) {
        try {
            return new Gson().fromJson(gson, new TypeToken<List<? extends Number>>() {
            }.getType());
        } catch (Exception e) {
            return null;
        }
    }
}
