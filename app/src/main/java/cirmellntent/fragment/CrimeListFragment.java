package cirmellntent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import com.cnpaypal.home.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import cirmellntent.activity.CrimeActivity;
import cirmellntent.activity.CrimePagerActivity;
import cirmellntent.model.Crime;
import cirmellntent.model.CrimeLab;

/**
 *
 */
public class CrimeListFragment extends ListFragment{
    private ArrayList<Crime> mCrimes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    private void initData(){
        mCrimes = CrimeLab.getCrimeLab(getActivity()).getCrimes();
        if(mCrimes!=null){
            setListAdapter(new CrimeAdapter(mCrimes));
        }
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Crime crime = ((CrimeAdapter)getListAdapter()).getItem(position);
        Intent intent = new Intent(getActivity(),CrimePagerActivity.class);
        intent.putExtra(CrimeFragment.EXTRA_CRIME_ID,crime.getId());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_left_in, R.anim.activity_alpha_out);

    }

    private class CrimeAdapter extends ArrayAdapter<Crime>{
        private LayoutInflater inflater;

        public CrimeAdapter(ArrayList<Crime> crimes) {
            //不需要预定的的布局，使用0作为参数传入，自定义布局
            super(getActivity(),0,crimes);

            inflater = getActivity().getLayoutInflater();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                convertView = inflater.inflate(R.layout.list_item_crime_layout,null);
                viewHolder = new ViewHolder();
                viewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.crime_list_checkbox);
                viewHolder.crimeTitle = (TextView)convertView.findViewById(R.id.crime_list_title);
                viewHolder.crimeDate = (TextView)convertView.findViewById(R.id.crime_list_date);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder)convertView.getTag();
            }

            Crime crime = getItem(position);
            viewHolder.checkBox.setChecked(crime.isSolved());
            viewHolder.checkBox.setEnabled(false);
            viewHolder.crimeTitle.setText(crime.getTitle());
            viewHolder.crimeDate.setText(crime.getDate());
            return convertView;
        }
    }

    private class ViewHolder{
        TextView crimeTitle;
        TextView crimeDate;
        CheckBox checkBox;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }
}
