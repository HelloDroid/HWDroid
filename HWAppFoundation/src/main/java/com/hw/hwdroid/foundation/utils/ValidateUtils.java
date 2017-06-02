package com.hw.hwdroid.foundation.utils;

public class ValidateUtils {
    /*************************************************************************
     *
     * 以下方法用作参考
     *
     *************************************************************************/

    /**
     * 判断是否是表情的标志，<\1>或者<\45>,目前数字为0-55
     * 注意：本方法用作参考
     *
     * @param num
     * @return
     */
    @Deprecated
    public static boolean isFace(String num) {
        String checkStr = "^\\<\\\\([0-9]|[1-4][0-9]|[5][0-5])\\>$";
        return MatcherUtils.matches(checkStr, num);
    }

    /**
     * TL规定，200-500为自定义表情，500+为图片 ;这里统一判断是否是图片的标志，<\200>,<\300>,<\503>目前数字为大于200
     * 注意：本方法用作参考
     *
     * @param num
     * @return
     */
    @Deprecated
    public static boolean isPic(String num) {
        String checkStr = "^\\<\\\\([1-9][0-9]{3,}|[2-9][0-9][0-9])\\>$";
        return MatcherUtils.matches(checkStr, num);
    }

    /**
     * 自定义字符串是否为vconf p2p record 格式为<\\VConfP2PRecord>......<\\VConfP2PRecord>
     * 以<\\VConfP2PRecord>开头和结尾
     * <p>
     * 注意：本方法用作参考
     *
     * @param str
     * @return
     */
    @Deprecated
    public static boolean isVConfP2PRecord(String str) {
        String checkStr = "^(\\<\\\\VConfP2PRecord\\>).*(<\\\\VConfP2PRecord\\>)$";
        return MatcherUtils.matches(checkStr, str);
    }


    /**
     * E164号(13位纯数字)
     * 注意：本方法用作参考
     *
     * @param e164
     * @return
     */
    @Deprecated
    public static boolean isE164(String e164) {
        String checkE164 = "^[0-9]{13}$";
        return MatcherUtils.matches(checkE164, e164);
    }

    /**
     * 版本(参考)
     *
     * @param version
     * @return
     */
    @Deprecated
    public static boolean isVersion(String version) {
        String checkVersion = "^([0-9]*\\.)*[0-9]*$";
        return MatcherUtils.matches(checkVersion, version);
    }

    /**
     * 判断帐号 $代表结束符
     * chenjian@gmail.com
     * 注意：本方法用作参考
     *
     * @param account
     * @return
     */
    @Deprecated
    private static boolean isGmailAccount(String account) {
        if (StringUtils.isNullOrWhiteSpace(account)) {
            return false;
        }
        String check = "^.*@.*\\.gmail\\.com$";
        String check2 = "^.*@gmail\\.com$";
        return MatcherUtils.matches(check, account) || MatcherUtils.matches(check2, account);
    }

}
