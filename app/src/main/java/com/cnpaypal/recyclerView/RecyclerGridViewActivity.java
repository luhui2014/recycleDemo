package com.cnpaypal.recyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cnpaypal.home.R;

/**
 * Created by Administrator on 2015/6/12.
 */
public class RecyclerGridViewActivity extends Activity{
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recycle_listview_layout);
        initView();
    }

    private void initView(){
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        //这里用线性显示 类似于listView
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //这里使用gridView 分两行列
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(new NormalRecyclerViewAdapter(this));

    }

    public class NormalRecyclerViewAdapter extends RecyclerView.Adapter<NormalRecyclerViewAdapter.NormalTextViewHolder>{
        private final LayoutInflater mLayoutInflater;
        private final Context mContext;
        private String[] mTitles;

        public NormalRecyclerViewAdapter(Context context) {
            mTitles = context.getResources().getStringArray(R.array.date);
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new NormalTextViewHolder(mLayoutInflater.inflate(R.layout.item_text, parent, false));
        }

        @Override
        public void onBindViewHolder(NormalTextViewHolder holder, int position) {
            holder.mTextView.setText(mTitles[position]);
        }

        @Override
        public int getItemCount() {
            return mTitles == null ? 0 : mTitles.length;
        }



        public class NormalTextViewHolder extends RecyclerView.ViewHolder {
            TextView mTextView;

            NormalTextViewHolder(View view) {
                super(view);
                mTextView = (TextView)view.findViewById(R.id.text_view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(RecyclerGridViewActivity.this,"onClick--> positio" + getPosition(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
