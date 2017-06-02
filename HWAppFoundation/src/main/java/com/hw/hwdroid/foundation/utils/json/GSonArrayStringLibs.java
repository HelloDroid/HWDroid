/**
 * @(#)GSonArrayNumberList.java 2014-4-8 Copyright 2014 . All
 *                              rights reserved.
 */

package com.hw.hwdroid.foundation.utils.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * @author chenjian
 * @date 2014-4-8
 */

public class GSonArrayStringLibs {

    /**
     * to json
     *
     * @param arr
     * @return
     */
    public static String toGson(String[] arr) {
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
    public static String[] toArray(String gson) {
        try {
            return new Gson().fromJson(gson, new TypeToken<String[]>() {
            }.getType());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * to list
     *
     * @param gson
     * @return
     */
    public static List<String> toList(String gson) {
        try {
            return new Gson().fromJson(gson, new TypeToken<String>() {
            }.getType());
        } catch (Exception e) {
            return null;
        }
    }

}
