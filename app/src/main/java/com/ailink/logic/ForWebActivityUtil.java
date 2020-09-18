package com.ailink.logic;

import org.json.JSONObject;

public class ForWebActivityUtil {

    public static String getCopyString(String json){
        String text="";
        try {
            JSONObject jsonObject=new JSONObject(json);
            text=jsonObject.optString("text");
        }catch (Exception e){

        }
        return text;
    }

    public static long getHomeActivityUserId(String json){
        try {
            JSONObject jsonObject=new JSONObject(json);
            long id=jsonObject.optLong("id");
            return id;
        }catch (Exception e){

        }
        return 0;
    }



}
