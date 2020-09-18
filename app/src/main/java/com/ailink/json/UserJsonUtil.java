package com.ailink.json;

import com.ailink.activity.SpeedRecordActivity;
import com.ailink.constants.ConstantString;
import com.ailink.db.ApplicationData;
import com.ailink.db.Configuration;
import com.ailink.db.MyApplication;
import com.ailink.pojo.IncomePojo;
import com.ailink.pojo.InvitePojo;
import com.ailink.pojo.MissionPojo;
import com.ailink.pojo.TitlePojo;
import com.ailink.pojo.TradePojo;
import com.ailink.pojo.UserPojo;
import com.ailink.util.BroadcastUtil;
import com.ailink.util.Logg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserJsonUtil {


    public static UserPojo getMineInfo(String json){
        Logg.e("用户信息="+json);
        try {
            if(json!=null){
                JSONObject jsonObject=new JSONObject(json);
                JSONObject dataJson=jsonObject.optJSONObject("data");
                UserPojo user= Configuration.getInstance(MyApplication.getContext()).getUserPojo();
                if(user==null){
                    user= new UserPojo();
                }
                user.userId=dataJson.optLong("id");
                user.sex=dataJson.optInt("sex");
                user.speed=dataJson.optInt("speed");
                user.speedGift=dataJson.optInt("speedGift");

                user.totalAsset=dataJson.optDouble("asset");
                user.priceNew=dataJson.optDouble("priceNew");
                user.avatarUrl=dataJson.optString("avatar");
                user.userName=dataJson.optString("name");
                user.userTitle=dataJson.optString("title");
                user.userPhone=dataJson.optString("phone");
                Configuration.getInstance(MyApplication.getContext()).setUserPojo(user);

                boolean gainTips=dataJson.optBoolean("gainTips");
                int gainTipsNum=dataJson.optInt("gainTipsNum");
                if(gainTips){
                    ApplicationData.getInstance().missionTipsCount=gainTipsNum+1;
                }else{
                    ApplicationData.getInstance().missionTipsCount=0;
                }


                Logg.e(".name="+user.userName);
                Logg.e(".avatarUrl="+user.avatarUrl);
                return  user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }









    public static void setMyDetailInfo(String json){
        Logg.e("设置自己的详细用户信息="+json);
        try {
            if(json!=null){
                JSONObject jsonObject=new JSONObject(json);
                JSONObject dataJson=jsonObject.optJSONObject("data");
                UserPojo user= Configuration.getInstance(MyApplication.getContext()).getUserPojo();
                if(user==null){
                    user= new UserPojo();
                }
                user.sex=dataJson.optInt("sex");
                user.avatarUrl=dataJson.optString("avatar");
                user.userName=dataJson.optString("name");
                user.userPhone=dataJson.optString("phone");
                user.review=dataJson.optBoolean("review");
                Configuration.getInstance(MyApplication.getContext()).setUserPojo(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }











    public static void setUserInfo(String json,UserPojo userSelf, UserPojo userOwner, ArrayList<UserPojo> listMiner){
        try {
            if(json==null){
                return;
            }
            listMiner.clear();
            JSONObject jsonObject=new JSONObject(json);
            JSONObject dataJson=jsonObject.optJSONObject("data");
            JSONObject bossInfo=dataJson.optJSONObject("bossInfo");
            JSONArray minerList=dataJson.optJSONArray("minerList");


            userSelf.userId=dataJson.optLong("id");
            userSelf.sex=dataJson.optInt("sex");
            userSelf.speed=dataJson.optInt("speed");
            userSelf.speedGift=dataJson.optInt("speedGift");
            userSelf.added=dataJson.optInt("added");
            userSelf.priceDown=dataJson.optBoolean("priceDown");

            userSelf.totalAsset=dataJson.optDouble("asset");
            userSelf.priceNew=dataJson.optDouble("priceNew");
            userSelf.priceOld=dataJson.optDouble("priceOld");
            userSelf.avatarUrl=dataJson.optString("avatar");
            userSelf.userName=dataJson.optString("name");
            userSelf.userTitle=dataJson.optString("title");
            userSelf.canAction=dataJson.optString("canAction");
            userSelf.replaceNum=dataJson.optInt("replaceNum");

            int size=minerList.length();
            for (int i = 0; i <size ; i++) {
                JSONObject temp=minerList.optJSONObject(i);
                UserPojo user=new UserPojo();
                user.userId=temp.optLong("codeMiner");
                user.avatarUrl=temp.optString("minerAvatar");
                user.userName=temp.optString("minerName");
                listMiner.add(user);
            }
            userOwner.userId=bossInfo.optLong("code");
            userOwner.avatarUrl=bossInfo.optString("avatar");
            userOwner.userName=bossInfo.optString("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static ArrayList<UserPojo> getMyMinerList(String json,UserPojo userSelf){
        try {
            if(json!=null){
                JSONObject mainJson=new JSONObject(json);
                JSONObject jsonObject=mainJson.optJSONObject("data");
                userSelf.minerPkgCount=jsonObject.optInt("minerPkgCount");
                userSelf.totalAsset=jsonObject.optDouble("asset");
                boolean meTips=jsonObject.optBoolean("meTips");
                if(meTips){
                    ApplicationData.getInstance().meTipsCount=1;
                    BroadcastUtil.sendBroadcast(MyApplication.getContext(), ConstantString.BroadcastActions.Action_Change_Bottom_Tab_Tips);
                }else {
                    ApplicationData.getInstance().meTipsCount=0;
                    BroadcastUtil.sendBroadcast(MyApplication.getContext(), ConstantString.BroadcastActions.Action_Change_Bottom_Tab_Tips);
                }
                ApplicationData.getInstance().isFreeze=jsonObject.optBoolean("freeze");
                ApplicationData.getInstance().waKuangBuff =jsonObject.optString("addition");
                ApplicationData.getInstance().topSpeedBuff =jsonObject.optString("topSpeedBuff");
                ApplicationData.getInstance().homeSpeedBuff =jsonObject.optString("addition");

                String msgHave=jsonObject.optString("msg");
                if("have".equals(msgHave)){
                    ApplicationData.getInstance().homeMsgShow=true;
                }else {
                    ApplicationData.getInstance().homeMsgShow=false;
                }


                JSONArray array=jsonObject.optJSONArray("minerList");
                ArrayList<UserPojo>  list =new ArrayList<UserPojo>();
                int size=array.length();
                for(int i=0;i<size;i++){
                    JSONObject temp=array.getJSONObject(i);
                    UserPojo userPojo=new UserPojo();
                    userPojo.userName=temp.optString("minerName");
                    userPojo.avatarUrl=temp.optString("minerAvatar");
                    userPojo.minerStatus=temp.optString("minerStatus");
                    userPojo.userId=temp.optLong("codeMiner");

                    userPojo.priceNew=temp.optDouble("priceNew");
                    userPojo.priceOld=temp.optDouble("priceOld");
                    userPojo.speed=temp.optInt("speed");
                    userPojo.speedGift=temp.optInt("speedGift");
                    userPojo.pkgId=temp.optInt("pkgId");

                    userPojo.poolId=temp.optInt("poolId");
                    userPojo.poolName=temp.optString("poolName");
                    userPojo.poolLogo=temp.optString("poolLogo");
                    list.add(userPojo);
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    public static ArrayList<UserPojo> getMyMinerListForPool(String json,UserPojo userSelf){
        try {
            if(json!=null){
                JSONObject mainJson=new JSONObject(json);
                JSONObject jsonObject=mainJson.optJSONObject("data");
                userSelf.minerPkgCount=jsonObject.optInt("minerPkgCount");

                JSONArray array=jsonObject.optJSONArray("minerList");
                ArrayList<UserPojo>  list =new ArrayList<UserPojo>();
                int size=array.length();
                for(int i=0;i<size;i++){
                    JSONObject temp=array.getJSONObject(i);
                    UserPojo userPojo=new UserPojo();
                    userPojo.userName=temp.optString("minerName");
                    userPojo.avatarUrl=temp.optString("minerAvatar");
                    userPojo.minerStatus=temp.optString("minerStatus");
                    userPojo.userId=temp.optLong("codeMiner");

                    userPojo.priceNew=temp.optDouble("priceNew");
                    userPojo.priceOld=temp.optDouble("priceOld");
                    userPojo.speed=temp.optInt("speed");
                    userPojo.speedGift=temp.optInt("speedGift");
                    userPojo.pkgId=temp.optInt("pkgId");

                    userPojo.poolId=temp.optInt("poolId");
                    userPojo.poolName=temp.optString("poolName");
                    userPojo.poolLogo=temp.optString("poolLogo");

                    userPojo.canPlace=temp.optBoolean("canPlace");
                    list.add(userPojo);
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }






    public static ArrayList<UserPojo> getFriendListDu(String json){
        try {
            if(json!=null){
                JSONObject mainJson=new JSONObject(json);
                JSONArray array=mainJson.optJSONArray("data");
                ArrayList<UserPojo>  list =new ArrayList<UserPojo>();
                int size=array.length();
                for(int i=0;i<size;i++){
                    JSONObject temp=array.getJSONObject(i);
                    UserPojo userPojo=new UserPojo();
                    userPojo.userName=temp.optString("name");
                    userPojo.avatarUrl=temp.optString("avatar");
                    userPojo.userId=temp.optLong("code");
                    userPojo.priceNew=temp.optDouble("priceNew");
                    list.add(userPojo);
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }












    public static ArrayList<InvitePojo> getMyInviteList(String json){
        try {
            if(json!=null){
                JSONObject mainJson=new JSONObject(json);
                JSONArray array=mainJson.optJSONArray("data");
                ArrayList<InvitePojo>  list =new ArrayList<InvitePojo>();
                int size=array.length();
                for(int i=0;i<size;i++){
                    JSONObject temp=array.getJSONObject(i);
                    InvitePojo invitePojo=new InvitePojo();
                    invitePojo.inviteCount=temp.optInt("addNum");
                    invitePojo.inviteSingleReward=temp.optInt("prize");
                    list.add(invitePojo);
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }












    public static ArrayList<MissionPojo> getMissionList(String json){
        try {
            if(json!=null){
                JSONObject mainJson=new JSONObject(json);
                JSONArray array=mainJson.optJSONArray("data");
                ArrayList<MissionPojo>  list =new ArrayList<MissionPojo>();
                int size=array.length();

                for(int i=0;i<size;i++){
                    JSONObject temp=array.getJSONObject(i);
                    MissionPojo missionPojo=new MissionPojo();
                    missionPojo.name=temp.optString("name");
                    missionPojo.state=temp.optString("status");
                    missionPojo.id=temp.optInt("id");
                    missionPojo.price=temp.optInt("price");
                    list.add(missionPojo);
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }






    public static ArrayList<TitlePojo> getTitleList(String json){
        try {
            if(json!=null){
                JSONObject mainJson=new JSONObject(json);
                JSONArray array=mainJson.optJSONArray("data");
                ArrayList<TitlePojo>  list =new ArrayList<TitlePojo>();
                int size=array.length();
                for(int i=0;i<size;i++){
                    JSONObject temp=array.getJSONObject(i);
                    TitlePojo titlePojo=new TitlePojo();
                    titlePojo.titleName=temp.optString("title");
                    titlePojo.id=temp.optInt("id");
                    titlePojo.titleDesc=temp.optString("info");
                    titlePojo.status=temp.optString("status");
                    list.add(titlePojo);
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


























    public static ArrayList<TradePojo> getTradeMsgList(String json){
        try {
            if(json!=null){
                JSONObject mainJson=new JSONObject(json);
                JSONObject dataJson=mainJson.optJSONObject("data");
                ApplicationData.getInstance().noticeCount=dataJson.optInt("noticeCount");
                JSONArray array=dataJson.optJSONArray("msgList");
                ArrayList<TradePojo>  list =new ArrayList<TradePojo>();
                int size=array.length();
                for(int i=0;i<size;i++){
                    JSONObject temp=array.getJSONObject(i);
                    TradePojo tradePojo=new TradePojo();

                    tradePojo.msgType=temp.optString("msgType");
                    tradePojo.minerName=temp.optString("minerName");
                    tradePojo.oldBossName=temp.optString("oldBossName");
                    tradePojo.newBossName=temp.optString("newBossName");
                    tradePojo.time=temp.optString("time");
                    tradePojo.title=temp.optString("title");
                    tradePojo.msgContent=temp.optString("msg");



                    tradePojo.minerId=temp.optLong("minerCode");
                    tradePojo.oldBossId=temp.optLong("oldBossCode");
                    tradePojo.newBossId=temp.optLong("newBossCode");



                    tradePojo.minerCodeOld=temp.optLong("minerCodeOld");
                    tradePojo.minerCodeNew=temp.optLong("minerCodeNew");
                    tradePojo.minerNameOld=temp.optString("minerNameOld");
                    tradePojo.minerNameNew=temp.optString("minerNameNew");





                    tradePojo.openMineAdd=temp.optDouble("openMineAdd");
                    tradePojo.usedToken=temp.optDouble("usedToken");
                    tradePojo.getFee=temp.optDouble("getFee");
                    tradePojo.sendMine=temp.optDouble("sendMine");
                    tradePojo.newPrice=temp.optDouble("newPrice");

                    list.add(tradePojo);
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }









    public static ArrayList<IncomePojo> getIncomeList(String json){
        try {
//            {"data":{"speedAll":2543,"speedGift":0,"list":[]},"code":200,"msg":"","t":1526366621195}
            if(json!=null){
                JSONObject mainJson=new JSONObject(json);
                JSONObject dataJson=mainJson.optJSONObject("data");
                SpeedRecordActivity.speedAll=dataJson.optInt("speedAll");
                SpeedRecordActivity.speedGift=dataJson.optInt("speedGift");
                JSONArray array=dataJson.optJSONArray("list");
                ArrayList<IncomePojo>  list =new ArrayList<IncomePojo>();
                int size=array.length();

                for(int i=0;i<size;i++){
                    JSONObject temp=array.getJSONObject(i);
                    IncomePojo incomePojo=new IncomePojo();

                    incomePojo.userName=temp.optString("bossName");
                    incomePojo.userId=temp.optLong("bossCode");
                    incomePojo.type=temp.optInt("msgType");
                    incomePojo.speedAdd=temp.optInt("speedAdd");
                    incomePojo.time=temp.optString("time");
                    incomePojo.info=temp.optString("info");
                    list.add(incomePojo);
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }





























}
