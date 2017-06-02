package com.hw.hwdroid.foundation.utils;

import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.util.List;

/**
 * 文件工具类
 *
 * @author chenjian
 */
public final class FileUtils {

    /**
     * 创建文件夹
     *
     * @param dir
     */
    public static File createFolder(String dir) {
        if (isNull(dir)) {
            return null;
        }

        try {
            File file = new File(dir);
            if (!file.exists()) {
                file.mkdirs();
            }

            return file;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 创建文件夹
     *
     * @param dir
     * @param folder
     * @return
     */
    public static File createFolder(String dir, String folder) {
        if (isNull(dir) && isNull(folder)) {
            return null;
        }

        if (isNull(dir)) {
            return createFolder(folder);
        }

        if (isNull(folder)) {
            return createFolder(dir);
        }

        File file = new File(dir, folder);
        if (!file.exists()) {
            file.mkdirs();
        }

        return file;
    }

    /**
     * 创建文件夹
     *
     * @param dir
     * @param folder
     * @return
     */
    public static File createFolder(File dir, String folder) {
        if (null == dir && isNull(folder)) {
            return null;
        }

        if (null == dir) {
            return createFolder(folder);
        }

        File file = new File(dir, folder);
        if (!file.exists()) {
            file.mkdirs();
        }

        return file;
    }

    /**
     * 在dir/创建文件
     *
     * @param name dir/name
     */
    public static File createFile(String dir, String name) {
        if (isNull(dir) || isNull(name)) {
            return null;
        }

        File dirF = new File(dir);
        if (!dirF.exists())
            dirF.mkdirs();

        File f = new File(dirF, name);
        if (!f.exists() || !f.isFile()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
            }
        }

        return f;
    }

    /**
     * 在dir/创建文件
     *
     * @param dir
     * @param name
     * @return
     */
    public static File createFile(File dir, String name) {
        if (null == dir || isNull(name)) {
            return null;
        }

        if (!dir.exists())
            dir.mkdirs();

        File f = new File(dir, name);
        if (!f.exists() || !f.isFile()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
            }
        }

        return f;
    }

    public static boolean createDirByShell(String path) {
        if (isNull(path)) {
            return false;
        }

        File dd = new File(path);
        if (dd.exists() && !dd.isDirectory()) {
            return dd.mkdirs();
        }

        if (dd.exists() && dd.canWrite())
            return true;

        if (dd.exists() && !dd.canWrite()) {
            String command = "chmod 777" + path;

            return FileCommand.runCommand(command);
        }

        String command = "mkdir " + path;

        return FileCommand.runCommand(command);
    }

    /**
     * 是否存在文件or文件夹
     *
     * @param uri
     * @return
     */
    public static boolean isExist(String uri) {
        boolean b = false;
        if (isNull(uri)) {
            return b;
        }


        try {
            return (new File(uri)).exists();
        } catch (Exception e) {
        }
        return b;
    }

    /**
     * 文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean existsFile(String path) {
        if (isNull(path)) {
            return false;
        }

        boolean flag = false;
        File tmpF = new File(path);
        if (tmpF.exists() && tmpF.isFile()) {
            flag = true;
        } else {
            flag = false;
        }

        return flag;
    }

    /**
     * 文件是否存在
     *
     * @param dir
     * @param path
     * @return
     */
    public static boolean existsFile(String dir, String path) {
        if (isNull(dir) || isNull(path)) {
            return false;
        }

        boolean flag = false;
        File tmpF = new File(new File(dir), path);
        if (tmpF.exists() && tmpF.isFile()) {
            flag = true;
        } else {
            flag = false;
        }

        return flag;
    }

    /**
     * 文件是否存在
     *
     * @param dir
     * @param path
     * @return
     */
    public static boolean existsFile(File dir, String path) {
        if (isNull(path)) {
            return false;
        }

        File tmpF = new File(dir, path);
        if (tmpF.exists() && tmpF.isFile()) {
            return true;
        }

        return false;
    }

    /**
     * 文件夹
     *
     * @param name
     * @return
     */
    public static boolean existsFolder(String name) {
        if (isNull(name)) {
            return false;
        }

        boolean flag = false;
        File tmpF = new File(name);
        if (tmpF.exists() && tmpF.isDirectory()) {
            flag = true;
        } else {
            flag = false;
        }

        return flag;
    }

