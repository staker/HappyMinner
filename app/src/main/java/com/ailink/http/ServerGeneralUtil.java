package com.ailink.http;

import android.widget.TextView;

import com.ailink.constants.WebUrls;
import com.ailink.i.HttpObjectListener;
import com.ailink.json.JsonString;
import com.ailink.json.JsonUtil;
import com.ailink.pojo.DailyMissionPojo;
import com.ailink.pojo.GiftPojo;
import com.ailink.pojo.QiangInfoPojo;
import com.ailink.pojo.SendInfoPojo;
import com.ailink.util.Logg;
import com.ailink.util.NumberUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
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

public class ServerGeneralUtil {




    public static void getSignInfo(final  HttpObjectListener listener) {
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Sign_Info, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                Logg.e("e="+e.toString());
                if(listener!=null){
                    listener.onDataError("请检查网络设置");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("获取签到相关的信息="+response);
                if(listener==null){
                    return;
                }
                listener.onSucess(JsonUtil.getSignInfo(response));
            }
        });
    }





    public static void sendSignIn(final  HttpObjectListener listener) {
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Send_Sign_In, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                Logg.e("e="+e.toString());
                if(listener!=null){
                    listener.onDataError("请检查网络设置");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("获取点击每日签到  返回数据="+response);
                if(listener==null){
                    return;
                }
                try {
                    if(response!=null){
                        JSONObject jsonObject=new JSONObject(response);
                        JSONObject dataObject=jsonObject.optJSONObject("data");
                        int code=jsonObject.optInt("code");
                        if(code==200){
                            listener.onSucess(null);
                        }else{
                            listener.onDataError(jsonObject.optString("msg"));
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }




    public static void getQiangInfo(final  HttpObjectListener listener) {
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Qiang_Mission_Info, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                Logg.e("e="+e.toString());
                if(listener!=null){
                    listener.onDataError("请检查网络设置");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("获取抢矿任务相关的信息="+response);
                if(listener==null){
                    return;
                }
                try {
                    if(response!=null){
                        JSONObject jsonObject=new JSONObject(response);
                        JSONObject dataObject=jsonObject.optJSONObject("data");
                        int code=jsonObject.optInt("code");
                        if(code==200){
                            QiangInfoPojo qiangInfoPojo=new QiangInfoPojo();
                            qiangInfoPojo.need=dataObject.optInt("need");
                            qiangInfoPojo.now=dataObject.optInt("now");
                            qiangInfoPojo.addSpeed=dataObject.optInt("addSpeed");
                            qiangInfoPojo.status=dataObject.optInt("status");
                            listener.onSucess(qiangInfoPojo);
                        }else{
                            listener.onDataError(jsonObject.optString("msg"));
                        }
                    }
                } catch (Exception e) {
                    listener.onDataError("获取数据失败，请检查网络设置");
                    e.printStackTrace();
                }
            }
        });
    }




    public static void getQiangGift(final  HttpObjectListener listener) {
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Qiang_Gift, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                Logg.e("e="+e.toString());
                if(listener!=null){
                    listener.onDataError("登录失败，请检查网络设置");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("获取抢矿奖励  返回数据="+response);
                if(listener==null){
                    return;
                }
                try {
                    if(response!=null){
                        JSONObject jsonObject=new JSONObject(response);
                        int code=jsonObject.optInt("code");
                        if(code==200){
                            listener.onSucess(null);
                        }else{
                            listener.onDataError(jsonObject.optString("msg"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }






    public static void getSendInfo(final  HttpObjectListener listener) {
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Send_Mission_Info, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                Logg.e("e="+e.toString());
                if(listener!=null){
                    listener.onDataError("获取数据失败，请检查网络设置");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("获取送礼任务相关的信息="+response);
                if(listener==null){
                    return;
                }
                try {
                    if(response!=null){
                        JSONObject jsonObject=new JSONObject(response);
                        JSONObject dataObject=jsonObject.optJSONObject("data");
                        int code=jsonObject.optInt("code");
                        if(code==200){
                            SendInfoPojo sendInfoPojo=new SendInfoPojo();
                            sendInfoPojo.need=dataObject.optInt("need");
                            sendInfoPojo.now=dataObject.optInt("now");
                            sendInfoPojo.addSpeed=dataObject.optInt("addSpeed");
                            sendInfoPojo.addToken=dataObject.optInt("addToken");
                            sendInfoPojo.status=dataObject.optInt("status");
                            listener.onSucess(sendInfoPojo);
                        }else{
                            listener.onDataError(jsonObject.optString("msg"));
                        }
                    }
                } catch (Exception e) {
                    listener.onDataError("获取数据失败，请检查网络设置");
                    e.printStackTrace();
                }
            }
        });
    }



    public static void getSendGift(final  HttpObjectListener listener) {
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Send_Gift, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                Logg.e("e="+e.toString());
                if(listener!=null){
                    listener.onDataError("登录失败，请检查网络设置");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("获取送礼奖励  返回数据="+response);
                if(listener==null){
                    return;
                }
                try {
                    if(response!=null){
                        JSONObject jsonObject=new JSONObject(response);
                        int code=jsonObject.optInt("code");
                        if(code==200){
                            listener.onSucess(null);
                        }else{
                            listener.onDataError(jsonObject.optString("msg"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }




    public static void sendConfirmContract(long contractId,final  HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("id", ""+contractId);
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Send_Deal_Contract, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                Logg.e("e="+e.toString());
                if(listener!=null){
                    listener.onDataError("请检查网络设置");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("交易一条合约  返回数据="+response);
                if(listener==null){
                    return;
                }
                try {
                    if(response!=null){
                        JSONObject jsonObject=new JSONObject(response);
                        int code=jsonObject.optInt("code");
                        if(code==200){
                            listener.onSucess(null);
                        }else{
                            listener.onDataError(jsonObject.optString("msg"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }






    public static void sendNewContract(int lanCount,final  HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("asset", ""+lanCount);
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Send_New_Contract, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                Logg.e("e="+e.toString());
                if(listener!=null){
                    listener.onDataError("请检查网络设置");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("创建一条新的合约  返回数据="+response);
                if(listener==null){
                    return;
                }
                try {
                    if(response!=null){
                        JSONObject jsonObject=new JSONObject(response);
                        int code=jsonObject.optInt("code");
                        if(code==200){
                            listener.onSucess(null);
                        }else{
                            listener.onDataError(jsonObject.optString("msg"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }




    public static void sendCancelContract(long contractId,final  HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("id", ""+contractId);
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Send_Delete_Contract, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                Logg.e("e="+e.toString());
                if(listener!=null){
                    listener.onDataError("请检查网络设置");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("取消一条合约  返回数据="+response);
                if(listener==null){
                    return;
                }
                try {
                    if(response!=null){
                        JSONObject jsonObject=new JSONObject(response);
                        int code=jsonObject.optInt("code");
                        if(code==200){
                            listener.onSucess(null);
                        }else{
                            listener.onDataError(jsonObject.optString("msg"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }





}
