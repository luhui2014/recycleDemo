package com.cnpaypal.StickyNavLayout;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.cnpaypal.home.R;


/**
 * 自定义下拉出现，上滑隐藏
 */
public class StickyNavLayout extends LinearLayout {
    //OverScroller是个辅助类，用于自定移动时帮我们处理掉数学的计算部分
    private OverScroller mScroller;

    //VelocityTracker顾名思义即速度跟踪
    private VelocityTracker mVelocityTracker;

    //mTouchSlop帮我区别用户是点击还是拖拽。
    private int mTouchSlop;

    private int mMaximumVelocity, mMinimumVelocity;
    private int mTopViewHeight;
    private boolean isTopHidden = false;

    private View mTop;
    private View mNav;
    private ViewPager mViewPager;

    private float mLastY;
    private boolean mDragging;
    private ViewGroup mInnerScrollView;

    public StickyNavLayout(Context context) {
        super(context);
    }

    public StickyNavLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        setOrientation(LinearLayout.VERTICAL);
        mScroller = new OverScroller(context);
        //绑定速度跟踪事件
        mVelocityTracker = VelocityTracker.obtain();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        //获取默认滚动下滑的距离
        mTouchSlop = viewConfiguration.getScaledTouchSlop();
        mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    }

    /**
     * 当View中所有的子控件均被映射成xml后触发
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        Log.d("AAAA", "自定义控件执行了 onFinishInflate 函数");
        mTop = findViewById(R.id.id_stickynavlayout_topview);
        mNav = findViewById(R.id.id_stickynavlayout_indicator);
        View view = findViewById(R.id.id_stickynavlayout_viewpager);

        if (!(view instanceof ViewPager)) {
            throw new RuntimeException("id_stickyNavLayout_viewpager show used by ViewPager !");
        }
        mViewPager = (ViewPager) view;
    }

    /**
     * 确定所有子元素的大小
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.d("AAAA", "自定义控件执行了 onMeasure 函数");

        //手动设置viewpager的高度，viewpager不会自己去设置孩子的高度，而是去那它父类的控件的高度
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        params.height = getMeasuredHeight() - mNav.getMeasuredHeight();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Log.d("AAAA", "自定义控件执行了 onSizeChanged 函数");
        mTopViewHeight = mTop.getMeasuredHeight();
    }

    /**
     * 触摸事件，控制LinearLayout的滚动的事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int action = event.getAction();
        final float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished())
                    mScroller.abortAnimation();
                mVelocityTracker.clear();
                mVelocityTracker.addMovement(event);
                mLastY = y;
                return true;

            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;
                if (!mDragging && Math.abs(dy) > mTouchSlop) {
                    mDragging = true;
                }

                if (mDragging) {
                    scrollBy(0, (int) -dy);
                    mLastY = y;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                mDragging = false;
                if (!mScroller.isFinished())
                    mScroller.abortAnimation();
                break;

            case MotionEvent.ACTION_UP:
                mDragging = false;
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int velocityY = (int) mVelocityTracker.getYVelocity();
                if (Math.abs(velocityY) > mMinimumVelocity) {
                    fling(-velocityY);
                }
                mVelocityTracker.clear();
                break;
        }
        return super.onTouchEvent(event);
    }


    public void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mTopViewHeight);
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight) {
            y = mTopViewHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }

        isTopHidden = getScrollY() == mTopViewHeight;

    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        float y = ev.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;
                getCurrentScrollView();
                if (Math.abs(dy) > mTouchSlop) {
                    mDragging = true;
                    if (!isTopHidden
                            || (mInnerScrollView.getScrollY() == 0 && isTopHidden && dy > 0)) {
                        return true;
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void getCurrentScrollView() {
        int currentItem = mViewPager.getCurrentItem();
        PagerAdapter a = mViewPager.getAdapter();
        if (a instanceof FragmentPagerAdapter) {
            FragmentPagerAdapter fragmentPagerAdapter = (FragmentPagerAdapter) a;
            Fragment item = (Fragment) fragmentPagerAdapter.instantiateItem(mViewPager, currentItem);
            if(item.getView()!=null) {
                mInnerScrollView = (ViewGroup) (item.getView().findViewById(R.id.id_stickynavlayout_innerscrollview));
            }
        } else if (a instanceof FragmentStatePagerAdapter) {
            FragmentStatePagerAdapter fsAdapter = (FragmentStatePagerAdapter) a;
            Fragment item = (Fragment) fsAdapter.instantiateItem(mViewPager, currentItem);
            if(item.getView()!=null) {
                mInnerScrollView = (ViewGroup) (item.getView().findViewById(R.id.id_stickynavlayout_innerscrollview));
            }
        }

    }
}
