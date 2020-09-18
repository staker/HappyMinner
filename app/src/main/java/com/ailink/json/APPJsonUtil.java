package com.ailink.json;

import com.ailink.constants.WebUrls;
import com.ailink.db.Configuration;
import com.ailink.db.MyApplication;
import com.ailink.pojo.DailyMissionPojo;
import com.ailink.pojo.GiftPojo;
import com.ailink.pojo.NoticePojo;
import com.ailink.pojo.UserPojo;
import com.ailink.pojo.VersionPojo;
import com.ailink.util.Logg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import libs.banner.BannerPojo;

public class APPJsonUtil {


    public static String getHomeNotice(String json){
        try {
            if(json!=null){
                JSONObject jsonObject=new JSONObject(json);
                JSONArray arrayJson=jsonObject.optJSONArray("data");
                String text="";
                int size=arrayJson.length();
                for (int i = 0; i <size ; i++) {
                    JSONObject temp=arrayJson.optJSONObject(i);
                    text=text+temp.optString("info")+" ";
                }
                return  text;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static VersionPojo getUpdateVersion(String json){
        try {
            if(json!=null){
                JSONObject jsonObject=new JSONObject(json);
                JSONObject dataJson=jsonObject.optJSONObject("data");

                VersionPojo versionPojo=new VersionPojo();
                versionPojo.versionCode=dataJson.optInt("version_code");
                versionPojo.versionLog=dataJson.optString("version_log");
                versionPojo.url=dataJson.optString("url");
                versionPojo.state=dataJson.optInt("state");
                return  versionPojo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    public static ArrayList<NoticePojo> getNoticeList(String json){
        try {
            if(json!=null){
                JSONObject jsonObject=new JSONObject(json);
                JSONArray arrayJson=jsonObject.optJSONArray("data");
                int size=arrayJson.length();
                ArrayList<NoticePojo> list=new ArrayList<NoticePojo>();
                for (int i = 0; i <size ; i++) {
                    JSONObject temp=arrayJson.optJSONObject(i);
                    NoticePojo noticePojo=new NoticePojo();
                    noticePojo.id=temp.optInt("id");
                    noticePojo.name=temp.optString("name");
                    noticePojo.status=temp.optString("status");
                    noticePojo.time=temp.optString("time");
                    list.add(noticePojo);
                }
                return  list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }






    public static NoticePojo getNoticeDetail(String json){
        try {
            if(json!=null){
                JSONObject jsonObject=new JSONObject(json);

                JSONObject dataJson=jsonObject.optJSONObject("data");
                NoticePojo noticePojo=new NoticePojo();
                noticePojo.name=dataJson.optString("name");
                noticePojo.content=dataJson.optString("info");
                return  noticePojo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    public static ArrayList<BannerPojo> getBannerList(String json){
        try {
            if(json!=null){
                JSONObject jsonObject=new JSONObject(json);
                JSONArray arrayJson=jsonObject.optJSONObject("data").optJSONArray("bannerList");
                int size=arrayJson.length();
                ArrayList<BannerPojo> list=new ArrayList<BannerPojo>();
                for (int i = 0; i <size ; i++) {
                    JSONObject temp=arrayJson.optJSONObject(i);
                    BannerPojo bannerPojo=new BannerPojo();
                    bannerPojo.id=temp.optInt("id");
                    bannerPojo.titleName=temp.optString("title");
                    bannerPojo.pageUrl= WebUrls.BasePathUrl+temp.optString("path")+WebUrls.App_PathUrl_End;
                    bannerPojo.imgUrl=temp.optString("imgUrl");
                    bannerPojo.url=temp.optString("url");
                    bannerPojo.status=temp.optString("status");
                    if("on".equals(bannerPojo.status)){
                        list.add(bannerPojo);
                    }
                }
                return  list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



















    public static ArrayList<GiftPojo> getGiftList(String json){
        try {
            if(json!=null){
                JSONObject jsonObject=new JSONObject(json);
                JSONArray arrayJson=jsonObject.optJSONObject("data").optJSONArray("giftList");
                int size=arrayJson.length();
                ArrayList<GiftPojo> list=new ArrayList<GiftPojo>();
                for (int i = 0; i <size ; i++) {
                    JSONObject temp=arrayJson.optJSONObject(i);
                    GiftPojo giftPojo=new GiftPojo();

                    giftPojo.giftId=temp.optInt("id");
                    giftPojo.giftName=temp.optString("name");
                    giftPojo.desc=temp.optString("info");
                    giftPojo.speedAdd=temp.optInt("speedAdd");
                    giftPojo.price=temp.optInt("price");
                    giftPojo.iconUrl=temp.optString("icon");
                    list.add(giftPojo);
                }
                return  list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }






    public static void getDailyMission(String json,ArrayList<DailyMissionPojo> listDaily,ArrayList<DailyMissionPojo> listBase){
        try {
            if(json!=null){

                JSONObject jsonObject=new JSONObject(json);
                JSONObject dataJson=jsonObject.optJSONObject("data");
                JSONArray dailyArray=dataJson.optJSONArray("dailyMission");
                JSONArray baseArray=dataJson.optJSONArray("baseMission");
                int dSize=dailyArray.length();
                int bSize=baseArray.length();

                if(dSize>0){
                    listDaily.clear();
                }
                  for (int i = 0; i <dSize ; i++) {
                    JSONObject temp=dailyArray.optJSONObject(i);
                    DailyMissionPojo dailyMissionPojo=new DailyMissionPojo();
                      dailyMissionPojo.id=temp.optInt("id");
                      dailyMissionPojo.name=temp.optString("name");
                      dailyMissionPojo.status=temp.optString("status");
                      dailyMissionPojo.iconUrl=temp.optString("icon");
                      dailyMissionPojo.path=temp.optString("path");
                      dailyMissionPojo.desc=temp.optString("showInfo");
                      dailyMissionPojo.click=temp.optString("click");
                      listDaily.add(dailyMissionPojo);
                }

                if(bSize>0){
                    listBase.clear();
                }
                for (int i = 0; i <bSize ; i++) {
                    JSONObject temp=baseArray.optJSONObject(i);
                    DailyMissionPojo dailyMissionPojo2=new DailyMissionPojo();
                    dailyMissionPojo2.id=temp.optInt("id");
                    dailyMissionPojo2.name=temp.optString("name");
                    dailyMissionPojo2.status=temp.optString("status");
                    dailyMissionPojo2.addToken=temp.optInt("addToken");
                    dailyMissionPojo2.iconUrl=temp.optString("icon");
                    dailyMissionPojo2.path=temp.optString("path");
                    dailyMissionPojo2.desc=temp.optString("showInfo");
                    dailyMissionPojo2.click=temp.optString("click");

                    dailyMissionPojo2.tipsNumber=temp.optInt("tipsNum");
                    dailyMissionPojo2.showTips=temp.optBoolean("tips");

                    listBase.add(dailyMissionPojo2);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            Logg.e("e="+e.toString());
        }
    }





















}
