package com.hw.hwdroid.foundation.utils.network;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.hw.hwdroid.foundation.utils.StringUtils;
import com.orhanobut.logger.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * 网络相关工具类
 *
 * @author chenj
 * @date 2013-6-6
 */
public class NetWorkUtils {

    /**
     * Connection Type
     */
    public static class ConnectionType {
        public static int Unknown = 0;

        public static int Ethernet = 1;

        public static int Wifi = 2;

        public static int Unknown_Generation = 3;

        public static int G2 = 4;

        public static int G3 = 5;

        public static int G4 = 6;
    }

    /**
     * 自定义网络类型值
     */
    public static final int C_NET_TYPE_NONE = 0;
    public static final int C_NET_TYPE_2G = 1;
    public static final int C_NET_TYPE_3G = 2;
    public static final int C_NET_TYPE_4G = 3;
    public static final int C_NET_TYPE_OTHER = 4;
    public static final int C_NET_TYPE_WIFI = 5;

    public static final String CTWAP = "ctwap";

    public static final String CMWAP = "cmwap";

    public static final String WAP_3G = "3gwap";

    public static final String UNIWAP = "uniwap";

    // 网络不可用
    public static final int APN_TYPE_DISABLED = -1;

    public static final int APN_TYPE_OTHER = 0;

    // 移动联通wap10.0.0.172
    public static final int APN_TYPE_CM_CU_WAP = 1;

    // 电信wap 10.0.0.200
    public static final int APN_TYPE_CT_WAP = 2;

    // 电信,移动,联通,wifi 等net网络
    public static final int APN_TYPE_NET = 3;


    public static Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

