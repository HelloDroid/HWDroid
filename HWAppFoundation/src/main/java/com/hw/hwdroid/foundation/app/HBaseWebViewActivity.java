package com.hw.hwdroid.foundation.app;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.hw.hwdroid.foundation.R;
import com.hw.hwdroid.foundation.R2;
import com.hw.hwdroid.foundation.app.model.ViewModelActivity;
import com.hw.hwdroid.foundation.app.widget.HWebView;
import com.hw.hwdroid.foundation.utils.StringUtils;
import com.hw.hwdroid.foundation.utils.network.NetWorkUtils;

import butterknife.BindView;

/**
 * Created by ChenJ on 2017/2/16.
 */
// @HContentViewRes(R.layout.activity_web_view)
public class HBaseWebViewActivity<ViewModelData extends ViewModelActivity> extends HBaseActivity<ViewModelData> {


    public static final String EXTRA_URL = "web_view_url";
    public static final String EXTRA_TITLE = "web_view_title";


    @BindView(R2.id.web_view)
    HWebView mWebView;

    @BindView(R2.id.progress_bar)
    ProgressBar mProgressBar;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = getIntent().getStringExtra(EXTRA_URL);
        // getTitleBarView().getTitleBarBackView().setVisibility(View.INVISIBLE);
        setTitle(StringUtils.changeNull(getIntent().getStringExtra(EXTRA_TITLE)));

        mWebView.setWebViewClient(myWebViewClient);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (isFinishing()) {
                    return;
                }

                if (null != mProgressBar) {
                    mProgressBar.setProgress(newProgress);
                }
            }
        });

        if (NetWorkUtils.isNetworkAvailable(getApplication())) {
            mWebView.loadUrl(url);
        }
    }

    @Override
    protected void onDestroy() {
        if (null != mWebView) {
            mWebView.stopLoading();
            mWebView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.activity_pullup_enter_in, R.anim.activity_pullup_back_out);
    }

    private WebViewClient myWebViewClient = new WebViewClient() {
        boolean isSuccess = true;

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
            view.loadUrl(url);
            return true;
        }

        // 有页面跳转时被回调
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (null != mProgressBar) {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(0);
            }
        }

        // 页面跳转结束后被回调
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (null != mProgressBar) {
                mProgressBar.setVisibility(View.GONE);
            }

            if (null != mWebView) {
                if (isSuccess) {
                    mWebView.setVisibility(View.VISIBLE);
                } else {
                    isSuccess = true;
                    mWebView.setVisibility(View.GONE);
                }
            }
        }

        // 出错
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            isSuccess = false;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // super.onReceivedSslError(view, handler, error);

            //handler.cancel(); 默认的处理方式，WebView变成空白页

            // 接受所有证书
            handler.proceed();

            //handleMessage(Message msg); 其他处理
        }
    };

}
