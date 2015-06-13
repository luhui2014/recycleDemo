package com.cnpaypal.recyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.cnpaypal.home.R;

/**
 * Created by Administrator on 2015/6/13.
 */
public class RecycleMultipleItemActivity extends Activity{
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recycle_listview_layout);
        initView();
    }


    private void initView(){
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MultipleItemAdapter(this));
    }


    public class MultipleItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private LayoutInflater mLayoutInflater;
        private String[] mTitles;

        public MultipleItemAdapter(Context context) {
            //获取静态的数据,测试用的
            mTitles = context.getResources().getStringArray(R.array.date);
            //在构造函数里面初始化好，避免多次的new
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //返回此枚举常量的序数（其枚举声明中的位置，其中初始常量分配的序数为零）
            if(viewType ==ITEM_TYPE.ITEM_TYPE_IMAGE.ordinal()){
                //如果view的类型是图片布局
                return new ImageViewHolder(mLayoutInflater.inflate(R.layout.item_image,parent,false));
            }else{
                //如果view的类型是文字布局
                return new TextViewHolder(mLayoutInflater.inflate(R.layout.item_text, parent, false));
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position % 2 == 0 ? ITEM_TYPE.ITEM_TYPE_IMAGE.ordinal() : ITEM_TYPE.ITEM_TYPE_TEXT.ordinal();
        }

        @Override
        public int getItemCount() {
            return mTitles == null ? 0 : mTitles.length;
        }

        // 这种方法在内部调用onBindViewHolder(ViewHolder int)
        // 更新RecyclerView。ViewHolder内容与项目在给定的位置,还设置了一些私有字段由RecyclerView使用。
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof TextViewHolder) {
                ((TextViewHolder) holder).mTextView.setText(mTitles[position]);
            } else if (holder instanceof ImageViewHolder) {
                ((ImageViewHolder) holder).mTextView.setText(mTitles[position]);
            }
        }

        //设置内部的holder
        public class TextViewHolder extends RecyclerView.ViewHolder{
            private TextView mTextView;

            public TextViewHolder(View itemView) {
                super(itemView);
                mTextView = (TextView)itemView.findViewById(R.id.text_view);
                mTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(RecycleMultipleItemActivity.this,"TextViewHolder position ="+getPosition(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }


        public class ImageViewHolder extends RecyclerView.ViewHolder{
            private TextView mTextView;
            private ImageView mImageView;

            public ImageViewHolder(View itemView) {
                super(itemView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(RecycleMultipleItemActivity.this,"ImageViewHolder onClick position ="+getPosition(),Toast.LENGTH_SHORT).show();
                    }
                });
                //itemView 是父布局返回的View
                //在构造函数初始化布局、
                mTextView = (TextView)itemView.findViewById(R.id.text_view);
                mImageView = (ImageView)itemView.findViewById(R.id.image_view);
            }
        }
    }

    public enum ITEM_TYPE {
        ITEM_TYPE_IMAGE,
        ITEM_TYPE_TEXT
    }

}
