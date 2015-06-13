package com.cnpaypal.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.cnpaypal.home.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ListViewMainActivity extends Activity {
    private String uriApi = "http://www.imooc.com/api/teacher?type=4&num=30";
//    private String uriApi = "http://mwebapi.liguolicai.com/api/Products/GetProducts?pageId=1&pageSize=1";

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);

        initView();
    }

    private void initView(){
        mListView = (ListView)findViewById(R.id.home_listView);
        new MerchantAsync().execute(uriApi);
    }

    private class MerchantAsync extends AsyncTask<String,Void,List<MerchantBean>>{
        @Override
        protected List<MerchantBean> doInBackground(String... params) {
            return getJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(List<MerchantBean> merchantBeans) {
            super.onPostExecute(merchantBeans);

            //这个是正常的adapter
//            mListView.setAdapter(new ListAdapter(MainActivity.this,merchantBeans,mListView));

            //这个adapter可以适配大部分的list,不需要重复的viewholder和其他的控件
            mListView.setAdapter(new ListAdapterWithCommentViewHolder(ListViewMainActivity.this,merchantBeans,mListView));

        }
    }

    private List<MerchantBean> getJsonData(String url){
        List<MerchantBean> merchantBennList = new ArrayList<>();
        MerchantBean merchantBean;
        try {
            String jsonString = readStream(new URL(url).openStream());
            Log.d("AAAA","获取的xml 是="+jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++){
                jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.optString("name","");
                String content = jsonObject.optString("description","");
                String imageUrl = jsonObject.optString("picSmall","");
                merchantBean = new MerchantBean(imageUrl,title,content);
                merchantBennList.add(merchantBean);
            }
            return merchantBennList;
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private String readStream(InputStream inputStream){
        InputStreamReader inputStreamReader;
        String result = "";

        try {
            String line;
            inputStreamReader = new InputStreamReader(inputStream,"utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while((line = bufferedReader.readLine())!= null){
                result += line;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return result;
    }

}