    /**
     * Returns whether the network is available
     * 网络是否可用
     *
     * @param context Context
     * @return
     */
    public static boolean isNetworkAvailable(@NonNull Context context) {
        return null == context ? false : getConnectedNetworkInfo(context) != null;
    }

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(@NonNull Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null ? (networkInfo.isAvailable() && networkInfo.isConnected()) : false;
        } catch (Exception e) {
            Logger.e(e);
            return false;
        }
    }

    /**
     * 返回已连接NetworkInfo
     *
     * @param context
     * @return
     */
    public static NetworkInfo getConnectedNetworkInfo(@NonNull Context context) {
        try {
            if (null == context) {
                return null;
            }

            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] infoArr = connectivity.getAllNetworkInfo();
            for (NetworkInfo info : infoArr) {
                if (info != null && info.getState() == NetworkInfo.State.CONNECTED) {
                    return info;
                }
            }
        } catch (Exception e) {
            Logger.e(e);
        }

        return null;
    }

    /**
     * 判断网络是不是wifi
     *
     * @param context Context
     * @return boolean
     */
    public static boolean isWifiNetwork(Context context) {
        return isNetworkAvailable(context) && (ConnectivityManager.TYPE_WIFI == getNetworkType(context));
    }

    /**
     * 判断网络是不是手机网络，非wifi
     *
     * @param context Context
     * @return boolean
     * @see [类、类#方法、类#成员]
     */
    public static boolean isMobileNetwork(Context context) {
        return isNetworkAvailable(context) && (ConnectivityManager.TYPE_MOBILE == getNetworkType(context));
    }

    /**
     * 获取网络类型
     *
     * @param context Context
     * @return 网络类型
     */
    public static int getNetworkType(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] infoArr = connectivity.getAllNetworkInfo();
            for (NetworkInfo info : infoArr) {
                if (info != null && info.getState() == NetworkInfo.State.CONNECTED) {
                    return info.getType();
                }
            }
        } catch (Exception e) {
            Logger.e(e.toString(), e);
        }

        return -1;
    }

    /**
     * 网络类型
     *
     * @param context
     * @return
     */
    public static int getNetworkType2(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        return netInfo.getType();
    }

    /**
     * 自定义的网络类型
     *
     * @param context
     * @return
     */
    public static int getNetworkCustomType(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isAvailable() || !networkInfo.isConnected()) {
            return 0;
        }


        int nType = networkInfo.getType();

        if (nType != 0 && nType != 0) {
            return C_NET_TYPE_NONE;
        }

        if (nType == 1) {
            return C_NET_TYPE_WIFI;
        }

        int netWorkType;

        // nType == 0
        int subType = networkInfo.getSubtype();
        if (subType != 4 && subType != 1 && subType != 2) {
            if (subType != 3 && subType != 8 && subType != 10 && subType != 9 && subType != 5 && subType != 6 && subType != 3) {
                netWorkType = C_NET_TYPE_OTHER;
            } else {
                netWorkType = C_NET_TYPE_3G;
            }
        } else {
            netWorkType = C_NET_TYPE_2G;
        }

        return netWorkType;
    }

    /**
     * 自定义网络类型名称
     *
     * @param context
     * @return
     */
    public static String getNetworkCustomName(Context context) {
        String name = "NA";
        int flag = getNetworkCustomType(context);
        if (flag == C_NET_TYPE_2G) {
            name = "2G";
        } else if (flag == C_NET_TYPE_3G) {
            name = "3G";
        } else if (flag == C_NET_TYPE_WIFI) {
            name = "wifi";
        }

        return name;
    }

    /**
     * 获取网络类型
     */
    public static String getNetworkTypeInfo(@NonNull Context context) {
        String netTypeStr = "Unknown";

        if (context == null) {
            return netTypeStr;
        }

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (connectivityManager == null || telephonyManager == null) {
                return netTypeStr;
            }

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                int type = networkInfo.getType();
                int tempType = getSwitchedType(type);
                int networkType = telephonyManager.getNetworkType();
                ;
                if (tempType == ConnectivityManager.TYPE_WIFI) {
                    netTypeStr = "WIFI";
                } else if (tempType == ConnectivityManager.TYPE_MOBILE) {
                    switch (networkType) {
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            netTypeStr = "2G";
                            break;
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            netTypeStr = "3G";
                            break;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            netTypeStr = "4G";
                            break;
                    }
                }
            } else {
                netTypeStr = "None";
            }
        } catch (Exception e) {

        }
        return netTypeStr;
    }

    public static String getCarrierName(@NonNull Context context) {
        if (context == null) {
            return "";
        }

        String IMSI = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                IMSI = telephonyManager.getSubscriberId();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        String carrierName = getNetworkProvider(IMSI);
        return carrierName == null ? "" : carrierName;
    }

    private static int getSwitchedType(int type) {
        switch (type) {
            case ConnectivityManager.TYPE_MOBILE:
            case ConnectivityManager.TYPE_MOBILE_DUN:
            case ConnectivityManager.TYPE_MOBILE_HIPRI:
            case ConnectivityManager.TYPE_MOBILE_MMS:
            case ConnectivityManager.TYPE_MOBILE_SUPL:
                return ConnectivityManager.TYPE_MOBILE;
            default:
                return type;
        }
    }

    private static String getNetworkProvider(String IMSI) {
        String provide = IMSI;
        if (IMSI != null) {
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007") || IMSI.startsWith("46020")) {
                provide = "移动";
            } else if (IMSI.startsWith("46001") || IMSI.startsWith("46006")) {
                provide = "联通";
            } else if (IMSI.startsWith("46003") || IMSI.startsWith("46005") || IMSI.startsWith("46011")) {
                provide = "电信";
            }
        }
        return provide;
    }

    /**
     * 网络子类型
     *
     * @param context
     * @return
     */
    public static int getNetworkSubType(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        return netInfo.getSubtype();
    }


    /**
     * Connection Type
     *
     * @param context
     * @return
     */
    public static int getConnectionType(Context context) {
        if (isWifiNetwork(context)) {
            return ConnectionType.Wifi;
        }

        NetworkInfo networkInfo = getConnectedNetworkInfo(context);
        if (networkInfo == null) {
            return ConnectionType.Unknown;
        }

        int nType = networkInfo.getType();
        if (nType != ConnectivityManager.TYPE_MOBILE) {
            return ConnectionType.Unknown;
        }

        try {
            final Cursor c = context.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
            if (c != null) {
                c.moveToFirst();
                final String user = c.getString(c.getColumnIndex("user"));
                if (!TextUtils.isEmpty(user)) {
                    Logger.d("===>代理：%s", c.getString(c.getColumnIndex("proxy")));
                    if (user.startsWith(CTWAP)) {
                        Logger.d("===>电信wap网络");
                        return ConnectionType.G2;
                    }
                }

                c.close();
            }
        } catch (Exception e) {
            Logger.e(e.toString(), e);
        }

        String extraInfo = networkInfo.getExtraInfo();

        if (null != extraInfo)
            Logger.d("extraInfo:" + extraInfo);

        if (extraInfo != null) {
            // 通过apn名称判断是否是联通和移动wap
            extraInfo = extraInfo.toLowerCase();
            if (extraInfo.equals(CMWAP) || extraInfo.equals(WAP_3G) || extraInfo.equals(UNIWAP)) {
                Logger.d(" ======>移动联通wap网络");
                return ConnectionType.G2;
            }
        }

        Logger.d(" ======>net网络");

        return ConnectionType.G3;
    }

    /**
     * 获取接入点类型</br> Net网络：运营商（移动联通电信）net网络，wifi，usb网络共享<br/>
     * Wap网络：移动联通wap（代理相同：10.0.0.172：80），电信wap（代理：10.0.0.200：80）<br/>
     */
    public static int getAPNType(Context context) {
        NetworkInfo networkInfo = getConnectedNetworkInfo(context);
        if (networkInfo == null) {
            return APN_TYPE_DISABLED;
        }

        int nType = networkInfo.getType();
        if (nType != ConnectivityManager.TYPE_MOBILE) {
            return APN_TYPE_OTHER;
        }

        // 判断是否电信wap:
        // 不要通过getExtraInfo获取接入点名称来判断类型，
        // 因为通过目前电信多种机型测试发现接入点名称大都为#777或者null，
        // 电信机器wap接入点中要比移动联通wap接入点多设置一个用户名和密码,
        // 所以可以通过这个进行判断！
        try {
            final Cursor c = context.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
            if (c != null) {
                c.moveToFirst();
                final String user = c.getString(c.getColumnIndex("user"));
                if (!TextUtils.isEmpty(user)) {
                    Logger.d("===>代理： %s", c.getString(c.getColumnIndex("proxy")));
                    if (user.startsWith(CTWAP)) {
                        Logger.d("===>电信wap网络");
                        return APN_TYPE_CT_WAP;
                    }
                }

                c.close();
            }
        } catch (Exception e) {
            Logger.e(e.toString(), e);
        }

        // 判断是移动联通wap:
        // 其实还有一种方法通过getString(c.getColumnIndex("proxy")获取代理ip
        // 来判断接入点，10.0.0.172就是移动联通wap，10.0.0.200就是电信wap，但在
        // 实际开发中并不是所有机器都能获取到接入点代理信息，例如魅族M9 （2.2）等...
        // 所以采用getExtraInfo获取接入点名字进行判断
        String extraInfo = networkInfo.getExtraInfo();

        if (null != extraInfo)
            Logger.d("extraInfo:" + extraInfo);

        if (extraInfo != null) {
            // 通过apn名称判断是否是联通和移动wap
            extraInfo = extraInfo.toLowerCase();
            if (extraInfo.equals(CMWAP) || extraInfo.equals(WAP_3G) || extraInfo.equals(UNIWAP)) {
                Logger.d(" ======>移动联通wap网络");
                return APN_TYPE_CM_CU_WAP;
            }
        }

        Logger.d(" ======>net网络");

        return APN_TYPE_NET;

    }

    /**
     * 获取wifi名称
     */
    public static String getWifiSSID(Context context) {
        if (isWifiNetwork(context)) {
            try {
                WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = wifi.getConnectionInfo();
                return info.getSSID();
            } catch (Exception e) {
                Logger.e(e.toString(), e);
            }
        }
        return "";
    }

    /**
     * 漫游
     * Returns whether the network is roaming
     *
     * @param context Context
     * @return boolean
     * @see [类、类#方法、类#成员]
     */
    public static boolean isNetworkRoaming(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                Logger.e("couldn't get connectivity manager");
                return false;
            }

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
                if (/* TelephonyManager.getDefault() */telephonyManager.isNetworkRoaming()) {
                    return true;
                } else {

                }
            } else {

            }
        } catch (Exception e) {
            Logger.e(e.toString(), e);
        }

        return false;
    }

    /**
     * 获取本机ip
     * 注意：4.0有问题
     */
    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface intf = en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    // loopback地址就是代表本机的IP地址，只要第一个字节是127，就是lookback地址
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                        // ipaddress = ipaddress + ";" +
                        // inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
            Logger.e(e.toString(), e);
        }

        return "";
    }

    /**
     * WIFI第一个地址
     * <p>
     * <pre>
     * 第一个IP地址，即本机IP
     * </pre>
     *
     * @param context
     * @return WIFI First IP Address eg: 1979820224
     */
    public static int getFirstWiFiIpAddres(Context context) {
        if (null == context) {
            return 0;
        }

        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();

        return info.getIpAddress();
    }

    /**
     * Wifi IP地址
     * <p>
     * <pre>
     * 第一个IP地址，即本机IP
     * </pre>
     *
     * @param context
     * @return WIFI Normal IP Address eg: 192.168.1.118
     */
    @Deprecated
    public static String getNormalWiFiIpAddres(Context context) {
        if (null == context) {
            return "";
        }

        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        int inIP = info.getIpAddress();

        return (inIP & 0xFF) + "." + ((inIP >> 8) & 0xFF) + "." + ((inIP >> 16) & 0xFF) + "." + (inIP >> 24 & 0xFF);
    }

    /**
     * 判断端口是否被占用
     *
     * @param port
     * @return
     */
    public static boolean isPortUsed(int port) {
        String[] cmds = {"netstat", "-an"};
        Process process = null;
        InputStream is = null;
        DataInputStream dis = null;
        try {

            String line = "";
            Runtime runtime = Runtime.getRuntime();

            process = runtime.exec(cmds);
            is = process.getInputStream();
            dis = new DataInputStream(is);
            while ((line = dis.readLine()) != null) {
                if (line.contains(":" + port)) {
                    return true;
                }
            }
        } catch (Exception e) {
            Logger.e(e.toString(), e);
        } finally {
            try {
                if (dis != null) {
                    dis.close();
                }
                if (is != null) {
                    is.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                Logger.e(e.toString(), e);
            }
        }
        return false;
    }

    /**
     * Mac Addr
     *
     * @param context
     * @return
     */
    public static String getMacAddressPure(Context context) {
        String addr = getMacAddress(context);
        if (addr == null) {
            return "";
        }

        addr = addr.replaceAll(":", "");
        addr = addr.replaceAll("-", "");

        return addr.toUpperCase();
    }

    /**
     * Mac Addr
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        String addr = getMacAddrInFile("/sys/class/net/eth0/address");
        if (TextUtils.isEmpty(addr)) {
            addr = getMacAddrInFile("/sys/class/net/wlan0/address");
        }

        if (TextUtils.isEmpty(addr)) {
            return getWifiMacAddress(context);
        }

        return addr;
    }


    /**
     * Mac Addr
     *
     * @param context
     * @return
     */
    public static String getMacAddress2(Context context) {
        String macAddress = "";
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (wifiManager != null) {
            WifiInfo info = wifiManager.getConnectionInfo();
            if (info != null && info.getMacAddress() != null) {
                macAddress = info.getMacAddress().replace(":", "");
            }
        }

        //解决Android 6.0以上无法获取Mac地址的问题
        if (StringUtils.isEmptyOrNull(macAddress) || macAddress.equalsIgnoreCase("020000000000")) {
            try {
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface iF = interfaces.nextElement();

                    byte[] addr = iF.getHardwareAddress();
                    if (addr == null || addr.length == 0) {
                        continue;
                    }

                    StringBuilder buf = new StringBuilder();
                    for (byte b : addr) {
                        buf.append(String.format("%02X:", b));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    String mac = buf.toString();
                    if (iF.getName().startsWith("wlan0")) {
                        macAddress = mac.replace(":", "");
                        break;
                    }
                }
            } catch (SocketException e) {
                Logger.e("mac addr", e);
            }
        }

        macAddress = StringUtils.getUnNullString(macAddress);

        return macAddress;
    }

    /**
     * Mac Addr WIFI
     *
     * @param context
     * @return
     */
    private static String getWifiMacAddress(Context context) {
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            return wifi.getConnectionInfo().getMacAddress();
        } catch (Exception e) {
            Logger.e(e.toString(), e);
        }
        return "";
    }

    /**
     * Mac Addr
     *
     * @param filepath
     * @return
     */
    private static String getMacAddrInFile(String filepath) {
        File f = new File(filepath);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            BufferedReader rd = new BufferedReader(new InputStreamReader(fis));
            String str = rd.readLine();

            // 去除空格
            str = str.replaceAll(" ", "");

            // 查看是否是全0的无效MAC地址 如 00:00:00:00:00:00
            String p = str.replaceAll("-", "");
            p = p.replaceAll(":", "");
            if (p.matches("0*")) {
                return null;
            }
            return str;
        } catch (Exception e) {
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }

        return "";
    }


    /**
     * Load UTF8withBOM or any ansi text file.
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static String loadFileAsString(String filename) throws IOException {
        final int BUFLEN = 1024;
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(filename), BUFLEN);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFLEN);
            byte[] bytes = new byte[BUFLEN];
            boolean isUTF8 = false;
            int read, count = 0;
            while ((read = is.read(bytes)) != -1) {
                if (count == 0 && bytes[0] == (byte) 0xEF && bytes[1] == (byte) 0xBB && bytes[2] == (byte) 0xBF) {
                    isUTF8 = true;
                    baos.write(bytes, 3, read - 3); // drop UTF8 bom marker
                } else {
                    baos.write(bytes, 0, read);
                }
                count += read;
            }
            return isUTF8 ? new String(baos.toByteArray(), "UTF-8") : new String(baos.toByteArray());
        } finally {
            try {
                is.close();
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Returns MAC address of the given interface name.
     *
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return mac address or empty string
     */
    // public static String getMACAddress(String interfaceName)
    // {
    // try
    // {
    // List<NetworkInterface> interfaces =
    // Collections.list(NetworkInterface.getNetworkInterfaces());
    // for (NetworkInterface intf : interfaces)
    // {
    // if (interfaceName != null)
    // {
    // if (!intf.getName().equalsIgnoreCase(interfaceName))
    // continue;
    // }
    // byte[] mac = intf.getHardwareAddress();
    // if (mac == null)
    // return "";
    // StringBuilder buf = new StringBuilder();
    // for (int idx = 0; idx < mac.length; idx++)
    // {
    // buf.append(String.formatChinese("%02X:", mac[idx]));
    // }
    // if (buf.length() > 0)
    // {
    // buf.deleteCharAt(buf.length() - 1);
    // }
    // return buf.toString();
    // }
    // }
    // catch (Exception ex)
    // {
    // LogUtils.error(ex.toString(), ex);
    // } // for now eat exceptions
    // return "";
    // /*
    // * try { // this is so Linux hack return
    // * loadFileAsString("/sys/class/net/" +interfaceName +
    // * "/address").toUpperCase().trim(); } catch (IOException ex) { return
    // * null; }
    // */
    // }

    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6
                                // port suffix
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    /**
     * 获取DNS
     *
     * @param context
     * @return
     */
    public static String getDns(Context context) {
        if (context == null) {
            return "";
        }

        WifiManager my_wifiManager = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE));
        if (my_wifiManager == null) {
            return null;
        }
        DhcpInfo dhcpInfo = my_wifiManager.getDhcpInfo();

        if (dhcpInfo == null) {
            return null;
        }
        String dns = intToIp(dhcpInfo.dns1);

        return dns;
    }

    /**
     * 是否连接3G网络
     * <p/>
     * <p>
     * 注意：NETWORK_TYPE_HSPA 和 NETWORK_TYPE_HSUPA 还没有定位是否为联通3G
     * </p>
     *
     * @param context
     * @return NETWORK_TYPE_HSDPA(联通3G) || NETWORK_TYPE_UMTS(联通3G) || NETWORK_TYPE_EVDO_0(电信3G) || NETWORK_TYPE_EVDO_A(电信3G)
     */
    public static boolean is3G(Context context) {
        NetworkInfo netInfo = getConnectedNetworkInfo(context);

        if (null == netInfo) {
            return false;
        }

        // 非手机网络
        if (ConnectivityManager.TYPE_MOBILE != netInfo.getType()) {
            return false;
        }

        // NetworkInfo mMoble =
        // connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        int subType = netInfo.getSubtype();
        if (subType == TelephonyManager.NETWORK_TYPE_HSDPA // 联通3G
                || subType == TelephonyManager.NETWORK_TYPE_UMTS // 联通3G
                || subType == TelephonyManager.NETWORK_TYPE_EVDO_0 // 电信3G
                || subType == TelephonyManager.NETWORK_TYPE_EVDO_A)// 电信3G
        {
            return true;
        }

        return false;
    }

    /**
     * 是否为联通3G网络
     *
     * @param context
     * @return NETWORK_TYPE_HSDPA || NETWORK_TYPE_UMTS
     */
    public static boolean isUnicom3G(Context context) {
        NetworkInfo netInfo = getConnectedNetworkInfo(context);

        if (null == netInfo) {
            return false;
        }

        // 非手机网络
        if (ConnectivityManager.TYPE_MOBILE != netInfo.getType()) {
            return false;
        }

        int subType = netInfo.getSubtype();
        if (subType == TelephonyManager.NETWORK_TYPE_HSDPA || subType == TelephonyManager.NETWORK_TYPE_UMTS) { // 联通3G
            return true;
        }

        return false;
    }

    /**
     * 是否为电信3G网络
     *
     * @param context
     * @return NETWORK_TYPE_EVDO_0 || NETWORK_TYPE_EVDO_A
     */
    public static boolean isTelecom3G(Context context) {
        NetworkInfo netInfo = getConnectedNetworkInfo(context);

        if (null == netInfo) {
            return false;
        }

        // 非手机网络
        if (ConnectivityManager.TYPE_MOBILE != netInfo.getType()) {
            return false;
        }

        int subType = netInfo.getSubtype();
        if (subType == TelephonyManager.NETWORK_TYPE_EVDO_0 || subType == TelephonyManager.NETWORK_TYPE_EVDO_A) { // 电信3G
            return true;
        }

        return false;
    }

    /**
     * 是否连接2G网络
     *
     * @param context
     * @return TelephonyManager.NETWORK_TYPE_GPRS(移动和联通2G) || TelephonyManager.NETWORK_TYPE_CDMA(电信2G) || TelephonyManager.NETWORK_TYPE_EDGE(移动和联通2G)
     */
    public static boolean is2G(Context context) {
        NetworkInfo netInfo = getConnectedNetworkInfo(context);

        if (null == netInfo) {
            return false;
        }
        // 非手机网络
        if (ConnectivityManager.TYPE_MOBILE != netInfo.getType()) {
            return false;
        }

        int subType = netInfo.getSubtype();
        if (subType == TelephonyManager.NETWORK_TYPE_GPRS // 移动和联通2G
                || subType == TelephonyManager.NETWORK_TYPE_CDMA // 电信2G
                || subType == TelephonyManager.NETWORK_TYPE_EDGE) // 移动和联通2G
        {
            return true;
        }

        return false;
    }

    /**
     * 转换ip4格式地址
     *
     * @param intIp 整型格式地址
     * @return
     */
    public static String intToIp(int intIp) {
        return (intIp & 0xFF) + "." +

                ((intIp >> 8) & 0xFF) + "." +

                ((intIp >> 16) & 0xFF) + "." +

                (intIp >> 24 & 0xFF);
    }

    public static long ip2int(String ip) {
        String[] items = ip.split("\\.");
        return Long.valueOf(items[0]) << 24 | Long.valueOf(items[1]) << 16 | Long.valueOf(items[2]) << 8 | Long.valueOf(items[3]);
    }

    /**
     * 将127.0.0.1形式的IP地址转换成十进制整数
     *
     * @param strIp
     * @return
     */
    public static long ipToLong(String strIp) {
        long[] ip = new long[4];
        // 先找到IP地址字符串中.的位置
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        // 将每个.之间的字符串转换成整型
        ip[0] = Long.parseLong(strIp.substring(0, position1));
        ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(strIp.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    /**
     * 将十进制整数  转换成127.0.0.1形式的IP地址
     *
     * @param longIp
     * @return
     */
    public static String LongToIp(long longIp) {

        StringBuffer sb = new StringBuffer("");
        // 将高24位置0
        sb.append(String.valueOf((longIp & 0x000000FF)));
        sb.append(".");
        // 将高16位置0，然后右移8位
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
        sb.append(".");
        // 将高8位置0，然后右移16位
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        // 直接右移24位
        sb.append(String.valueOf((longIp >>> 24)));

        return sb.toString();
    }

    /**
     * ipv4地址转有符号byte[5]
     */
    private static byte[] ipv4ToBytes(String ipv4) {
        byte[] ret = new byte[5];
        ret[0] = 0;
        // 先找到IP地址字符串中.的位置
        int position1 = ipv4.indexOf(".");
        int position2 = ipv4.indexOf(".", position1 + 1);
        int position3 = ipv4.indexOf(".", position2 + 1);
        // 将每个.之间的字符串转换成整型
        ret[1] = (byte) Integer.parseInt(ipv4.substring(0, position1));
        ret[2] = (byte) Integer.parseInt(ipv4.substring(position1 + 1, position2));
        ret[3] = (byte) Integer.parseInt(ipv4.substring(position2 + 1, position3));
        ret[4] = (byte) Integer.parseInt(ipv4.substring(position3 + 1));
        return ret;
    }

    /**
     * ipv6地址转有符号byte[17]
     */
    private static byte[] ipv6ToBytes(String ipv6) {
        byte[] ret = new byte[17];
        ret[0] = 0;
        int ib = 16;
        boolean comFlag = false;// ipv4混合模式标记
        if (ipv6.startsWith(":")) // 去掉开头的冒号
            ipv6 = ipv6.substring(1);
        String groups[] = ipv6.split(":");
        for (int ig = groups.length - 1; ig > -1; ig--) {// 反向扫描
            if (groups[ig].contains(".")) {
                // 出现ipv4混合模式
                byte[] temp = ipv4ToBytes(groups[ig]);
                ret[ib--] = temp[4];
                ret[ib--] = temp[3];
                ret[ib--] = temp[2];
                ret[ib--] = temp[1];
                comFlag = true;
            } else if ("".equals(groups[ig])) {
                // 出现零长度压缩,计算缺少的组数
                int zlg = 9 - (groups.length + (comFlag ? 1 : 0));
                while (zlg-- > 0) {// 将这些组置0
                    ret[ib--] = 0;
                    ret[ib--] = 0;
                }
            } else {
                int temp = Integer.parseInt(groups[ig], 16);
                ret[ib--] = (byte) temp;
                ret[ib--] = (byte) (temp >> 8);
            }
        }
        return ret;
    }

    /**
     * 将字符串形式的ip地址转换为BigInteger
     *
     * @param ipInString 字符串形式的ip地址
     * @return 整数形式的ip地址
     */
    public static BigInteger stringToBigInt(String ipInString) {
        ipInString = ipInString.replace(" ", "");
        byte[] bytes;
        if (ipInString.contains(":")) {
            bytes = ipv6ToBytes(ipInString);
        } else {
            bytes = ipv4ToBytes(ipInString);
        }

        return new BigInteger(bytes);
    }

    /**
     * Convert byte array to hex string
     *
     * @param bytes
     * @return
     */
    public static String bytesToHexStr(byte[] bytes) {
        StringBuilder sbuf = new StringBuilder();
        for (int idx = 0; idx < bytes.length; idx++) {
            int intVal = bytes[idx] & 0xff;
            if (intVal < 0x10)
                sbuf.append("0");
            sbuf.append(Integer.toHexString(intVal).toUpperCase());
        }
        return sbuf.toString();
    }

    /**
     * Get utf8 byte array.
     *
     * @param str
     * @return array of NULL if error was found
     */
    public static byte[] getUTF8Bytes(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (Exception ex) {
            return null;
        }
    }

}
