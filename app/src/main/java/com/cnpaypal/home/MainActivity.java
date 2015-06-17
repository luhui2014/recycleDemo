package com.cnpaypal.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cnpaypal.CrashCatchUtil.CrashHandler;
import com.cnpaypal.ObservableScrollView.ScrollStickyActivity;
import com.cnpaypal.animation.AnimationMainActivity;
import com.cnpaypal.animation.LayoutChange;
import com.cnpaypal.animation.ObjectAnimatorActivity;
import com.cnpaypal.async.ListViewMainActivity;
import com.cnpaypal.fragment.FragmentMainActivity;
import com.cnpaypal.loading.LeafLoadingActivity;
import com.cnpaypal.recyclerView.RecycleMultipleItemActivity;
import com.cnpaypal.recyclerView.RecyclerGridViewActivity;
import com.cnpaypal.recyclerView.RecyclerListViewActivity;
import com.cnpaypal.util.CommentUtil;

/**
 * Created by Administrator on 2015/6/8.
 */
public class MainActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_layout);

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

        initView();
    }

    private void initView(){
        TextView showListBtn = (TextView)findViewById(R.id.home_list_btn);
        showListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListViewMainActivity.class);
                startActivity(intent);
            }
        });

        TextView showFragmentBtn = (TextView)findViewById(R.id.home_fragment_btn);
        showFragmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FragmentMainActivity.class);
                startActivity(intent);
            }
        });


        TextView loadingBtn = (TextView)findViewById(R.id.home_fragment_loading);
        loadingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LeafLoadingActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.home_layout_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LayoutChange.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.home_animation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentUtil.startActivityTranslate(MainActivity.this, AnimationMainActivity.class);
            }
        });

        findViewById(R.id.home_recyclerView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentUtil.startActivityTranslate(MainActivity.this, RecyclerListViewActivity.class);
            }
        });

        findViewById(R.id.home_recyclerView_gridView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentUtil.startActivityTranslate(MainActivity.this, RecyclerGridViewActivity.class);
            }
        });

        findViewById(R.id.home_recyclerView_multiple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentUtil.startActivityTranslate(MainActivity.this, RecycleMultipleItemActivity.class);
            }
        });

        findViewById(R.id.home_scrollView_sticky).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentUtil.startActivityTranslate(MainActivity.this, ScrollStickyActivity.class);
            }
        });

    }
}
