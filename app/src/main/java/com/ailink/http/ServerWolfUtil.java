package com.ailink.http;

import com.ailink.constants.WebUrls;
import com.ailink.i.HttpObjectListener;
import com.ailink.json.JsonString;
import com.ailink.json.WolfJsonUtil;
import com.ailink.util.Logg;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

import libs.okhttps.MyOkHttpsUtil;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Staker on 2017/7/11.
 */

public class ServerWolfUtil {


    //获取狼人杀的基本信息，里面包含最后一个购买人id
    public static void getWolfPool(final HttpObjectListener listener) {
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Wolf_Pool, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("打印狼人杀信息="+response);
                if (listener!=null){
                    listener.onSucess(WolfJsonUtil.getWolfLastNameList(response));
                }

            }
        });
    }





    public static void sendBuyWolfWater(int team,int keyCount,int keyPrice,final HttpObjectListener listener) {

        String url= WebUrls.Send_Buy_Wolf_Pool;
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

                Logg.e("购买金水信息信息="+response);
                listener.onSucess(null);

            }
        });
    }








}
