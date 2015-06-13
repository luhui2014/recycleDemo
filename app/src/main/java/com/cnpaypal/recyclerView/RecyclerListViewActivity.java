package com.cnpaypal.recyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.cnpaypal.home.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/6/12.
 * implements SlideView.OnSlideListener
 */
public class RecyclerListViewActivity extends Activity  {
    private RecyclerView recyclerView;
    private List<String> mListData;
    private NormalRecyclerViewAdapter normalRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recycle_listview_layout);

        initData();
        initView();
    }

    private void initData(){
        mListData = new ArrayList<>();
        for(int i=0;i<50;i++){
            mListData.add(i+"测试数据 i:"+i);
        }
    }

    private void initView(){
        findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AAAA","增加按钮点击了");
                normalRecyclerViewAdapter.notifyItemInserted(5);
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        //这里用线性显示 类似于listView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        normalRecyclerViewAdapter = new NormalRecyclerViewAdapter(this,mListData);
        recyclerView.setAdapter(normalRecyclerViewAdapter);

    }

    public class NormalRecyclerViewAdapter extends RecyclerView.Adapter<NormalRecyclerViewAdapter.NormalTextViewHolder>{
        private final LayoutInflater mLayoutInflater;
        private List<String> mList;

        public NormalRecyclerViewAdapter(Context context,List<String> list) {
            mList = list;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.item_text, parent, false);
            return new NormalTextViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NormalTextViewHolder holder, int position) {
            holder.mTextView.setText(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

        public class NormalTextViewHolder extends RecyclerView.ViewHolder {
            TextView mTextView;

            NormalTextViewHolder(View view) {
                super(view);
                mTextView = (TextView)view.findViewById(R.id.text_view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(RecyclerListViewActivity.this,"onClick--> positio" + getPosition(),Toast.LENGTH_SHORT).show();
//                        mList.remove(mList.get(getPosition()));
//                        notifyItemRemoved(getPosition());

                        v.setScrollX(100);

                    }
                });
            }
        }
    }

}
