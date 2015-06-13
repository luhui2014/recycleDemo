package com.cnpaypal.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnpaypal.home.R;

/**
 * Created by Administrator on 2015/6/8.
 */
public class FragmentAttention extends Fragment implements View.OnClickListener {
    private View layoutView;
    private FragmentAttentionDetail fragmentAttentionDetail;
    private FragmentRank fragmentRank;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("AAAA", "Fragment方法测试 FragmentAttention onCreateView");

        layoutView = inflater.inflate(R.layout.fragment_attention_layout, container, false);

        initView(savedInstanceState);

        return layoutView;
    }

    private void initView(Bundle savedInstanceState) {
        layoutView.findViewById(R.id.attention_rank).setOnClickListener(this);
        layoutView.findViewById(R.id.attention_my).setOnClickListener(this);

        //进行动态添加fragment
//        setTabSelected(R.id.attention_rank);
        if(savedInstanceState == null) {
            showAndHideFragment(R.id.attention_rank);
        }

    }

    @Override
    public void onClick(View v) {
//        setTabSelected(v.getId());
        showAndHideFragment(v.getId());
    }

    /**
     * 此处是动态添加fragment 每次点击的时候都会重新onCreateView
     * @param viewId
     */
    private void setTabSelected(int viewId) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (viewId) {
            case R.id.attention_rank:
                if(fragmentRank == null){
                    fragmentRank = new FragmentRank();
                }
                fragmentTransaction.replace(R.id.attention_content,fragmentRank);
                break;

            case R.id.attention_my:
                if(fragmentAttentionDetail == null){
                    fragmentAttentionDetail = new FragmentAttentionDetail();
                }
                fragmentTransaction.replace(R.id.attention_content,fragmentAttentionDetail);
                break;
        }

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);//设置动画效果
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    /**
     * 此处用了显示和隐藏来显示，当数据改变的时候，需要动态通过回调来刷新数据
     * @param viewId
     */
    private void showAndHideFragment(int viewId){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragment(fragmentTransaction);
        switch (viewId) {
            case R.id.attention_rank:
                if(fragmentRank == null){
                    fragmentRank = new FragmentRank();
                    fragmentTransaction.add(R.id.attention_content,fragmentRank);
                }else {
                    fragmentTransaction.show(fragmentRank);
                }
                break;

            case R.id.attention_my:
                if(fragmentAttentionDetail == null){
                    fragmentAttentionDetail = new FragmentAttentionDetail();
                    fragmentTransaction.add(R.id.attention_content,fragmentAttentionDetail);
                }else {
                    fragmentTransaction.show(fragmentAttentionDetail);
                }
                break;
        }

//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);//设置动画效果
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void hideFragment(FragmentTransaction fragmentTransaction){
        if(fragmentRank != null){
            fragmentTransaction.hide(fragmentRank);
        }

        if(fragmentAttentionDetail!= null){
            fragmentTransaction.hide(fragmentAttentionDetail);
        }
    }


}
