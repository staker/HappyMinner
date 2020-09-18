package com.ailink.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.view.LoadingDialog;
import com.ailink.view.util.ToastUtil;
import com.ailink.view.util.TopBarLayoutUtil;

import juhuiwan.ailink.android.R;


/**
 *
 */
public class ChangePwdActivity extends BaseActivity {

    private Button btnSure;
    private EditText edtOldPwd,edtNewPwd01,edtNewPwd02;



    TopBarLayoutUtil topBar;
    LoadingDialog loadingDialog;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_change_pwd);

        edtOldPwd = (EditText) findViewById(R.id.edt_old_pwd);
        edtNewPwd01 = (EditText) findViewById(R.id.edt_new_pwd01);
        edtNewPwd02 = (EditText) findViewById(R.id.edt_new_pwd02);
        btnSure=(Button) findViewById(R.id.btn_sure);
        initIdsClickLinstener(R.id.btn_sure);
        initTopBar();
        init();
    }

    private void init() {
        loadingDialog=LoadingDialog.getInstance();
        edtOldPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                checkSureButton();
            }
        });
        edtNewPwd01.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                checkSureButton();
            }
        });
        edtNewPwd02.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                checkSureButton();
            }
        });
    }
    private void checkSureButton(){
        String newPwd01=edtNewPwd01.getText().toString().trim();
        if(newPwd01.length()<=0){
            btnSure.setEnabled(false);
            btnSure.setBackgroundResource(R.drawable.selector_shape_gray_5);
            return;
        }

        String newPwd02=edtNewPwd02.getText().toString().trim();
        if(newPwd02.length()<=0){
            btnSure.setEnabled(false);
            btnSure.setBackgroundResource(R.drawable.selector_shape_gray_5);
            return;
        }
        btnSure.setEnabled(true);
        btnSure.setBackgroundResource(R.drawable.selector_color_gradient_green_55);
    }
    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
        topBar.seBottomLineColor(Color.argb(255, 234, 234, 234));
        topBar.setTitle("修改密码");
        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }




    private void sure(){

        String oldPwd=edtOldPwd.getText().toString().trim();


        String pwd01=edtNewPwd01.getText().toString().trim();
        if(pwd01.length()<6){
            ToastUtil.showToast("新密码的长度不能小于6位",this);
            return;
        }
        String pwd02=edtNewPwd02.getText().toString().trim();
        if(!pwd01.equals(pwd02)){
            ToastUtil.showToast("两次输入密码不一致",this);
            return;
        }


        loadingDialog.showDialogLoading(true,this,null);
        //开始调用修改密码的接口
        ServerUtil.sendUpdatePassword(oldPwd, pwd01, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                ToastUtil.showToast("密码修改成功！",mContext);
                loadingDialog.showDialogLoading(false,mContext,null);
                finish();
            }
            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                ToastUtil.showToast(""+dataError,mContext);
                loadingDialog.showDialogLoading(false,mContext,null);
            }
        });






    }



    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_sure:
                sure();
                break;
        }

    }
}
