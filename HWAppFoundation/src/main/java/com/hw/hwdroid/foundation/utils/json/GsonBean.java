/**
 *
 */
package com.hw.hwdroid.foundation.utils.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Google Json Bean
 *
 * @author chenj
 * @date 2014-12-4
 */
public class GsonBean {

    /**
     * Json格式字符串
     *
     * @return
     */
    public String toJson() {
        return new Gson().toJson(this);
    }

    /**
     * gson反序列化，Gson提供了fromJson()方法来实现从Json相关对象到java实体的方法。
     */
    public GsonBean fromJson(String gson) {
        return new Gson().fromJson(gson, getClass());
    }

    public GsonBean fromJson2(String gson) {
        return new Gson().fromJson(gson, new TypeToken<GsonBean>() {
        }.getType());
    }

}
