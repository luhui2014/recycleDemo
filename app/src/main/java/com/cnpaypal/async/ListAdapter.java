package com.cnpaypal.async;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cnpaypal.home.R;

import java.util.List;

public class ListAdapter extends BaseAdapter implements AbsListView.OnScrollListener{
    private LayoutInflater mInflater;
    private List<MerchantBean> mMerchantBeanList;
    private ImageLoader mImageLoader;
    private int mStar,mEnd;
    private boolean mFirstIn;

    public static String[] URLS;

    public ListAdapter(Context context,List<MerchantBean> merchantBeanList,ListView listView) {
        mInflater = LayoutInflater.from(context);
        mMerchantBeanList = merchantBeanList;


        mFirstIn = true;
        URLS = new String[mMerchantBeanList.size()];
        for(int i=0;i<merchantBeanList.size();i++){
            URLS[i] = merchantBeanList.get(i).getMerchantImageUrl();
        }
        mImageLoader = new ImageLoader(context,listView);
        listView.setOnScrollListener(this);
    }

    @Override
    public int getCount() {
        return mMerchantBeanList.size();
    }

    @Override
    public MerchantBean getItem(int position) {
        return mMerchantBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.list_blur_item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView)convertView.findViewById(R.id.merchant_image);
            viewHolder.title = (TextView)convertView.findViewById(R.id.merchant_title);
            viewHolder.chineseName = (TextView)convertView.findViewById(R.id.merchant_chinese_name);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        MerchantBean merchantBean = getItem(position);
        viewHolder.title.setText(merchantBean.getMerchantTitle());
        viewHolder.chineseName.setText(merchantBean.getMerchantChineseName());

        //防止复用的时候，加载位置出错
//        viewHolder.image.setImageResource(R.drawable.ic_launcher);
        viewHolder.image.setTag(merchantBean.getMerchantImageUrl());

        //加载网络图片
//        new ImageLoader().showImageByThread(viewHolder.image,merchantBean.getMerchantImageUrl());
        mImageLoader.showImageByTask(viewHolder.image, merchantBean.getMerchantImageUrl());

        return convertView;
    }

    public class ViewHolder{
        ImageView image;
        TextView title;
        TextView chineseName;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == SCROLL_STATE_IDLE){
            //商户停止滚动了
            mImageLoader.loadImage(mStar,mEnd);
        }else {
            //表示列表在滚动，停止下载任务
            mImageLoader.cancelAllTask();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStar = firstVisibleItem;
        mEnd = firstVisibleItem+visibleItemCount;

        //开始进入的时候加载可见的几个item
        if(mFirstIn && visibleItemCount>0){
            mImageLoader.loadImage(mStar,mEnd);
        }

    }
}


