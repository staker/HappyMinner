package com.ailink.http;

import android.widget.ImageView;
import android.widget.TextView;

import com.ailink.constants.WebUrls;
import com.ailink.i.HttpObjectListener;
import com.ailink.json.JsonString;
import com.ailink.pojo.DailyMissionPojo;
import com.ailink.pojo.GiftPojo;
import com.ailink.pojo.UserPojo;
import com.ailink.util.Logg;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import libs.okhttps.MyOkHttpsUtil;
import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by Staker on 2017/7/4.
 * 这个类用来存放所有的后台请求调用，这里只是做一个转接的类，所以的具体实现都在对应的工具类里面
 * 这个类的好处就是包含了所有的  后台请求调用接口
 */

public class ServerUtil {

    //通用的反馈数据接口
    public static void sendGeneralValue( String key,String value, HttpObjectListener listener) {
       ServerAPPUtil.sendGeneralValue( key,value, listener);
    }


    //用户注册接口
    public static void sendRegister(String quhao,String phoneNumber,String password,String code, HttpObjectListener listener) {
        ServerUserUtil.sendRegister( quhao,phoneNumber,password,code,listener);
    }
    //通过帐号密码登录
    public static void sendLoginbyPassword(String phoneNumber,String password, HttpObjectListener listener) {
        ServerUserUtil.sendLoginbyPassword(phoneNumber,password,listener);
    }

    //通过手机和验证码登录
    public static void sendLoginByCode(String quhao,String phoneNumber,String code, HttpObjectListener listener) {
        ServerUserUtil.sendLoginByCode(quhao,phoneNumber,code,listener);
    }
    public static void sendBindPhone(String quhao,String phoneNumber,String code,final HttpObjectListener listener) {
        ServerUserUtil.sendBindPhone(quhao,phoneNumber,code,listener);
    }

    /**
     *  获取注册短信验证码
     */
    public static void getRegisterPhoneCode(String phoneNumber,HttpObjectListener listener) {
        ServerUserUtil.getRegisterPhoneCode( phoneNumber,listener);
    }
    /**
     *  获取登录短信验证码
     */
    public static void getLoginPhoneCode(String phoneNumber, HttpObjectListener listener) {
        ServerUserUtil.getLoginPhoneCode( phoneNumber,listener);
    }

    //修改用户密码
    public static void sendUpdatePassword(String oldPwd,String newPwd, HttpObjectListener listener) {
        ServerUserUtil.sendUpdatePassword(oldPwd,newPwd,listener);
    }








//设置本人更详细的信息，里面包含手机号
    public static void getMyDetailInfo( HttpObjectListener listener) {
        ServerUserUtil.getMyDetailInfo(listener);
    }

    /**
     *获取本人的信息
     */
    public static void getMyInfo(HttpObjectListener listener) {
       ServerUserUtil.getUserInfo(listener);
    }
    //获取首页滚动公告
    public static void getNoticeMessage(TextView txtView) {
        ServerAPPUtil.getNoticeHome(txtView);
    }


    //获取系统公告
    public static void getNoticeList(  HttpObjectListener listener) {
        ServerAPPUtil.getNoticeList(listener);
    }

    //获取系统公告详情
    public static void getNoticeDetail(int noticeId,final  HttpObjectListener listener) {
            ServerAPPUtil.getNoticeDetail(noticeId,listener);
    }

    public static void getBannerList(final  HttpObjectListener listener) {
        ServerAPPUtil.getBannerList(listener);
    }



    //获取app的总产出蓝钻
    public static void getAppAllToken(final  HttpObjectListener listener) {
        ServerAPPUtil.getAppAllToken(listener);
    }
    //获取每日任务列表
    public static void getDailyMission(ArrayList<DailyMissionPojo> listDaily, ArrayList<DailyMissionPojo> listBase,HttpObjectListener listener) {
        ServerAPPUtil.getDailyMission(listDaily,listBase,listener);
    }


    //获取交易次数排行榜
    public static void getTransMinerTopList(final HttpObjectListener listener) {
        ServerListUtil.getTransMinerTopList(listener);
    }



    /**
     * 获取用户的详细信息
     * @param listener
     * @param userId  用户的id
     * @param userSelf  存放用户自己的信息
     * @param userOwner  存放用户主人的信息
     * @param listMiner  存放用户矿工列表的信息
     */
    public static void getUserInfo(long userId,  UserPojo userSelf,  UserPojo userOwner,  ArrayList<UserPojo> listMiner, HttpObjectListener listener) {
        ServerUserUtil.getUserInfo(userId,userSelf, userOwner, listMiner,listener);
    }

    public static void sendUpdateSex(String sex, HttpObjectListener listener) {
        ServerUserUtil.sendUpdateSex(sex,listener);
    }

    public static void sendUpdateName(String name, HttpObjectListener listener) {
        ServerUserUtil.sendUpdateName(name,listener);
    }
    public static void sendUpdateAvatar(String filsStream,final HttpObjectListener listener) {
        ServerUserUtil.sendUpdateAvatar(filsStream,listener);
    }


    public static void getHomeMinerList(UserPojo userSelf, HttpObjectListener listener) {
        ServerUserUtil.getHomeMinerList(listener,userSelf);
    }


    public static void getMyMinerListForPool(int poolId, UserPojo userSelf, HttpObjectListener listener) {
        ServerUserUtil.getMyMinerListForPool(poolId,userSelf,listener);
    }


    /**
     * 用过用户的id进行购买和解雇矿工
     * @param userId
     * @param action 购买  buy  解雇  fire
     */
    public static void sendOperateMiner(long userId,String action,final HttpObjectListener listener) {
        ServerUserUtil.sendOperateMiner(userId,action,listener);
    }


