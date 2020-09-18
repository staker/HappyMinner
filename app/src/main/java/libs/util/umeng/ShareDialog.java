package libs.util.umeng;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.ailink.activity.BaseActivity;
import com.ailink.util.Logg;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import juhuiwan.ailink.android.R;
import cn.sharesdk.wechat.utils.g;
import com.mob.tools.utils.DeviceHelper;

/**
 * 分享Dialig
 * wjz
 * 2016/8/11.
 */
public class ShareDialog extends Dialog{
    private String iconUrl="http://hm.jhw.la/happyminer/img/logo.png";

    private BaseActivity mActivity;
    public ShareDialog(BaseActivity mActivity, String title, String content, String image, String url) {
        super(mActivity, R.style.share_dialog_style);
        this.mActivity = mActivity;
        this.title = title;
        this.content = content;
        this.image = image;
        this.url = url;

//        this.title="好听到爆炸";
//        this.content="中国好声音算什么，你能听到比这个更好听的声音吗？";
//        this.image="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1526459743412&di=352dbe9064f5fbe129b913e7f904f1b6&imgtype=0&src=http%3A%2F%2Fp1.wmpic.me%2Farticle%2F2015%2F12%2F18%2F1450417947_qHITAEcW.jpg";
//        this.url = "http://hm.jhw.la/happyminer/dist/#/home?unicode=1528354187083&t=1526449487084";
    }

    public ShareDialog(BaseActivity mActivity, ShareEntity shareEntity){
        this(mActivity,shareEntity.getTitle(), shareEntity.getContent(), shareEntity.getImage(), shareEntity.getUrl());
    }

    private String title;
    private String content;
    private String image;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.dialog_share, null);
        AutoUtils.auto(view);
        setContentView(view);

        findViewById(R.id.shareWechat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(Wechat.NAME);
            }
        });

        findViewById(R.id.shareWxcircle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(WechatMoments.NAME);
            }
        });

//        findViewById(R.id.shareQQ).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                share(QQ.NAME);
//            }
//        });
        findViewById(R.id.cancleBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setCanceledOnTouchOutside(true);
    }

    private void share(String name){
        try{
//            ShareSDK.
            Platform.ShareParams sp = new Platform.ShareParams();
            if(TextUtils.isEmpty(title)){
                sp.setTitle(mActivity.getString(R.string.app_name));
            }else{
                sp.setTitle(title);
                sp.setTitleUrl(url);
            }
            if(content != null){
                sp.setText(content);
            }

            if(TextUtils.isEmpty(image)){
                sp.setImageUrl(iconUrl);
            }else{
                sp.setImageUrl(image);
            }

            if(url != null){
                sp.setUrl(url);
            }
            sp.setShareType(Platform.SHARE_WEBPAGE);
            sp.setVenueName(mActivity.getString(R.string.app_name));
            Platform mPlatform = ShareSDK.getPlatform(name);
            mPlatform.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                }

                @Override
                public void onCancel(Platform platform, int i) {

                }
            });
            mPlatform.share(sp);
            dismiss();
        }catch (Exception e){
        }
    }
}