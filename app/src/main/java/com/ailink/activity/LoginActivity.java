package com.ailink.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ailink.constants.WebUrls;
import com.ailink.db.Configuration;
import com.ailink.db.MyApplication;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.i.ResultListener;
import com.ailink.logic.DialogSelectQuhao;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.util.Logg;
import com.ailink.view.LoadingDialog;
import com.ailink.view.util.ButtonCheckBoxUtil;
import com.ailink.view.util.ToastUtil;
import com.ailink.view.util.TopBarLayoutUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import juhuiwan.ailink.android.R;
import juhuiwan.ailink.android.wxapi.WeixinUtil;
import libs.okhttp.MyOkHttpUtil;
import libs.okhttps.MyOkHttpsUtil;
import libs.smmob.SMMobUtil;
import okhttp3.Call;
import okhttp3.Response;


/**
 *
 */
public class LoginActivity extends BaseActivity {

    private Button btnXieYi,btnCode,btnLogin;
    private TextView txtLogin01, txtLogin02,txtXieYi,txtQuhao;
    private EditText edtPhone,edtPwdCode;


    SMMobUtil smMobUtil;
    TopBarLayoutUtil topBar;
    boolean isLogin01 = true,isXieYiYes=true;
    private LoadingDialog loadingDialog;
    private int timeSecond=60;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            timeSecond--;
            if(timeSecond<=0){
                btnCode.setEnabled(true);
                btnCode.setText("获取验证码");
            }else{
                btnCode.setText(timeSecond+"s");
                handler.sendEmptyMessageDelayed(0,1000);
            }
        }
    };



    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);

        txtLogin01 = (TextView) findViewById(R.id.txt_login01);
        txtLogin02 = (TextView) findViewById(R.id.txt_login02);
        txtXieYi = (TextView) findViewById(R.id.txt_xieyi);
        txtQuhao = (TextView) findViewById(R.id.txt_phone_quhao);
        edtPhone = (EditText) findViewById(R.id.edt_phone);
        edtPwdCode = (EditText) findViewById(R.id.edt_password);
        btnXieYi=(Button)findViewById(R.id.btn_xieyi);
        btnCode=(Button)findViewById(R.id.btn_code);
        btnLogin=(Button)findViewById(R.id.btn_login);
        initIdsClickLinstener(R.id.btn_weixin);
        initViewsClickLinstener(txtLogin01, txtLogin02,txtXieYi,btnCode,btnLogin,txtQuhao);
        initTopBar();
        init();
    }

    private void init() {
        loadingDialog=LoadingDialog.getInstance();
        ButtonCheckBoxUtil buttonCheckBoxUtil=new ButtonCheckBoxUtil(btnXieYi,R.drawable.icon_xieyi_yes,R.drawable.icon_xieyi_no);
        buttonCheckBoxUtil.setChecked(true);
        buttonCheckBoxUtil .setCheckLisetener(new ButtonCheckBoxUtil.CheckLisetener() {
            @Override
            public void isCheck(boolean isChecked) {
                isXieYiYes=isChecked;
                checkLoginButton();
            }
        });

        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                checkLoginButton();
            }
        });
        edtPwdCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                checkLoginButton();
            }
        });

        smMobUtil=new SMMobUtil(new HttpObjectListener() {
            public void onSucess(Object object) {
                loadingDialog.showDialogLoading(false,mContext,null);
                ToastUtil.showToast("验证码已发送到手机，请注意查收",mContext);
                timeSecond=60;
                btnCode.setText("60s");
                handler.sendEmptyMessageDelayed(0,1000);
                btnCode.setEnabled(false);
            }
            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                loadingDialog.showDialogLoading(false,mContext,null);
                ToastUtil.showToast(dataError+"",mContext);
            }
        });



        MyOkHttpsUtil.sendGet("https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxdd0d5625afe09ce6&secret=1e49558182440a17344e92d5612ce47a&code=07165sAb0d9jYt1LnfDb0BklAb065sAp&grant_type=authorization_code", new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                Logg.e("resp.response="+response);
//                finish();
            }
            @Override
            public void onResponse(String response,int id) {
                ToastUtil.showToast(""+response,mContext);
                Logg.e("resp.response="+response);
//                login(response);
            }
        });
//        MyOkHttpUtil.getUrlResult("https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxdd0d5625afe09ce6&secret=1e49558182440a17344e92d5612ce47a&code=07165sAb0d9jYt1LnfDb0BklAb065sAp&grant_type=authorization_code", new HttpObjectListener() {
//            @Override
//            public void onSucess(Object object) {
//                Logg.e("resp.object="+object);
//            }
//        });

//        MyOkHttpUtil.getUrlResult("https://www.hao123.com/", new StringCallback() {
//            @Override
//            public void onError(Call call, Response response, Exception e, int id) {
////                finish();
////                ToastUtil.showToast("33333="+response,mContext);
//                Logg.e("resp.onError="+response);
//            }
//            @Override
//            public void onResponse(String response,int id) {
//                ToastUtil.showToast(""+response,mContext);
//                Logg.e("resp.onResponse="+response);
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Configuration.getInstance(this).getUserToken() != null){
            JumpActivityUtil.showNormalActivityFinishSelf(this, MainHomeActivity.class);
        }
    }

    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
        topBar.seBottomLineColor(Color.argb(255, 234, 234, 234));
        topBar.setTitle("登录");
        topBar.setRightText("注册", new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                JumpActivityUtil.showNormalActivityFinishSelf(mContext,RegisterActivity.class);
            }
        });
