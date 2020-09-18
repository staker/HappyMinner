package com.ailink.http;

import android.widget.ImageView;

import com.ailink.constants.WebUrls;
import com.ailink.i.HttpObjectListener;
import com.ailink.json.JsonString;
import com.ailink.json.JsonUtil;
import com.ailink.pojo.UserPojo;
import com.ailink.util.Logg;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

import libs.okhttp.MyOkHttpUtil;
import libs.okhttps.MyOkHttpsUtil;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Staker on 2017/7/11.
 * 这个类主要存放单个用户和他相关的信息，诸如排行榜或者其他推荐矿工之类的列表是不会放在这个里面的
 * 但是获取单个人的所有矿工是需要放在这个类里面的
 */

public class ServerListUtil {




    public static void getPoolList(final HttpObjectListener listener) {
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Pool_List, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("获取矿池列表="+response);
                if(listener!=null){
                    listener.onSucess(JsonUtil.getPoolList(response));
                }
            }
        });
    }








    public static void getTransMinerTopList(final HttpObjectListener listener) {
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_TopTrans_Miner, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                if(listener!=null){
                   listener.onSucess(JsonUtil.getTransMinerTopList(response));
                }
            }
        });
    }


    public static void getMinerByPrice(final UserPojo userSelf,final HttpObjectListener listener) {
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Miner_ByPrice, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                if(listener!=null){
                    listener.onSucess(JsonUtil.getMinerByPrice(userSelf,response));
                }
            }
        });
    }





    public static void getRecommendMiner(int page,final HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("pageCount", "20");
        map.put("pageNum", page+"");
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Recommend, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("推荐  ="+response);
                if(listener!=null){
                    listener.onSucess(JsonUtil.getRecommendList(response));
                }
            }
        });
    }










    public static void getSearchList(final  String searchKey,int page,final HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("pageNum", page+"");
        map.put("name", searchKey);
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Search_Miner, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("获取搜索结果，关键字key="+searchKey+"结果="+response);
                if(listener!=null){
                    listener.onSucess(JsonUtil.getSearchList(response));
                }
            }
        });
    }









    public static void getContractList(int order,int page,final HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("pageCount", "10");
        map.put("pageNum", page+"");
        map.put("order", order+"");
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Contract, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("  获取交易合约 信息="+response);
                if(listener!=null){
                    listener.onSucess(JsonUtil.getContractList(response));
                }
            }
        });
    }

    public static void getMyContractList(int page,final HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("pageCount", "10");
        map.put("pageNum", page+"");
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_My_Contract, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("  获取我的交易合约 信息="+response);
                if(listener!=null){
                    listener.onSucess(JsonUtil.getContractList(response));
                }
            }
        });
    }



    public static void getHomeRank(final ImageView imgView,final HttpObjectListener listener) {
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Home_Rank, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("  获取首页推荐排行榜 信息="+response);
                if(listener!=null){
                    listener.onSucess(JsonUtil.getHomeBang(imgView,response));
                }
            }
        });
    }



















}
