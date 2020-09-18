package libs.util.umeng;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;


import com.ailink.db.MyApplication;
import com.umeng.analytics.MobclickAgent;


/**
 * Created by Staker on 2017/7/12.
 */

public class UmengUtil {

    public static void onResume(Context context){
        MobclickAgent.onResume(context);
    }
    public static void onPause(Context context){
        MobclickAgent.onPause(context);
    }


}
