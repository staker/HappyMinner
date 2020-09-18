package juhuiwan.ailink.android.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ailink.constants.ConstantString;
import com.ailink.constants.WebUrls;
import com.ailink.db.Configuration;
import com.ailink.db.MyApplication;
import com.ailink.i.HttpObjectListener;
import com.ailink.util.BroadcastUtil;
import com.ailink.util.Logg;
import com.ailink.util.PhoneDeviceUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.callback.StringCallback;


import org.json.JSONObject;

import java.util.HashMap;

import libs.okhttp.MyOkHttpUtil;
import okhttp3.Call;
import okhttp3.Response;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;
    private IWXAPI api=MyApplication.getContext().weixinApi;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        IWXAPI api= WXAPIFactory.createWXAPI(MyApplication.getContext(), WeixinUtil.AppId,true);
        //如果没回调onResp，八成是这句没有写
        if(api==null){
            api=WXAPIFactory.createWXAPI(this, WeixinUtil.AppId, false);
            api.registerApp(WeixinUtil.AppId);
        }
        api.handleIntent(getIntent(), this);
        handleIntent(getIntent());
        Logg.e("微信登陆成功了说明");
    }
    private void handleIntent(Intent intent) {
        SendAuth.Resp resp = new SendAuth.Resp(intent.getExtras());
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            //用户同意
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }
    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        finish();
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp resp) {
        Logg.e("resp.errCode="+resp.errCode);
//        Logg.e(resp.errStr);
//        Logg.e("错误码 : " + resp.errCode + "");
//        switch (resp.errCode) {
//            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                if (RETURN_MSG_TYPE_SHARE == resp.getType()) Logg.e("分享失败");
//                else Logg.e("登录失败");
//                break;
//            case BaseResp.ErrCode.ERR_OK:
//                switch (resp.getType()) {
//                    case RETURN_MSG_TYPE_LOGIN:
//                        //拿到了微信返回的code,立马再去请求access_token
//                        String code = ((SendAuth.Resp) resp).code;
//                        Logg.e("code = " + code);
//
//                        //就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求
//
//                        break;

//                    case RETURN_MSG_TYPE_SHARE:
//                        Logg.e("微信分享成功");
//                        finish();
//                        break;
//                }
//                break;
//        }





        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //微信分享成功和登录成功都会调用这里，所以这里需要分开处理，  目前简单的做法就是  异常处理，出了异常就代表是微信分享的回调，因为下面的强转会报异常
                try{
                    String code = ((SendAuth.Resp) resp).code;
                    Logg.e("resp.url="+WeixinUtil.getTokenUrl(code));//只有是微信登录的回调，上面的强行转换才会成功，否则会出现异常
//                    MyOkHttpUtil.sendGet(WeixinUtil.getTokenUrl(code), new StringCallback() {
//                        @Override
//                        public void onError(Call call, Response response, Exception e, int id) {
//                            finish();
//                        }
//                        @Override
//                        public void onResponse(String response,int id) {
//                            Logg.e("resp.response="+response);
//                            login(response);
//                        }
//                    });
                    MyOkHttpUtil.getUrlResult(WeixinUtil.getTokenUrl(code), new HttpObjectListener() {
                                @Override
                                public void onSucess(Object object) {
                                    String ss=(String)object;
                                    login(ss);
                                    Logg.e("获取微信登录的用户的返回数据="+object);
                        }
                    });

                }catch (Exception e){
                    BroadcastUtil.sendBroadcast(MyApplication.getContext(), ConstantString.BroadcastActions.Action_Share_Success);
                    finish();
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                finish();
                break;
            case RETURN_MSG_TYPE_SHARE:
                        Logg.e("微信分享成功");
                        finish();
                        break;
            default:
                finish();
                break;
        }
    }

    private void login(String userInfo){
        try{
            final JSONObject jsonObject=new JSONObject(userInfo);
            jsonObject.put("appSystem","Android");
            jsonObject.put("device", PhoneDeviceUtil.getMachineInfo());
            String temp=jsonObject.toString();
            HashMap<String,String> map=new HashMap<String,String>();
            map.put("jsonStr",temp);

            MyOkHttpUtil.sendPost(WebUrls.Send_WeiXin_Login, map, new StringCallback() {
                @Override
                public void onError(Call call, Response response, Exception e, int id) {
                    finish();
                }
                @Override
                public void onResponse(String response,int id) {
                    try{
                        JSONObject jsonObject1=new JSONObject(response);
                        JSONObject dataJson=jsonObject1.optJSONObject("data");
                        String userToken=dataJson.optString("token");
                        boolean isNewUser=dataJson.optBoolean("newUser");

                        Logg.e("userToken="+userToken);
                        Logg.e("isNewUser="+isNewUser);
                        Configuration.getInstance(MyApplication.getContext()).setUserToken(userToken);
                        Configuration.getInstance(MyApplication.getContext()).setNewUser(isNewUser);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    finish();
                }
            });
            Logg.e("temp="+temp);

        }catch (Exception e){

        }
    }
}
