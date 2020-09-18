package com.ailink.db;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


import com.ailink.constants.WebUrls;
import com.ailink.util.CheckNet;
import com.ailink.util.Logg;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import juhuiwan.ailink.android.wxapi.WeixinUtil;
import okhttp3.OkHttpClient;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 *
 */
public class MyApplication extends Application {
    private static MyApplication application;
    public boolean isConnectNet = true;


    public IWXAPI weixinApi;




    public static MyApplication getContext()
    {
        return application;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        init();
        weixinApi= WeixinUtil.registerToWeixin();
        OkHttpUtils.getInstance().sslSocketFactory(WebUrls.Certificate, getApplicationContext());
        //由于这个证书是全局添加的，那么会导致调用外部的url的时候，会把这个证书添加进去，直接导致别的url调用不成功，比如https://www.hao123.com
        //解决的方法是我另外在单独写了原始的调用url的接口，不再使用okhttp
    }

    private void init() {
        //注册一个全局广播，比如里面有检测网络变化
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.conn.LINK_STATE_CHANGED");
        filter.addAction("android.intent.action.MEDIA_MOUNTED");
        filter.addAction("android.intent.action.MEDIA_EJECT");
        registerReceiver(baseReceiver, filter);
    }



    BroadcastReceiver baseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (CheckNet.getAPNType(application) > 0) {
                isConnectNet = true;
            } else {
                isConnectNet = false;
            }
        }
    };
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Logg.e("内存不够了--------------------------------------");

    }



}
