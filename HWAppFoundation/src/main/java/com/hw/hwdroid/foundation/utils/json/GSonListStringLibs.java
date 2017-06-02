/**
 * @(#)GSonStringLibs.java 2014-4-8 Copyright 2014 . All
 *                         rights reserved.
 */

package com.hw.hwdroid.foundation.utils.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * @author chenjian
 * @date 2014-4-8
 */

public class GSonListStringLibs {

    /**
     * to json
     *
     * @param list
     * @return
     */
    public static String toGson(List<String> list) {
        try {
            return new Gson().toJson(list);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     */
    public static List<String> toList(String gson) {
        try {
            return new Gson().fromJson(gson, new TypeToken<List<String>>() {
            }.getType());
        } catch (Exception e) {
            return null;
        }
    }

}
