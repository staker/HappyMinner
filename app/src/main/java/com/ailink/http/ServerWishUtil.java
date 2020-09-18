package com.ailink.http;

import android.widget.TextView;

import com.ailink.constants.WebUrls;
import com.ailink.db.ApplicationData;
import com.ailink.i.HttpObjectListener;
import com.ailink.json.JsonString;
import com.ailink.json.JsonUtil;
import com.ailink.pojo.DailyMissionPojo;
import com.ailink.util.Logg;
import com.ailink.util.NumberUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import libs.okhttp.MyOkHttpUtil;
import libs.okhttps.MyOkHttpsUtil;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Staker on 2017/7/11.
 */

public class ServerWishUtil {


    //获取许愿池的基本信息，里面包含最后一个购买人id
    public static void getWishPool(final HttpObjectListener listener) {
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Wish_Pool, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
            }
            @Override
            public void onResponse(String response,int id) {

                Logg.e("打印许愿池信息="+response);


            }
        });
    }




    public static void sendJoinLottery(final HttpObjectListener listener) {
        String url= WebUrls.Send_Lottery_Join;
        MyOkHttpsUtil.sendPostAddHeader(url, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("参与抢矿信息="+response);


            }
        });
    }



    public static void sendBuyWishStone(int team,int keyCount,int keyPrice,final HttpObjectListener listener) {

        String url= WebUrls.Send_Buy_Wish_Stone;
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("keyCount", keyCount+"");
        map.put("team", team+"");
        map.put("keyPrice", keyPrice+"");
        map.put("canPremium", "true");

        MyOkHttpsUtil.sendPostAddHeader(url, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
            }
            @Override
            public void onResponse(String response,int id) {

                Logg.e("购买许愿石信息信息="+response);


            }
        });
    }








}
