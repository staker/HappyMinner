package com.ailink.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ailink.constants.WebUrls;
import com.ailink.db.Configuration;
import com.ailink.db.MyApplication;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.util.AsynTaskUtil;
import com.ailink.util.Logg;
import com.ailink.util.PhoneDeviceUtil;
import com.ailink.util.StakerUtil;
import com.ailink.view.util.StatusBarUtil;
import com.ailink.view.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;

import juhuiwan.ailink.android.R;
import juhuiwan.ailink.android.wxapi.WeixinUtil;
import libs.okhttp.MyOkHttpUtil;
import okhttp3.Call;
import okhttp3.Response;


/**
 *
 */
public class LuancherActivity extends BaseActivity {
    long startTime;
    private TextView txtVersion;
    @Override
    protected void initViews(Bundle savedInstanceState) {
        StakerUtil.setFullScreen(this);
        setContentView(R.layout.activity_luancher);
         startTime = System.currentTimeMillis();
        txtVersion=(TextView)findViewById(R.id.txt_version);
        txtVersion.setText("版本:"+PhoneDeviceUtil.getVersionName(this));


        if(Configuration.getInstance(this).getUserToken() != null){
            checkToken();
         }else{
            firstInto();
        }
    }



    private void init() {

    }


    private void checkToken(){


        ServerUtil.getCheckToken(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                String tokenStatus=(String)object;
                if("200".equals(tokenStatus)){
                    firstInto();
                }else{
                    ToastUtil.showToast("登录过期，请重新登录",mContext);
                    Configuration.getInstance(mContext).setUserToken(null);
                    firstInto();
                }
            }

            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                ToastUtil.showToast(dataError,mContext);
                finish();
            }
        });
    }
    private void firstInto(){
        new AsynTaskUtil().startTaskOnThread(new AsynTaskUtil.IAsynTask() {
            @Override
            public Object run() {
                try {
                    init();
                    long endTime = System.currentTimeMillis();
                    long subTime = endTime - startTime;
                    if (subTime > 3000) {
                        return null;
                    } else {
                        Thread.sleep(3000 - subTime);
                    }

                } catch (Exception e) {

                }
                return null;
            }

            @Override
            public void over(Object object) {
                initData();
            }
        });
    }

    private void initData() {
        if (Configuration.getInstance(mContext).isFirstRun()) {
            JumpActivityUtil.showNormalActivityFinishSelf(mContext, AppIntroActivity.class);
            Configuration.getInstance(mContext).setFirstRun(false);
        } else {
            if(Configuration.getInstance(this).getUserToken() == null){
                JumpActivityUtil.showNormalActivityFinishSelf(mContext, LoginActivity.class);
            }else{
                JumpActivityUtil.showNormalActivityFinishSelf(this, MainHomeActivity.class);
            }
        }


    }


}
