package com.cnpaypal.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnpaypal.home.R;

/**
 * Created by Administrator on 2015/6/8.
 */
public class FragmentUser extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("AAAA", "Fragment FragmentUser onCreateView");
        return inflater.inflate(R.layout.fragment_user_layout,container,false);
    }
}
