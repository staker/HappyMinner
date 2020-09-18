package com.ailink.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ailink.util.Logg;
import com.ailink.view.ProgressWebView;
import com.ailink.view.util.TopBarLayoutUtil;

import juhuiwan.ailink.android.R;


/**
 * 这是一个比较通用的网页界面，非常好用。里面暂时屏蔽了右上角的分享功能。
 */
public class HuodongActivity extends BaseActivity {
    private String title = "";
    private String pageUrl = "";
    private boolean isReplaceTitle;

    private ProgressWebView webview;
    private JavaScriptInterface javaScriptInterface;
    public boolean isShare = false;

    private MyDownloadListener myDownloadListener;

    TopBarLayoutUtil topBarLayout;
//
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(isReplaceTitle){
                topBarLayout.setTitle(title);
            }
            switch (msg.what){
                case 1://分享
                    Logg.e("33333333333333333333333333");
                    break;
            }
            return false;
        }
    });



    public void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_web);
        pageUrl =getIntent().getStringExtra("pageUrl");//必传
        title = getIntent().getStringExtra("title");//可以不传 如果isReplaceTitle为false则为传的title，否则取网页title
        isReplaceTitle= getIntent().getBooleanExtra("isReplaceTitle",true);

        if(title == null){
            title = "";
        }
        initWebView(pageUrl);
        topBarLayout=new TopBarLayoutUtil(this);
        topBarLayout.setTitle(title);
        topBarLayout.seBottomLineColor(Color.argb(255, 234, 234, 234));
        topBarLayout.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                finish();
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(webview != null){
            webview.removeAllViews();
            webview.destroy();
            webview = null;
        }
    }

    private void initWebView(String url) {
        webview = (ProgressWebView) findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if(isReplaceTitle){
                    HuodongActivity.this.title = title;
                    handler.sendEmptyMessage(1);
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                webview.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webview.getSettings().setJavaScriptEnabled(true);//支持JS
        webview.getSettings().setUseWideViewPort(true);  //将图片调整到适合webview的大小
        webview.getSettings().setSupportZoom(false);  //支持缩放
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS); //支持内容重新布局
        webview.getSettings().supportMultipleWindows();  //多窗口
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE );  //关闭webview中缓存
        webview.getSettings().setAllowFileAccess(true);  //设置可以访问文件
        webview.getSettings().setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        webview.getSettings().setBuiltInZoomControls(false); //设置支持缩放
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webview.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webview.getSettings().setLoadsImagesAutomatically(true);  //支持自动加载图片
        javaScriptInterface = new JavaScriptInterface();
        webview.addJavascriptInterface(javaScriptInterface, "javaScriptInterface");
        myDownloadListener = new MyDownloadListener();
        webview.setDownloadListener(myDownloadListener);

        webview.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (webview != null && webview.canGoBack())
            webview.goBack();
        else
            super.onBackPressed();
    }


    private class JavaScriptInterface {
        /**
         * JS交互获取分享内容
         *
         * @param param
         */
        @JavascriptInterface
        public void setShareParam(String param) {
            if (!"".equals(param)) {
                try {
//                    mShareEntity = GsonUtil.getInstance().json2Bean(param,ShareEntity.class);
                    isShare = true;
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
//                    mShareEntity = null;
                    isShare = false;
                    handler.sendEmptyMessage(1);
                }
            } else {
                isShare = false;
                handler.sendEmptyMessage(1);
            }
        }

        @JavascriptInterface
        public void finishActivity(){
            Logg.e("4444444444444444444444444444444444444444");
            try{
                finish();
            }catch (Exception e){
            }
        }
    }

    /**
     * WebView 下载回调
     */
    private class MyDownloadListener implements DownloadListener{
        @Override
        public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            try{
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }catch (Exception e){
                Logg.e("下载异常..");
            }
        }
    }

}