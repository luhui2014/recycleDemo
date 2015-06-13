
package com.cnpaypal.loading;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.cnpaypal.home.R;


public class LeafLoadingActivity extends Activity {

    private static final int REFRESH_PROGRESS = 0x10;
    private static final int REFRESH_ANIMATION = 1;
    private LeafLoadingView mLeafLoadingView;
    private View mFanView;
    private int mProgress = 0;
    private TextView completeText;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_PROGRESS:
                    if (mProgress < 40) {
                        mProgress += 1;
                        // 随机800ms以内刷新一次
                        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS,
                                new Random().nextInt(800));
                        mLeafLoadingView.setProgress(mProgress);
                    } else {
                        Log.d("AAAA","叶子形状的加载动画 mHandler mProgress="+mProgress);
                        if(mProgress==100){
                            mLeafLoadingView.setProgress(mProgress);
                            stopRotateAnimation();
                            return;
                        }
                        mProgress += 1;
                        // 随机1200ms以内刷新一次
                        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS,
                                new Random().nextInt(1200));
                        mLeafLoadingView.setProgress(mProgress);

                    }
                    break;

                case REFRESH_ANIMATION:
                    starNumberAnimation();
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaf_loading_layout);
        initViews();
        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS, 3000);
    }

    private void initViews() {
        mFanView = findViewById(R.id.fan_pic);
        completeText = (TextView)findViewById(R.id.progress_complete);
        RotateAnimation rotateAnimation = AnimationUtils.initRotateAnimation(false, 800, true,
                Animation.INFINITE);
        mFanView.startAnimation(rotateAnimation);
        mLeafLoadingView = (LeafLoadingView) findViewById(R.id.leaf_loading);
    }

    private void stopRotateAnimation(){
        mFanView.clearAnimation();

        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f,1.0f,0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        //设置动画时间
        scaleAnimation.setDuration(1500);
        scaleAnimation.setFillAfter(true);
        mFanView.startAnimation(scaleAnimation);

        mHandler.sendEmptyMessageDelayed(REFRESH_ANIMATION,700);

    }

    private void starNumberAnimation(){
        completeText.setVisibility(View.VISIBLE);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.4f, 1.0f,0.4f,1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(800);
        scaleAnimation.setFillAfter(true);
        completeText.startAnimation(scaleAnimation);
    }

}
