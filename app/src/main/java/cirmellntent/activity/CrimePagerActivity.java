package cirmellntent.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.cnpaypal.home.R;
import com.cnpaypal.util.CommentUtil;

import java.util.ArrayList;
import java.util.UUID;

import cirmellntent.fragment.CrimeFragment;
import cirmellntent.model.Crime;
import cirmellntent.model.CrimeLab;

/**
 * Created by luhui on 2015/7/3.
 * fragment viewpager使用FragmentStatePagerAdapter的方式
 */
public class CrimePagerActivity extends FragmentActivity{
    private ViewPager mViewPager;
    private ArrayList<Crime> mCrimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crime_viewpager_layout);
        initData();
        initView();
    }

    private void initData(){
        mCrimes = CrimeLab.getCrimeLab(this).getCrimes();
    }

    private void initView(){
        mViewPager = (ViewPager)findViewById(R.id.crime_viewpager);
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        setupCrimeDetail();
    }

    private void setupCrimeDetail(){
        //点击的时候，显示当前对应的viewpager的页面
        UUID crimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        for(int i=0;i<mCrimes.size();i++){
            if(mCrimes.get(i).getId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            CommentUtil.finishActivityTranslate(this);
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }
}
