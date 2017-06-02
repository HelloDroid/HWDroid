package com.hw.hwdroid.foundation.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取字符串中不重复的最大字符串长度
 *
 * @author chenj
 * @date 2015-4-23
 */
public class LongestSubseqWithUniqueChars {

    public static class LongestNodupSubstring {

        public int longestLen;
        public List<String> longestSubstrings;
    }

    /**
     * 获取字符串中不重复的最大字符串
     *
     * @param string
     */
    public static LongestNodupSubstring longestNodupSubstring(String string) {
        int len = string.length();

        Map<Character, Integer> cursor = new HashMap<Character, Integer>();
        cursor.put(string.charAt(0), 1);

        int[] lengthAt = new int[string.length()];
        lengthAt[0] = 1;
        int max = 1;

        // abcbec
        for (int i = 1; i < len; i++) {
            char c = string.charAt(i);

            if (cursor.containsKey(c)) {
                // c的上一个位置为cursor.get(c)
                // 从上一个位置到新位置的长度：i - cursor.get(c)
                lengthAt[i] = Math.min(lengthAt[i - 1] + 1, i - cursor.get(c));
            } else {
                lengthAt[i] = lengthAt[i - 1] + 1;
            }

            max = Math.max(max, lengthAt[i]);
            cursor.put(c, i);
        }

        LongestNodupSubstring longestNodupSubstring = new LongestNodupSubstring();

        List<String> longestSubstrings = new ArrayList<String>();

        for (int i = 0; i < len; i++) {
            if (max != lengthAt[i]) {
                continue;
            }

            try {
                longestSubstrings.add(string.substring(i - max + 1, i + 1));
            } catch (Exception e) {
            }
        }

        longestNodupSubstring.longestLen = max;
        longestNodupSubstring.longestSubstrings = longestSubstrings;

        return longestNodupSubstring;
    }

    /**
     * 获取字符串中不重复的最大字符串的长度
     *
     * @param string
     * @return
     */
    public static int longestLenNodupSubstring(String string) {
        int len = string.length();

        Map<Character, Integer> cursor = new HashMap<Character, Integer>();
        cursor.put(string.charAt(0), 1);

        int[] lengthAt = new int[string.length()];
        lengthAt[0] = 1;
        int max = 1;

        // abcbec
        for (int i = 1; i < len; i++) {
            char c = string.charAt(i);

            if (cursor.containsKey(c)) {
                // c的上一个位置为cursor.get(c)
                // 从上一个位置到新位置的长度：i - cursor.get(c)
                lengthAt[i] = Math.min(lengthAt[i - 1] + 1, i - cursor.get(c));
            } else {
                lengthAt[i] = lengthAt[i - 1] + 1;
            }

            max = Math.max(max, lengthAt[i]);
            cursor.put(c, i);
        }

        return max;
    }

    /**
     * @param str
     * @return
     */
    public static int solve(String str) {
        int n = str.length();
        int[] prefixlen = new int[n + 1];
        prefixlen[n] = 0;

        int[] maxlenstart = new int[n + 1];
        maxlenstart[n] = n;

        int[] maxlenend = new int[n + 1];
        maxlenend[n] = n - 1;

        for (int i = n - 1; i >= 0; i--) {
            char c = str.charAt(i);
            // caculate prefixlen[i] by prefixlen[i+1]
            int j = 0, k;
            for (j = 0, k = i + 1; j < prefixlen[i + 1]; j++, k++) {
                if (c == str.charAt(k)) {
                    break;
                }
            }
            prefixlen[i] = j + 1;

            // caculate maxlenstart[i] and maxlenend[i]
            maxlenstart[i] = maxlenstart[i + 1];
            maxlenend[i] = maxlenend[i + 1];
            if (maxlenstart[i + 1] == i + 1) {
                if (prefixlen[i] == prefixlen[i + 1] + 1) {
                    maxlenstart[i] = i;
                }
            } else {
                // update the max len for i...n-1
                if (maxlenend[i] - maxlenstart[i] + 1 < prefixlen[i]) {
                    maxlenstart[i] = i;
                    maxlenend[i] = i + prefixlen[i] - 1;
                }
            }
        }
        return maxlenend[0] - maxlenstart[0] + 1;
    }

    public static void main(String[] args) {
        String str = "abcbec";
        int maxlen = LongestSubseqWithUniqueChars.solve(str);
        LongestNodupSubstring longestNodupSubstring = longestNodupSubstring(str);
        System.out.println(longestNodupSubstring.longestLen);
        System.out.format("max len of substring with unique chars for string \"%s\" = %d.%n", str, maxlen);

        str = "adabcbec";
        maxlen = LongestSubseqWithUniqueChars.solve(str);
        longestNodupSubstring = longestNodupSubstring(str);
        System.out.println(longestNodupSubstring.longestLen);
        System.out.format("max len of substring with unique chars for string \"%s\" = %d.%n", str, maxlen);

        str = "abadadabbc";
        maxlen = LongestSubseqWithUniqueChars.solve(str);
        longestNodupSubstring = longestNodupSubstring(str);
        System.out.println(longestNodupSubstring.longestLen);
        System.out.format("max len of substring with unique chars for string \"%s\" = %d.%n", str, maxlen);

        str = "ffdeefghff";
        maxlen = LongestSubseqWithUniqueChars.solve(str);
        longestNodupSubstring = longestNodupSubstring(str);
        System.out.println(longestNodupSubstring.longestLen);
        System.out.format("max len of substring with unique chars for string \"%s\" = %d.%n", str, maxlen);

        str = "abcbecghijkl";
        maxlen = LongestSubseqWithUniqueChars.solve(str);
        longestNodupSubstring = longestNodupSubstring(str);
        System.out.println(longestNodupSubstring.longestLen);
        System.out.format("max len of substring with unique chars for string \"%s\" = %d.%n", str, maxlen);
    }

}
