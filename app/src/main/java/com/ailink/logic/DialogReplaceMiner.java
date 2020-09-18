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
import com.ailink.util.Logg;
import com.ailink.util.NumberUtil;
import com.ailink.view.LoadingDialog;
import com.ailink.view.util.ToastUtil;

import org.json.JSONObject;

import juhuiwan.ailink.android.R;


/**
 * 这是平仓的对话框，里面包含逻辑的
 */
public class DialogReplaceMiner {
    Activity activity;
    private Dialog dialog;
    private Button btn01, btn02;
    private TextView txtContent01, txtContent02;
    private ResultListener resultListener;
    private LoadingDialog loadingDialog;
    private long srcMinerCode;//自己的矿工
    private long destMinerCode;//别人的矿工

    private double replaceAsset;
    private double needAsset;

    public DialogReplaceMiner(Activity context, long srcMinerCode, long destMinerCode,ResultListener resultListener) {
        this.resultListener=resultListener;
        this.destMinerCode = destMinerCode;
        this.srcMinerCode = srcMinerCode;
        loadingDialog = LoadingDialog.getInstance();
        activity = context;
    }

    public void showDialog() {
        checkReplaceMiner();
    }

    private void showDialogTure() {


        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.dialog_view_replace_miner, null);
        btn01 = (Button) v.findViewById(R.id.btn_01);
        btn02 = (Button) v.findViewById(R.id.btn_02);
        txtContent01 = (TextView) v.findViewById(R.id.txt_content01);
        txtContent02 = (TextView) v.findViewById(R.id.txt_content02);
//        5738321 是否替换

        txtContent01.setText("原有矿工抵扣 "+replaceAsset+" 蓝钻");

        TextColorSetting.addTextColor3String(txtContent02," 还需支付 ",""+needAsset," 蓝钻",Color.parseColor("#333333"),Color.parseColor("#aa0000"),Color.parseColor("#333333"));

        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        btn02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                startReplaceMiner();

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


    private void startReplaceMiner() {
        loadingDialog.showDialogLoading(true, activity, null);
        ServerUtil.sendReplaceMiner(srcMinerCode, destMinerCode, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                String result = (String) object;
                loadingDialog.showDialogLoading(false,activity,null);
                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONObject dataJson = mainJson.optJSONObject("data");
                    int status = dataJson.optInt("status");
                    if (status == 1) {
                        if(resultListener!=null){
                            resultListener.onSucess(null);
                        }
                        ToastUtil.showToast("替换成功", activity);
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog = null;
                        }
                    } else {
                        String errMsg = dataJson.optString("errMsg");
                        ToastUtil.showToast(errMsg, activity);
                    }
                } catch (Exception e) {
                    ToastUtil.showToast("数据异常", activity);
                }
            }

            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                 ToastUtil.showToast(dataError,activity);
                loadingDialog.showDialogLoading(false,activity,null);
            }
        });
//        ServerUtil.sendOperateMiner(userId, "buy", new HttpObjectListener() {
//            @Override
//            public void onSucess(Object object) {
//                loadingDialog.showDialogLoading(false,activity,null);
//                ToastUtil.showToast("购买成功！",activity);
//                if(resultListener!=null){
//                    resultListener.onSucess(null);
//                }
//            }
//            @Override
//            public void onDataError(String dataError) {
//                super.onDataError(dataError);
//                ToastUtil.showToast(dataError,activity);
//                loadingDialog.showDialogLoading(false,activity,null);
//                if(resultListener!=null){
//                    resultListener.onDataError(dataError);
//                }
//            }
//        });
    }






//    {"data":{"status":1,"replaceAsset":689.5,"needAsset":0,"errMsg":""},"code":200,"msg":"","t":1526356178452}
    private void checkReplaceMiner() {
        ServerUtil.getCheckReplaceMiner(srcMinerCode, destMinerCode, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                String result = (String) object;
                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONObject dataJson = mainJson.optJSONObject("data");
                    int status = dataJson.optInt("status");
                    if (status == 1) {
                        replaceAsset=dataJson.optDouble("replaceAsset");
                        needAsset=dataJson.optDouble("needAsset");
                        showDialogTure();
                    } else {
                        String errMsg = dataJson.optString("errMsg");
                        ToastUtil.showToast(errMsg, activity);
                    }
                } catch (Exception e) {
                    ToastUtil.showToast("数据异常", activity);
                }
            }
        });


    }

}
