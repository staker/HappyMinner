package com.ailink.json;

import com.ailink.activity.BuyLastWaterActivity;
import com.ailink.db.ApplicationData;
import com.ailink.fragment.RecommendFragment;
import com.ailink.pojo.UserPojo;
import com.ailink.util.Logg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class WolfJsonUtil {

    public static ArrayList<String> getWolfLastNameList(String json){
        try {
            if(json!=null){
                JSONObject jsonObject=new JSONObject(json);
                JSONObject dataObject=jsonObject.optJSONObject("data");
                BuyLastWaterActivity.leaveTime=dataObject.optLong("leaveTime");

                JSONArray lastBuyListArray=dataObject.optJSONArray("lastBuyList");

                ArrayList<String>  list =new ArrayList<String>();
                int size=lastBuyListArray.length();
                for(int i=0;i<size;i++){
                    JSONObject temp=lastBuyListArray.getJSONObject(i);
                    String name=temp.optString("name");
                    list.add(name);
                    Logg.e("名次："+(i+1)+" 名字="+name);
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


































}
