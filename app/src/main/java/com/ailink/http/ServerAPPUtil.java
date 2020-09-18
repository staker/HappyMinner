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

public class ServerAPPUtil {


    /**
     * 获取首页展示的公告信息
     */
    public static void getNoticeHome(final TextView txtView) {
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Home_Notice, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
            }
            @Override
            public void onResponse(String response,int id) {
                if(txtView!=null){
                    String string=JsonUtil.getHomeNotice(response);
                    txtView.setText(string);
                }
            }
        });
    }





    public static void getNoticeList(final  HttpObjectListener listener) {
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Notice_Msg, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
            }
            @Override
            public void onResponse(String response,int id) {
                if(listener!=null){
                    listener.onSucess(JsonUtil.getNoticeList(response));
                }
            }
        });
    }




    public static void getNoticeDetail(int noticeId,final  HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("id", noticeId+"");
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Notice_Detail, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
            }
            @Override
            public void onResponse(String response,int id) {
                if(listener!=null){
                   listener.onSucess(JsonUtil.getNoticeDetail(response));
                }
            }
        });
    }





    public static void getBannerList(final  HttpObjectListener listener) {
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Banner, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
            }
            @Override
            public void onResponse(String response,int id) {
                if(listener!=null){
                    listener.onSucess(JsonUtil.getBannerList(response));
                }
            }
        });
    }







    public static void getAppAllToken(final  HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("poolId","1");
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_App_All_Token, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
            }
            @Override
            public void onResponse(String response,int id) {
                if(listener!=null){
                    try {
                        if(response!=null){
                            JSONObject jsonObject=new JSONObject(response);
                            JSONObject dataJson=jsonObject.optJSONObject("data");
                            double giveToken=dataJson.optDouble("giveToken");
                            listener.onSucess(NumberUtil.parseENumberToNormal((int)giveToken,0));
                        }else{
                            listener.onSucess("0");
                        }
                    } catch (Exception e) {
                        listener.onSucess("0");
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    public static void getDailyMission(final ArrayList<DailyMissionPojo> listDaily, final ArrayList<DailyMissionPojo> listBase, final  HttpObjectListener listener) {

        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Daily_Mission, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("获取每日任务列表="+response);
                if(listener!=null){
                    JsonUtil.getDailyMission(response,listDaily,listBase);
                    listener.onSucess("success");
                }
            }
        });
    }




    public static void sendDailyShare() {
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Daily_Share, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("每日分享成功="+response);
            }
        });
    }






    public static void getCheckToken(final  HttpObjectListener listener) {
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Check_Token, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                Logg.e("e="+e.toString());
                if(listener!=null){
                    listener.onDataError("登录失败，请检查网络设置");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("检查token是否过期="+response);
                if(listener==null){
                    return;
                }
                try{
                    JSONObject object=new JSONObject(response);
                    int code=object.optInt("code");
                    JSONObject dataObject=object.optJSONObject("data");
                    ApplicationData.getInstance().signStatusShow=dataObject.optString("signStatusShow");


                    listener.onSucess(code+"");
                }catch (Exception e){
                    listener.onSucess("登录数据失败，请尝试重新登录");
                }
            }
        });
    }



    public static void getAppUpdate(final HttpObjectListener listener) {
        String url=WebUrls.Get_Update_App;
        MyOkHttpUtil.sendGet(url, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("更新app response="+response);
                if(listener!=null){
                    listener.onSucess(JsonUtil.getUpdateVersion(response));
                }
            }
        });
    }



    public static void sendGeneralValue(final String key,String value,final HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("key", key);
        if(value!=null){
            map.put("value", value);
        }
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Send_General_Value, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onDataError("这是通用 send General  请求报错");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("这是通用 send General返回数据 key="+key+""+response);
                if(listener==null){
                    return;
                }
            }
        });
    }



























}
