package cirmellntent.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.cnpaypal.home.R;

/**
 * 抽象出activity类
 */
public abstract class SingleFragmentActivity extends FragmentActivity{
    protected abstract Fragment createFragment();

    //只能自己用和子类用
    protected int getLayoutResId(){
        return R.layout.activity_fragment_crime;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //由子类直接提供布局，更加的灵活方便
        setContentView(getLayoutResId());

        initView();
    }

    private void initView(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.crime_fragmentContainer);
        if(fragment == null){
            fragment = createFragment();
            fragmentManager.beginTransaction().add(R.id.crime_fragmentContainer,fragment).commit();
        }
    }


}
