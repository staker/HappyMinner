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
public class RegisterActivity extends BaseActivity {

    private Button btnXieYi, btnCode, btnRegister;
    private TextView txtXieYi, txtQuhao;
    private EditText edtPhone, edtCode, edtPwd01, edtPwd02;

    SMMobUtil smMobUtil;
    TopBarLayoutUtil topBar;
    boolean isXieYiYes = true;


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
        setContentView(R.layout.activity_register);

        txtXieYi = (TextView) findViewById(R.id.txt_xieyi);
        txtQuhao = (TextView) findViewById(R.id.txt_phone_quhao);
        edtPhone = (EditText) findViewById(R.id.edt_phone);
        edtCode = (EditText) findViewById(R.id.edt_code);
        edtPwd01 = (EditText) findViewById(R.id.edt_pwd01);
        edtPwd02 = (EditText) findViewById(R.id.edt_pwd02);
        btnXieYi = (Button) findViewById(R.id.btn_xieyi);
        btnCode = (Button) findViewById(R.id.btn_code);
        btnRegister = (Button) findViewById(R.id.btn_register);
        initViewsClickLinstener(txtXieYi, btnRegister, txtQuhao);
        initIdsClickLinstener(R.id.btn_code);
        initTopBar();
        init();
    }

    private void init() {
        loadingDialog = LoadingDialog.getInstance();
        ButtonCheckBoxUtil buttonCheckBoxUtil = new ButtonCheckBoxUtil(btnXieYi, R.drawable.icon_xieyi_yes, R.drawable.icon_xieyi_no);
        buttonCheckBoxUtil.setChecked(true);
        buttonCheckBoxUtil.setCheckLisetener(new ButtonCheckBoxUtil.CheckLisetener() {
            @Override
            public void isCheck(boolean isChecked) {
                isXieYiYes = isChecked;
                checkRegisterButton();
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
                checkRegisterButton();
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

        edtPwd01.addTextChangedListener(new TextWatcher() {
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

        edtPwd02.addTextChangedListener(new TextWatcher() {
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
    }

    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
        topBar.seBottomLineColor(Color.argb(255, 234, 234, 234));
        topBar.setTitle("注册");
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
            btnRegister.setEnabled(false);
            btnRegister.setBackgroundResource(R.drawable.selector_shape_gray_5);
            return;
        }
        String password = edtCode.getText().toString().trim();
        if (password.length() <= 0) {
            btnRegister.setEnabled(false);
            btnRegister.setBackgroundResource(R.drawable.selector_shape_gray_5);
            return;
        }

        String pwd01 = edtPwd01.getText().toString().trim();
        if (pwd01.length() <= 0) {
            btnRegister.setEnabled(false);
            btnRegister.setBackgroundResource(R.drawable.selector_shape_gray_5);
            return;
        }
        String pwd02 = edtPwd02.getText().toString().trim();
        if (pwd02.length() <= 0) {
            btnRegister.setEnabled(false);
            btnRegister.setBackgroundResource(R.drawable.selector_shape_gray_5);
            return;
        }


        if (!isXieYiYes) {
            btnRegister.setEnabled(false);
            btnRegister.setBackgroundResource(R.drawable.selector_shape_gray_5);
            return;
        }
        btnRegister.setEnabled(true);
        btnRegister.setBackgroundResource(R.drawable.selector_color_gradient_green_55);
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
        if(number.length() == 11){
            if( number.startsWith("170")|| number.startsWith("171")){
                ToastUtil.showToast("暂不支持170和171的手机号码段", this);
                return;
            }

        }
        loadingDialog.showDialogLoading(true,mContext,null);
        String quhao=txtQuhao.getText()+"";
        quhao=quhao.substring(1, quhao.length());
        smMobUtil.sendCode(quhao,number);
    }



    //    private void getCode() {
//        String number = edtPhone.getText().toString().trim();
//        if (number.length() != 11) {
//            ToastUtil.showToast("请输入11位手机号码", this);
//            return;
//        }
//        if (!PhoneDeviceUtil.isPhoneNumberValid(number)) {
//            ToastUtil.showToast("请输入正确的手机号码", this);
//            return;
//        }
//        loadingDialog.showDialogLoadingCube(true, this, null);
//        ServerUtil.getRegisterPhoneCode(number, new HttpObjectListener() {
//            @Override
//            public void onSucess(Object object) {
//                loadingDialog.showDialogLoadingCube(false, mContext, null);
//                ToastUtil.showToast("验证码已发送到手机，请注意查收", mContext);
//                timeSecond = 60;
//                btnCode.setText("60s");
//                handler.sendEmptyMessageDelayed(0, 1000);
//                btnCode.setEnabled(false);
//            }
//
//            @Override
//            public void onDataError(String dataError) {
//                super.onDataError(dataError);
//                loadingDialog.showDialogLoadingCube(false, mContext, null);
//                ToastUtil.showToast(dataError + "", mContext);
//
//            }
//        });
//
//
//    }
    private void register() {
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
        String pwd01 = edtPwd01.getText().toString().trim();
        if (pwd01.length() < 6) {
            ToastUtil.showToast("密码的长度不能小于6位", this);
            return;
        }
        String pwd02 = edtPwd02.getText().toString().trim();

        if (!pwd01.equals(pwd02)) {
            ToastUtil.showToast("两次输入密码不一致", this);
            return;
        }

        if (!isXieYiYes) {
            ToastUtil.showToast("请先确认阅读并已阅读用户协议！", this);
            return;
        }



        String quhao=txtQuhao.getText()+"";
        quhao=quhao.substring(1, quhao.length());
        loadingDialog.showDialogLoading(true, mContext, null);
        //到了这里直接调用注册的接口
        ServerUtil.sendRegister(quhao,number, pwd01, code, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                loadingDialog.showDialogLoading(false, mContext, null);
                //到这里就注册成功了
                showIntoMeSettingActivity();
            }

            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                loadingDialog.showDialogLoading(false, mContext, null);
                ToastUtil.showToast("" + dataError, mContext);
            }
        });

    }

    private void showIntoMeSettingActivity() {
        DialogUtil.showDialogNormal(this, "是否现在去设置个人信息？", "暂不", "好的", new DialogUtil.OnIndexButtonListener() {
            @Override
            public void onClickIndex(int buttonIndex) {
                if (buttonIndex == 2) {
                    JumpActivityUtil.showMeSettingActivity(mContext, true);
                } else {
                    JumpActivityUtil.showNormalActivityFinishSelf(mContext, MainHomeActivity.class);
                }
                finish();
            }
        });

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.txt_xieyi:
                JumpActivityUtil.showWebActivity(this, "用户协议", WebUrls.App_Xieyi, false);
                break;
            case R.id.btn_register:
                register();
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
