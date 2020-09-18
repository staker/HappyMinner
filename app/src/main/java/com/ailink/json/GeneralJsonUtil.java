package com.ailink.json;

import com.ailink.constants.WebUrls;
import com.ailink.pojo.ContractPojo;
import com.ailink.pojo.DailyMissionPojo;
import com.ailink.pojo.GiftPojo;
import com.ailink.pojo.NoticePojo;
import com.ailink.pojo.QianDaoPojo;
import com.ailink.pojo.VersionPojo;
import com.ailink.util.Logg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import libs.banner.BannerPojo;

public class GeneralJsonUtil {



    public static QianDaoPojo  getSignInfo(String json){
        try {
            if(json!=null){
                JSONObject jsonObject=new JSONObject(json);
                JSONObject dataObject=jsonObject.optJSONObject("data");
                QianDaoPojo qianDaoPojo=new QianDaoPojo();


                qianDaoPojo.day=dataObject.optInt("day");
                qianDaoPojo.continueDay=dataObject.optInt("conDay");
                qianDaoPojo.status=dataObject.optString("status");
                qianDaoPojo.lastSignTime=dataObject.optString("lastSignTime");

                JSONArray arrayGift=dataObject.optJSONArray("signInGetInfo");
                JSONObject tempObject=arrayGift.optJSONObject(qianDaoPojo.day-1);
                qianDaoPojo.token=tempObject.optInt("token");
                qianDaoPojo.speed=tempObject.optInt("speed");

                return  qianDaoPojo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }






    public static ArrayList<ContractPojo>  getContractList(String json){
        try {
            if(json!=null){
                JSONObject jsonObject=new JSONObject(json);
                JSONObject dataObject=jsonObject.optJSONObject("data");
                JSONArray listArray=dataObject.optJSONArray("list");


                int size=listArray.length();
                ArrayList<ContractPojo> list=new ArrayList<ContractPojo>();


                for (int i = 0; i <size ; i++) {
                    ContractPojo contractPojo=new ContractPojo();
                    JSONObject temp=listArray.getJSONObject(i);
                    contractPojo.contractId=temp.optLong("id");
                    contractPojo.userId=temp.optLong("code");

                    contractPojo.userName=temp.optString("name");
                    contractPojo.avatarUrl=temp.optString("avatar");
                    contractPojo.time=temp.optString("time");

                    contractPojo.status=temp.optInt("status");
                    contractPojo.lan=temp.optInt("asset");
                    contractPojo.speed=temp.optInt("speed");

                    list.add(contractPojo);
                }



                return  list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }












}
