
package com.ailink.constants;

import com.ailink.db.Configuration;
import com.ailink.db.MyApplication;

/**
 * adb  connect 127.0.0.1:7555  连接木木模拟器
 * adb connect 127.0.0.1:26944  海马玩模拟器
 * 所有的接口都存放在这个类
 */
public class WebUrls {



	public static final String BaseApiUrl;//根API地址
	public static  final String BasePathUrl;
	public static final String Certificate;


	public static final boolean IsPublic = true;//是否是正式环境


    public static String[] joinTokens = {"hemzxVQ9ofA2reLEswf77uWl7IR8pKIZOprrYfgBBs4=", "VVm4fPWBV3P44XVPQ/ROyW+EmOuxENDu00U7Io1HDgA=",
			"k5cel2j9D1YWjFTxcU7oHSCgkb0vZtmqvHjNEVERoFI=","3leckJcnU36FXWGQl0jKg67AhIhGoqW9qqyAjWcr6S8=","p5Kcu/xlmOEmhnFDSuQSsjChkroOufi/w3CquovJZ9o=","gY8Hx9UT0AUl2ji0fqK4PDoY//vVGYLATsM8yj/Gr9E="};//这是一些token


    static {
		if(IsPublic){
			Configuration.getInstance(MyApplication.getContext()).setUserToken(WebUrls.joinTokens[2]);
			//正式地址
			BaseApiUrl = "http://hm.jhw.la/happyminer/";//根地址
			BasePathUrl="http://hm.jhw.la/happyminer/dist/#/";
			Certificate= "public_certificate.pem";
		}else{
			//测试地址
			BaseApiUrl = "https://v4.juhuiwan.cn/happyminer/";//根地址
			BasePathUrl="https://v4.juhuiwan.cn/happyminer/test/#/";
			Certificate= "test_certificate.pem";
		}
	}

	//下面的都是页面地址
	public  static final String App_PathUrl_End ="?appRunType=android&token=";//app跳转到页面地址，后面通用的url字符串

	public  static final String App_Rules =BasePathUrl +"rule"+App_PathUrl_End;//app的规则地址
	public  static final String App_Mineral_Pool =BasePathUrl +"mineralPool"+App_PathUrl_End;//矿池界面
	public  static final String App_Bind_Phone =BasePathUrl +"bindPhone"+App_PathUrl_End;//绑定手机
	public  static final String App_Wallet =BasePathUrl +"wallet"+App_PathUrl_End;//钱包
	public  static final String App_Speed =BasePathUrl +"speedAddition"+App_PathUrl_End;//算力加成界面
	public  static final String App_DuiHuan =BasePathUrl +"exchange"+App_PathUrl_End;//兑换
	public  static final String App_News=BasePathUrl +"news"+App_PathUrl_End;//消息界面
	public  static final String App_Bang=BasePathUrl +"rank"+App_PathUrl_End;//排行榜
	public  static final String App_Rank_Prize=BasePathUrl +"rankPrize"+App_PathUrl_End;//奖励&规则
    public  static final String App_Xieyi=BasePathUrl +"agreement";
	public  static final String App_QiangKuang=BasePathUrl +"auction"+App_PathUrl_End;
	public  static final String App_Safe=BasePathUrl +"safe"+App_PathUrl_End;//安全的界面
	public  static final String App_Trevi=BasePathUrl +"trevi"+App_PathUrl_End;//许愿池界面
	public  static final String App_Wolf_Kill=BasePathUrl +"wolvesKill"+App_PathUrl_End;//狼人杀的界面
	public  static final String App_Contract_Rule=BasePathUrl +"contractRule"+App_PathUrl_End;//合约交易规则






	//下面的全部都是接口地址

	public  static final String Send_General_Value =BaseApiUrl +"account/general";//这是一个通用的反馈接口，比如分享成功，登录成功，等都可以用这个接口来反馈，只是key对应传的值不一样，个别接口还要穿value的值
	public  static final String Get_Banner =BaseApiUrl +"account/showBanner";//获取banner返回结果
	public  static final String Get_Daily_Share =BaseApiUrl +"account/dailyShare";//每日分享成功调用一下后台这个接口
	public  static final String Get_Check_Token =BaseApiUrl +"account/checkTokenPhone";//检查token是否有效
    public  static final String Get_App_All_Token = BaseApiUrl + "pool/giveToken";//获取开心矿工的总产出蓝钻
	public  static final String Send_WeiXin_Login = BaseApiUrl + "account/loginPhone";//这是微信登陆的接口
	public  static final String Get_Search_Miner = BaseApiUrl + "miner/searchByName";//这是模糊搜索接口







	public  static final String Send_Register = BaseApiUrl + "account/phoneReg";//这是用户的注册接口
	public  static final String Send_Login_By_Password = BaseApiUrl + "account/loginPhonePwd";//通过帐号密码登录
	public  static final String Send_Login_By_Code = BaseApiUrl + "account/loginPhoneCode";//通过手机验证码登录
	public  static final String Send_Bind_Phone = BaseApiUrl + "phone/bindPhone";//绑定手机
	public  static final String Get_Register_Phone_Code = BaseApiUrl + "phone/regCode";//获取注册手机验证码
	public  static final String Get_Login_Phone_Code = BaseApiUrl + "phone/loginCode";//获取手机验证码登录的code
	public  static final String Send_Update_Pwd = BaseApiUrl + "account/updatePwd";//修改登陆密码
	public  static final String Get_My_Detail_Info = BaseApiUrl + "account/getUserInfo";//获取自己的信息，只要传token
    public  static final String Send_Update_Sex = BaseApiUrl + "account/updateSex";//更新用户的性别
	public  static final String Send_Update_Name = BaseApiUrl + "account/updateName";//更新用户的昵称
    public  static final String Send_Update_Avatar = BaseApiUrl + "upload/headPortrait";//上传头像





