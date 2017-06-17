package com.hw.hwdroid.foundation.app

import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.hw.hwdroid.foundation.R
import com.hw.hwdroid.foundation.app.model.ViewModelActivity
import com.hw.hwdroid.foundation.app.widget.HWebView
import com.hw.hwdroid.foundation.utils.StringUtils
import com.hw.hwdroid.foundation.utils.network.NetWorkUtils

/**
 * Base WebView Activity
 * Created by ChenJ on 2017/2/16.
 */
// @HContentViewRes(R.layout.activity_web_view)
class HWBaseWebViewActivity<ViewModelData : ViewModelActivity> : HWBaseActivity<ViewModelData>() {

    companion object {
        val EXTRA_URL = "web_view_url"
        val EXTRA_TITLE = "web_view_title"
    }

    private val webView: HWebView? by bindOptionalView<HWebView>(R.id.web_view)
    private val progressBar: ProgressBar? by bindOptionalView<ProgressBar>(R.id.progress_bar)

    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        url = intent.getStringExtra(EXTRA_URL)
        title = StringUtils.changeNull(intent.getStringExtra(EXTRA_TITLE))

        webView?.setWebViewClient(myWebViewClient)
        webView?.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (isFinishing) {
                    return
                }

                progressBar?.progress = newProgress
            }
        })

        if (NetWorkUtils.isNetworkAvailable(application) && url != null) {
            webView?.loadUrl(url)
        }
    }

    override fun onDestroy() {
        webView?.stopLoading()
        webView?.destroy()

        super.onDestroy()
    }

    override fun finish() {
        super.finish()

        overridePendingTransition(R.anim.activity_pullup_enter_in, R.anim.activity_pullup_back_out)
    }

    private val myWebViewClient = object : WebViewClient() {
        internal var isSuccess = true

        override fun onLoadResource(view: WebView, url: String) {
            super.onLoadResource(view, url)
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
            view.loadUrl(url)
            return true
        }

        // 有页面跳转时被回调
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
            super.onPageStarted(view, url, favicon)

            progressBar?.visibility = View.VISIBLE
            progressBar?.progress = 0
        }

        // 页面跳转结束后被回调
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)

            progressBar?.visibility = View.GONE

            if (isSuccess) {
                webView?.visibility = View.VISIBLE
            } else {
                isSuccess = true
                webView?.visibility = View.GONE
            }
        }

        // 出错
        override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
            super.onReceivedError(view, errorCode, description, failingUrl)

            isSuccess = false
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            // super.onReceivedSslError(view, handler, error);

            //handler.cancel(); 默认的处理方式，WebView变成空白页

            // 接受所有证书
            handler.proceed()

            //handleMessage(Message msg); 其他处理
        }
    }

}
