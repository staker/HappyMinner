package com.ailink.logic;

import android.app.Activity;
import android.content.Intent;

import com.ailink.activity.FangzhiMinerActivity;
import com.ailink.activity.MeSettingActivity;
import com.ailink.activity.MinerActivity;
import com.ailink.activity.MinerHomeActivity;
import com.ailink.activity.MissionQiangGiftActivity;
import com.ailink.activity.NoticeDetailActivity;
import com.ailink.activity.RankGiftActivity;
import com.ailink.activity.ReplaceMinerActivity;
import com.ailink.activity.SendGiftActivity;
import com.ailink.activity.SpeedRecordActivity;
import com.ailink.activity.TotalRankActivity;
import com.ailink.activity.WebActivity;


public class JumpActivityUtil {


	/**
	 * 跳转到普通的Activity不加任何的数据传递
	 * @param activity
	 * @param t
	 */
	public static void showNormalActivity(Activity activity,Class<?> t){
		Intent i = new Intent(activity, t);
		activity.startActivity(i);
	}
	/**
	 * 跳转到普通的Activity不加任何的数据传递  并且finish掉当前的activity
	 * @param activity
	 * @param t
	 */
	public static void showNormalActivityFinishSelf(Activity activity,Class<?> t){
		Intent i = new Intent(activity, t);
//		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		activity.startActivity(i);		
//		activity.overridePendingTransition(R.anim.screen_2_1,R.anim.screen_1_0);加跳转动画
		activity.finish();
	}
	public static void showNormalActivityClearTop(Activity activity,Class<?> t){
		Intent i = new Intent(activity, t);
//		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

		activity.startActivity(i);
//		activity.overridePendingTransition(R.anim.screen_2_1,R.anim.screen_1_0);加跳转动画
//		activity.finish();
	}




	/**
	 *跳转到矿工主页界面
	 */
	public static void showMinerHomeActivity(Activity activity,long userId,String userName,String avatarUrl){
		Intent i = new Intent(activity, MinerHomeActivity.class);
		i.putExtra("userId", userId);
		i.putExtra("userName", userName);
		i.putExtra("avatarUrl", avatarUrl);
		activity.startActivity(i);
	}


	public static void showMeSettingActivity(Activity activity,boolean isFromRegister){
		Intent i = new Intent(activity, MeSettingActivity.class);
		i.putExtra("isFromRegister", isFromRegister);
		activity.startActivity(i);
	}




	public static void showReplaceMinerActivity(Activity activity,long userId,String userName,int replaceTimes){
		Intent i = new Intent(activity, ReplaceMinerActivity.class);
		i.putExtra("userId", userId);
		i.putExtra("replaceTimes", replaceTimes);
		i.putExtra("userName", userName);
		activity.startActivity(i);
	}

	public static void showFangzhiMinerActivity(Activity activity,long removeUserId,int poolId){
		Intent i = new Intent(activity, FangzhiMinerActivity.class);
		i.putExtra("removeUserId", removeUserId);
		i.putExtra("poolId", poolId);
		activity.startActivity(i);
	}
	/**
	 *跳转到送礼界面
	 */
	public static void showSendGiftActivity(Activity activity,long userId,int speed,String userName,String avatarUrl){
		Intent i = new Intent(activity, SendGiftActivity.class);
		i.putExtra("userId", userId);
		i.putExtra("userName", userName);
		i.putExtra("avatarUrl", avatarUrl);
		i.putExtra("speed", speed);
		activity.startActivity(i);
	}
	/**
	 *跳转到算力记录界面
	 */
	public static void showSpeedRecordActivity(Activity activity,long userId,int speed,String userName,String avatarUrl){
		Intent i = new Intent(activity, SpeedRecordActivity.class);
		i.putExtra("userId", userId);
		i.putExtra("userName", userName);
		i.putExtra("avatarUrl", avatarUrl);
		i.putExtra("speed", speed);
		activity.startActivity(i);
	}
	/**
	 * 跳转到公告的详情界面
	 * @param activity
	 * @param noticeId
	 */
	public static void showNoticeDetailActivity(Activity activity,int noticeId){
		Intent i = new Intent(activity, NoticeDetailActivity.class);
		i.putExtra("noticeId", noticeId);
		activity.startActivity(i);
	}


	/**
	 *跳转到矿工算力界面
	 */
	public static void showSuanliActivity(Activity activity,long userId,int speed){
		Intent i = new Intent(activity, SpeedRecordActivity.class);
		i.putExtra("userId", userId);
		i.putExtra("speed", speed);
		activity.startActivity(i);
	}



	/**
	 *跳转到通用的  网页 界面
	 */
	public static void showWebActivity(Activity activity,String title,String pageUrl,boolean isReplaceTitle){
		Intent i = new Intent(activity, WebActivity.class);
		i.putExtra("title", title);
		i.putExtra("pageUrl", pageUrl);
		i.putExtra("isReplaceTitle", isReplaceTitle);
		activity.startActivity(i);
	}
	public static void showTotalRankActivity(Activity activity,String title,String pageUrl){
		Intent i = new Intent(activity, TotalRankActivity.class);
		i.putExtra("title", title);
		i.putExtra("pageUrl", pageUrl);
		activity.startActivity(i);
	}
	public static void showRankGiftActivity(Activity activity,String title,String pageUrl){
		Intent i = new Intent(activity, RankGiftActivity.class);
		i.putExtra("title", title);
		i.putExtra("pageUrl", pageUrl);
		activity.startActivity(i);
	}

	/**
	 *跳转包含三个tab的矿工界面
	 */
	public static void showMinerActivity(Activity activity,int tab){
		Intent i = new Intent(activity, MinerActivity.class);
		i.putExtra("tab", tab);
		activity.startActivity(i);
	}

}
