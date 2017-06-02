package com.hw.hwdroid.foundation.utils.http;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hw.hwdroid.foundation.utils.StringUtils;
import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by chenjian on 16/1/20.
 */
public class HttpUrlConnectionUtils {

    /**
     * 设置HttpsURLConnection信任所有证书
     */
    public static void setTrustAllCertificates4HttpsURLConnection() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        // Install the all-trusting trust manager
        try {// 注意这部分一定要
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    Log.i("RestUtilImpl", "Approving certificate for " + hostname);
                    return true;
                }
            });
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * post请求
     *
     * @param urlStr
     * @param param
     * @param headers
     * @param timeout
     * @return
     */
    public static String doPost(@NonNull String urlStr, Bundle param, Bundle headers, int timeout) {
        if (StringUtils.isNullOrWhiteSpace(urlStr)) {
            return "";
        }


        OutputStream os = null;
        HttpURLConnection conn = null;
        try {
            Logger.d(urlStr);

            boolean isHttps = urlStr.startsWith(HttpConstantValues.HPPTS_SCHEME);

            if (isHttps) {
                conn = getHttpsURLConnection(true, new URL(urlStr), headers, timeout);
            } else {
                conn = getHttpURLConnection(true, new URL(urlStr), headers, timeout);
            }


            if (null == conn) {
                return "";
            }

            conn.connect();

            String paramStr = URLEncodedUtils.formatParams(param);

            // 设置Cookie
            // connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

            if (!StringUtils.isNullOrWhiteSpace(paramStr)) {
                os = conn.getOutputStream();
                os.write(paramStr.toString().getBytes("utf-8"));
                os.flush();
            }

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "";
            }

            return dealResponseResult(conn.getInputStream());
        } catch (Exception e) {
            Logger.e(e);
        } finally {
            try {
                if (null != conn) {
                    conn.disconnect();
                }
            } catch (Exception e) {
            }
            try {
                if (null != os) {
                    os.close();
                }
            } catch (IOException e) {
            }
        }

        return "";
    }

    /**
     * get请求
     *
     * @param urlStr  url
     * @param param   参数
     * @param headers header
     * @param timeout time out
     * @return
     */
    public static String doGet(@NonNull String urlStr, Bundle param, Bundle headers, int timeout) {
        HttpURLConnection conn = null;
        try {
            String paramStr = URLEncodedUtils.formatParams(param);

            if (!StringUtils.isNullOrWhiteSpace(paramStr)) {
                urlStr += "?" + paramStr;
            }

            Logger.d(urlStr);

            boolean isHttps = urlStr.startsWith(HttpConstantValues.HPPTS_SCHEME);

            if (isHttps) {
                conn = getHttpsURLConnection(true, new URL(urlStr), headers, timeout);
            } else {
                conn = getHttpURLConnection(true, new URL(urlStr), headers, timeout);
            }

            // conn.setRequestProperty("Content-Type", "text/html; charset=UTF-8");

            if (null == conn) {
                return "";
            }

            conn.connect();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "";
            }

            return dealResponseResult(conn.getInputStream());
        } catch (Exception e) {
            Logger.e(e);
        } finally {
            try {
                if (null != conn) {
                    conn.disconnect();
                }
            } catch (Exception e) {
            }
        }

        return "";
    }


    /**
     * get Https Connection
     *
     * @param post
     * @param url
     * @param headers
     * @param timeout
     * @return
     */
    public static HttpsURLConnection getHttpsURLConnection(boolean post, @NonNull URL url, Bundle headers, int timeout) {
        if (null == url) {
            return null;
        }

        HttpsURLConnection conn = null;
        try {
            conn = (HttpsURLConnection) url.openConnection();

            if (timeout <= 0) {
                timeout = HttpConstantValues.DEFAULT_TIMEOUT;
            }

            conn.setReadTimeout(timeout);
            conn.setConnectTimeout(timeout);

            if (null != headers && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    if (StringUtils.isNullOrWhiteSpace(key) || StringUtils.isNullOrWhiteSpace(headers.getString(key))) {
                        continue;
                    }

                    conn.setRequestProperty(key, headers.getString(key));
                }
            }

            if (post) {
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setInstanceFollowRedirects(true);
                conn.setRequestMethod(HttpConstantValues.POST);
            } else {
                conn.setRequestMethod(HttpConstantValues.GET);
            }
        } catch (Exception ignored) {
            Logger.e(ignored);
        }


        return conn;
    }

    /**
     * get HttpURLConnection
     *
     * @param post
     * @param url
     * @param headers
     * @param timeout
     * @return
     */
    public static HttpURLConnection getHttpURLConnection(boolean post, @NonNull URL url, Bundle headers, int timeout) {
        if (null == url) {
            return null;
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();

            if (timeout <= 0) {
                timeout = HttpConstantValues.DEFAULT_TIMEOUT;
            }

            conn.setReadTimeout(timeout);
            conn.setConnectTimeout(timeout);

            if (null != headers && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    if (StringUtils.isNullOrWhiteSpace(key) || StringUtils.isNullOrWhiteSpace(headers.getString(key))) {
                        continue;
                    }

                    conn.setRequestProperty(key, headers.getString(key));
                }
            }

            if (post) {
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setInstanceFollowRedirects(true);
                conn.setRequestMethod(HttpConstantValues.POST);
            } else {
                conn.setRequestMethod(HttpConstantValues.GET);
            }
        } catch (Exception ignored) {
            Logger.e(ignored);
        }


        return conn;
    }

    /**
     * 处理服务器的响应结果（将输入流转化成字符串）
     *
     * @param connIS inputStream服务器的响应输入流
     * @return
     */
    private static String dealResponseResult(InputStream connIS) {
        if (null == connIS) {
            return "";
        }

        BufferedReader br = null;
        ByteArrayOutputStream byteArrayOS = null;
        try {
            //            br = new BufferedReader(new InputStreamReader(connIS));
            //
            //            String line;
            //            StringBuffer resultBuff = new StringBuffer();
            //            while ((line = br.readLine()) != null) {
            //                resultBuff.append(line);
            //            }
            //
            //            return resultBuff.toString();

            byteArrayOS = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;

            while ((len = connIS.read(buffer)) != -1) {
                byteArrayOS.write(buffer, 0, len);
            }

            byteArrayOS.flush();
            String result = new String(byteArrayOS.toByteArray(), "utf-8");

            Logger.json(result);

            return result;
        } catch (Exception e) {
            Logger.e(e);
        } finally {
            try {
                if (null != connIS) {
                    connIS.close();
                }
            } catch (IOException e) {
            }
            try {
                if (null != br) {
                    br.close();
                }
            } catch (IOException e) {
            }
            try {
                if (null != byteArrayOS) {
                    byteArrayOS.close();
                }
            } catch (IOException e) {
            }
        }

        return "";
    }


}
