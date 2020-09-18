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
import com.ailink.pojo.UserPojo;
import com.ailink.util.NumberUtil;
import com.ailink.view.LoadingDialog;
import com.ailink.view.util.ToastUtil;

import juhuiwan.ailink.android.R;


/**
 * 这是平仓的对话框，里面包含逻辑的
 */
public class DialogDeleteContract {
    Activity activity;
    private Dialog dialog;
    private Button btn01,btn02;
    private TextView txtContent01,txtContent02,txtContent03;
    private ResultListener resultListener;
    private LoadingDialog loadingDialog;

    int lanCount;
    long contractId;
    public DialogDeleteContract(Activity context,int lanCount, long contractId, ResultListener resultListener) {
        this.resultListener=resultListener;
        loadingDialog=LoadingDialog.getInstance();
        activity = context;
        this.lanCount=lanCount;
        this.contractId=contractId;
    }
    public void showDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.dialog_view_delete_contract, null);
        btn01=(Button) v.findViewById(R.id.btn_01);
        btn02=(Button) v.findViewById(R.id.btn_02);
        txtContent01=(TextView) v.findViewById(R.id.txt_content01);
        txtContent02=(TextView) v.findViewById(R.id.txt_content02);
        txtContent03=(TextView) v.findViewById(R.id.txt_content03);
        String text01="是否确认取消这份合约？";
        String text02="之前由系统托管的"+lanCount+"蓝钻";
        String text03="将在取消完成后进行返还。";

        TextColorSetting.addTextColor3String(txtContent02,"之前由系统托管的", lanCount+"","蓝钻",
                Color.parseColor("#333333"),Color.parseColor("#5080FD"),Color.parseColor("#333333"));



        txtContent01.setText(text01);
//        txtContent02.setText(text02);
        txtContent03.setText(text03);

        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog!=null&&dialog.isShowing()){
                    dialog.dismiss();
                }
                startDeleteContract();
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


    private void startDeleteContract(){
        loadingDialog.showDialogLoading(true,activity,null);
        ServerUtil.sendCancelContract(contractId, new HttpObjectListener() {
            public void onSucess(Object object) {
                ToastUtil.showToast("取消成功！",activity);
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
