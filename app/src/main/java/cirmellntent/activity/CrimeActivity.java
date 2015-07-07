package cirmellntent.activity;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import com.cnpaypal.util.CommentUtil;

import java.util.UUID;

import cirmellntent.fragment.CrimeFragment;

/**
 * Created by Administrator on 2015/7/1.
 */
public class CrimeActivity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment() {

        UUID crimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            CommentUtil.finishActivityTranslate(CrimeActivity.this);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
