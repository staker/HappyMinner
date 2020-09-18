package libs.smmob;

import android.os.Handler;
import android.os.Message;

import com.ailink.i.HttpObjectListener;
import com.ailink.util.Logg;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class SMMobUtil {
    HttpObjectListener listener;
    int result;
    Throwable data;




    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if(listener!=null){
                        try{
                            JSONObject jsonObject=new JSONObject(data.getMessage());
                            listener.onDataError(jsonObject.optString("description"));
                        }catch (Exception e){
                            listener.onDataError("发送短信失败");
                        }
                    }
                    break;
                case 1:
                    if(listener!=null){
                        listener.onSucess(null);
                    }
                    break;
            }
        }
    };
    public SMMobUtil(HttpObjectListener listener){
        this.listener=listener;
    }

//     SMMobUtil.sendCode("61","416552178");
    // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
    public  void sendCode(String country, String phone) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                    Logg.e("已经发送");
                    handler.sendEmptyMessage(1);
                } else{
                    // TODO 处理错误的结果

                    SMMobUtil.this.result=result;
                    SMMobUtil.this.data=(Throwable)data;
                    Logg.e("发送失败"+SMMobUtil.this.data.getMessage());
                    handler.sendEmptyMessage(0);
                }

            }
        });
        // 触发操作
        SMSSDK.getVerificationCode(country, phone);
    }

    // 提交验证码，其中的code表示验证码，如“1357”
    public  void submitCode(String country, String phone, String code) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证成功的结果
                    Logg.e("提交的验证码正确");
                } else{
                    // TODO 处理错误的结果
                    Logg.e("提交的验证码错误");
                }

            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
    }

    protected static void onDestroySMSSDK() {
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    };



}
