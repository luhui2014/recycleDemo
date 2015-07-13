package cirmellntent.activity;

import android.support.v4.app.Fragment;

import cirmellntent.fragment.CrimeCameraFragment;

/**
 * Created by Administrator on 2015/7/10.
 */
public class CrimeCameraActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeCameraFragment();
    }

}