    /**
     * 获取用户的几度人脉。
     */
    public static void getMinerRelationShip(int level,int page,final HttpObjectListener listener) {
        ServerUserUtil.getMinerRelationShip(level,page,listener);
    }



    /**
     * 获取矿工身价列表世界排行榜
     */
    public static void getMinerByPrice(final UserPojo userSelf,final HttpObjectListener listener) {
            ServerListUtil.getMinerByPrice(userSelf,listener);
    }







    //获取我的邀请人数列表
    public static void getMyInviteList(HttpObjectListener listener) {
        ServerUserUtil.getMyInviteList(listener);
    }



    //获取我的任务完成列表
    public static void getMissionInfo( HttpObjectListener listener) {
        ServerUserUtil.getMissionInfo(listener);
    }



    //获取用户的推荐矿工列表
    public static void getRecommendMiner(int page, HttpObjectListener listener) {
        ServerListUtil.getRecommendMiner(page,listener);
    }
    //获取模糊搜索结果
    public static void getSearchList(String searchKey,int page, HttpObjectListener listener) {
        ServerListUtil.getSearchList(searchKey,page,listener);
    }



    //获取用户的称号列表
    public static void getTitleList( HttpObjectListener listener) {
        ServerUserUtil.getTitleList(listener);
    }

    public static void sendBuyPackage(int packageId, HttpObjectListener listener) {
        ServerUserUtil.sendBuyPackage(packageId,listener);
    }

    public static void sendSelectedTitle(int titleId, HttpObjectListener listener) {
        ServerUserUtil.sendSelectedTitle(titleId,listener);
    }


    public static void sendOpenPool(int poolId, HttpObjectListener listener) {
        ServerUserUtil.sendOpenPool(poolId,listener);
    }

    public static void sendPoolInto(int poolId,long userId, HttpObjectListener listener) {
        ServerUserUtil.sendPoolInto(poolId,userId,listener);
    }


    public static void getMinerPkg(int pkgId, HttpObjectListener listener) {
            ServerUserUtil.getMinerPkg(pkgId,listener);
    }




    public static void getTradeMsgList(int page,final HttpObjectListener listener) {
        ServerUserUtil.getTradeMsgList(page,listener);
    }



    //获取收支记录
    public static void getIncomeRecord( int page,long userId, HttpObjectListener listener) {
         ServerUserUtil.getIncomeRecord(page,userId,listener);
    }



//获取礼物列表
    public static void getGiftList(final HttpObjectListener listener) {
        ServerUserUtil.getGiftList(listener);
    }

    public static void sendGift(long userId,ArrayList<GiftPojo> list, HttpObjectListener listener) {
        ServerUserUtil.sendGift(userId,list,listener);
    }




    //检查是否可以替换矿工
    public static void getCheckReplaceMiner(long srcMinerCode, long destMinerCode,  HttpObjectListener listener) {
        ServerUserUtil.getCheckReplaceMiner(srcMinerCode,destMinerCode,listener);
    }

    public static void getAppUpdate( HttpObjectListener listener) {
        ServerAPPUtil.getAppUpdate(listener);
    }
    //替换矿工
    public static void sendReplaceMiner(long srcMinerCode, long destMinerCode,  HttpObjectListener listener) {
        ServerUserUtil.sendReplaceMiner(srcMinerCode,destMinerCode,listener);
    }



    public static void sendDailyShare() {
        ServerAPPUtil.sendDailyShare();
    }
    public static void getCheckToken(HttpObjectListener listener) {
        ServerAPPUtil.getCheckToken(listener);
    }




    //获取矿池列表
    public static void getPoolList(final HttpObjectListener listener) {
        ServerListUtil.getPoolList(listener);
    }



    //获取签到相关的信息
    public static void getSignInfo(  HttpObjectListener listener) {
        ServerGeneralUtil.getSignInfo(listener);
    }
    public static void sendSignIn(final  HttpObjectListener listener) {
        ServerGeneralUtil.sendSignIn(listener);
    }


    //获取抢矿相关的信息
    public static void getQiangInfo( HttpObjectListener listener) {
        ServerGeneralUtil.getQiangInfo(listener);
    }
    //获取抢矿的奖励
    public static void getQiangGift( HttpObjectListener listener) {
        ServerGeneralUtil.getQiangGift(listener);
    }



    //获取送礼任务相关的信息
    public static void getSendInfo( HttpObjectListener listener) {
        ServerGeneralUtil.getSendInfo(listener);
    }
    //获取送礼任务的奖励信息
    public static void getSendGift( HttpObjectListener listener) {
        ServerGeneralUtil.getSendGift(listener);
    }


//获取合约信息
    public static void getContractList(int order,int page, HttpObjectListener listener) {
        ServerListUtil.getContractList(order,page,listener);
    }
    //创建一条合约
    public static void sendNewContract(int lanCount,final  HttpObjectListener listener) {
        ServerGeneralUtil.sendNewContract(lanCount,listener);
    }
    //获取我的交易合约
    public static void getMyContractList(int page,final HttpObjectListener listener) {
        ServerListUtil.getMyContractList(page,listener);
    }
    //交易一条合约
    public static void sendConfirmContract(long contractId,  HttpObjectListener listener) {
        ServerGeneralUtil.sendConfirmContract(contractId,listener);
    }
    //取消一条合约
    public static void sendCancelContract(long contractId,  HttpObjectListener listener) {
        ServerGeneralUtil.sendCancelContract(contractId,listener);
    }


    //获取首页排行榜
    public static void getHomeRank(ImageView imgView, HttpObjectListener listener) {
        ServerListUtil.getHomeRank(imgView,listener);
    }

}



