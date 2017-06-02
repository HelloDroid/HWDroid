package com.hw.hwdroid.foundation.utils.http;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hw.hwdroid.foundation.common.org.apache.http.BasicNameValuePair;
import com.hw.hwdroid.foundation.common.org.apache.http.NameValuePair;
import com.orhanobut.logger.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by chenjian on 16/1/20.
 */
public class URLEncodedUtils {

    private static final String PARAMETER_SEPARATOR = "&";
    private static final String NAME_VALUE_SEPARATOR = "=";

    /**
     * 为HttpGet 的 url 方便的添加多个name value 参数
     *
     * @param url
     * @param params
     * @return
     */
    public static String attachHttpGetParams(@NonNull String url, List<BasicNameValuePair> params) {
        return url + "?" + formatParams(params);
    }

    /**
     * 为HttpGet 的 url 方便的添加多个name value 参数
     *
     * @param url
     * @param params
     * @return
     */
    public static String attachHttpGetParams(@NonNull String url, Map params) {
        List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
        if (params != null) {
            for (Object key : params.keySet()) {
                String value = (String) params.get(key);
                if (!TextUtils.isEmpty(value)) {
                    BasicNameValuePair pair = new BasicNameValuePair((String) key, value);
                    list.add(pair);
                }
            }
        }

        return url + "?" + formatParams(list);
    }

    /**
     * 生成http query
     *
     * @param param
     * @return 分别对key和value进行Encode，再使用&进行连接
     */
    public static String formatParams(Bundle param) {
        if (param == null || param.isEmpty()) {
            return "";
        }

        StringBuffer sbBuffer = new StringBuffer();

        int i = 0;
        for (String key : param.keySet()) {
            if (i == 0) {
                sbBuffer.append("");
            } else {
                sbBuffer.append(PARAMETER_SEPARATOR);
            }

            // encode
            sbBuffer.append(encode(key) + NAME_VALUE_SEPARATOR + encode(param.getString(key)));

            i++;
        }

        Logger.d(sbBuffer);

        return sbBuffer.toString();
    }

    /**
     * 格式化属性为字符串
     *
     * @param parameters
     * @return
     */
    public static String formatParams(final List<? extends NameValuePair> parameters) {
        return formatParams(parameters, HttpConstantValues.CHARSET_NAME);
    }

    /**
     * 格式化属性为字符串
     *
     * @param parameters
     * @param encoding
     * @return
     */
    public static String formatParams(final List<? extends NameValuePair> parameters, final String encoding) {
        final StringBuilder result = new StringBuilder();

        for (final NameValuePair parameter : parameters) {
            final String encodedName = encode(parameter.getName(), encoding);
            final String value = parameter.getValue();
            final String encodedValue = value != null ? encode(value, encoding) : "";
            if (result.length() > 0)
                result.append(PARAMETER_SEPARATOR);

            result.append(encodedName);
            result.append(NAME_VALUE_SEPARATOR);
            result.append(encodedValue);
        }
        return result.toString();
    }

    /**
     * encode
     *
     * @param content
     * @return
     */
    public static String encode(final String content) {
        try {
            return URLEncoder.encode(content, HttpConstantValues.CHARSET_NAME);
        } catch (UnsupportedEncodingException problem) {
            return content;
        }
    }

    /**
     * encode
     *
     * @param content
     * @param encoding
     * @return
     */
    public static String encode(final String content, final String encoding) {
        try {
            return URLEncoder.encode(content, encoding != null ? encoding : HttpConstantValues.CHARSET_NAME);
        } catch (UnsupportedEncodingException problem) {
            throw new IllegalArgumentException(problem);
        }
    }

    /**
     * 解码
     *
     * @param content
     * @return
     */
    public static String decode(@NonNull final String content) {
        try {
            return decode(content, HttpConstantValues.CHARSET_NAME);
        } catch (Exception e) {
            return content;
        }
    }

    /**
     * 解码
     *
     * @param content
     * @param encoding
     * @return
     */
    public static String decode(@NonNull final String content, final String encoding) {
        try {
            return URLDecoder.decode(content,
                    encoding != null ? encoding : HttpConstantValues.CHARSET_NAME);
        } catch (UnsupportedEncodingException problem) {
            throw new IllegalArgumentException(problem);
        }
    }

}
