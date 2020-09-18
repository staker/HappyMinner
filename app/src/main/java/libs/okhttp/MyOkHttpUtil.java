package libs.okhttp;


import com.ailink.i.HttpObjectListener;
import com.ailink.util.AsynTaskUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.OkHttpRequestBuilder;
import com.zhy.http.okhttp.builder.PostFileBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 把张弘扬的okhttputils库，再次简单封装一下，变成自己习惯使用的方式
 * 这是通用的方式，可能传入的参数比较多，但是使用在具体项目的时候，还是需要再封装一层。请看MyOkHttpUtil类，专门针对单个项目而实现
 */

public class MyOkHttpUtil {

    //两个网络请求方式
    public interface HttpMode{
        public static final int Get=1;//
        public static final int Post=2;//
    }


    /**
     * 通过Post方式请求url
     * @param url  请求地址
     * @param map 传入的键值对
     * @param callback  回掉接口   有三种，分别是BitmapCallBack，FileCallBack，StringCallBack，大部分情况只用到了StringCallBack
     */
    public static void sendPost(String url,Map<String, String> map, Callback callback) {
        if(map==null||map.size()==0){
            map=new HashMap<>();
            map.put("test","test");
        }
        excuteRequest(url,HttpMode.Post,map, callback);
    }

    /**
     * 通过Post方式请求url
     * @param url  请求地址
     * @param headerMap 传入的键值对
     * @param callback  回掉接口   有三种，分别是BitmapCallBack，FileCallBack，StringCallBack，大部分情况只用到了StringCallBack
     */
    public static void sendPostAddHeader(String url,Map<String, String> headerMap, Map<String, String> bodyMap,Callback callback) {
        if(headerMap==null||headerMap.size()==0){
            headerMap=new HashMap<>();
            headerMap.put("test","test");
        }

        if(bodyMap==null||bodyMap.size()==0){
            bodyMap=new HashMap<>();
            bodyMap.put("test","test");
        }


        PostFormBuilder requestBuilder;
        requestBuilder= OkHttpUtils.post();
        requestBuilder.params(bodyMap);
        requestBuilder.headers(headerMap);
        requestBuilder.url(url).build().execute(callback);

    }






















    /**
     * 通过Get方式请求url
     * @param url  请求地址
     * @param map 传入的键值对
     * @param callback  回掉接口  有三种，分别是BitmapCallBack，FileCallBack，StringCallBack，大部分情况只用到了StringCallBack
     */
    public static void sendGet(String url,Map<String, String> map, Callback callback) {
        excuteRequest(url,HttpMode.Get,map, callback);
    }
    public static void sendGet(String url,Callback callback) {
        excuteRequest(url,HttpMode.Get,null, callback);
    }




