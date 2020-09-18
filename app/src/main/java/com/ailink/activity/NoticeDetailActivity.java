package com.ailink.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.pojo.NoticePojo;
import com.ailink.util.ImageCacheUtil;
import com.ailink.util.Logg;
import com.ailink.view.LoadingDialog;
import com.ailink.view.util.TopBarLayoutUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import juhuiwan.ailink.android.R;


/**
 * 新闻详情界面  2017/7/20.
 */
public class NoticeDetailActivity extends BaseActivity {

    private TopBarLayoutUtil topBar;
    private TextView txtContent;
    private LoadingDialog loadingDialog;

    int noticeId;
    NoticePojo noticePojo;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_notice_detail);
        txtContent = (TextView) findViewById(R.id.txt_content);
        noticeId = getIntent().getIntExtra("noticeId", 0);
        loadingDialog = LoadingDialog.getInstance();
        initTopBar();
        refreshData();
    }

    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
        topBar.seBottomLineColor(Color.argb(255, 234, 234, 234));
        topBar.setTitle("通知详情");
        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    private void refreshData() {
        loadingDialog.showDialogLoading(true, this, null);
        ServerUtil.getNoticeDetail(noticeId, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                loadingDialog.showDialogLoading(false, mContext, null);
                NoticePojo temp = (NoticePojo) object;
                initNoticeData(temp);
            }

            @Override
            public void onDataError(String dataError) {
                loadingDialog.showDialogLoading(false, mContext, null);

            }

            public void onException(Exception e) {
                loadingDialog.showDialogLoading(false, mContext, null);
            }
        });

    }

    private void initNoticeData(NoticePojo noticePojo) {
        if (noticePojo == null) {
            return;
        }
        this.noticePojo = noticePojo;
        txtContent.setText(Html.fromHtml(noticePojo.content, imgGetter, null));
        txtContent.setMovementMethod(LinkMovementMethod.getInstance());
    }

    //加载图片后显示html文字
    private Handler handler = new android.os.Handler() {
        public void handleMessage(android.os.Message msg) {
            txtContent.setText(Html.fromHtml(noticePojo.content, imgGetter, null));
            txtContent.setMovementMethod(LinkMovementMethod.getInstance());
        }
    };


    //加载网络图片
    Html.ImageGetter imgGetter = new Html.ImageGetter() {
        @Override
        public Drawable getDrawable(String source) {//参数为image的src属性
            Drawable drawable = null;
            File imageFile = ImageCacheUtil.getFileFromSDcard(source);
            if (imageFile != null && imageFile.exists()) {//本地存在直接加载本地图片
                //获取本地文件返回Drawable
                drawable = Drawable.createFromPath(imageFile.getAbsolutePath());
                //设置图片边界
                drawable.setBounds(0, 0, 400, 400);//给图片设置大小和位置
            } else {//图片没有家再过，则重新加载图片
                loadImage(source);
            }
            return drawable;
        }
    };


    //加载图片
    private void loadImage(final String imageUrl) {
        new Thread() {
            public void run() {
                try {
                    String filePath = ImageCacheUtil.getImagePath(imageUrl);
                    if (filePath != null) {
                        handler.sendEmptyMessage(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }



}
