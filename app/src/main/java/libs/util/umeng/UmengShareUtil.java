package libs.util.umeng;


import com.ailink.activity.BaseActivity;
import com.ailink.util.Logg;

import org.json.JSONObject;

import java.util.Random;

/**
 * Created by Staker on 2017/8/7.
 */

public class UmengShareUtil {


    static String appIconUrl="http://hm.jhw.la/happyminer/img/logo.png";
    static String pageUrl="http://hm.jhw.la/happyminer/wechat/oauth?state=";


    public static void share(BaseActivity activity,long userId,String userName,int newPrice,int speed){
        Random random=new Random();
        int index=random.nextInt(10);
        Logg.e("index="+index);
        ShareContentPojo shareContentPojo=new ShareContentPojo(index);
        String startDes=userName+":身价"+newPrice+",算力"+speed+","+shareContentPojo.getContent();
        new ShareDialog(activity,shareContentPojo.getTitle(),startDes,appIconUrl, pageUrl+userId).show();
    }


    public static void shareNormal(BaseActivity activity,String title, String content, String image, String url){
        new ShareDialog(activity,title,content,image, url).show();
    }


    public static void shareByServerJson(BaseActivity activity,String serverJson){
        Logg.e("serverJson="+serverJson.toString());
        try{
            JSONObject object=new JSONObject(serverJson);
            String title=object.optString("title");
            String content=object.optString("desc");
            String url=object.optString("link");
            String image=object.optString("imgUrl");
            new ShareDialog(activity,title,content,image, url).show();
        }catch (Exception e){

        }

    }

}
