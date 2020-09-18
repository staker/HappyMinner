package com.ailink.view;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.ailink.util.Logg;
import com.ailink.util.NumberUtil;

/**
 * Created by Administrator on 2018/11/2 0002.
 */
/**
 * API不能低于19。其实低于19也没关系，
 * setDuration方法设置动画执行时长
 * setAnimEndListener方法设置监听动画正常执行结束
 * setAnimInterpolator方法设置动画速率
 * 在该View的Activity或Fragment的onDestroy方法中调用clearAnimator方法，onPause调用onPause，onResume调用onResume。
 */


/**
 * 用法
 *
 * txt.setDuration(1000);//设置动画的总时间
 *  txt.setTextNumberWithAnim("前缀文字",起始值,最终值);
 *
 *  到这里就结束了，两行代码而已

 ---------------------

 */
public class RaiseNumberAnimTextView extends TextView {

    private  String preContent;


    private long mDuration = 1500; // 动画持续时间 ms，默认1s

    private ValueAnimator animator;

    private TimeInterpolator mTimeInterpolator = new LinearInterpolator(); // 动画速率

    private AnimEndListener mEndListener; // 动画正常结束监听事件

    public RaiseNumberAnimTextView(Context context) {
        super(context);
    }

    public RaiseNumberAnimTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RaiseNumberAnimTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    /**
     * 设置动画持续时间，默认为1s。需要在setNumberWithAnim之前设置才有效
     * @param duration
     */
    public void setDuration(long duration) {
        if (duration > 0) {
            mDuration = duration;
        }
    }
    /**
     * 设置动画速率，默认为LinearInterpolator。需要在setNumberWithAnim之前设置才有效
     * @param timeInterpolator
     */
    public void setAnimInterpolator(TimeInterpolator timeInterpolator) {
        mTimeInterpolator = timeInterpolator;
    }



    /**
     * 因为本身不带有ValueAnimator.ofDouble(startValue, endValue);方法
     * 所以需要引入TypeEvaluator，这样就可以设置多种多样的变化值，当然里面还可以自定义类
     *
     *
     * 设置要显示的double数字，带动画显示。
     * @param preContent  前置的文字，可以为空  “”
     * @param startValue  变化的起始值
     * @param endValue     变化的最终值
     */
    public void setTextNumberWithAnim(String preContent,double startValue,double endValue){
        clearAnimator();
        this.preContent=preContent;
        TypeEvaluator<Double> typeEvaluator=new TypeEvaluator<Double>() {
            @Override
            public Double evaluate(float fraction, Double startValue, Double endValue) {
//                fraction这个值是变化的速率，从0到1匀速变化
                double tempValue=(endValue-startValue)*fraction+startValue;
                return tempValue;
            }
        };
        animator=ValueAnimator.ofObject(typeEvaluator,startValue,endValue );
        startAnimatorForDouble();
    }



    /**
     * 设置要显示的int数字，带动画显示。
     * @param preContent  前置的文字，可以为空  “”
     * @param startValue  变化的起始值
     * @param endValue     变化的最终值
     */
    public void setTextNumberWithAnim(String preContent,int startValue,int endValue) {
        clearAnimator();
        this.preContent=preContent;
        // 设置动画，int值的起始值
        animator = ValueAnimator.ofInt(startValue, endValue);
        startAnimatorForInt();
    }

    // 清除动画
    public void clearAnimator() {
        if (null != animator) {
            if (animator.isRunning()) {
                animator.removeAllListeners();
                animator.removeAllUpdateListeners();
                animator.cancel();
            }
            animator = null;
        }
    }

    // 暂停动画
    public void onPause() {
        if (null != animator && animator.isRunning()) {
            if(Build.VERSION.SDK_INT >18){
                animator.pause();
            }
             // API 不低于19
        }
    }

    // 继续执行动画
    public void onResume() {
        if (null != animator && animator.isRunning()) {
            if(Build.VERSION.SDK_INT >18){
                animator.resume();
            }
        }
    }

    // 设置时常与过程处理，启动动画
    private void startAnimatorForDouble() {
        if (null != animator) {
            animator.setDuration(mDuration);
            animator.setInterpolator(mTimeInterpolator);
            // 动画过程中获取当前值，显示
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    double curValue = (double)valueAnimator.getAnimatedValue();
                    String tempText=NumberUtil.keep4DecimalString(curValue);
                    setText(preContent+tempText);
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (null != mEndListener) { // 动画不是中途取消，而是正常结束
                        mEndListener.onAnimFinish();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
            animator.start();
        }
    }
    private void startAnimatorForInt() {
        if (null != animator) {
            animator.setDuration(mDuration);
            animator.setInterpolator(mTimeInterpolator);
            // 动画过程中获取当前值，显示
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    setText(preContent+valueAnimator.getAnimatedValue().toString());
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (null != mEndListener) { // 动画不是中途取消，而是正常结束
                        mEndListener.onAnimFinish();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
            animator.start();
        }
    }























    //用于外部监听动画结束，这个是一个备用的功能
    public void setAnimEndListener(AnimEndListener listener) {
        mEndListener = listener;
    }
    // 动画显示数字的结束监听，当动画结束显示正确的数字时，可能需要做些处理
    public interface AnimEndListener {
        void onAnimFinish();
    }



}
