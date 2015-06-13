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
public class FragmentNearBy extends Fragment{
    public static final String FRAGMENT_NEARBY_ARGUMENT = "FRAGMENT_NEARBY_ARGUMENT";

    private String mArgument;

    /**
     * 场景，比如我们某个按钮触发Activity跳转，需要通过Intent传递参数到目标Activity的Fragment中
     * @param arguments
     * @return
     */
    public static FragmentNearBy newInstance(String arguments){
        FragmentNearBy fragmentNearBy = new FragmentNearBy();
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_NEARBY_ARGUMENT,arguments);
        fragmentNearBy.setArguments(bundle);
        return fragmentNearBy;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null)
            mArgument = bundle.getString(FRAGMENT_NEARBY_ARGUMENT);
        Log.d("AAAA", " FragmentNearBy onCreateView  mArgument="+mArgument);

        return inflater.inflate(R.layout.fragment_nearby_layout,container,false);
    }
}
