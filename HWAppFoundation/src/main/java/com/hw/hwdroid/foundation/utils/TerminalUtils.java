package com.hw.hwdroid.foundation.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.RandomAccessFile;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * 设备信息工具类
 *
 * @author chenj
 * @date 2014-7-30
 */
public class TerminalUtils {

    /**
     * 产品名称
     *
     * @return
     * @Description
     */
    public static String getProductName() {
        return android.os.Build.MODEL;
    }

    /**
     * 硬件制造商
     *
     * @return
     */
    public static String getManufacturerName() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 设备名称(制造商+产品名称)
     *
     * @return samsung GT-I9300
     */
    public static String getPhoneName() {
        return new StringBuffer(android.os.Build.MANUFACTURER).append(" ").append(android.os.Build.MODEL).toString();
    }

    /**
     * 获取IMSI
     *
     * @param context
     * @return
     */
    public static String getIMSI(@NonNull Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        return tm.getSubscriberId();
    }

    /**
     * 获取IMEI
     *
     * @param context
     * @return
     */
    public static String getIMEI(@NonNull Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 获取设备id
     */
    public static String getDeviceId(@NonNull Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * @param context
     * @param clientCode
     * @param isWechatWakeUp 是否由微信唤醒
     * @return
     */
    public static HashMap<String, Object> getMarketingInfo(@NonNull Context context, @NonNull String clientCode, boolean isWechatWakeUp) {
        // mac
        String macAddress = "";
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        try {
            if (wifi != null) {
                WifiInfo info = wifi.getConnectionInfo();
                if (info != null && info.getMacAddress() != null) {
                    macAddress = info.getMacAddress().replace(":", "");
                }
            }
        } catch (Exception e) {
        }

        // imei
        String imei = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        // IDFA IDFA
        String idfaString = "";

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("clientcode", clientCode);
        params.put("idfa", idfaString);
        params.put("mac", macAddress);
        params.put("imei", imei);
        params.put("isWechatWakeUp", isWechatWakeUp);
        return params;
    }

    /**
     * 预装信息
     *
     * @return
     */
    public static String getPreInstalledSourceId() {
        String preInstalledSourceId = "";

        if (TextUtils.isEmpty(preInstalledSourceId)) {
            List<String> preSourceIdPath = new ArrayList<String>();
            preSourceIdPath.add("/System/delapp/ctripPreInstalledInfo.dat");
            preSourceIdPath.add("/system/etc/appchannel/ctripPreInstalledInfo.dat");
            preSourceIdPath.add("/system/etc/ctripPreInstalledInfo.dat");
            preSourceIdPath.add("/system/lib/libctripPreInstalledInfo.so");

            for (String preInstalledChannelPath : preSourceIdPath) {
                File preInstalledFile = new File(preInstalledChannelPath);

                if (!preInstalledFile.exists()) {
                    continue;
                }

                String content = FileUtils.readFile(preInstalledChannelPath);

                if (TextUtils.isEmpty(content)) {
                    continue;
                }
                try {
                    JSONObject jobj = new JSONObject(content);
                    if (jobj != null) {
                        preInstalledSourceId = jobj.optString("SourceID", "");
                        break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return preInstalledSourceId;
    }

    /**
     * SDK Version
     *
     * @return
     */
    public static int getSdkVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * SDK Version name
     *
     * @return
     */
    public static String getSdkVersionName() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 手机号码
     *
     * @param context
     * @return
     */
    public static String getPhoneNumber(Context context) {
        if (context == null)
            return "";

        try {
            TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            return telephonyMgr.getLine1Number();
        } catch (Exception e) {
            Logger.e(e);
        }

        return "";
    }

    /**
     * 电话状态
     *
     * @param context
     * @return CALL_STATE_IDLE 无任何状态时; CALL_STATE_OFFHOOK 接起电话时;
     * CALL_STATE_RINGING 电话进来时
     * @Description
     */
    public static int getCallState(Context context) {
        if (context == null)
            return 0;

        TelephonyManager telephonMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        return telephonMgr.getCallState();
    }

    /**
     * 电话详细
     *
     * @param context
     * @return
     */
    @Deprecated
    public static String telephonyInfo(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String str = "";
        str += "DeviceId(IMEI) = " + tm.getDeviceId() + "\n";
        str += "DeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion() + "\n";
        str += "Line1Number = " + tm.getLine1Number() + "\n";
        str += "NetworkCountryIso = " + tm.getNetworkCountryIso() + "\n";
        str += "NetworkOperator = " + tm.getNetworkOperator() + "\n";
        str += "NetworkOperatorName = " + tm.getNetworkOperatorName() + "\n";
        str += "NetworkType = " + tm.getNetworkType() + "\n";
        str += "PhoneType = " + tm.getPhoneType() + "\n";
        str += "SimCountryIso = " + tm.getSimCountryIso() + "\n";
        str += "SimOperator = " + tm.getSimOperator() + "\n";
        str += "SimOperatorName = " + tm.getSimOperatorName() + "\n";
        str += "SimSerialNumber = " + tm.getSimSerialNumber() + "\n";
        str += "SimState = " + tm.getSimState() + "\n";
        str += "SubscriberId(IMSI) = " + tm.getSubscriberId() + "\n";
        str += "VoiceMailNumber = " + tm.getVoiceMailNumber() + "\n";

        return str;
    }

    /**
     * 检查通信是否使用蓝牙SCO
     *
     * @param context
     * @return
     */
    public static boolean isBluetoothScoOn(Context context) {
        if (null == context) {
            return false;
        }

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        return audioManager.isBluetoothScoOn();
    }

    /**
     * 检查A2DP音频路由到蓝牙耳机是否打开
     *
     * @param context
     * @return
     */
    public static boolean isBluetoothA2dpOn(Context context) {
        if (null == context) {
            return false;
        }

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        return audioManager.isBluetoothA2dpOn();
    }

    /**
     * 蓝牙是否关闭
     *
     * @param context
     * @return
     */
    public static boolean isBluetoothScoAvailableOffCall(Context context) {
        if (null == context) {
            return false;
        }

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        return audioManager.isBluetoothScoAvailableOffCall();
    }

    /**
     * 检查麦克风是否静音
     *
     * @param context
     * @return
     */
    public static boolean isMicrophoneMute(Context context) {
        if (null == context) {
            return false;
        }

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        return audioManager.isMicrophoneMute();
    }

    /**
     * 检查喇叭扩音器是否开着
     *
     * @param context
     * @return
     */
    public static boolean isSpeakerphoneOn(Context context) {
        if (null == context) {
            return false;
        }

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        return audioManager.isSpeakerphoneOn();
    }

    /**
     * 检查音频路由到有线耳机是否开着
     *
     * @param context
     * @return
     */
    public static boolean isWiredHeadsetOn(Context context) {
        if (null == context) {
            return false;
        }

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        return audioManager.isWiredHeadsetOn();
    }

    /**
     * 设置听筒模式
     *
     * @param context
     * @param useMaxVolume    使用最大音量
     * @param forceOpenVolume 强制开启音量，当音量为0时有效
     */
    public static void setReceiverModel(Context context, boolean useMaxVolume, boolean forceOpenVolume) {
        if (null == context) {
            return;
        }

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        /*if (!audioManager.isSpeakerphoneOn()) {
            return;
		}
		*/
        int volumeIndex = 0;
        if (useMaxVolume) {
            volumeIndex = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        } else {
            volumeIndex = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
            if (volumeIndex <= 0 && forceOpenVolume) {
                volumeIndex = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
            }
        }
        // 关闭扬声器
        audioManager.setSpeakerphoneOn(false);
        audioManager.setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL);
        // 设置音量
        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, volumeIndex, AudioManager.STREAM_VOICE_CALL);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
    }

    /**
     * 扬声器模式
     *
     * @param context
     * @param useMaxVolume    使用最大音量
     * @param forceOpenVolume 强制开启音量，当音量为0时有效
     */
    public static void setSpeakerphoneOn(Context context, boolean useMaxVolume, boolean forceOpenVolume) {
        if (null == context) {
            return;
        }

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.isSpeakerphoneOn()) {
            return;
        }

        int volumeIndex = 0;
        if (useMaxVolume) {
            volumeIndex = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        } else {
            volumeIndex = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
            if (volumeIndex <= 0 && forceOpenVolume) {
                volumeIndex = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
            }
        }
        // 打开扬声器
        audioManager.setSpeakerphoneOn(true);

        // 设置音量
        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, volumeIndex, AudioManager.STREAM_VOICE_CALL);
        audioManager.setMode(AudioManager.MODE_NORMAL);
    }

    /**
     * 获取网络类型
     *
     * @param context
     * @return NETWORK_TYPE_CDMA 网络类型为CDMA; NETWORK_TYPE_EDGE 网络类型为EDGE;
     * NETWORK_TYPE_EVDO_0 网络类型为EVDO0; NETWORK_TYPE_EVDO_A 网络类型为EVDOA;
     * NETWORK_TYPE_GPRS 网络类型为GPRS; NETWORK_TYPE_HSDPA 网络类型为HSDPA
     * NETWORK_TYPE_HSPA 网络类型为HSPA; NETWORK_TYPE_HSUPA 网络类型为HSUPA;
     * NETWORK_TYPE_UMTS 网络类型为UMTS
     * 联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
     * @Description
     * @see com.android.common.utils.network.NetWorkUtils#getNetworkSubType(Context)
     */
    @Deprecated
    public static int getNetworkType(Context context) {
        if (context == null)
            return 0;

        TelephonyManager telephonMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        return telephonMgr.getNetworkType();
    }

    /**
     * 是否为3G网络
     * <p/>
     * <p>
     * 注意：NETWORK_TYPE_HSPA 和 NETWORK_TYPE_HSUPA 还没有定位是否为联通3G
     * </p>
     *
     * @param context
     * @return
     * @Description
     * @see com.android.common.utils.network.NetWorkUtils#is3G(Context)
     */
    @Deprecated
    public static boolean is3GNetwork(Context context) {
        if (context == null)
            return false;

        TelephonyManager telephonMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int type = telephonMgr.getNetworkType();
        if (type == TelephonyManager.NETWORK_TYPE_HSDPA // 联通3G
                || type == TelephonyManager.NETWORK_TYPE_UMTS // 联通3G
                || type == TelephonyManager.NETWORK_TYPE_EVDO_0 // 电信3G
                || type == TelephonyManager.NETWORK_TYPE_EVDO_A)// 电信3G
        {
            return true;
        }

        return false;
    }

    /**
     * 是否是2G网络
     *
     * @param context
     * @return
     * @Description
     * @see com.android.common.utils.network.NetWorkUtils#is2G(Context)
     */
    @Deprecated
    public static boolean is2GNetwork(Context context) {
        if (context == null)
            return false;

        TelephonyManager telephonMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int type = telephonMgr.getNetworkType();
        if (type == TelephonyManager.NETWORK_TYPE_GPRS // 移动和联通2G
                || type == TelephonyManager.NETWORK_TYPE_CDMA // 电信2G
                || type == TelephonyManager.NETWORK_TYPE_EDGE) // 联通2G
        {
            return true;
        }

        return false;
    }

    /**
     * 本机所有安装程序
     *
     * @param context
     * @param sort    排序
     * @return List<ApplicationInfo>
     */
    public static List<ApplicationInfo> getApplicationInfo(Context context, boolean sort) {
        if (null == context) {
            return null;
        }

        PackageManager appInfo = context.getPackageManager();
        List<ApplicationInfo> listInfo = appInfo.getInstalledApplications(0);

        if (sort) {
            Collections.sort(listInfo, new ApplicationInfo.DisplayNameComparator(appInfo));
        }

        List<ApplicationInfo> data = new ArrayList<ApplicationInfo>();
        try {
            for (int index = 0; index < listInfo.size(); index++) {
                ApplicationInfo content = listInfo.get(index);
                if (content.flags == ApplicationInfo.FLAG_SYSTEM || content.enabled)
                    continue;

                data.add(content);
            }
        } catch (Exception e) {
        }

        return data;
    }


    /**
     * 本机所有安装程序
     *
     * @param context
     * @param sort    排序
     * @return List<PackageItem>
     */
    public static List<PackageItem> getApp4Package(Context context, boolean sort) {
        if (null == context)
            return null;

        PackageManager appInfo = context.getPackageManager();
        List<ApplicationInfo> listInfo = appInfo.getInstalledApplications(0);

        if (sort) {
            Collections.sort(listInfo, new ApplicationInfo.DisplayNameComparator(appInfo));
        }

        List<PackageItem> data = new ArrayList<PackageItem>();
        try {
            for (int index = 0; index < listInfo.size(); index++) {
                ApplicationInfo content = listInfo.get(index);
                if (content.flags == ApplicationInfo.FLAG_SYSTEM || content.enabled)
                    continue;

                if (content.icon != 0) {
                    PackageItem item = new PackageItem();
                    item.setName(appInfo.getApplicationLabel(content).toString());
                    item.setPackageName(content.packageName);
                    item.setIcon(appInfo.getDrawable(content.packageName, content.icon, content));
                    data.add(item);
                }
            }
        } catch (Exception e) {
        }

        return data;
    }


    /**
     * 计算系统总内存
     *
     * @param context
     * @return
     */
    public static int getTotalMemory(Context context) {
        if (context == null) {
            return 0;
        }

        String str1 = "/proc/meminfo";
        String str2;
        String[] arrayOfString;
        int initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");

            // for (String num : arrayOfString) {
            // // Log.i(str2, num + "\t");
            // }

            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() / 1024;// 显示单位为MB

            localBufferedReader.close();
        } catch (IOException e) {
        }

        return initial_memory;
    }

    /**
     * 返回cpu型号
     *
     * @return
     */
    public static String getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String cpuType = "";
        String[] cpuInfo = {
                "", ""
        }; // 1-cpu型号 //2-cpu频率
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
        }
        cpuType = cpuInfo[0];
        return cpuType;
    }


    /**
     * 获取CPU序列号
     *
     * @return CPU序列号(16位) 读取失败为"0000000000000000"
     * @Description
     */
    public static String getCPUSerial() {
        String str = "";
        String strCPU = "";
        String cpuAddress = "0000000000000000";
        try {
            Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo"); // 读取CPU信息
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (int i = 1; i < 100; i++) {// 查找CPU序列号
                str = input.readLine();
                if (str != null) {
                    if (str.indexOf("Serial") > -1) {// 查找到序列号所在行
                        strCPU = str.substring(str.indexOf(":") + 1, str.length());// 提取序列号
                        cpuAddress = strCPU.trim();// 去空格

                        break;
                    }
                } else {
                    break;// 文件结尾
                }
            }
            input.close();
            ir.close();
        } catch (IOException ex) {
        }

        return cpuAddress;
    }

    /**
     * 资料地址：http://hi.baidu.com/ch_ff/item/e2d74df357f59c0f85d278f9 <br>
     * 网上方法，未验证
     *
     * @return
     */
    public static short readUsage() {
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            String load = reader.readLine();
            String[] toks = load.split(" ");
            long idle1 = Long.parseLong(toks[5]);
            long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4]) + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            Thread.sleep(360);

            reader.seek(0);
            load = reader.readLine();
            reader.close();
            toks = load.split(" ");
            long idle2 = Long.parseLong(toks[5]);
            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4]) + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);
            return (short) (100 * (cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1)));
        } catch (IOException ex) {
        } catch (Exception e) {
        }

        return 0;
    }

    /**
     * 获取内存使用率
     *
     * @param context
     */
    public static short getMemoryUsage(Context context) {
        if (context == null) {
            return 0;
        }
        short usuge = 0;
        final ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityMgr.getMemoryInfo(memoryInfo);

        // 系统剩余内存
        final long availMem = (memoryInfo.availMem >> 10) >> 10;

        // 已使用内存
        final long usedMen = getTotalMemory(context) - availMem;

        usuge = (short) ((float) usedMen / getTotalMemory(context) * 100);
        return usuge;
    }

    /**
     * 获取cpu的核数 <br>
     * <p>
     * Gets the number of cores available in this device, across all processors.
     * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
     *
     * @return The number of cores, or 1 if failed to get result
     */
    public static int getNumCores() {
        try {
            // Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    // Check if filename is "cpu", followed by a single digit
                    // number
                    if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                        return true;
                    }
                    return false;
                }
            });
            // Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            // Default to return 1 core
            return 1;
        }
    }

    public static int getCPUFrequencyMax() {
        return readSystemFileAsInt("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
    }

    private static int readSystemFileAsInt(final String pSystemFile) {
        InputStream in = null;
        String content = "";
        try {
            final Process process = new ProcessBuilder(new String[]{
                    "/system/bin/cat", pSystemFile
            }).start();

            in = process.getInputStream();

            final StringBuilder sb = new StringBuilder();
            final Scanner sc = new Scanner(in);
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine());
            }
            content = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.parseInt(content);
    }

    /**
     * App data files
     *
     * @param context
     * @return data/data/application package/files
     */
    public static String getAppFilesDir(@NonNull Context context) {
        return context.getFilesDir().getAbsolutePath() + File.separator;
    }

    /**
     * 保存List数据
     *
     * @param context
     * @param fileName /data/data/<package name>/files/fileName
     * @param obj
     */
    public static void writeObject2File(Context context, String fileName, Object obj) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
        } catch (Exception e) {
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * 读取存有List的File，并转换为一个List
     *
     * @param fileName /data/data/<package name>/files/fileName
     * @param context
     * @return
     */
    public static Object readObject4File(Context context, String fileName) {
        if (StringUtils.isNullOrWhiteSpace(fileName) || context == null) {
            return null;
        }

        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(fileName);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (FileNotFoundException e1) {
        } catch (StreamCorruptedException e1) {
        } catch (OptionalDataException e1) {
        } catch (ClassNotFoundException e1) {
        } catch (IOException e1) {
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
            }
        }

        return null;
    }

}
