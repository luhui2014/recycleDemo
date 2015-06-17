package com.cnpaypal.ObservableScrollView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.cnpaypal.home.R;

/**
 * scrollview 里面的浮动的标签的界面
 */
public class ScrollStickyActivity extends Activity implements ObservableScrollView.ScrollCallBack{
    private TextView txtContent;
    private View mPlaceholderView;
    private ObservableScrollView observableScrollView;

    /**
     * 上滑动状态
     */
    private static final int STATE_ONSCREEN = 0;
    /**
     * 上滑动至完全遮盖住mPlaceholderView
     */
    private static final int STATE_OFFSCREEN = 1;
    /**
     * 完全遮盖住时，下滑状态
     */
    private static final int STATE_RETURING = 2;
    private int mState = STATE_ONSCREEN;
    /**
     * 高度
     */
    private int mViewHeight;
    private int minRaw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_sticky);

        initView();
    }

    private void initView(){
        //定位 sticky 位置的一个标记的view,会被最终的sticky覆盖
        mPlaceholderView = findViewById(R.id.placeholder);

        //真正的浮动的view ,他会不断执行位移动画
        txtContent = (TextView)findViewById(R.id.sticky);
        txtContent.setText("ScrollView sticky");

        //找到对应的布局，并设置监听的事件，进行回调的处理
        observableScrollView = (ObservableScrollView)findViewById(R.id.scroll_sticky);
        observableScrollView.setCallBack(this);

        //当布局绘制完全的时候我们才可以得到view.getTop(),来初始化 浮动层的具体的位置
        observableScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                mViewHeight = txtContent.getBottom()-txtContent.getTop();
                onScrollChanged(observableScrollView.getScrollY());
            }
        });

    }

    @Override
    public void onScrollChanged(int verticalDistances) {
        int translation = Math.max(verticalDistances , mPlaceholderView.getTop());
        //函数说明  translation in y of the view.
        txtContent.setTranslationY(translation);

//        //这是一个相对的距离，其实是scrollview的顶部距离 浮动按钮的距离，
//        int relativeDistances = mPlaceholderView.getTop()-verticalDistances;
//
//        //浮动层垂直滚动的距离
//        int translationY = 0;
//
//        switch (mState){
//            case STATE_ONSCREEN:
//                if(relativeDistances<-mViewHeight){
//                    mState = STATE_OFFSCREEN;
//                    minRaw = relativeDistances;
//                }
//                translationY = relativeDistances;
//                break;
//
//            case STATE_OFFSCREEN:
//                if (relativeDistances<=minRaw){
//                    minRaw = relativeDistances;
//                }else{
//                    mState = STATE_RETURING;
//                }
//                translationY = relativeDistances;
//                break;
//
//            case STATE_RETURING:
//                translationY = (relativeDistances - minRaw) - mViewHeight;
//                if (translationY > 0) {
//                    translationY = 0;
//                    minRaw = relativeDistances - mViewHeight;
//                }
//
//                if (relativeDistances > 0) {
//                    mState = STATE_ONSCREEN;
//                    translationY = relativeDistances;
//                }
//
//                if (translationY < -mViewHeight) {
//                    mState = STATE_OFFSCREEN;
//                    minRaw = relativeDistances;
//                }
//                break;
//        }
//        txtContent.setTranslationY(translationY+verticalDistances);

    }
}