    /**
     * 以String形式返回文件内容
     *
     * @param aFile
     * @return
     */
    public static String getFileString(File aFile) {
        if (aFile == null) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(aFile));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 以String形式返回文件内容
     *
     * @param path
     * @return
     */
    public static String getFileString(String path) {
        if (path == null) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * read file
     *
     * @param filepath
     * @return
     */
    public static String readFile(String filepath) {
        return readFile(filepath, "utf-8");
    }

    public static String readFile(String filepath, String enCodingType) {
        String path = filepath;
        if (null == path) {
            return null;
        }

        String filecontent = "";
        File f = new File(path);

        if (f == null || !f.exists()) {
            return "";
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return null;
        }

        CharBuffer cb;
        try {
            cb = CharBuffer.allocate(fis.available());
        } catch (IOException e1) {
            e1.printStackTrace();
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        InputStreamReader isr;
        try {
            isr = new InputStreamReader(fis, enCodingType);
            try {
                if (cb != null) {
                    isr.read(cb);
                }
                filecontent = new String(cb.array());
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return filecontent;
    }

    /**
     * 写入文件
     *
     * @param dir
     * @param fileName
     * @param info
     */
    public static void writeFile(String dir, String fileName, String info) {
        writeFile(dir, fileName, info, true, false);
    }

    /**
     * 写入文件
     *
     * @param dir
     * @param fileName
     * @param info
     * @param append
     * @param lineBreak
     */
    public static void writeFile(String dir, String fileName, String info, boolean append, boolean lineBreak) {
        if (StringUtils.isNullOrWhiteSpace(dir) || StringUtils.isNullOrWhiteSpace(fileName)) {
            return;
        }

        File file = FileUtils.createFile(dir, fileName);
        FileWriter fWriter = null;
        try {
            fWriter = new FileWriter(file, append);
            fWriter.write(lineBreak ? "\n" + info : info);
            fWriter.flush();
            fWriter.close();
        } catch (IOException e) {
            Logger.e(e);
        } finally {
            try {
                if (null != fWriter) {
                    fWriter.close();
                }
            } catch (Exception e2) {
            }
        }
    }

    /**
     * @param aFile
     * @return
     */
    public static boolean deleteFile(File aFile) {
        if (aFile == null) {
            return false;
        }

        boolean bResult = aFile.delete();

        return bResult;
    }

    /**
     * delete file
     *
     * @param files
     */
    public static void deleteFiles(List<File> files) {
        if (null == files || files.size() == 0) {
            return;
        }

        for (File file : files) {
            if (null == file) {
                continue;
            }

            try {
                deleteFile(file);
            } catch (Exception e) {
                Logger.e(e);
            }
        }
    }

    /**
     * 删除所有文件
     */
    public static boolean removeAllFile(String path) {
        try {
            File fileDirectory = new File(path);
            File[] files = fileDirectory.listFiles();
            if (files == null) {
                return true;
            }
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static boolean del(String sPath) {
        if (isNull(sPath)) {
            return false;
        }

        boolean flag = deleteDir(sPath);
        if (!flag) {
            flag = deleteFile(sPath);
        }

        return flag;
    }

    /**
     * delete文件
     *
     * @param sPath
     * @return
     */
    public static boolean deleteFile(String sPath) {
        if (isNull(sPath)) {
            return false;
        }

        boolean flag = false;
        File file = new File(sPath);
        if (file.exists() && file.isFile()) {
            file.delete();
            flag = true;
        } else {
            flag = false;
        }

        return flag;
    }

    /**
     * delete一级目录
     *
     * @param sPath
     * @return
     */
    public static boolean deleteDir(String sPath) {
        if (isNull(sPath))
            return false;

        boolean flag = false;
        File file = new File(sPath);
        if (file.exists() && file.isDirectory()) {
            File[] tmpF = file.listFiles();
            for (File file2 : tmpF) {
                file2.delete();
            }
            file.delete();
            flag = true;
        } else {
            flag = false;
        }

        return flag;
    }

    /**
     * copyFile
     *
     * @param srcFile
     * @param destPath
     * @return
     */
    public static boolean copyFile(String srcFile, String destPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(srcFile);
            if (!oldfile.exists()) {// 文件不存在时
                return false;
            }

            File dir = new File(destPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            InputStream inStream = new FileInputStream(srcFile); // 读入原文件
            FileOutputStream fs = new FileOutputStream(destPath + File.separator + oldfile.getName());
            byte[] buffer = new byte[1444];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread; // 字节数 文件大小
                // System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
                fs.flush();
            }
            inStream.close();
            fs.close();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * 复制文件保存
     *
     * @param srcFile  源文件绝对路径
     * @param fileName 保存的文件名称
     * @param destPath 目的路径
     * @return
     */
    public static boolean copyFileByName(String srcFile, String fileName, String destPath) {
        File oldfile = new File(srcFile);
        if (!oldfile.exists()) {// 文件不存在时
            return false;
        }

        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(srcFile);
            fileOutputStream = new FileOutputStream(destPath + File.separator + fileName);
            // 一次读取1024字节
            byte[] bytes = new byte[1024];
            while (fileInputStream.read(bytes) != -1) {
                fileOutputStream.write(bytes);
                fileOutputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    /**
     * movie file
     *
     * @param srcFile
     * @param destPath
     */
    public static boolean moveFile(String srcFile, String destPath) {
        if (isNull(srcFile))
            return false;

        File file = new File(srcFile);
        if (!file.exists())
            return false;

        File dir = new File(destPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // Move file to new directory
        return file.renameTo(new File(dir, file.getName()));
    }

    /**
     * 用新的内容替换原有File内容
     *
     * @param filePath
     * @param newString
     * @return
     */
    public static boolean replaceFileContent(String filePath, String newString) {
        if (isNull(filePath))
            return false;

        boolean bResult = true;
        try {
            File aFile = new File(filePath);
            if (aFile.exists()) {
                aFile.delete();
            }

            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(aFile), "utf-8");
            // /Writer writer = new FileWriter(aFile);
            writer.write(newString);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            bResult = false;
        }

        return bResult;
    }

    public static boolean replaceFileContent(String filePath, byte[] newString) {
        if (isNull(filePath))
            return false;
        boolean bResult = true;
        try {
            File aFile = new File(filePath);
            if (aFile.exists()) {
                aFile.delete();
            }
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(aFile), "utf-8");
            // /Writer writer = new FileWriter(aFile);
            writer.write(new String(newString, "utf-8"));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            bResult = false;
        }
        return bResult;
    }

    private static boolean isNull(String str) {
        boolean b = false;
        if (str == null || str.length() == 0 || str.trim().length() == 0) {
            b = true;
        }

        return b;
    }

}