    /**
     *
     * @param url
     * @param requestbody  json格式的字符串  可以传null
     *
     * @param map 键值对参数，目前这种传参还没有进行验证  可以传null
     * @return  String
     * @throws Exception
     */
    public static String sendPut(String url,String requestbody,HashMap<String,String> map){
        try{
            StringBuilder sbStr = new StringBuilder();
            URL postURL = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) postURL.openConnection();
            httpURLConnection.setDoOutput(true);// http正文内，因此需要设为true, 默认情况下是false;
            httpURLConnection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
            httpURLConnection.setRequestMethod("PUT");// 可以根据需要 提交 GET、POST、DELETE、PUT等http提供的功能
            httpURLConnection.setUseCaches(false);//设置缓存，注意设置请求方法为post不能用缓存
            httpURLConnection.setInstanceFollowRedirects(true);
//       httpURLConnection.setRequestProperty("Host", "*******");  //设置请 求的服务器网址，域名，例如***.**.***.***
            //application/json x-www-form-urlencoded
            //httpURLConnection.setRequestProperty("Content-Type",  "application/x-www-form-urlencoded");//表单上传的模式
            httpURLConnection.setRequestProperty("Content-Type",  "application/json;charset=utf-8");//json格式上传的模式
            httpURLConnection.setReadTimeout(10000);//设置读取超时时间
            httpURLConnection.setConnectTimeout(10000);//设置连接超时时间
            httpURLConnection.connect();
            if(map != null) {
                for(String pKey : map.keySet()) {
                    httpURLConnection.setRequestProperty(pKey, map.get(pKey));
                }
            }
            if(requestbody != null) {
                OutputStream out = httpURLConnection.getOutputStream();//向对象输出流写出数据，这些数据将存到内存缓冲区中
                out.write(requestbody.toString().getBytes());            //out.write(new String("测试数据").getBytes());
                // 刷新对象输出流，将任何字节都写入潜在的流中
                out.flush();
                // 关闭流对象,此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中
                out.close();

                //下面这种写法也有效果

                //            JSONObject obj = new JSONObject();
//            obj.put("nickName","京NB1314");
//            PrintWriter out = new PrintWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(),"utf-8"));
//            out.println(obj);
//            out.close();
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection
                    .getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                sbStr.append(inputLine);
            }
            in.close();
            httpURLConnection.disconnect();
            return new String(sbStr.toString().getBytes(),"utf-8");
        }catch (Exception e){
        }
        return null;
    }
























    //执行定义好的请求
    private static void excuteRequest(String url,int httpMode,Map<String, String> map, Callback callback ){
//        OkHttpRequestBuilder requestBuilder;
        if(httpMode==HttpMode.Post){
            PostFormBuilder postBuilder= OkHttpUtils.post();
            if(map!=null){
                postBuilder.params(map);
            }
            postBuilder.url(url).build().execute(callback);
        }else{
            GetBuilder getBuilder= OkHttpUtils.get();;
            if(map!=null){
                getBuilder.params(map);
            }
            getBuilder.url(url).build().execute(callback);
        }
    }

    /**
     * 调用下面的实现方式，直接用get形式，这个比较通用（需要自己启动线程）
     * @param url
     * @return  String
//     */
//    public static String getRequestContent(String url){
//        return getRequestContent(url,HttpMode.Get,null);
//    }














    /**
     * 这个就是一个简单的通过url获取结果的方法,用最原始的方法，为什么要加这个一个方法呢
     * 原因如下：
     *   由于自己服务器采用https的方法，添加了一个全局的证书。那么会导致调用外部https的url的时候，会把这个证书添加进去，直接导致别的url调用不成功，比如https://www.hao123.com
     解决的方法是我另外在单独写了原始的调用url的接口，不再使用okhttp
     * 这个就是一个简单的通过url获取结果的方法
     */
    public static void getUrlResult(final String url, final HttpObjectListener listener){
        new AsynTaskUtil().startTaskOnThread(new AsynTaskUtil.IAsynTask() {
            @Override
            public Object run() {
                try{
                    StringBuilder sbStr = new StringBuilder();
                    URL postURL = new URL(url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) postURL.openConnection();
                    httpURLConnection.setDoOutput(true);// http正文内，因此需要设为true, 默认情况下是false;
                    httpURLConnection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
                    httpURLConnection.setRequestMethod("GET");// 可以根据需要 提交 GET、POST、DELETE、PUT等http提供的功能
                    httpURLConnection.setUseCaches(false);//设置缓存，注意设置请求方法为post不能用缓存
                    httpURLConnection.setInstanceFollowRedirects(true);
                    httpURLConnection.setRequestProperty("Content-Type",  "application/json;charset=utf-8");//json格式上传的模式
                    httpURLConnection.setReadTimeout(10000);//设置读取超时时间
                    httpURLConnection.setConnectTimeout(10000);//设置连接超时时间
                    httpURLConnection.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection
                            .getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        sbStr.append(inputLine);
                    }
                    in.close();
                    httpURLConnection.disconnect();
                    return new String(sbStr.toString().getBytes(),"utf-8");
                }catch (Exception e){
                    return "e"+e.getMessage()+e.toString();
                }
            }
            @Override
            public void over(Object object) {
                if(listener!=null){
                    listener.onSucess(object);
                }
            }
        });
    }










//    /*
//这个里面主要是把reqbody
//和map  放进builder里面，由于我现在不需要这些，所以暂时就不管，也不放了
// */
//    public static FormEncodingBuilder  addParamToBuilder(HashMap<String,String> map){
//        FormEncodingBuilder builder=new FormEncodingBuilder();
//        if(map!=null){
//            Iterator<java.util.Map.Entry<String,String>> ite= map.entrySet().iterator();
//            while (ite.hasNext()){
//                java.util.Map.Entry<String,String> kv=ite.next();
//                builder.add(kv.getKey(), kv.getValue().toString());
//            }
//        }
//        return builder;
//    }
}
