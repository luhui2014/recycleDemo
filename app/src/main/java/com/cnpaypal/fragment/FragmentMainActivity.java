package com.cnpaypal.fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.cnpaypal.home.R;

/**
 * 1.直接每次点击都是刷新当前页面，重新获取布局 http://blog.csdn.net/lmj623565791/article/details/37970961
 * 2.动态添加fragment的布局（以下是实现方法）
 * 3.还有一个就是静态的添加 （在xml布局中写好布局直接调用）
 */
public class FragmentMainActivity extends Activity implements View.OnClickListener {
    private FragmentHome fragmentHome;
    private FragmentNearBy fragmentNearBy;
    private FragmentAttention fragmentAttention;
    private FragmentUser fragmentUser;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fragment_main_layout);

        initView();
        setDefaultFragment(savedInstanceState);
    }

    /**
     * 初始化布局
     */
    private void initView() {
        LinearLayout homeBottomLayout = (LinearLayout) findViewById(R.id.fragment_main_bottomBar);
        LinearLayout homeBtn = (LinearLayout) homeBottomLayout.findViewById(R.id.fragment_home_btn);
        LinearLayout nearByBtn = (LinearLayout) homeBottomLayout.findViewById(R.id.fragment_nearby_btn);
        LinearLayout attentionBtn = (LinearLayout) homeBottomLayout.findViewById(R.id.fragment_attention_btn);
        LinearLayout userBtn = (LinearLayout) homeBottomLayout.findViewById(R.id.fragment_user_btn);

        homeBtn.setOnClickListener(this);
        nearByBtn.setOnClickListener(this);
        attentionBtn.setOnClickListener(this);
        userBtn.setOnClickListener(this);


    }

    private void setDefaultFragment(Bundle savedInstanceState) {
        //获取Fragment的管理
        fragmentManager = getFragmentManager();
        if(savedInstanceState == null){
            setTabSelection(R.id.fragment_home_btn);
        }
    }

    @Override
    public void onClick(View v) {
        setTabSelection(v.getId());
    }


    private void setTabSelection(int id) {
        if(fragmentManager == null){
            fragmentManager = getFragmentManager();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragment(fragmentTransaction);
        switch (id) {
            case R.id.fragment_home_btn:
                if (fragmentHome == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    fragmentHome = new FragmentHome();
                    fragmentTransaction.add(R.id.id_content, fragmentHome);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    fragmentTransaction.show(fragmentHome);
                }
                break;

            case R.id.fragment_nearby_btn:
                if (fragmentNearBy == null) {
                    //使用fragment的主要目的就是复用，这里是Fragment 传递参数的例子
                    //这样就完成了Fragment和Activity间的解耦。当然了这里需要注意：
                    //setArguments方法必须在fragment创建以后，添加给Activity前完成。千万不要，首先调用了add，然后设置arguments。
                    fragmentNearBy = FragmentNearBy.newInstance("传递过来的参数:fragmentNearBy");
                    fragmentTransaction.add(R.id.id_content, fragmentNearBy);
                } else {
                    fragmentTransaction.show(fragmentNearBy);
                }
                break;

            case R.id.fragment_attention_btn:
                if (fragmentAttention == null) {
                    fragmentAttention = new FragmentAttention();
                    fragmentTransaction.add(R.id.id_content, fragmentAttention);
                } else {
                    fragmentTransaction.show(fragmentAttention);
                }
                break;

            case R.id.fragment_user_btn:
                if (fragmentUser == null) {
                    fragmentUser = new FragmentUser();
                    fragmentTransaction.add(R.id.id_content, fragmentUser);
                } else {
                    fragmentTransaction.show(fragmentUser);
                }
                break;
            default:
                break;
        }

        fragmentTransaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (fragmentHome != null) {
            transaction.hide(fragmentHome);
        }

        if (fragmentNearBy != null) {
            transaction.hide(fragmentNearBy);
        }

        if (fragmentAttention != null) {
            transaction.hide(fragmentAttention);
        }

        if (fragmentUser != null) {
            transaction.hide(fragmentUser);
        }
    }


}
