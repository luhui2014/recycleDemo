package com.cnpaypal.async;

/**
 * Created by Administrator on 2015/6/2.
 */
public class MerchantBean {

    private String mMerchantImageUrl;
    private String mMerchantTitle;
    private String mMerchantChineseName;

    public MerchantBean(String merchantImageUrl, String merchantTitle, String merchantChineseName){
        this.mMerchantImageUrl = merchantImageUrl;
        this.mMerchantTitle = merchantTitle;
        this.mMerchantChineseName = merchantChineseName;
    }

    public String getMerchantTitle() {
        return mMerchantTitle;
    }

    public String getMerchantChineseName() {
        return mMerchantChineseName;
    }

    public String getMerchantImageUrl() {
        return mMerchantImageUrl;
    }


}
