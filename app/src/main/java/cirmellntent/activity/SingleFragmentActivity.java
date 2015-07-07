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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_crime);

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
