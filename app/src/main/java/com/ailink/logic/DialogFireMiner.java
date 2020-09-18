package com.ailink.logic;


import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.i.ResultListener;
import com.ailink.view.LoadingDialog;
import com.ailink.view.util.ButtonCheckBoxUtil;
import com.ailink.view.util.ToastUtil;

import juhuiwan.ailink.android.R;


/**
 * 这是平仓的对话框，里面包含逻辑的
 */
public class DialogFireMiner {
    Activity activity;
    private Dialog dialog;
    private Button btn01,btn02,btnCheckBox;
    private ResultListener resultListener;
    private LoadingDialog loadingDialog;
    private long userId;
    private ButtonCheckBoxUtil checkBoxUtil;

    public DialogFireMiner(Activity context, long userId, ResultListener resultListener) {
        this.resultListener=resultListener;
        this.userId=userId;
        loadingDialog=LoadingDialog.getInstance();
        activity = context;
    }
    public void showDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.dialog_view_fire_miner, null);
        btn01=(Button) v.findViewById(R.id.btn_01);
        btn02=(Button) v.findViewById(R.id.btn_02);
        btnCheckBox=(Button) v.findViewById(R.id.btn_checkbox);
        checkBoxUtil=new ButtonCheckBoxUtil(btnCheckBox);
        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog!=null&&dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });

        btn02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkBoxUtil.getCheckedState()){
                    ToastUtil.showToast("请勾选确认解雇矿工",activity);
                    return;
                }
                if(dialog!=null&&dialog.isShowing()){
                    dialog.dismiss();
                }
                startFireMiner();

            }
        });
        dialog = new Dialog(activity, R.style.help_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(v);
        if (activity.isFinishing()) {
            dialog = null;
            return;
        }
        dialog.show();
    }
    private void startFireMiner(){
        loadingDialog.showDialogLoading(true,activity,null);
        ServerUtil.sendOperateMiner(userId, "fire", new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                loadingDialog.showDialogLoading(false,activity,null);
                if(resultListener!=null){
                    resultListener.onSucess(null);
                }
            }
            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                ToastUtil.showToast(dataError,activity);
                loadingDialog.showDialogLoading(false,activity,null);
                if(resultListener!=null){
                    resultListener.onDataError(dataError);
                }
            }
        });
    }

}
