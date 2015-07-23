package cirmellntent.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.cnpaypal.home.R;
import com.cnpaypal.util.CommentUtil;

import cirmellntent.fragment.CrimeFragment;
import cirmellntent.fragment.CrimeListFragment;
import cirmellntent.model.Crime;

/**
 * 由子类直接提供布局 on 2015/7/2.
 */
public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.CallBacks,CrimeFragment.CrimeFragmentListener{
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        if(CommentUtil.isTablet(this)){
            return R.layout.activity_twopane_layout;
        }
        return super.getLayoutResId();
    }

    @Override
    public void onCrimesSelected(Crime crime) {
        if((findViewById(R.id.detailFragmentContainer)) == null){
            Intent intent = new Intent(this,CrimePagerActivity.class);
            intent.putExtra(CrimeFragment.EXTRA_CRIME_ID,crime.getId());
            startActivity(intent);
        }else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment oldDetail = fragmentManager.findFragmentById(R.id.detailFragmentContainer);
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());

            if(oldDetail != null){
                fragmentTransaction.remove(oldDetail);
            }

            fragmentTransaction.add(R.id.detailFragmentContainer,newDetail);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onCrimeUpdate(Crime crime) {

    }
}
