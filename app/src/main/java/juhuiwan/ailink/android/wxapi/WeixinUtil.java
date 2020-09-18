package juhuiwan.ailink.android.wxapi;

import com.ailink.db.MyApplication;
import com.ailink.util.Logg;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WeixinUtil {




//    第二步：通过code获取access_token
//    获取第一步的code后，请求以下链接获取access_token：
//    https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code



    public final static String  AppId="wxdd0d5625afe09ce6";//"wxdd0d5625afe09ce6";
    public final static String  AppSecret="1e49558182440a17344e92d5612ce47a";
//    public static String tokenUrl="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+AppId+"&secret="+AppSecret+"&code="+code+"&grant_type=authorization_code";


    public static IWXAPI  registerToWeixin(){
        IWXAPI api= WXAPIFactory.createWXAPI(MyApplication.getContext(),AppId,false);
        api.registerApp(AppId);
        return  api;
    }


    public static void  loginWeixin( IWXAPI api){
        // send oauth request
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        boolean flag=api.sendReq(req);
    }
    public static String getTokenUrl(String code){
        return "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+AppId+"&secret="+AppSecret+"&code="+code+"&grant_type=authorization_code";
    }


}
