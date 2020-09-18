package com.ailink.logic;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.i.ResultListener;
import com.ailink.pojo.ContractPojo;
import com.ailink.view.LoadingDialog;
import com.ailink.view.util.ToastUtil;

import juhuiwan.ailink.android.R;


/**
 * 这是平仓的对话框，里面包含逻辑的
 */
public class DialogConfirmContract {
    Activity activity;
    private Dialog dialog;
    private Button btn01,btn02;
    private TextView txtContent01,txtContent02,txtContent03;
    private ResultListener resultListener;
    private LoadingDialog loadingDialog;
    ContractPojo contractPojo;

    public DialogConfirmContract(Activity context, ContractPojo contractPojo, ResultListener resultListener) {
        this.resultListener=resultListener;
        this.contractPojo=contractPojo;
        loadingDialog=LoadingDialog.getInstance();
        activity = context;
    }
    public void showDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.dialog_view_confirm_contract, null);
        btn01=(Button) v.findViewById(R.id.btn_01);
        btn02=(Button) v.findViewById(R.id.btn_02);
        txtContent01=(TextView) v.findViewById(R.id.txt_content01);
        txtContent02=(TextView) v.findViewById(R.id.txt_content02);
        String text01="此次交易,将要消耗蓝钻：";
        String text02="此次交易,将会增加算力：";

        TextColorSetting.addTextColor2String(txtContent01,text01, contractPojo.lan+"",
                Color.parseColor("#333333"),Color.parseColor("#5080FD"));

        TextColorSetting.addTextColor2String(txtContent02,text02, contractPojo.lan/50+"",
                Color.parseColor("#333333"),Color.parseColor("#FB4E63"));



        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog!=null&&dialog.isShowing()){
                    dialog.dismiss();
                }
                startConfirmContract();
            }
        });

        btn02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog!=null&&dialog.isShowing()){
                    dialog.dismiss();
                }
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


    private void startConfirmContract(){
        loadingDialog.showDialogLoading(true,activity,null);
        ServerUtil.sendConfirmContract(contractPojo.contractId, new HttpObjectListener() {
            public void onSucess(Object object) {
                ToastUtil.showToast("合约交易成功！",(Activity) activity);
                loadingDialog.showDialogLoading(false,activity,null);
                resultListener.onSucess(null);
            }
            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                ToastUtil.showToast(dataError,activity);
                loadingDialog.showDialogLoading(false,activity,null);
            }
        });
    }

}
