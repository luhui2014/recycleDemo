package com.cnpaypal.util;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CommentViewHolder {

    private int mPosition;
    private SparseArray<View> mViews;
    private View mConvertView;

    public CommentViewHolder(Context context, ViewGroup parent,int position,int layoutId) {
        mViews = new SparseArray<>();
        mPosition = position;
        mConvertView = LayoutInflater.from(context).inflate(layoutId,parent,false);
        mConvertView.setTag(this);
    }

    public static CommentViewHolder getViewHolder(Context context, View convertView, ViewGroup parent,int position,int layoutId){
        if(convertView == null){
            return new CommentViewHolder(context,parent,position,layoutId);
        }else {
            CommentViewHolder commentViewHolder = (CommentViewHolder)convertView.getTag();

            //跟新位置信息
            commentViewHolder.mPosition = position;
            return commentViewHolder;
        }
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId){
        View view = mViews.get(viewId);

        if(view == null){
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }

        return (T)view;
    }

    public CommentViewHolder setText(int ViewId,String text){
        TextView textView = getView(ViewId);
        textView.setText(text);
        return this;

    }
}
