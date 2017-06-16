package com.hw.hwdroid.foundation.app.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by ChenJ on 2017/2/16.
 */

public class HWebView extends WebView {

    public HWebView(Context context) {
        super(context);
        initWebView();
    }

    public HWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebView();
    }

    public HWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWebView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initWebView();
    }


    /**
     * 基本属性设置
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    private void initWebView() {
        setHorizontalScrollBarEnabled(false);
        // setVerticalScrollBarEnabled(false);
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(false);
        webSettings.setSavePassword(false);
        webSettings.setPluginState(WebSettings.PluginState.OFF);

        disableSecurityRisk();

        // Increase the priority of the rendering thread to high
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        // Enable application caching
        webSettings.setAppCacheEnabled(true);

        // Enable HTML5 local storage and make it persistent
        // webSettings.setDomStorageEnabled(true);
        // webSettings.setDatabaseEnabled(true);
        // webSettings.setDatabasePath("/dataList/dataList/" +
        // WebViewActivity.this.getPackageName() + "/databases/");

        //webSettings.setSupportZoom(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        // 自适应屏幕
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);


        // Clear spurious cache dataList
        clearHistory();
        clearFormData();
        // clearCache(true);
        // webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // Set viewing area
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSaveFormData(false);
        //webSettings.setBuiltInZoomControls(true);
        // Accept cookies
        // CookieSyncManager.createInstance(WebViewActivity.this);
        // CookieManager cookieManager = CookieManager.getInstance();
        // cookieManager.setAcceptCookie(true);

        // Make sure that the webview does not allocate blank space on the side
        // for the scrollbars
        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void disableSecurityRisk() {
        // 删除存在漏洞的对象
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            removeJavascriptInterface("searchBoxJavaBridge_");
        }
    }

}