//        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
//            @Override
//            public void onClick() {
//                finish();
//            }
//        });
        topBar.hideLeft();
    }

    private void clickOtherLogin(boolean isClickLogin01) {
        if(isLogin01&&isClickLogin01){
            return;
        }else if(!isLogin01&&!isClickLogin01){
            return;
        }
        if (isLogin01) {
            txtLogin01.setTextColor(Color.parseColor("#333333"));
            txtLogin02.setTextColor(Color.parseColor("#022889"));
            btnCode.setVisibility(View.VISIBLE);
            edtPwdCode.setHint("验证码");
            edtPwdCode.setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else {
            txtLogin02.setTextColor(Color.parseColor("#333333"));
            txtLogin01.setTextColor(Color.parseColor("#022889"));
            btnCode.setVisibility(View.GONE);
            edtPwdCode.setHint("输入密码");
            edtPwdCode.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
        }
        isLogin01 = !isLogin01;
    }

    private void login(){
        if(isLogin01){
            loginByPassword();
        }else{
            loginByCode();
        }
    }
    private void checkLoginButton(){
        String number=edtPhone.getText().toString().trim();
        if(number.length()<=0){
            btnLogin.setEnabled(false);
            btnLogin.setBackgroundResource(R.drawable.selector_shape_gray_5);
            return;
        }
        String password=edtPwdCode.getText().toString().trim();
        if(password.length()<=0){
            btnLogin.setEnabled(false);
            btnLogin.setBackgroundResource(R.drawable.selector_shape_gray_5);
            return;
        }
        if(!isXieYiYes){
            btnLogin.setEnabled(false);
            btnLogin.setBackgroundResource(R.drawable.selector_shape_gray_5);
            return;
        }
        btnLogin.setEnabled(true);
        btnLogin.setBackgroundResource(R.drawable.selector_color_gradient_green_55);
    }
    private   void loginByPassword(){
        String number=edtPhone.getText().toString().trim();
        if(number.length()==0){
            ToastUtil.showToast("请输入登录手机号码",this);
            return;
        }
        String password=edtPwdCode.getText().toString().trim();
        if(password.length()<1){
            ToastUtil.showToast("请输入登录密码",this);
            return;
        }
        if(!isXieYiYes){
            ToastUtil.showToast("请先确认阅读并已阅读用户协议！",this);
            return;
        }
        loadingDialog.showDialogLoading(true,mContext,null);


        ServerUtil.sendLoginbyPassword(number, password, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                loadingDialog.showDialogLoading(false,mContext,null);
                JumpActivityUtil.showNormalActivityFinishSelf(mContext,MainHomeActivity.class);
            }
            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                loadingDialog.showDialogLoading(false,mContext,null);
                ToastUtil.showToast(dataError+"",mContext);
            }
        });




    }
    private void loginByCode(){
        String number=edtPhone.getText().toString().trim();
        if(number.length()==0){
            ToastUtil.showToast("请输入登录手机号码",this);
            return;
        }
        String code=edtPwdCode.getText().toString().trim();
        if(code.length()<1){
            ToastUtil.showToast("请输入验证码",this);
            return;
        }
        if(!isXieYiYes){
            ToastUtil.showToast("请先确认阅读并已阅读用户协议！",this);
            return;
        }
        String quhao=txtQuhao.getText()+"";
        quhao=quhao.substring(1, quhao.length());
        loadingDialog.showDialogLoading(true,this,null);
        ServerUtil.sendLoginByCode(quhao,number, code, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                loadingDialog.showDialogLoading(false,mContext,null);
                JumpActivityUtil.showNormalActivityFinishSelf(mContext,MainHomeActivity.class);

            }
            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                loadingDialog.showDialogLoading(false,mContext,null);
                ToastUtil.showToast(dataError+"",mContext);

            }
        });


    }

    private void getCode() {
        String number=edtPhone.getText().toString().trim();
        if(number.length()==0){
            ToastUtil.showToast("请输入手机号码",this);
            return;
        }

        loadingDialog.showDialogLoading(true,this,null);
        String quhao=txtQuhao.getText()+"";
        quhao=quhao.substring(1, quhao.length());
        smMobUtil.sendCode(quhao,number);
    }

//    private void getCode() {
//        String number=edtPhone.getText().toString().trim();
//        if(number.length()==0){
//            ToastUtil.showToast("请输入手机号码",this);
//            return;
//        }
//
//        loadingDialog.showDialogLoading(true,this,null);
//        ServerUtil.getLoginPhoneCode(number, new HttpObjectListener() {
//            @Override
//            public void onSucess(Object object) {
//                loadingDialog.showDialogLoading(false,mContext,null);
//                ToastUtil.showToast("验证码已发送到手机，请注意查收",mContext);
//                timeSecond=60;
//                btnCode.setText("60s");
//                handler.sendEmptyMessageDelayed(0,1000);
//                btnCode.setEnabled(false);
//            }
//
//            @Override
//            public void onDataError(String dataError) {
//                super.onDataError(dataError);
//                loadingDialog.showDialogLoading(false,mContext,null);
//                ToastUtil.showToast(dataError+"",mContext);
//
//            }
//        });
//
//    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_weixin:
                WeixinUtil.loginWeixin(MyApplication.getContext().weixinApi);
                break;
            case R.id.txt_login01:
                clickOtherLogin(true);
                break;
            case R.id.txt_login02:
                clickOtherLogin(false);
                break;
            case R.id.txt_xieyi:
                JumpActivityUtil.showWebActivity(this,"用户协议", WebUrls.App_Xieyi,false);
                break;

            case  R.id.btn_login:
                login();
                break;
            case  R.id.btn_code:
                getCode();
                break;
            case R.id.txt_phone_quhao:
                new DialogSelectQuhao(this, new ResultListener() {
                    @Override
                    public void onSucess(Object object) {
                        String quhao = (String) object;
                        txtQuhao.setText("+" + quhao);
                    }
                }).showDialog();
                break;
        }

    }
}
