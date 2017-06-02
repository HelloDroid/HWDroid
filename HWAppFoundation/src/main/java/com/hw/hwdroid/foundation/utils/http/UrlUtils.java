package com.hw.hwdroid.foundation.utils.http;

import android.net.Uri;

import com.hw.hwdroid.foundation.utils.MatcherUtils;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by ChenJ on 16/4/28.
 */
public class UrlUtils {

    /**
     * url是否有效
     * 参考：OkHttp.Request
     *
     * @param url
     * @return
     */
    public static boolean valid(String url) {
        if (url == null || url.length() == 0) {
            return false;
        }

        // Silently replace websocket URLs with HTTP URLs.
        if (url.regionMatches(true, 0, "ws:", 0, 3)) {
            url = "http:" + url.substring(3);
        } else if (url.regionMatches(true, 0, "wss:", 0, 4)) {
            url = "https:" + url.substring(4);
        }

        return MatcherUtils.isUrl2(url);

        //        HttpUrl parsed = HttpUrl.parse(url);

        //        return parsed != null && url != null;
    }

    public static Set<String> getQueryParameterNames(Uri uri) {
        String query = uri.getEncodedQuery();
        if (query == null) {
            return Collections.emptySet();
        }

        Set<String> names = new LinkedHashSet<>();
        int start = 0;
        do {
            int next = query.indexOf('&', start);
            int end = (next == -1) ? query.length() : next;

            int separator = query.indexOf('=', start);
            if (separator > end || separator == -1) {
                separator = end;
            }

            String name = query.substring(start, separator);
            names.add(Uri.decode(name));

            // Move start to end of name.
            start = end + 1;
        }
        while (start < query.length());

        return Collections.unmodifiableSet(names);
    }

}
