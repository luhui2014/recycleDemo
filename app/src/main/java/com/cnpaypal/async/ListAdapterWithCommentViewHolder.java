package com.cnpaypal.async;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.cnpaypal.home.R;
import com.cnpaypal.util.CommentAdapter;
import com.cnpaypal.util.CommentViewHolder;
import java.util.List;

/**
 * 监听listView 停止滚动的时候开始加载，搭配一个通用的adapter
 */
public class ListAdapterWithCommentViewHolder extends CommentAdapter<MerchantBean> implements AbsListView.OnScrollListener{
    private List<MerchantBean> mMerchantBeanList;
    private ImageLoader mImageLoader;
    private int mStar,mEnd;
    private boolean mFirstIn;

    public static String[] URLS;

    public ListAdapterWithCommentViewHolder(Context context,List<MerchantBean> merchantBeanList,ListView listView) {
        super(context,merchantBeanList, R.layout.list_item);

        mMerchantBeanList = merchantBeanList;

        mFirstIn = true;
        URLS = new String[merchantBeanList.size()];
        for(int i=0;i<merchantBeanList.size();i++){
            URLS[i] = merchantBeanList.get(i).getMerchantImageUrl();
        }

        mImageLoader = new ImageLoader(context,listView);
        listView.setOnScrollListener(this);
    }

    @Override
    public void convert(CommentViewHolder commentViewHolder, MerchantBean merchantBean) {
        //链式
        commentViewHolder.setText(R.id.merchant_title,merchantBean.getMerchantTitle())
                .setText(R.id.merchant_chinese_name,merchantBean.getMerchantChineseName());

        //
        ImageView imageView = commentViewHolder.getView(R.id.merchant_image);
        imageView.setTag(merchantBean.getMerchantImageUrl());

        mImageLoader.showImageByTask(imageView, merchantBean.getMerchantImageUrl());
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == SCROLL_STATE_IDLE){
            mImageLoader.loadImage(mStar,mEnd);
        }else {
            mImageLoader.cancelAllTask();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStar = firstVisibleItem;
        mEnd = firstVisibleItem+visibleItemCount;

        if(mFirstIn && visibleItemCount>0){
            mImageLoader.loadImage(mStar,mEnd);
            mFirstIn = false;
        }

    }
}



