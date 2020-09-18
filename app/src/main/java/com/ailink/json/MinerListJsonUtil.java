package com.ailink.json;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.ailink.db.ApplicationData;
import com.ailink.db.Configuration;
import com.ailink.db.MyApplication;
import com.ailink.fragment.RecommendFragment;
import com.ailink.pojo.UserPojo;
import com.ailink.util.Logg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import libs.glide.GlideUtil;

public class MinerListJsonUtil {

    public static ArrayList<UserPojo> getTransMinerTopList(String json){
        try {
            if(json!=null){
                JSONObject jsonObject=new JSONObject(json);

                JSONArray array=jsonObject.optJSONArray("data");
                ArrayList<UserPojo>  list =new ArrayList<UserPojo>();
                int size=array.length();
                for(int i=0;i<size;i++){
                    JSONObject temp=array.getJSONObject(i);
                    UserPojo userPojo=new UserPojo();
                    userPojo.userName=temp.optString("name");
                    userPojo.avatarUrl=temp.optString("avatar");
                    userPojo.userId=temp.optLong("code");
                    userPojo.tradeCount=temp.optInt("sum");
                    userPojo.tradeRank=i+1;
                    list.add(userPojo);
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }







    public static ArrayList<UserPojo> getMinerByPrice(UserPojo userSelf,String json){
        try {
            if(json!=null){
                JSONObject jsonObject=new JSONObject(json);
                JSONObject  dataJson=jsonObject.optJSONObject("data");
                userSelf.avatarUrl=dataJson.optString("avatar");
                userSelf.priceRank=dataJson.optInt("ranking");
                JSONArray array=dataJson.optJSONArray("list");
                ArrayList<UserPojo>  list =new ArrayList<UserPojo>();
                int size=array.length();
                for(int i=0;i<size;i++){
                    JSONObject temp=array.getJSONObject(i);
                    UserPojo userPojo=new UserPojo();
                    userPojo.userName=temp.optString("name");
                    userPojo.avatarUrl=temp.optString("avatar");
                    userPojo.userId=temp.optLong("code");
                    userPojo.priceNew=temp.optDouble("priceNew");
                    userPojo.priceRank=i+1;
                    list.add(userPojo);
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }





    public static ArrayList<UserPojo> getRecommendList(String json){
        try {
            if(json!=null){
                JSONObject jsonObject=new JSONObject(json);
                JSONObject dataJson=jsonObject.optJSONObject("data");

                RecommendFragment.recommendErrorMsg=dataJson.optString("msg");
                ApplicationData.getInstance().replaceFlag=dataJson.optBoolean("replaceFlag");

                JSONArray array=dataJson.optJSONArray("list");
                ArrayList<UserPojo>  list =new ArrayList<UserPojo>();
                int size=array.length();
                for(int i=0;i<size;i++){
                    JSONObject temp=array.getJSONObject(i);
                    UserPojo userPojo=new UserPojo();
                    userPojo.userName=temp.optString("name");
                    userPojo.avatarUrl=temp.optString("avatar");
                    userPojo.userId=temp.optLong("code");
                    userPojo.priceNew=temp.optDouble("priceNew");
                    userPojo.speed=temp.optInt("speed");
                    userPojo.speedGift=temp.optInt("speedGift");
                    userPojo.added=dataJson.optInt("added");
                    userPojo.priceDown=dataJson.optBoolean("priceDown");
                    list.add(userPojo);
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }










    public static ArrayList<UserPojo> getSearchList(String json){
        try {
            if(json!=null){
                JSONObject jsonObject=new JSONObject(json);
                JSONArray array=jsonObject.optJSONArray("data");
                ArrayList<UserPojo>  list =new ArrayList<UserPojo>();
                int size=array.length();
                for(int i=0;i<size;i++){
                    JSONObject temp=array.getJSONObject(i);
                    UserPojo userPojo=new UserPojo();
                    userPojo.userName=temp.optString("name");
                    userPojo.avatarUrl=temp.optString("avatar");
                    userPojo.userId=temp.optLong("code");
                    userPojo.priceNew=temp.optDouble("priceNew");
                    userPojo.speed=temp.optInt("speed");
                    userPojo.added=temp.optInt("added");
                    userPojo.priceDown=temp.optBoolean("priceDown");
                    list.add(userPojo);
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }








    public static ArrayList<UserPojo> getHomeBang(ImageView imgView, String json){
        try {
            if(json!=null){
                JSONObject jsonObject=new JSONObject(json);
                JSONObject  dataJson=jsonObject.optJSONObject("data");

                String title=dataJson.optString("title");
                String url=dataJson.optString("url");
                if (imgView!=null){
                    GlideUtil.getInstance().setImage(imgView,url);
                }
                String util=dataJson.optString("util");
                JSONArray array=dataJson.optJSONArray("list");
                ArrayList<UserPojo>  list =new ArrayList<UserPojo>();
                int size=array.length();
                for(int i=0;i<size;i++){
                    JSONObject temp=array.getJSONObject(i);
                    UserPojo userPojo=new UserPojo();
                    userPojo.userName=temp.optString("name");
                    userPojo.avatarUrl=temp.optString("avatar");
                    userPojo.userId=temp.optLong("code");
                    userPojo.homeBangValue=temp.optInt("sum");
                    userPojo.homeBangText=util;
                    userPojo.homeValueRank=i+1;
                    list.add(userPojo);
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




















}
