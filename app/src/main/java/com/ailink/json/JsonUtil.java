package com.ailink.json;



import android.widget.ImageView;
import android.widget.TextView;

import com.ailink.activity.SearchMinerActivity;
import com.ailink.constants.WebUrls;
import com.ailink.db.ApplicationData;
import com.ailink.db.Configuration;
import com.ailink.db.MyApplication;
import com.ailink.http.ServerListUtil;
import com.ailink.pojo.ContractPojo;
import com.ailink.pojo.DailyMissionPojo;
import com.ailink.pojo.GiftPojo;
import com.ailink.pojo.IncomePojo;
import com.ailink.pojo.InvitePojo;
import com.ailink.pojo.MissionPojo;
import com.ailink.pojo.NoticePojo;
import com.ailink.pojo.PoolPojo;
import com.ailink.pojo.QianDaoPojo;
import com.ailink.pojo.TitlePojo;
import com.ailink.pojo.TradePojo;
import com.ailink.pojo.UserPojo;
import com.ailink.pojo.VersionPojo;
import com.ailink.util.Logg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import libs.banner.BannerPojo;

/**
 * Created by Staker on 2017/7/14.
 * 所有的json解析方法都在这个类里面，但是具体的实现在相应的类里面
 */

public class JsonUtil {


    public static UserPojo getMineInfo(String json) {
        return UserJsonUtil.getMineInfo(json);
    }

    public static void setMyDetailInfo(String json){
        UserJsonUtil.setMyDetailInfo(json);
    }






    //获取首页的显示的滚动公告
    public static String getHomeNotice(String json){
        return APPJsonUtil.getHomeNotice(json);
    }

    //获取系统公告列表
    public static ArrayList<NoticePojo> getNoticeList(String json){
        return APPJsonUtil.getNoticeList(json);
    }

    public static NoticePojo getNoticeDetail(String json){
        return APPJsonUtil.getNoticeDetail(json);
    }

    public static VersionPojo getUpdateVersion(String json){
        return  APPJsonUtil.getUpdateVersion(json);
    }

    public static ArrayList<BannerPojo> getBannerList(String json){
       return APPJsonUtil.getBannerList(json);
    }





    //获取交易排行榜Top10
    public static ArrayList<UserPojo> getTransMinerTopList(String json){
        return MinerListJsonUtil.getTransMinerTopList(json);
    }


    public static void setUserInfo(String json,UserPojo userSelf, UserPojo userOwner, ArrayList<UserPojo> listMiner){
        UserJsonUtil.setUserInfo( json, userSelf,  userOwner, listMiner);
    }



    //获取矿工列表  和保存这个用户的资产和矿工位置数据
    public static ArrayList<UserPojo> getMyMinerList(String json,UserPojo userSelf){
        return UserJsonUtil.getMyMinerList(json,userSelf);
    }

    //获取放置在矿池里面对应的矿工列表
    public static ArrayList<UserPojo> getMyMinerListForPool(String json,UserPojo userSelf){
        return UserJsonUtil.getMyMinerListForPool(json,userSelf);
    }




    //获取用户的人脉列表  通过度数
    public static ArrayList<UserPojo> getFriendListDu(String json){
           return UserJsonUtil.getFriendListDu(json);
    }




    //获取矿工的身价列表，即世界排行榜
    public static ArrayList<UserPojo> getMinerByPrice(UserPojo userSelf,String json){
        return MinerListJsonUtil.getMinerByPrice(userSelf,json);
    }




    public static ArrayList<InvitePojo> getMyInviteList(String json){
       return UserJsonUtil.getMyInviteList(json);
    }



    public static ArrayList<MissionPojo> getMissionList(String json){
        return UserJsonUtil.getMissionList(json);
    }


    public static ArrayList<UserPojo> getRecommendList(String json){
        return MinerListJsonUtil.getRecommendList(json);
    }


    public static ArrayList<UserPojo> getSearchList(String json){
        return MinerListJsonUtil.getSearchList(json);
    }


    //获取用户的称号列表
    public static ArrayList<TitlePojo> getTitleList(String json){
        return UserJsonUtil.getTitleList(json);
    }



    public static ArrayList<TradePojo> getTradeMsgList(String json){
        return UserJsonUtil.getTradeMsgList(json);
    }




    public static ArrayList<GiftPojo> getGiftList(String json){
        return APPJsonUtil.getGiftList(json);
    }



    public static ArrayList<IncomePojo> getIncomeList(String json){
        return UserJsonUtil.getIncomeList(json);
    }



    public static void getDailyMission(String json,ArrayList<DailyMissionPojo> listDaily,ArrayList<DailyMissionPojo> listBase){
        APPJsonUtil.getDailyMission(json,listDaily,listBase);
    }




    public static ArrayList<PoolPojo> getPoolList(String json){
        return PoolJsonUtil.getPoolList(json);
    }



    public static QianDaoPojo getSignInfo(String json){
            return  GeneralJsonUtil.getSignInfo(json);
    }

    public static ArrayList<ContractPojo>  getContractList(String json){
        return  GeneralJsonUtil.getContractList(json);
    }



    public static ArrayList<UserPojo> getHomeBang(ImageView imgView, String json){
            return MinerListJsonUtil.getHomeBang(imgView,json);
    }



}
