package libs.OneDrawable;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Administrator on 2018/11/13 0013.
 *
 *
 * 这是一个可以给view 设置按下去效果的工具类，把原来的drawable图片放进来之后
 * 进行处理一下就会变成带有按下去效果的view。这是一个通用发方法，可以适用于
 * 图片和颜色背景
 */

public class OneDrawableUtil {


    /**
     * 把图片设置设置一个按下去的背景效果
     * @param context
     * @param view
     * @param resId
     */
    public static void setBackgroundDrawable(Context context,View view, int resId){
        Drawable drawable = OneDrawable.createBgDrawable(context,resId);
        view.setBackgroundDrawable(drawable);
    }


    /**
     * 把颜色设置一个 黑色半透明 的按下去效果
     * @param context
     * @param view
     * @param resId
     */
    public static void setBackgroundColor(Context context,View view, int resId){
        Drawable color = OneDrawable.createBgColor(context,resId);
        view.setBackgroundDrawable(color);
    }


}


/**



    有时候按下去的效果不一定是要在上面盖一层半透明的黑色，而是将本身的图片加一些透明度，那么就可以用
 下面的方法。（由于下面的WithAlphaMode这种设置本身透明度的方式用的不多，所以暂时不考虑）
 默认情况下上面的两个方式使用的是WithDarkMode，也就是在本身的view上面盖一层黑色半透明的效果

 Drawable icon2 = OneDrawable.createBgDrawableWithDarkMode(this,R.drawable.ic_action_add,0.4f);
 tvIcon2.setBackgroundDrawable(icon2);
 Drawable icon4 = OneDrawable.createBgColorWithDarkMode(this,R.color.colorAccent,0.3f);
 tvIcon4.setBackgroundDrawable(icon4);



 Drawable icon4 = OneDrawable.createBgColorWithAlphaMode(this,R.drawable.ic_action_name,0.3f);
 tvIcon4.setBackgroundDrawable(icon4);
 Drawable icon4 = OneDrawable.createBgColorWithAlphaMode(this,R.color.colorAccent,0.3f);
 tvIcon4.setBackgroundDrawable(icon4);










 */