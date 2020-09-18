package com.ailink.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ailink.constants.WebUrls;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.i.ResultListener;
import com.ailink.logic.DialogSelectQuhao;
import com.ailink.logic.DialogUtil;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.view.LoadingDialog;
import com.ailink.view.util.ButtonCheckBoxUtil;
import com.ailink.view.util.ToastUtil;
import com.ailink.view.util.TopBarLayoutUtil;

import juhuiwan.ailink.android.R;
import libs.smmob.SMMobUtil;


/**
 *
 */
public class BindPhoneActivity extends BaseActivity {

    private Button btnCode, btnBindPhone;
    private TextView txtQuhao;

    private  EditText edtPhone,edtCode;
    SMMobUtil smMobUtil;
    TopBarLayoutUtil topBar;


    private LoadingDialog loadingDialog;
    private int timeSecond = 60;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            timeSecond--;
            if (timeSecond <= 0) {
                btnCode.setEnabled(true);
                btnCode.setText("获取验证码");
            } else {
                btnCode.setText(timeSecond + "s");
                handler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };


    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bind_phone);
        txtQuhao = (TextView) findViewById(R.id.txt_phone_quhao);
        btnCode = (Button) findViewById(R.id.btn_code);
        btnBindPhone = (Button) findViewById(R.id.btn_bind_phone);
        edtPhone = (EditText) findViewById(R.id.edt_phone);
        edtCode = (EditText) findViewById(R.id.edt_code);
        initViewsClickLinstener( btnBindPhone, txtQuhao);
        initIdsClickLinstener(R.id.btn_code);
        initTopBar();
        init();
    }

    private void init() {
        loadingDialog = LoadingDialog.getInstance();

        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                checkRegisterButton();
            }
        });

        smMobUtil=new SMMobUtil(new HttpObjectListener() {
            public void onSucess(Object object) {
                loadingDialog.showDialogLoading(false,mContext,null);
                ToastUtil.showToast("验证码已发送到手机，请注意查收", mContext);
                timeSecond = 60;
                btnCode.setText("60s");
                handler.sendEmptyMessageDelayed(0, 1000);
                btnCode.setEnabled(false);
            }
            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                loadingDialog.showDialogLoading(false,mContext,null);
                ToastUtil.showToast(dataError + "", mContext);
            }
        });


        edtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkRegisterButton();
            }
        });
    }

    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
        topBar.seBottomLineColor(Color.argb(255, 234, 234, 234));
        topBar.setTitle("绑定手机");
        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                JumpActivityUtil.showNormalActivity(mContext, LoginActivity.class);
                finish();

            }
        });
    }

    private void checkRegisterButton() {
        String number = edtPhone.getText().toString().trim();
        if (number.length() <= 0) {
            btnBindPhone.setEnabled(false);
            btnBindPhone.setBackgroundResource(R.drawable.selector_shape_gray_5);
            return;
        }
        String code = edtCode.getText().toString().trim();
        if (code.length() <= 0) {
            btnBindPhone.setEnabled(false);
            btnBindPhone.setBackgroundResource(R.drawable.selector_shape_gray_5);
            return;
        }
        btnBindPhone.setEnabled(true);
        btnBindPhone.setBackgroundResource(R.drawable.selector_color_gradient_green_55);
    }

    @Override
    public void onBackPressed() {
        JumpActivityUtil.showNormalActivity(mContext, LoginActivity.class);
        super.onBackPressed();
    }



    private void getCode() {
        String number = edtPhone.getText().toString().trim();
        if (number.length() == 0) {
            ToastUtil.showToast("请输入手机号码", this);
            return;
        }
        loadingDialog.showDialogLoading(true,mContext,null);
        String quhao=txtQuhao.getText()+"";
        quhao=quhao.substring(1, quhao.length());
        smMobUtil.sendCode(quhao,number);
    }


    private void bindPhone() {
        String number = edtPhone.getText().toString().trim();
        if (number.length() == 0) {
            ToastUtil.showToast("请输入手机号码", this);
            return;
        }
        String code = edtCode.getText().toString().trim();
        if (code.length() == 0) {
            ToastUtil.showToast("请输入验证码", this);
            return;
        }

        String quhao=txtQuhao.getText()+"";
        quhao=quhao.substring(1, quhao.length());
        loadingDialog.showDialogLoading(true, mContext, null);

        ServerUtil.sendBindPhone(quhao,number, code, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                loadingDialog.showDialogLoading(false,mContext,null);
                ToastUtil.showToast("手机绑定成功",mContext);
                finish();
            }
            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                loadingDialog.showDialogLoading(false,mContext,null);
                ToastUtil.showToast(dataError+"",mContext);

            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_bind_phone:
                bindPhone();
                break;
            case R.id.btn_code:
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
