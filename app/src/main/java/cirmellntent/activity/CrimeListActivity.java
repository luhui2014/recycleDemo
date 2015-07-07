package cirmellntent.activity;

import android.support.v4.app.Fragment;

import cirmellntent.fragment.CrimeListFragment;

/**
 * Created by Administrator on 2015/7/2.
 */
public class CrimeListActivity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
