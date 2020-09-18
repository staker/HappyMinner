package com.ailink.http;

import com.ailink.constants.WebUrls;
import com.ailink.db.ApplicationData;
import com.ailink.db.Configuration;
import com.ailink.db.MyApplication;
import com.ailink.i.HttpObjectListener;
import com.ailink.json.JsonString;
import com.ailink.json.JsonUtil;
import com.ailink.pojo.GiftPojo;
import com.ailink.pojo.PkgInfoPojo;
import com.ailink.pojo.UserPojo;
import com.ailink.util.Logg;

import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import libs.okhttp.MyOkHttpUtil;
import libs.okhttps.MyOkHttpsUtil;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Staker on 2017/7/11.
 * 这个类主要存放单个用户和他相关的信息，诸如排行榜或者其他推荐矿工之类的列表是不会放在这个里面的
 * 但是获取单个人的所有矿工是需要放在这个类里面的
 */

public class ServerUserUtil {



    public static void sendRegister(String quhao,String phoneNumber,String password,String code,final HttpObjectListener listener) {
        String url= WebUrls.Send_Register;
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("phone", phoneNumber);
        map.put("zone", quhao);
        map.put("password", password);
        map.put("code", code);
        MyOkHttpUtil.sendPostAddHeader(url, null, map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("注册返回数据="+response);
//                {"data":[],"code":400,"msg":"验证码错误","t":1528356757623}
//                {"data":{"token":"kwMxK31zTA1c6FlOZNec0RxmByJZbyr4c9Lni5wnHjg=","shareCode":"","rank":21,"name":"name_679781","speed":10,"priceNew":100,"code":679781,"newUser":true},"code":200,"msg":"","t":1528356807526}
                //注册成功了就返回token
                if(listener!=null){
                    try{
                        JSONObject jsonObject=new JSONObject(response);
                        int code=jsonObject.optInt("code");
                        if(code==200){
                            JSONObject dataObject=jsonObject.optJSONObject("data");
                            String token=dataObject.optString("token");
                            Logg.e("注册返回token="+token);
                            if(token!=null&&!token.equals("")){
                                Configuration.getInstance(MyApplication.getContext()).setUserToken(token);
                                listener.onSucess(null);
                            }else{
                                listener.onDataError("登录失败");
                            }

                        }else{
                            listener.onDataError(jsonObject.optString("msg"));
                        }
                    }catch (Exception e){
                        listener.onDataError("请求失败");
                    }
                }
            }
        });
    }
    public static void sendLoginbyPassword(String phoneNumber,String password,final HttpObjectListener listener) {
        String url= WebUrls.Send_Login_By_Password;
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("phone",phoneNumber);
        map.put("password",password);
        Logg.e("url===="+url);
        MyOkHttpsUtil.sendPostAddHeader(url, null, map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                Logg.e("response="+e.getMessage()+e.toString());
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("帐号密码登录  返回数据="+response);
//                {"data":[],"code":400,"msg":"密码错误","t":1528359412769}
                if(listener!=null){
                    try{
                        JSONObject jsonObject=new JSONObject(response);
                        int code=jsonObject.optInt("code");
                        if(code==200){
                            JSONObject dataObject=jsonObject.optJSONObject("data");
                            String token=dataObject.optString("token");
                            Logg.e("登录返回token="+token);
                            if(token!=null&&!token.equals("")){
                                Configuration.getInstance(MyApplication.getContext()).setUserToken(token);
                                listener.onSucess(null);
                            }else{
                                listener.onDataError("登录失败");
                            }
                        }else{
                            listener.onDataError(jsonObject.optString("msg"));
                        }
                    }catch (Exception e){

                    }
                }
            }
        });
    }



    public static void sendLoginByCode(String quhao,String phoneNumber,String code,final HttpObjectListener listener) {
        String url= WebUrls.Send_Login_By_Code;
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("phone", phoneNumber);
        map.put("zone", quhao);
        map.put("code", code);
        MyOkHttpUtil.sendPostAddHeader(url,null, map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("手机验证码登录 返回数据="+response);
                if(listener!=null){
                    try{
                        JSONObject jsonObject=new JSONObject(response);
                        int code=jsonObject.optInt("code");
                        if(code==200){
                            JSONObject dataObject=jsonObject.optJSONObject("data");
                            String token=dataObject.optString("token");
                            Logg.e("手机验证码 登录返回token="+token);
                            if(token!=null&&!token.equals("")){
                                Configuration.getInstance(MyApplication.getContext()).setUserToken(token);
                                listener.onSucess(null);
                            }else{
                                listener.onDataError("登录失败");
                            }
                        }else{
                            listener.onDataError(jsonObject.optString("msg"));
                        }
                    }catch (Exception e){

                    }
                }
            }
        });
    }

    public static void sendBindPhone(String quhao,String phoneNumber,String code,final HttpObjectListener listener) {
        String url= WebUrls.Send_Bind_Phone;
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("phone", phoneNumber);
        map.put("zone", quhao);
        map.put("code", code);
        MyOkHttpUtil.sendPostAddHeader(url,JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("绑定手机返回数据 返回数据="+response);
                if(listener!=null){
                    try{
                        JSONObject jsonObject=new JSONObject(response);
                        int code=jsonObject.optInt("code");
                        if(code==200){
                            listener.onSucess(null);
                        }else{
                            listener.onDataError(jsonObject.optString("msg"));
                        }
                    }catch (Exception e){

                    }
                }
            }
        });
    }




    public static void getLoginPhoneCode(String phoneNumber,final HttpObjectListener listener) {
        String url= WebUrls.Get_Login_Phone_Code;
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("phone", phoneNumber);
        MyOkHttpsUtil.sendPostAddHeader(url, null, map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("获取登录时的手机验证码 返回数据="+response);
                if(listener!=null){
                    try{
                        JSONObject jsonObject=new JSONObject(response);
                        int code=jsonObject.optInt("code");
                        if(code==200){
                            JSONObject dataObject=jsonObject.optJSONObject("data");
                            int status=dataObject.optInt("status");
                            if(status==1){
                                listener.onSucess("");
                            }else{
                                listener.onDataError(dataObject.optString("errMsg"));
                            }
                        }else{
                            listener.onDataError(jsonObject.optString("msg"));
                        }
                    }catch (Exception e){

                    }
                }
            }
        });

    }

    public static void getRegisterPhoneCode(String phoneNumber,final HttpObjectListener listener) {
        String url= WebUrls.Get_Register_Phone_Code;
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("phone", phoneNumber);
        MyOkHttpsUtil.sendPostAddHeader(url, null, map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("获取注册时的手机验证码 返回数据="+response);
                if(listener!=null){
                    try{
                        JSONObject jsonObject=new JSONObject(response);
                        int code=jsonObject.optInt("code");
                        if(code==200){
                                JSONObject dataObject=jsonObject.optJSONObject("data");
                                int status=dataObject.optInt("status");
                                if(status==1){
                                    listener.onSucess("");
                                }else{
                                    listener.onDataError(dataObject.optString("errMsg"));
                                }
                        }else{
                            listener.onDataError(jsonObject.optString("msg"));
                        }
                    }catch (Exception e){

                    }
                }
            }
        });

    }






    public static void sendUpdatePassword(String oldPwd,String newPwd,final HttpObjectListener listener) {
        String url= WebUrls.Send_Update_Pwd;
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("passwordSrc", oldPwd);
        map.put("passwordNew", newPwd);
        MyOkHttpsUtil.sendPostAddHeader(url,JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("修改密码 返回数据="+response);
//                {"data":[],"code":400,"msg":"原密码错误","t":1528367482955}
                if(listener!=null){
                    try{
                        JSONObject jsonObject=new JSONObject(response);
                        int code=jsonObject.optInt("code");
                        if(code==200){
                            listener.onSucess(null);
                        }else{
                            listener.onDataError(jsonObject.optString("msg"));
                        }
                    }catch (Exception e){

                    }
                }
            }
        });
    }




















    public static void getMyDetailInfo(final HttpObjectListener listener) {
        String url= WebUrls.Get_My_Detail_Info;
        MyOkHttpsUtil.sendPostAddHeader(url, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                if(listener!=null){
                    JsonUtil.setMyDetailInfo(response);
                    listener.onSucess(null);
                }
            }
        });
    }





    public static void getUserInfo(final HttpObjectListener listener) {
        String url= WebUrls.Get_My_Info;
        MyOkHttpsUtil.sendPostAddHeader(url, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                if(listener!=null){
                    listener.onSucess(JsonUtil.getMineInfo(response));
                }
            }
        });
    }











    /**
     * 获取用户的详细信息
     * @param listener
     * @param userId  用户的id
     * @param userSelf  存放用户自己的信息
     * @param userOwner  存放用户主人的信息
     * @param listMiner  存放用户矿工列表的信息
     */
    public static void getUserInfo(long userId,final UserPojo userSelf, final UserPojo userOwner, final ArrayList<UserPojo> listMiner,final HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("minerCode", userId+"");
        MyOkHttpUtil.sendPostAddHeader(WebUrls.Get_User_Info, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("获取用户的详细信息="+response);
                if(listener!=null){
                    JsonUtil.setUserInfo(response,userSelf,userOwner,listMiner);
                    listener.onSucess("");
                }
            }
        });
    }






    public static void sendUpdateSex(String sex,final HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("sex", sex);
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Send_Update_Sex, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("更新用户的性别信息="+response);
//                {"data":[],"code":200,"msg":"","t":1528426509995}
                if(listener!=null){
                        try{
                            JSONObject object=new JSONObject(response);
                            int code=object.optInt("code");
                            if(code==200){
                                listener.onSucess(null);
                            }else{
                                listener.onDataError(object.optString("msg"));
                            }
                        }catch (Exception e){
                            listener.onDataError("请求失败");
                        }
                }
            }
        });
    }


    public static void sendUpdateName(String name,final HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("name", name);
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Send_Update_Name, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("更新用户的用户名名字="+response);
//                {"data":[],"code":200,"msg":"","t":1528427153584}
                if(listener!=null){
                    try{
                        JSONObject object=new JSONObject(response);
                        int code=object.optInt("code");
                        if(code==200){
                            listener.onSucess(null);
                        }else{
                            listener.onDataError(object.optString("msg"));
                        }
                    }catch (Exception e){
                        listener.onDataError("请求失败");
                    }
                }
            }
        });
    }





    public static void sendUpdateAvatar(String filsStream,final HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("files", filsStream);
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Send_Update_Avatar, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("更新用户的头像="+response);
//                {"data":[],"code":200,"msg":"","t":1528427153584}
                if(listener!=null){
                    try{
                        JSONObject object=new JSONObject(response);
                        int code=object.optInt("code");
                        if(code==200){
                            listener.onSucess(null);
                        }else{
                            listener.onDataError(object.optString("msg"));
                        }
                    }catch (Exception e){
                        listener.onDataError("请求失败");
                    }
                }
            }
        });
    }


    public static void getMyMinerListForPool(int poolId,final UserPojo userSelf,final HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("poolId", poolId+"");
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_My_Miner_For_Pool, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("获取我的矿工列表 放置在矿池="+response);
                if(listener!=null){
                    listener.onSucess(JsonUtil.getMyMinerListForPool(response,userSelf));
                }
            }
        });
    }





    public static void getHomeMinerList(final HttpObjectListener listener, final UserPojo userSelf) {
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Home_My_Miner, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("获取我的矿工列表="+response);
                if(listener!=null){
                    listener.onSucess(JsonUtil.getMyMinerList(response,userSelf));
                }
            }
        });
    }




    public static void sendOperateMiner(long userId, final String action, final HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("minerCode", userId+"");
        map.put("action", action);
        map.put("poolId", ApplicationData.getInstance().poolId+"");
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Send_Operate_Miner, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
//                {"data":[],"code":400,"msg":"您已超过本日最大买卖矿工次数，请明日再试","t":1534412925325}
                Logg.e("操作矿工 action="+action+" 返回结果="+response);
                if(listener!=null){
                    try {
                        if(response!=null){
                            JSONObject mainJson=new JSONObject(response);
                            int code=mainJson.optInt("code");
                            if(code!=200){
                                String errMsg=mainJson.optString("msg");
                                listener.onDataError(errMsg);
                                return;
                            }

                            JSONObject jsonObject=mainJson.optJSONObject("data");
                            int status=jsonObject.optInt("status");
                            if(status==1){
                                listener.onSucess("");
                            }else{
                                String errMsg=jsonObject.optString("errMsg");
                                listener.onDataError(errMsg);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        listener.onDataError("数据异常");
                    }

                }
            }
        });
    }






    public static void getMinerRelationShip(final int level,int page,final HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("lev", level+"");
        map.put("pageNum", page+"");
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Miner_Relationship, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e(level+"度人脉圈="+response);
                if(listener!=null){
                    listener.onSucess(JsonUtil.getFriendListDu(response));
                }
            }
        });
    }





    public static void getMyInviteList(final HttpObjectListener listener) {
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_My_Invite_List, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("我的邀请返回数据="+response);
                if(listener!=null){
                    listener.onSucess(JsonUtil.getMyInviteList(response));
                }
            }
        });
    }



    public static void getMissionInfo(final HttpObjectListener listener) {
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_MissionInfo, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("任务列表="+response);
                if(listener!=null){
                    listener.onSucess(JsonUtil.getMissionList(response));
                }
            }
        });
    }








    public static void getTitleList(final HttpObjectListener listener) {
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Title_List, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                if(listener!=null){
                    listener.onSucess(JsonUtil.getTitleList(response));
                }
            }
        });
    }

    public static void sendBuyPackage(int packageId,final HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("packageId",""+packageId);
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Send_Buy_Package, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("购买矿工位置返回：="+response);
                if(listener!=null){
                    try {
                        JSONObject object=new JSONObject(response);
                        int code=object.optInt("code");
                        if(code==200){
                            listener.onSucess(null);
                        }else{
                            listener.onDataError(object.optString("msg"));
                        }

                    }catch (Exception e){
                        listener.onDataError("请求失败");
                    }
                }
            }
        });
    }


    public static void sendSelectedTitle(int titleId,final HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("id",""+titleId);
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Send_Select_Title, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                if(listener!=null){
                   listener.onSucess(response);
                }
            }
        });
    }


    public static void sendOpenPool(int poolId,final HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("poolId",""+poolId);
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Send_Pool_Open, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("开启矿池返回：="+response);
                if(listener!=null){
                    try {
                        JSONObject object=new JSONObject(response);
                        int code=object.optInt("code");
                        if(code==200){
                            listener.onSucess(null);
                        }else{
                            listener.onDataError(object.optString("msg"));
                        }

                    }catch (Exception e){
                        listener.onDataError("请求失败");
                    }

                }
            }
        });
    }

    public static void sendPoolInto(int poolId,long userId,final HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("poolId",""+poolId);
        map.put("minerCode",""+userId);
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Send_Pool_Into, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("放置矿工返回：="+response);
                if(listener!=null){
                    try {
                        JSONObject object=new JSONObject(response);
                        int code=object.optInt("code");
                        if(code==200){
                            listener.onSucess(null);
                        }else{
                            listener.onDataError(object.optString("msg"));
                        }

                    }catch (Exception e){
                        listener.onDataError("请求失败");
                    }

                }
            }
        });
    }







    public static void getMinerPkg(final int pkgId,final HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("pkgId", pkgId+"");
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Miner_Pkg, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("包过PKD"+pkgId+",给用户收矿="+response);
                if(listener!=null){
                    try {
                        if(response!=null){
                            JSONObject mainJson=new JSONObject(response);
                            JSONObject dataJson=mainJson.optJSONObject("data");
                            JSONArray arrayJson=dataJson.optJSONArray("openMineList");
                            int status=dataJson.optInt("status");
                            String errMsg=dataJson.optString("errMsg");
                            ArrayList<PkgInfoPojo> list=new ArrayList<PkgInfoPojo>();
                            int size=arrayJson.length();
                            for(int i=0;i<size;i++){
                                JSONObject temp=arrayJson.getJSONObject(i);
                                PkgInfoPojo pkgInfoPojo=new PkgInfoPojo();
                                pkgInfoPojo.status=status;
                                pkgInfoPojo.errMsg=errMsg;
                                pkgInfoPojo.tokenType=temp.optString("tokenType");
                                pkgInfoPojo.addToken=temp.optString("addToken");
                                pkgInfoPojo.poolId=temp.optInt("poolId");
                                list.add(pkgInfoPojo);
                            }
                            listener.onSucess(list);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Logg.e("333333333333333333333333333333=");
                        listener.onDataError("收矿失败");
                    }
                }
            }
        });
    }







    public static void getTradeMsgList(int page,final HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("pageCount", "20");
        map.put("pageNum", page+"");
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Trade_Msg, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("  获取交易 信息="+response);
                if(listener!=null){
                    listener.onSucess(JsonUtil.getTradeMsgList(response));
                }
            }
        });
    }









    public static void getIncomeRecord(final int page,long userId,final HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("pageCount", "20");
        map.put("pageNum", page+"");
        map.put("minerCode", userId+"");
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Income_Record, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e(page+"页"+"  获取收支记录 列表="+response);
                if(listener!=null){
                    listener.onSucess(JsonUtil.getIncomeList(response));
                }
            }
        });
    }




    public static void getGiftList(final HttpObjectListener listener) {
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Gift_List, JsonString.getTokenMap(), null, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("获取礼物 列表="+response);
                if(listener!=null){
                    listener.onSucess(JsonUtil.getGiftList(response));
                }
            }
        });
    }


    public static void sendGift(long userId, final ArrayList<GiftPojo> list, final HttpObjectListener listener) {
        String json=JsonString.getSendGiftJson(list);
        HashMap<String,String> map=new HashMap<String,String>();
        Logg.e("id="+userId);
        map.put("minerCode", userId+"");
        map.put("giftList", json);
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Send_Gift, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("赠送礼物 返回结果="+response);
                if(listener!=null){
                    try{
                        //{"data":[],"code":200,"msg":"","t":1525845371281}
                        JSONObject jsonObject=new JSONObject(response);
                        int code=jsonObject.optInt("code");
                        if(code==200){
                            listener.onSucess("赠送礼物成功");
                        }else{
                            listener.onDataError(jsonObject.optString("msg"));
                        }
                    }catch (Exception e){

                    }

                }
            }
        });
    }









    public static void getCheckReplaceMiner(long srcMinerCode, long destMinerCode, final HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("destMinerCode", destMinerCode+"");
        map.put("srcMinerCode", srcMinerCode+"");
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Get_Check_Replace_Miner, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("检查是否可以替换矿工 ="+response);
                if(listener!=null){
                    if(response!=null){
                        listener.onSucess(response);
                    }else{
                        listener.onDataError("数据异常");
                    }
                }
            }
        });
    }









    public static void sendReplaceMiner(long srcMinerCode, long destMinerCode, final HttpObjectListener listener) {
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("destMinerCode", destMinerCode+"");
        map.put("srcMinerCode", srcMinerCode+"");
        MyOkHttpsUtil.sendPostAddHeader(WebUrls.Send_Replace_Miner, JsonString.getTokenMap(), map, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e, int id) {
                if(listener!=null){
                    listener.onException(e);
                    listener.onDataError("请求失败");
                }
            }
            @Override
            public void onResponse(String response,int id) {
                Logg.e("开始替换矿工 替换矿工 ="+response);
                if(listener!=null){
                    if(response!=null){
                        listener.onSucess(response);
                    }else{
                        listener.onDataError("数据异常");
                    }
                }
            }
        });
    }



























}
