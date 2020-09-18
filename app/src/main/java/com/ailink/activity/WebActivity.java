package com.ailink.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ailink.logic.ForWebActivityUtil;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.util.BitmapFactoryUtil;
import com.ailink.util.ImageHandleUtil;
import com.ailink.util.Logg;
import com.ailink.util.StakerUtil;
import com.ailink.view.ProgressWebView;
import com.ailink.view.util.ToastUtil;
import com.ailink.view.util.TopBarLayoutUtil;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;

import org.json.JSONObject;

import java.io.File;

import juhuiwan.ailink.android.R;
import libs.util.umeng.UmengShareUtil;


/**
 * 这是一个比较通用的网页界面，非常好用。里面暂时屏蔽了右上角的分享功能。
 */
public class WebActivity extends BaseActivity implements  TakePhoto.TakeResultListener,InvokeListener {
    private String title = "";
    private String pageUrl = "";
    private boolean isReplaceTitle;

    private ProgressWebView webview;
    private JavaScriptInterface javaScriptInterface;

    private MyDownloadListener myDownloadListener;

    TopBarLayoutUtil topBarLayout;


    String serverMethod,serverJson;
//下面是为了调用相册用到的
final int Take_Photo = 1, Pick_Pictrue = 2;
    File tempFile;
String tempPath ;
    int RequestStoragePermissionCode=100;//申请读取图片的写文件的权限
    InvokeParam invokeParam;
    TakePhoto takePhoto;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(isReplaceTitle){
                topBarLayout.setTitle(title);
            }
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
                case 3:
                    checkPerm();
                    break;
                case 4:
                    JumpActivityUtil.showMinerHomeActivity(mContext,ForWebActivityUtil.getHomeActivityUserId(serverJson),"","");
                    break;
            }
            return false;
        }
    });



    public void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_web);
        pageUrl =getIntent().getStringExtra("pageUrl");//必传
        Logg.e("webview="+pageUrl);
        title = getIntent().getStringExtra("title");//可以不传 如果isReplaceTitle为false则为传的title，否则取网页title
        isReplaceTitle= getIntent().getBooleanExtra("isReplaceTitle",true);

        if(title == null){
            title = "";
        }
        initWebView(pageUrl);
        topBarLayout=new TopBarLayoutUtil(this);
        topBarLayout.setTitle(title);
        topBarLayout.setLeftText("返回");
        topBarLayout.seBottomLineColor(Color.argb(255, 234, 234, 234));
        topBarLayout.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                onBackPressed();
            }
        });
        topBarLayout.setRightText("关闭", new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                finish();
            }
        });
        init();
        getTakePhoto();



    }
    private void init() {
        tempPath= StakerUtil.getSDPath()+"/happy_miner/miner_default_head.jpg";
        tempFile = new File(tempPath);
        if (!tempFile.exists()) {
            // 如果这个临时文件不存在点话，则新建一个父的空文件夹
            File vDirPath = tempFile.getParentFile();
            vDirPath.mkdirs();
            try{
                tempFile.createNewFile();
            }catch (Exception e){

            }
        }else{
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        webview.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webview.onResume();
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
                    WebActivity.this.title = title;
                    handler.sendEmptyMessage(1000);
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
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT );  //关闭webview中缓存
        webview.getSettings().setAllowFileAccess(true);  //设置可以访问文件
        webview.getSettings().setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        webview.getSettings().setBuiltInZoomControls(false); //设置支持缩放
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webview.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webview.getSettings().setLoadsImagesAutomatically(true);  //支持自动加载图片
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setDatabaseEnabled(true);
        webview.getSettings().setDefaultTextEncodingName("UTF-8");
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
            }else if("goToDetail".equals(type)){
                handler.sendEmptyMessage(4);
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




































    protected void doPickPictrue() {




        try {
            // 调用系统相册选择图片，并按照给定点大小进行裁剪
            // Launch picker to choose photo for selected contact
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            // DisplayMetrics dm = getResources().getDisplayMetrics();
            int width = 400;
            int height = 400;
            intent.putExtra("crop", "true");
            // 这里只是设置图片的裁剪比例，并不是图片的大小，所以直接用1：1就可以，因为头像是正方形
//            intent.putExtra("aspectX", 1);
//            intent.putExtra("aspectY", 1);

            // 图片输出大小(指图片尺寸的大小)
//            intent.putExtra("outputX", width);
//            intent.putExtra("outputY", height);
            intent.putExtra("scale", true);
            intent.putExtra("noFaceDetection", true);
            intent.putExtra("return-data", false);
            intent.putExtra("output", Uri.fromFile(new File(tempPath)));// 裁剪之后的图片直接放在这个文件里面
            startActivityForResult(intent, Pick_Pictrue);
        } catch (ActivityNotFoundException e) {
            Logg.e("选择相片出异常", e);
        }
    }
    private void checkPerm() {
        /**1.在AndroidManifest文件中添加需要的权限。
         *
         * 2.检查权限
         *这里涉及到一个API，ContextCompat.checkSelfPermission，
         * 主要用于检测某个权限是否已经被授予，方法返回值为PackageManager.PERMISSION_DENIED
         * 或者PackageManager.PERMISSION_GRANTED。当返回DENIED就需要进行申请授权了。
         * */
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){ //权限没有被授予
            /**3.申请授权
             * @param
             *  @param activity The target activity.（Activity|Fragment、）
             * @param permissions The requested permissions.（权限字符串数组）
             * @param requestCode Application specific request code to match with a result（int型申请码）
             *    reported to {@link OnRequestPermissionsResultCallback#onRequestPermissionsResult(
             *    int, String[], int[])}.
             * */
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},RequestStoragePermissionCode);
        }else{//权限被授予
            doPickPictrue();
            //直接操作
        }

    }





    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }


    /**
     *  获取TakePhoto实例
     * @return
     */
    public TakePhoto getTakePhoto(){
        if (takePhoto==null){
            takePhoto= (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this,this));
        }
        return takePhoto;
    }
    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type=PermissionManager.checkPermission(TContextWrap.of(this),invokeParam.getMethod());
        if(PermissionManager.TPermissionType.WAIT.equals(type)){
            this.invokeParam=invokeParam;
        }
        return type;
    }

    @Override
    public void takeCancel() {
        Logg.e("takeCancel");
    }

    @Override
    public void takeSuccess(TResult tResult) {
        Logg.e("takeSuccess");
//                GlideUtil.getInstance().setImage(imgHead,tempPath);
        Bitmap bitmap= BitmapFactoryUtil.readBmpFromPath(tempPath,500,500);
        byte[] fileStream= ImageHandleUtil.compressBitmap(bitmap,500);
        String base64= Base64.encodeToString(fileStream,Base64.DEFAULT);
       //拿到的图片的base64


        try{
            JSONObject ob=new JSONObject();
            ob.put("str",base64);
            String temp="javascript:hmcallback('"+serverMethod+"','"+"success"+"','"+base64+"')";
            webview.loadUrl(temp);
        }catch (Exception e){

        }



    }

    @Override
    public void takeFail(TResult tResult, String s) {
        Logg.e("takeFail");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        Logg.e("requestCode="+requestCode);
        switch (requestCode) {
            case Pick_Pictrue: // 调用Gallery返回的(包括直接选择和裁剪)
                takeSuccess(null);
//                GlideUtil.getInstance().setImage(imgHead,tempPath);
//                Bitmap bitmap= BitmapFactoryUtil.readBmpFromPath(tempPath,200,200);
//                String fileStream=ImageHandleUtil.compressBitmap(bitmap,100).toString();
//                ServerUtil.sendUpdateAvatar(fileStream, new HttpObjectListener() {
//                    @Override
//                    public void onSucess(Object object) {
//                        ToastUtil.showToast("头像上传成功!",mContext);
//                    }
//                    @Override
//                    public void onDataError(String dataError) {
//                        super.onDataError(dataError);
//                        ToastUtil.showToast(""+dataError,mContext);
//                    }
//                });
                break;
            case Take_Photo: {// 照相机程序返回的,再次调用图片剪辑程序去修剪图片
//                doCropPhoto();
                break;
            }
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
        switch (requestCode) {
            case 2000:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tempFile = new File(tempPath);
                    if (!tempFile.exists()) {
                        // 如果这个临时文件不存在点话，则新建一个父的空文件夹
                        File vDirPath = tempFile.getParentFile();
                        vDirPath.mkdirs();
                    }
                }else{
                    ToastUtil.showToast("权限没有开启",mContext);
                }
                break;
        }

        if(requestCode ==RequestStoragePermissionCode){//读取图片和写文件的时候返回的权限
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tempFile = new File(tempPath);
                if (!tempFile.exists()) {
                    // 如果这个临时文件不存在点话，则新建一个父的空文件夹
                    File vDirPath = tempFile.getParentFile();
                    vDirPath.mkdirs();
                }
                doPickPictrue();
            }else{
                ToastUtil.showToast("权限没有开启",mContext);
            }
        }

    }

}