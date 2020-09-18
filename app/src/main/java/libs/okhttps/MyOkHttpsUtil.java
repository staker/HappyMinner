package libs.okhttps;

import com.ailink.util.Logg;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;

public class MyOkHttpsUtil {




    /**
     * 通过Post方式请求url
     * @param url  请求地址
     * @param headerMap 传入的键值对
     * @param callback  回掉接口   有三种，分别是BitmapCallBack，FileCallBack，StringCallBack，大部分情况只用到了StringCallBack
     */
    public static void sendPostAddHeader(String url, Map<String, String> headerMap, Map<String, String> bodyMap, Callback callback) {
        if(headerMap==null||headerMap.size()==0){
            headerMap=new HashMap<>();
            headerMap.put("test","test");
        }

        if(bodyMap==null||bodyMap.size()==0){
            bodyMap=new HashMap<>();
            bodyMap.put("test","test");
        }
        OkHttpUtils
                .post()
                .url(url)
                .params(bodyMap)
                .headers(headerMap)
                .id(100)
                .build()
                .execute(callback);
    }




    public static void sendGet(String url, Callback callback) {
        OkHttpUtils.
        get()
                .url(url)
                .id(101)
                .build()
                .execute(callback);
    }
}
