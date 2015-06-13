package com.cnpaypal.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnpaypal.home.R;
import com.cnpaypal.util.CommentUtil;

/**
 * ObjectAnimator     动画的执行类
 * ValueAnimator      动画的执行类
 * AnimatorSet        用于控制一组动画的执行：线性，一起，每个动画的先后执行等。
 * AnimatorInflater   用户加载属性动画的xml文件
 * TypeEvaluator      类型估值，主要用于设置动画操作属性的值。
 * TimeInterpolator   时间插值
 */
public class ObjectAnimatorActivity extends Activity{
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.animation_layout);

        initView();
    }

    private void initView(){
        imageView = (ImageView)findViewById(R.id.animation_imageView);

        findViewById(R.id.objectAnimator_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showObjectorAnimator(imageView);
            }
        });

        findViewById(R.id.valueAnimator_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showValueAnimation();
            }
        });


        final TextView parabolaBtn = (TextView)findViewById(R.id.parabola_btn);
        parabolaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                int x = location[0] + CommentUtil.dip2px(ObjectAnimatorActivity.this, 15);
                int y = location[1] + CommentUtil.dip2px(ObjectAnimatorActivity.this, 15*3);
                Log.d("AAAA", "initView 位置坐标 x=" + x + ", y=" + y + ",f x" + parabolaBtn.getHeight());
                showParabola(imageView, x, y);
            }
        });

        //几个动画一起执行
        findViewById(R.id.AnimationSet_together_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        //控制动画的执行顺序
        findViewById(R.id.AnimationSet_control_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }


    private void showObjectorAnimator(final View view){
        ObjectAnimator objectAnimator = new ObjectAnimator()
                .ofFloat(view, "lu", 1.0F, 0.0F,1.0F)
                .setDuration(2000);
        objectAnimator.start();
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                view.setAlpha(cVal);
                view.setScaleX(cVal);
                view.setScaleY(cVal);
            }
        });

        /**
         * animator还有cancel()和end()方法：cancel动画立即停止，停在当前的位置；end动画直接到最终状态
         */
        objectAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d("AAAA","动画事件的监听 onAnimationEnd");
                view.setAlpha(1);
                view.setScaleX(1);
                view.setScaleY(1);
            }

        });
    }

    private void showValueAnimation(){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 100);
        valueAnimator.setTarget(imageView);
        valueAnimator.setDuration(1500);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                imageView.setTranslationY((Float) animation.getAnimatedValue());
            }
        });
    }


    /**
     * 展示抛物线的动画效果
     * @param view 要执行动画的view
     */
    private void showParabola(final View view,final int x,final int y){
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(2000);

        //二维矢量，包含两个公共整型属性，表示它与某个特定位置的水平和垂直距离
        valueAnimator.setObjectValues(new PointF(x,y));
        //插值器，选取的是线性插值
        valueAnimator.setInterpolator(new LinearInterpolator());

        /**
         * 设置时间比率
         * 就是用来计算属性值的，即可计算任意类型的值。
         * fraction 表示时间的比率。
         */
        valueAnimator.setEvaluator(new TypeEvaluator() {
            @Override
            public Object evaluate(float fraction, Object startValue, Object endValue) {
                // x方向200px/s ，则y方向0.5 * 10 * t
                PointF point = new PointF(x,y);
                point.x = x+200 * fraction * 3;
                point.y = y+0.5f * 200 * (fraction * 3) * (fraction * 3);
                return point;
            }
        });
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                Log.d("AAAA", "point.x="+point.x+",point.y="+point.y);
                view.setX(point.x);
                view.setY(point.y);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setX(x);
                view.setY(y);
            }
        });


    }


    private void showAnimatorSet(View view){

    }

}
