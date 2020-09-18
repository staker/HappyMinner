package com.ailink.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.UserPojo;
import com.ailink.util.GsonUtil;
import com.ailink.util.Logg;

import java.net.URLEncoder;


/**
 * 保存用户各种信息的配置文件，这是一个单例
 *
 * @author 小木桩（staker） 04-06-21 04-06-24
 */
public class Configuration {
    private static Configuration config = null;
    private SharedPreferences share;


    private Configuration(Context context) {
        share = context.getSharedPreferences("configurations_data", Context.MODE_PRIVATE);
    }

    public static Configuration getInstance(Context context) {
        if (config == null) {
            config = new Configuration(context);
        }
        return config;
    }


    /**
     *
     * @param code
     */
    public void setVersionCode(int code) {
        Editor editor = share.edit();
        editor.putInt("guide_version_code", code);
        editor.commit();
    }
    /**
     * 这个方法主要是记录是否需要跳转到引导页面，GuideActivity
     * @return   int
     */
    public int getVersionCode() {
        return share.getInt("guide_version_code", 0);
    }


    /**
     *  获取用户的Token信息
     * @return String
     */
    public String getUserToken() {
        return share.getString("userToken", null);
    }

    public String getEncodeUserToken() {
        try{
            String token=getUserToken();
            if(token==null){
                return null;
            }
            String encodeToken= URLEncoder.encode(token,"utf-8");
            return  encodeToken;
        }catch (Exception e){
        }
        return null;
    }

    public void setUserToken(String token) {
        Logg.e("设置 新的 到 本地token="+token);
        Editor editor = share.edit();
        editor.putString("userToken", token);
        editor.commit();
    }


    public void setUserPojo(UserPojo userPojo) {
        Editor editor = share.edit();
        if(userPojo==null){
            editor.putString("user_pojo", null);
            editor.commit();
            return;
        }
        String userStr= GsonUtil.getInstance().toJson(userPojo);
        editor.putString("user_pojo", userStr);
        editor.commit();
    }
    /**
     * 获取用户实体信息类
     * @return  UserPojo
     */
    public UserPojo getUserPojo() {
        String userStr=share.getString("user_pojo", null);
        if(userStr==null){
            return  null;
        }
        UserPojo userPojo=GsonUtil.getInstance().json2Bean(userStr,UserPojo.class);
        return userPojo;
    }

    public boolean isNewUser() {
        return share.getBoolean("is_new_user", true);
    }
    public void setNewUser(boolean isNewUser){
        Editor editor = share.edit();
        editor.putBoolean("is_new_user", isNewUser);
        editor.commit();
    }








    public boolean isFirstRun() {
        return share.getBoolean("is_first_run_app", false);
    }
    public void setFirstRun(boolean isFirstRun){
        Editor editor = share.edit();
        editor.putBoolean("is_first_run_app", isFirstRun);
        editor.commit();
    }


    public void clearAllInfos() {
        Editor editor = share.edit();
        editor.clear();
        editor.commit();
    }
}
