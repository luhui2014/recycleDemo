package cirmellntent.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cnpaypal.home.R;

import java.util.ArrayList;

import cirmellntent.activity.CrimePagerActivity;
import cirmellntent.model.Crime;
import cirmellntent.model.CrimeLab;

/**
 *
 */
public class CrimeListFragment extends ListFragment{
    private ArrayList<Crime> mCrimes;
    View layoutView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_crimelistfragment_layout,container,false);

        initData();
        return layoutView;
    }

    private void initData(){
        TextView addBtn = (TextView)layoutView.findViewById(R.id.crime_add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity()," 点击了 增加的按钮 ",Toast.LENGTH_SHORT).show();
                intentToDetail();
            }
        });

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);
    }

    @TargetApi(11)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                intentToDetail();
                return true;
            case R.id.menu_show_subtitle:
                if(getActivity().getActionBar()!=null) {
                    if (getActivity().getActionBar().getSubtitle() == null) {
                        getActivity().getActionBar().setSubtitle(R.string.show_title);
                        item.setTitle("隐藏主题");
                    } else {
                        getActivity().getActionBar().setSubtitle(null);
                        item.setTitle("显示主题");
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void intentToDetail() {
        Crime crime = new Crime();
        CrimeLab.getCrimeLab(getActivity()).addCrime(crime);
        Intent intent = new Intent(getActivity(),CrimePagerActivity.class);
        intent.putExtra(CrimeFragment.EXTRA_CRIME_ID,crime.getId());
        startActivityForResult(intent,0);//需要onActivityResult 回调接受A
    }
}
