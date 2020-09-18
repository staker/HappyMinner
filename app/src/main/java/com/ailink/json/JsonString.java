package com.ailink.json;


import com.ailink.db.Configuration;
import com.ailink.db.MyApplication;
import com.ailink.pojo.GiftPojo;
import com.ailink.util.GsonUtil;
import com.ailink.util.Logg;
import com.ailink.util.MD5Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Staker on 2017/7/14.
 * 这个类用来创建一些json，比如获取分页数据的时候就需要创建json传到后台
 */

public class JsonString {


    /**
     * 这里默认  把pageSize定义为20，所以只要传page就可以
     *
     * @param page
     */
    public static String getTopicString(int page) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("page", page + "");
        map.put("pageSize", "20");
        String json = GsonUtil.getInstance().toJson(map).trim();
        return json;
    }

    public static HashMap<String, String> getTokenMap() {
        String token = Configuration.getInstance(MyApplication.getContext()).getUserToken();
        if (token == null) {
            return null;
        }
        long time = System.currentTimeMillis();
        String stime10 = (time + "").substring(10, 11);
        String stime11 = (time + "").substring(11, 12);
        String stime12 = (time + "").substring(12, 13);

        int time10 = Integer.parseInt(stime10);
        int time11 = Integer.parseInt(stime11);
        int time12 = Integer.parseInt(stime12);

        int type = (time10 * time11 * time12 % 4) + 1;
        String subTime = "";
        String subToken = "";

        switch (type) {
            case 1:
                subTime = (time + "").substring(7, 11);
                subToken = token.substring(7, 11);
                break;
            case 2:
                subTime = (time + "").substring(4, 9);
                subToken = token.substring(7, 10);
                break;
            case 3:
                subTime = (time + "").substring(6, 10);
                subToken = token.substring(4, 9);
                break;
            case 4:
                subTime = (time + "").substring(5, 11);
                subToken = token.substring(6, 11);
                break;
        }

        String md5Result = MD5Util.md5String(subTime + "happyMiner20180816" + subToken);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("token", Configuration.getInstance(MyApplication.getContext()).getUserToken());
        map.put("timestamp", time + "");
        map.put("sign", md5Result);
        return map;
    }


    public static String getSendGiftJson(ArrayList<GiftPojo> list) {
        String sJson = "";
        try {
            JSONArray array = new JSONArray();
            for (GiftPojo giftPojo : list) {
                if (giftPojo.count == 0) {
                    continue;
                }
                JSONObject item = new JSONObject();
                item.put("id", giftPojo.giftId);
                item.put("num", giftPojo.count);
                array.put(item);
            }
            sJson = array.toString();
            Logg.e("sjson=" + sJson);
        } catch (Exception e) {

        }

        return sJson;
    }


}
