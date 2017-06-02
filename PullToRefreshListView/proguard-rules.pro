# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/chenjian/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}

-ignorewarnings

-keep class *.R
-keepclasseswithmembers class **.R$* {    public static <fields>; }

# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

# 保持自定义控件类不被混淆
-keepattributes *Annotation*
-keepclassmembers class ** { @org.greenrobot.eventbus.Subscribe <methods>; }

-keep public class android.net.http.SslError
-keep public class android.webkit.WebViewClient

-keep public class * extends android.webkit.WebViewClient
-dontwarn android.content.**
-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient


-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep class * extends android.app.Dialog
-keep class * implements android.content.DialogInterface
-keep class * extends android.widget.PopupWindow
-keep class * implements android.widget.Adapter
-keep class * extends android.widget.BaseAdapter
-keep class * implements android.widget.ListAdapter
-keep public class com.android.vending.licensing.ILicensingService
-keep public class android.telephony.CellLocation
-keep public class android.view.View
-keep class * extends android.app.Fragment{
  public *;
  protected *;
}
-keep class * extends android.app.Application{
  public *;
  protected *;
}
-keep class * extends android.support.v4.app.Fragment{
   public *;
   protected *;
}

-keep public class javax.net.ssl.**
-keepclassmembers public class javax.net.ssl.** { *; }

-keep public class org.apache.http.**
-keepclassmembers public class org.apache.http.** { *; }

-keep class android.net.http.** { *; }
-keep class com.android.common.universalimageloader.** { *; }

# 保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable
-keep class * implements java.io.Serializable {
    public *;
    protected *;
}

-dontwarn org.mockito.**
-dontwarn sun.reflect.**
-dontwarn android.test.**

-keep class org.junit.** { *; }
-dontwarn org.junit.**

-keep class junit.** { *; }
-dontwarn junit.**

-keep class sun.misc.** { *; }
-dontwarn sun.misc.**

-keep class org.apache.** { *; }
-keep class com.google.** { *; }


#################################################################################
##############################   android support  ###############################
#################################################################################
# 保留android support包
-keep class android.support.** { *; }

# android support eg. v4、v7、annotations、design、compat==
-keep class android.support.** { *; }
-dontwarn android.support.**

# nineoldandroids
-keep class com.nineoldandroids.** { *; }
-keep public class com.nineoldandroids.animation.** { public *; }
-keep public class com.nineoldandroids.view.ViewHelper { public *; }

#############################################################################################
##############################   常用第三方模块的混淆选项  #####################################
#############################################################################################
# gson 用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错
# -libraryjars libs/gson-2.8.0.jar
-keepattributes Signature

# -keep class com.google.** { *; }
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *;}
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.** {
    <fields>;
    <methods>;
}
-dontwarn com.google.gson.**

#zxing
-keep class com.google.zxing.** { *; }

# butterknife -keep class butterknife.*
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keep public class * implements butterknife.Unbinder { public <init>(...); }

# AMap_Location
-keep class com.aps.** { *; }
-keep class com.amap.** { *; }
-keep class com.autonavi.** { *; }
-keep class com.amap.api.** { *; }
-keep class com.amap.api.location.** { *; }

# commons-cli
-keep class org.apache.commons.cli.** { *; }

# commons-codec
-keep class org.apache.commons.codec.** { *; }

# commons-logging
-keep class org.apache.** { *; }
-keep class org.apache.commons.** { *; }
-keep class org.apache.commons.lang3.** { *; }
-keep class org.apache.commons.logging.** { *; }
-keepnames class * implements org.apache.commons.logging.Log

# hamcrest-core
-keep class org.hamcrest.** { *; }
-keep class org.hamcrest.core.** { *; }
-keep class org.hamcrest.internal.** { *; }

# httpclient/httpmime/httpcore
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**

# jsr javax.annotation javax.inject
-keep class javax.** { *; }
-keep class javax.annotation.** { *; }

# junit
-keep class junit.** { *; }
-keep class org.junit.** { *; }

# okhttp
-keep class okhttp3.** { *; }
-keep class okio.** { *; }
-keep class com.squareup.** { *; }
-keep class com.squareup.okhttp.** { *; }
-dontwarn okio.**

# org.apache.heep.legacy
-keep class android.net.** { *; }
-keep class com.android.internal.http.** { *; }
-keep class org.apache.http.** { *; }
-keep class org.apache.commons.codec.** { *; }
-keep class org.apache.commons.logging.** { *; }

# rxbinding
-keep class com.jakewharton.rxbinding.** { *; }

# rxjava rxandroid
-keep class io.reactivex.** { *; }
-keep class rx.** { *; }
-keep class rx.android.** { *; }
-keep class org.reactivestreams.** { *; }

# kotlin
-keep class kotlin.** { *; }
-keep class org.jetbrains.annotations.** { *; }

# stetho
-keep class com.facebook.** { *; }
-keep class com.facebook.stetho.** { *; }
-keep class com.facebook.stetho.okhttp.** { *; }

# libmmsdk mta-sdk open-sdk
-keep class com.tencent.** { *; }

# fastjason android
-keep class com.alibaba.** { *; }
-keep class com.alibaba.fastjson.** { *; }

# retrofit2
-keep class retrofit2.** { *; }
-keep class com.jakewharton.retrofit2.** { *; }

# greendao
-keep class org.greenrobot.** { *; }
-keep class org.greenrobot.greendao.** { *; }

-keep class com.squareup.javawriter.** { *; }

# 百度sdk
-keep class com.baidu.** { *; }
-keep class vi.com.** { *; }
-keep class com.baidu.location.** { *; }
-dontwarn com.baidu.**

-keep class com.sina.** { *; }
-keep class com.weibo.** { *; }
-keep class com.alibaba.** { *; }
-keep class android.net.http.** { *; }

-dontwarn com.sina.**
-dontwarn com.tencent.**
-dontwarn com.alibaba.**

-keep class com.alipay.** { *; }
-keep class com.squareup.picasso.** { *; }
-keep class com.ut.*
-keep class com.samsung.android.sdk.**{ *; }
-keep class com.ta.utdid2.** { *; }
-keep class com.ut.device.** { *; }

-keep class a.**{*;}
-keep class b.**{*;}
-keep class c.**{*;}
-keep class d.**{*;}
-keep class e.**{*;}
-keep class f.**{*;}
-keep class g.**{*;}
-keep class h.**{*;}
-keep class i.**{*;}
-keep class j.**{*;}
-keep class k.**{*;}
-keep class l.**{*;}
-keep class m.**{*;}
-keep class n.**{*;}
-keep class o.**{*;}
-keep class p.**{*;}
-keep class q.**{*;}
-keep class r.**{*;}
-keep class s.**{*;}
-keep class t.**{*;}
-keep class u.**{*;}
-keep class v.**{*;}
