package com.ailink.activity;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ailink.logic.ForWebActivityUtil;
import com.ailink.util.Logg;
import com.ailink.util.StakerUtil;
import com.ailink.view.ProgressWebView;

import juhuiwan.ailink.android.R;
import libs.util.umeng.UmengShareUtil;


/**
 * 这是一个比较通用的网页界面，非常好用。里面暂时屏蔽了右上角的分享功能。
 */
public class RankGiftActivity extends BaseActivity  {
    private String title = "";
    private String pageUrl = "";

    private ProgressWebView webview;
    private JavaScriptInterface javaScriptInterface;

    private MyDownloadListener myDownloadListener;


    String serverMethod,serverJson;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what){
                case 0:
                    finish();
                    break;
                case 1://分享
                    Logg.e("t11111111111111111111111111111111ype=");
//                    String status="success";
//                    String json="123";
//                    String temp="javascript:hmcallback('"+serverMethod+"','"+status+"','"+json+"')";
//                    webview.loadUrl(temp);
                    UmengShareUtil.shareByServerJson(mContext,serverJson);
                    break;
                case 2://复制
                    String temp="javascript:hmcallback('"+serverMethod+"','"+"success"+"','"+""+"')";
                    StakerUtil.copyStringToClipboard(mContext,ForWebActivityUtil.getCopyString(serverJson));
                    webview.loadUrl(temp);
                    break;
            }
            return false;
        }
    });



    public void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_rank_gift);
        initIdsClickLinstener(R.id.layout_back);
        TextView textView=(TextView) findViewById(R.id.txt_title);
        pageUrl =getIntent().getStringExtra("pageUrl");//必传
        Logg.e("webview="+pageUrl);
        title = getIntent().getStringExtra("title");//可以不传 如果isReplaceTitle为false则为传的title，否则取网页title

        if(title == null){
            title = "";
        }
        textView.setText("奖励&规则");
        initWebView(pageUrl);


        init();


    }
    private void init() {

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
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT );  //关闭webview中缓存
        webview.getSettings().setAllowFileAccess(true);  //设置可以访问文件
        webview.getSettings().setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        webview.getSettings().setBuiltInZoomControls(false); //设置支持缩放
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webview.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webview.getSettings().setLoadsImagesAutomatically(true);  //支持自动加载图片
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setDatabaseEnabled(true);
        webview.getSettings().setDatabasePath(getApplicationContext().getCacheDir().getAbsolutePath());
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
         * JS交互获取分享或者界面等等之类的交互
         */
        @JavascriptInterface
        public void setRunResult(String type,String json,String methodId) {
            serverMethod=methodId;
            serverJson=json;
            Logg.e("serverMethod="+serverMethod);
            Logg.e("serverJson="+serverJson);
            Logg.e("type="+type);
            if ("share".equals(type)) {
                handler.sendEmptyMessage(1);
            } else if("finish".equals(type)){
                handler.sendEmptyMessage(0);
            } else if("copy".equals(type)){
                handler.sendEmptyMessage(2);
            } else if("photo".equals(type)){
                handler.sendEmptyMessage(3);
            }else {

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


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.layout_back:
                onBackPressed();
                break;
            case R.id.layout_gift:

            break;
        }
    }
}