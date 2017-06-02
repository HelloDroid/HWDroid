/**
 * @(#)SDcardUtil.java 2014-5-16 Copyright 2014 . All rights
 * reserved.
 */

package com.hw.hwdroid.foundation.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.io.File;

/**
 * SDcard Util
 *
 * @author chenj
 * @date 2014-5-16
 */

public class SDCardUtils {

    private SDCardUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断SDCard是否可用
     *
     * @return true 可用
     */
    public static boolean isCanUseSD() {
        try {
            return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        } catch (Exception e) {
            Logger.e(e);
        }

        return false;
    }

    /**
     * 外部存储目录是否存在且可写
     *
     * @return
     */
    public static boolean existExternalStorageDirectory() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && Environment.getExternalStorageDirectory().canWrite()) {

            return true;
        }

        return false;
    }

    /**
     * 获取外部存储目录
     *
     * @return
     * @see #getSDCardPath()
     */
    public static String getExternalStorageDirectory() {
        return getSDCardPath();
    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    /**
     * 获取外部存储目录
     *
     * @return
     */
    public static File getExternalStorageDir() {
        return Environment.getExternalStorageDirectory();
    }


    /**
     * 计算sdcard上的剩余空间 MB
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static int freeSpaceOnSDMB() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocksLong() * (double) stat.getBlockSizeLong()) / 1024 * 1024;

        return (int) sdFreeMB;
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    public static long getSDCardAllSize() {
        if (isCanUseSD()) {
            StatFs stat = new StatFs(getExternalStorageDirectory());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }

        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getExternalStorageDirectory())) {
            filePath = getExternalStorageDirectory();
        }
        // 如果是内部存储的路径，则获取内存存储的可用容量
        else {
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }

        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;

        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 计算sdcard上的剩余空间
     *
     * @return
     */
    public static long freeSpaceOnSD() {
        // 取得sdcard文件路径
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());

        // // 获取block的SIZE
        // long blocSize = statFs.getBlockSize();
        //
        // // 可使用的Block的数量
        // long availaBlock = statFs.getAvailableBlocks();
        //
        // // long total = totalBlocks * blocSize;
        // long availableSpare = availaBlock * blocSize;

        try {
            return (statFs.getAvailableBlocks() / 1024) * (statFs.getBlockSize() / 1024);
        } catch (Exception e) {
            // Log.e(SDcardUtils.class.getSimpleName(), "freeSpaceOnSD ", e);
            return -1;
        }
    }

    /**
     * 创建文件夹
     *
     * @param dir 相对sd路径
     * @return /sdcard/dir/
     */
    public static File createDir(@NonNull String dir) {
        try {
            if (null == dir || "".equals(dir) || 0 == dir.trim().length()) {
                return null;
            }

            if (!isCanUseSD()) {
                return null;
            }

            return FileUtils.createFolder(getExternalStorageDirectory() + File.separator + dir);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 创建文件夹
     *
     * @param dir    相对sd路径
     * @param subDir
     * @return /sdcard/dir/path
     */
    public static File createDir(@NonNull String dir, @NonNull String subDir) {
        try {
            if (!isCanUseSD()) {
                return null;
            }

            if (null == dir || "".equals(dir) || 0 == dir.trim().length()) {
                if (null == subDir || "".equals(subDir) || 0 == subDir.trim().length()) {
                    return null;
                }
                return FileUtils.createFolder(getExternalStorageDirectory() + File.separator + subDir);
            } else {
                if (null == subDir || "".equals(subDir) || 0 == subDir.trim().length()) {
                    return FileUtils.createFolder(getExternalStorageDirectory() + File.separator + dir);
                }
                return FileUtils.createFolder(new File(getExternalStorageDirectory() + File.separator + dir), subDir);
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 创建文件
     *
     * @param dir      相对sd路径
     * @param filename
     * @return /sdcard/dir/filename
     */
    public static File createFile(@NonNull String dir, @NonNull String filename) {
        if (StringUtils.isNullOrWhiteSpace(filename)) {
            return null;
        }

        try {
            if (!isCanUseSD()) {
                return null;
            }

            return FileUtils.createFile(getExternalStorageDirectory() + File.separator + dir, filename);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * name文件是否存在于ExternalStorageDirectory
     *
     * @param file
     * @return
     */
    public static boolean existsFromExternalStorageDirectory(String file) {
        if (StringUtils.isNullOrWhiteSpace(file)) {
            return false;
        }

        try {
            if (!isCanUseSD()) {
                return false;
            }

            return new File(getExternalStorageDirectory() + File.separator + file).exists();
        } catch (Exception e) {
            return false;
        }
    }

}
