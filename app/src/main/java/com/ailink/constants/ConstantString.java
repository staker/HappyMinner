package com.ailink.constants;


/**
 * sava all const String
 * 
 * @author 小木桩（staker）
 * 
 */
public class ConstantString {

	public static final String APP_NAME = "ailink";
	public static final String EXIT_APP = "exit.application.xfrl";
	public static final String Package_Name = "com.ailink";






	/**
	 * 里面含有一些广播的类型
	 * 可以把自定义的广播类型 字符串都写在这个里面
	 */
	public interface BroadcastActions {
		public static final String Action_Exit_App = "action_exit_app";//退出app的广播
		public static final String Action_Change_Bottom_Tab = "action_change_bottom_tab";//切换首页导航的广播
		public static final String Action_Replace_Miner_Success= "action_replace_miner_success";//替换矿工成功
		public static final String Action_Share_Success = "action_share_success";//分享成功的回调
		public static final String Action_Home_Jump_To_Buy_Miner = "action_home_jump_to_buy_miner";
		public static final String Action_Change_Bottom_Tab_Tips = "action_change_bottom_tab_tips";//更改底部导航红点提示

	}


	public interface OperateMinerTypes {
		public static final String Type_Buy_Miner = "buy";//购买矿工
		public static final String Type_Fire_Miner = "fire";//解雇矿工
		public static final String Type_Master = "master";//主人
		public static final String Type_No_Money = "noMoney";//买不起
		public static final String Type_Me = "me";//自己
		public static final String Type_Replace = "replace";//替换（当出现这个字段的时候 replaceNum 字段）
	}


	public interface TradeMsgTypes {
		public static final String Type_Grab = "grab";//你抢了别人的矿工
		public static final String Type_Fire = "fire";//解雇矿工
		public static final String Type_Snatch = "snatch";//我的矿工被别人买走
		public static final String Type_ActivityInvite = "activityInvite";//邀请别人奖励
		public static final String Type_ActivityInvitees = "activityInvitees";//被邀请
		public static final String Type_ActivityGiveToken = "activityGiveToken";//自己进入游戏通知
		public static final String Type_Buy = "buy";//买了个无主矿工
		public static final String Type_Transaction = "transaction";//你被购买了
		public static final String Type_Replace = "replace";
		public static final String Type_Activity_Reward = "activityTemplate_1";//活动奖励
		public static final String Type_System = "system";//系统故障的奖励
	}


}
