package com.ailink.json;

import com.ailink.pojo.NoticePojo;
import com.ailink.pojo.PoolPojo;

import com.ailink.constants.WebUrls;
import com.ailink.db.Configuration;
import com.ailink.db.MyApplication;
import com.ailink.pojo.DailyMissionPojo;
import com.ailink.pojo.GiftPojo;
import com.ailink.pojo.NoticePojo;
import com.ailink.pojo.RequirePojo;
import com.ailink.pojo.UserPojo;
import com.ailink.pojo.VersionPojo;
import com.ailink.util.Logg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.ArrayList;

public class PoolJsonUtil {



    public static ArrayList<PoolPojo> getPoolList(String json){
        try {
            if(json!=null){
                JSONObject jsonObject=new JSONObject(json);
                JSONObject dataJson=jsonObject.optJSONObject("data");
                JSONArray arrayJson=dataJson.optJSONArray("minePoolList");
                int size=arrayJson.length();
                ArrayList<PoolPojo> list=new ArrayList<PoolPojo>();
                for (int i = 0; i <size ; i++) {
                    JSONObject temp=arrayJson.optJSONObject(i);
                    PoolPojo poolPojo=new PoolPojo();
                    poolPojo.poolName=temp.optString("name");
                    poolPojo.mainOutput=temp.optString("mainOutput");
                    poolPojo.poolLogo=temp.optString("logo");
                    poolPojo.status=temp.optString("status");
                    poolPojo.online=temp.optInt("online");
                    poolPojo.mineSum=temp.optString("mineSum");
                    poolPojo.mineLeave=temp.optString("mineLeave");
                    poolPojo.beginTimeQueue=temp.optString("beginTimeQueue");
                    poolPojo.beginTime=temp.optString("beginTime");
                    poolPojo.endTime=temp.optString("endTime");


                    poolPojo.poolId=temp.optInt("id");
                    poolPojo.emptyCount=temp.optInt("emptyCount");
                    poolPojo.ratio=temp.optString("ratio");

                    poolPojo.open=temp.optBoolean("open");

                    JSONArray arrayMinerList=temp.optJSONArray("minerList");
                    if(arrayMinerList!=null&&arrayMinerList.length()>0){
                        int count=arrayMinerList.length();
                        poolPojo.listMiner=new ArrayList<UserPojo>();
                        for(int j=0;j<count;j++){
                            JSONObject tempMiner=arrayMinerList.optJSONObject(j);
                            UserPojo userPojo=new UserPojo();
                            userPojo.userId=tempMiner.optLong("codeMiner");
                            userPojo.userName=tempMiner.optString("minerName");
                            userPojo.avatarUrl=tempMiner.optString("minerAvatar");
                            userPojo.poolId=tempMiner.optInt("poolId");
                            poolPojo.listMiner.add(userPojo);
                        }
                    }
                    JSONArray arrayRequireList=temp.optJSONArray("require");
                    if(arrayRequireList!=null&&arrayRequireList.length()>0){
                        int count=arrayRequireList.length();
                        poolPojo.listRequire=new ArrayList<RequirePojo>();
                        for(int j=0;j<count;j++){
                            JSONObject tempRequire=arrayRequireList.optJSONObject(j);
                            RequirePojo requirePojo=new RequirePojo();
                            requirePojo.name=tempRequire.optString("name");
                            requirePojo.status=tempRequire.optString("status");
                            poolPojo.listRequire.add(requirePojo);
                        }
                    }


                    JSONArray arrayPlaceRequireList=temp.optJSONArray("placeList");
                    if(arrayPlaceRequireList!=null&&arrayPlaceRequireList.length()>0){
                        int count=arrayPlaceRequireList.length();
                        poolPojo.listPlaceRequire=new ArrayList<RequirePojo>();
                        for(int j=0;j<count;j++){
                            JSONObject tempRequire=arrayPlaceRequireList.optJSONObject(j);
                            RequirePojo requirePojo=new RequirePojo();
                            requirePojo.name=tempRequire.optString("name");
                            requirePojo.status=tempRequire.optString("status");
                            poolPojo.listPlaceRequire.add(requirePojo);
                        }
                    }
                    list.add(poolPojo);
                }

                return  list;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logg.e("e="+e.getMessage()+e.toString());
        }
        return null;
    }

}
