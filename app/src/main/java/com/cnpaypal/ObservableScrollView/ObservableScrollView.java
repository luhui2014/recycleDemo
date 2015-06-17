package com.cnpaypal.ObservableScrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 自定义滚动类，浮动标签
 */
public class ObservableScrollView extends ScrollView{
    private ScrollCallBack mCallBack;

    //注册回调事件
    public interface ScrollCallBack{
        void onScrollChanged(int verticalDistances);
    }

    //构造函数
    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCallBack(ScrollCallBack callBack){
        this.mCallBack = callBack;
    }


    /**
     * 以屏幕左上角为坐标原点 （0,0）
     * l	Current horizontal scroll origin. 当前水平滚动到的点
     * t	Current vertical scroll origin.      当前垂直滚动到的点
     * oldl	Previous horizontal scroll origin. 滚动前水平的点
     * oldt	Previous vertical scroll origin.     滚动前垂直的点
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mCallBack!=null){
            mCallBack.onScrollChanged(t);
        }
    }


    /**
     * 由垂直方向滚动条代表的所有垂直范围，缺省的范围是当前视图的画图高度。
     */
    public int computeVerticalScrollRange(){
        return super.computeVerticalScrollRange();
    }
}
