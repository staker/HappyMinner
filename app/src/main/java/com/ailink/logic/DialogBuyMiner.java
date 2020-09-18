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
import com.ailink.pojo.UserPojo;
import com.ailink.util.NumberUtil;
import com.ailink.view.LoadingDialog;
import com.ailink.view.util.ToastUtil;

import juhuiwan.ailink.android.R;


/**
 * 这是平仓的对话框，里面包含逻辑的
 */
public class DialogBuyMiner {
    Activity activity;
    private Dialog dialog;
    private Button btn01,btn02;
    private TextView txtContent;
    private ResultListener resultListener;
    private LoadingDialog loadingDialog;
    private UserPojo userPojo;
//    private long userId;
//    private double price;


//    userPojo.priceNew, userSelf.userId
    public DialogBuyMiner(Activity context,UserPojo userPojo,ResultListener resultListener) {
        this.resultListener=resultListener;
//        this.userId=userId;
//        this.price=price;
        this.userPojo=userPojo;
        loadingDialog=LoadingDialog.getInstance();
        activity = context;
    }
    public void showDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.dialog_view_buy_miner, null);
        btn01=(Button) v.findViewById(R.id.btn_01);
        btn02=(Button) v.findViewById(R.id.btn_02);
        txtContent=(TextView) v.findViewById(R.id.txt_content);
        if(userPojo.priceDown){
            int totalPrice=(int)(userPojo.added+userPojo.priceNew);
            String text="多次购买同一用户，\n"+"需要额外支付"+userPojo.added+"蓝钻，\n"+"总计花费"+totalPrice+"蓝钻\n"+"是否继续购买?";
            txtContent.setText(text);
        }else {
            TextColorSetting.addTextColor3String(txtContent,"您将消耗", NumberUtil.keep1DecimalString(userPojo.priceNew),"蓝钻购买此矿工",
                    Color.argb(255,51,51,51),Color.argb(255,255,0,0),Color.argb(255,51,51,51));
        }


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
                if(dialog!=null&&dialog.isShowing()){
                    dialog.dismiss();
                }
                startBuyMiner();

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


    private void startBuyMiner(){
        loadingDialog.showDialogLoading(true,activity,null);
        ServerUtil.sendOperateMiner(userPojo.userId, "buy", new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                loadingDialog.showDialogLoading(false,activity,null);
                ToastUtil.showToast("购买成功！",activity);
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
