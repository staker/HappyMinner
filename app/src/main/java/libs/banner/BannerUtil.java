package libs.banner;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ailink.util.StakerUtil;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import libs.glide.GlideUtil;
import libs.okhttp.MyOkHttpUtil;


/**
 * Created by Staker on 2017/7/3.
 * <p>
 * 由于Banner已经封装得很好，所以这个Banner的封装类只是稍微的封装一下使用
 */

public class BannerUtil {




    public static void initBanner(Banner banner, ArrayList<BannerPojo> list,OnBannerClickListener listener){
        //初始化Banner参数
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        setBannerImage(banner,list);
        banner.setDelayTime(60000);//设置Banner自动滚动的时间间隔
        if(list==null){
            return;
        }
        banner.setOnBannerClickListener(listener);
        banner.start();
//        banner.setOnBannerClickListener(new OnBannerClickListener() {
//            @Override
//            public void OnBannerClick(int position) {
//                Logg.e("position="+position);
//                BannerPojo bannerPojo= listBanner.get(position-1);
//                JumpActivityUtil.showWebActivity(getActivity(),bannerPojo.titleName,bannerPojo.pageUrl,false);
//            }
//        });
    }


//
//    public static void initGeneralBanner(Banner banner, ArrayList<GeneralInfoPojo> list, OnBannerClickListener listener){
//        //初始化Banner参数
//        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
//        banner.setIndicatorGravity(BannerConfig.RIGHT);
//        setGeneralBannerImage(banner,list);
//        banner.setDelayTime(3000);//设置Banner自动滚动的时间间隔
//        if(list==null){
//            return;
//        }
//        banner.setOnBannerClickListener(listener);
//        banner.start();
//    }
//    private static void setGeneralBannerImage(Banner banner, ArrayList<GeneralInfoPojo> list) {
//        if (banner == null || list == null || list.size() == 0) {
//            return;
//        }
//        banner.setImages(list);
//        banner.setImageLoader(new ImageLoader() {
//            @Override
//            public void displayImage(Context context, Object object, ImageView imageView) {
//                String url = ((GeneralInfoPojo) object).imgUrl;
//                GlideUtil.getInstance().setImage(imageView, url);
//            }
//        });
////        banner.
//    }




    /**
     * 由于是定制化的工具类，所以对应Banner使用的实体类也必须是Banner
     *
     * @param banner
     * @param list
     */
    private static void setBannerImage(Banner banner, ArrayList<BannerPojo> list) {
        if (banner == null || list == null || list.size() == 0) {
            return;
        }

        banner.setImages(list);
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object object, ImageView imageView) {
                String url = ((BannerPojo) object).imgUrl;
                GlideUtil.getInstance().setImage(imageView, url);
            }
        });
//        banner.
    }

    /**
     * 给Banner设置标题
     *
     * @param banner
     * @param list
     */
    public static void setBannerTitle(Banner banner, ArrayList<BannerPojo> list) {
        if (banner == null || list == null || list.size() == 0) {
            return;
        }
        ArrayList<String> listTitle = new ArrayList<String>();
        for (BannerPojo bannerPojo : list) {
            listTitle.add(bannerPojo.titleName);
        }
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.setBannerTitles(listTitle);
    }


    /**
     * 更新Banner
     *
     * @param banner
     * @param listBanner
     * @param isShowTitle 是否显示标题
     */
    public static void updateBanner(Banner banner, ArrayList<BannerPojo> listBanner, boolean isShowTitle) {
        if (banner == null || listBanner == null || listBanner.size() == 0) {
            return;
        }
        if (isShowTitle) {
            ArrayList<String> listTitle = new ArrayList<String>();
            for (BannerPojo bannerPojo : listBanner) {
                listTitle.add(bannerPojo.titleName);
            }
            banner.update(listBanner, listTitle);
        } else {
            banner.update(listBanner);
        }
    }

//
//    /**
//     * 根据后台url返回的banner图片，自动调整Banner的高度
//     * @param banner
//     * @param bannerUrl
//     * @param context
//     */
//    public static void autoLayoutHeight(final Banner banner, String bannerUrl, final Context context) {
//
//        MyOkHttpUtil.sendGet(bannerUrl, new ByteCallback() {
//            @Override
//            public void onError(Request request, Exception e) {
//
//            }
//            @Override
//            public void onResponse(byte[] response) {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true; //设置只读取图片的属性，不将图片内容读到内存
//                BitmapFactory.decodeByteArray(response, 0, response.length, options);;
//                int height = options.outHeight;
//                int width = options.outWidth;
//                int screenW = StakerUtil.getWindowWidth(context);
////                Logg.e("height="+height);
////                Logg.e("width="+width);
////                Logg.e("screenW="+screenW);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, height * screenW / width);
//                banner.setLayoutParams(params);
//            }
//        });
//
//    }

}


/**
 * 常量
 * 常量名称描述所属方法setBannerStyle
 * BannerConfig.NOT_INDICATOR不显示指示器和标题
 * BannerConfig.CIRCLE_INDICATOR显示圆形指示器
 * BannerConfig.NUM_INDICATOR显示数字指示器
 * BannerConfig.NUM_INDICATOR_TITLE显示数字指示器和标题
 * BannerConfig.CIRCLE_INDICATOR_TITLE显示圆形指示器和标题（垂直显示）
 * BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE显示圆形指示器和标题（水平显示）
 * <p>
 * <p>
 * 方法setIndicatorGravity  设置指示器的位置
 * BannerConfig.LEFT  指示器居左
 * BannerConfig.CENTER指示器居中
 * BannerConfig.RIGHT指示器居右
 */







