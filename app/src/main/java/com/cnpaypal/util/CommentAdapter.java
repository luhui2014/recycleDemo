package com.cnpaypal.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.List;

/**
 * Created by Administrator on 2015/6/4.
 */
public abstract class CommentAdapter<T> extends BaseAdapter{
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<T> mData;
    protected int mLayoutId;

    public CommentAdapter(Context mContext, List<T> mData,int layoutId) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.mData = mData;
        this.mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        CommentViewHolder commentViewHolder = CommentViewHolder.getViewHolder(mContext,convertView,parent,position,mLayoutId);
        convert(commentViewHolder,getItem(position));
        return commentViewHolder.getConvertView();
    }

    public abstract void convert(CommentViewHolder commentViewHolder,T t);


}