	public  static final String Get_Update_App = BaseApiUrl + "admin/appUpdateInfo?code=YSbdREFzQO9ayGjcBNRMY3yXi6kA";//是否要更新app
	public  static final String Get_My_Info = BaseApiUrl + "miner/getMinerInfo";//获取自己的信息
	public  static final String Get_User_Info = BaseApiUrl + "miner/getMinerInfo";//获取用户信息
	public  static final String Get_Home_Notice = BaseApiUrl + "notice/hint";//获取首页公告

    public  static final String Get_TopTrans_Miner = BaseApiUrl + "miner/getMinerByTrans";//获取活跃排行榜Top10
	public  static final String Get_Home_Rank = BaseApiUrl + "account/homeRank";//获取首页排行榜
	public  static final String Get_Home_My_Miner = BaseApiUrl + "account/home";//获取我的矿工列表
	public  static final String Get_My_Miner_For_Pool = BaseApiUrl + "pool/minerList";//获取我的矿工列表用于放置在某个矿池
	public  static final String Send_Operate_Miner = BaseApiUrl + "miner/operateMiner";//解雇或者购买矿工
	public  static final String Get_Miner_Relationship = BaseApiUrl + "miner/getMinerRelationship";//获取用户的人脉列表，按度数返回
	public  static final String Get_Miner_ByPrice = BaseApiUrl + "miner/getMinerByPrice";//获取矿工的身价列表，从身价最高的开始返回

	public  static final String Get_My_Invite_List = BaseApiUrl + "account/getRelationList";//获取我的邀请人数列表
	public  static final String Get_MissionInfo = BaseApiUrl + "account/getMissionInfo";//获取任务完成列表

	public  static final String Get_Recommend = BaseApiUrl + "miner/recommend";//获取用户的推荐矿工
    public  static final String Get_Miner_Pkg = BaseApiUrl + "miner/openMine";//获取矿工挖到的矿石

	public  static final String Get_Title_List = BaseApiUrl + "miner/titleList";//获取称号列表
	public  static final String Send_Select_Title = BaseApiUrl + "miner/titleSelect";//选择一个称号来显示，提交称号的id


	public  static final String Get_Notice_Msg = BaseApiUrl + "notice/msgList";//获取系统公告列表
	public  static final String Get_Notice_Detail = BaseApiUrl + "notice/getMsgInfo";//获取系统公告详情

	public  static final String Get_Trade_Msg = BaseApiUrl + "miner/getMsgList";//获取用户的交易信息  分页
	public  static final String Get_Income_Record = BaseApiUrl + "miner/getSpeedInfoList";//获取用户的收支记录
	public  static final String Get_Gift_List = BaseApiUrl + "miner/getGiftList";//获取礼物列表
	public  static final String Send_Gift = BaseApiUrl + "miner/sendGift";//赠送礼物

	public  static final String  Get_Check_Replace_Miner= BaseApiUrl + "miner/checkReplaceMiner";//检查是否可以替换矿工
	public  static final String  Send_Replace_Miner= BaseApiUrl + "miner/replaceMiner";//替换矿工
	public  static final String  Get_Daily_Mission= BaseApiUrl + "mission/getTokenMissionInfo";//获取每日任务


	public  static final String  Get_Pool_List= BaseApiUrl + "pool/getList";//获取矿池列表
	public  static final String  Send_Pool_Open= BaseApiUrl + "pool/open";//开启矿池
	public  static final String  Send_Pool_Into= BaseApiUrl + "pool/into";//放置矿工
	public  static final String  Send_Buy_Package= BaseApiUrl + "miner/buyPackage";//花费蓝钻购买矿工位置



	public  static final String  Get_Wish_Pool= BaseApiUrl + "wishingPool/getInfo";//获取许愿池的相关信息
	public  static final String  Send_Buy_Wish_Stone= BaseApiUrl + "wishingPool/buy";//购买许愿石
	public  static final String  Get_Lottery_Info= BaseApiUrl + "lottery/getInfo";//获取抢矿结果
	public  static final String  Send_Lottery_Join= BaseApiUrl + "lottery/join";//参与抢矿


	public  static final String  Get_Wolf_Pool= BaseApiUrl + "wolfPool/getInfo";//获取狼人杀的相关信息
	public  static final String  Send_Buy_Wolf_Pool= BaseApiUrl + "wolfPool/buy";//购买狼人杀金水



	public  static final String  Get_Sign_Info= BaseApiUrl + "mission/getSignInfo";//获取签到相关的信息
	public  static final String  Send_Sign_In= BaseApiUrl + "mission/signIn";//签到
	public  static final String  Get_Qiang_Mission_Info= BaseApiUrl + "mission/getLotteryMissionInfo";//获取抢矿任务详情
	public  static final String  Get_Send_Mission_Info= BaseApiUrl + "mission/getGiftMissionInfo";//获取每日送礼任务详情
	public  static final String  Get_Qiang_Gift= BaseApiUrl + "mission/recvLotteryMission";//领取抢矿任务奖励
	public  static final String  Get_Send_Gift= BaseApiUrl + "mission/recvGiftMission";//领取送礼任务奖励


	public  static final String Get_Contract = BaseApiUrl + "contract/list";//获取合约的数据，分页获取
	public  static final String Get_My_Contract = BaseApiUrl + "contract/myList";//获取自己的合约
	public  static final String Send_Deal_Contract = BaseApiUrl + "contract/recv";//交易一条合约
	public  static final String Send_New_Contract = BaseApiUrl + "contract/create";//创建一条新的合约
	public  static final String Send_Delete_Contract = BaseApiUrl + "contract/cancel";//删除一条合约
}
