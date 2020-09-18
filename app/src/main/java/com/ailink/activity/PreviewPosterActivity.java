package com.ailink.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.ailink.db.ApplicationData;
import com.ailink.db.Configuration;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.util.Logg;

import juhuiwan.ailink.android.R;
import libs.glide.GlideUtil;


/**
 *
 */
public class PreviewPosterActivity extends Activity implements View.OnClickListener{

    private ImageView imgPoster;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_poster);
        imgPoster=(ImageView)findViewById(R.id.img_poster);
        imgPoster .setOnClickListener(this);
        findViewById(R.id.mainLayout).setOnClickListener(this);
        init();
    }
//
//    @Override
//    protected void initViews(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_preview_poster);
//        imgPoster=(ImageView)findViewById(R.id.img_poster);
//        imgPoster .setOnClickListener(this);
//        init();
//    }

    private void init() {
//        Logg.e("海报地址="+ApplicationData.getInstance().previewPoster);
//        if(ApplicationData.getInstance().previewPoster!=null){
//            GlideUtil.getInstance().setImage(imgPoster,ApplicationData.getInstance().previewPoster);
//        }
    }



    @Override
    public void onClick(View v) {
            finish();
    }
}
