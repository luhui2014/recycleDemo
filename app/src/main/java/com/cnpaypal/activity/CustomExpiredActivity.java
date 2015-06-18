package com.cnpaypal.activity;

import android.app.Activity;
import android.os.Bundle;

import com.cnpaypal.home.R;

/**
 * Created by Administrator on 2015/6/18.
 */
public class CustomExpiredActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.item_expired_layout);
    }
}
