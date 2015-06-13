package com.cnpaypal.animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.cnpaypal.home.R;
import com.cnpaypal.util.CommentUtil;

/**
 * Created by Administrator on 2015/6/12.
 */
public class AnimationMainActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_animation_layout);

        initView();
    }

    private void initView(){

        //ObjectAnimator的属性动画实现的按钮
        findViewById(R.id.animation_objectAnimator_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentUtil.startActivityTranslate(AnimationMainActivity.this,ObjectAnimatorActivity.class);
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            CommentUtil.finishActivityTranslate(AnimationMainActivity.this);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
