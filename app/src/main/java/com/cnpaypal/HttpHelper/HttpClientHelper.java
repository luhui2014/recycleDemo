package com.cnpaypal.HttpHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * HttpClient 由Apache 提供了网络链接类
 */
public class HttpClientHelper {

    public static String doGet(String baseUrl){
        String result = null;

        //首先需要生成一个请求的对象
        HttpGet httpGet = new HttpGet(baseUrl);

        //生成客户端请求对象，带参数的
        HttpParams httpParams = new BasicHttpParams();

        //连接的超时时间
        HttpConnectionParams.setConnectionTimeout(httpParams,10*1000);
        //读取等待的超时时间
        HttpConnectionParams.setSoTimeout(httpParams,10*1000);
        //设置最大通信缓存大小
        HttpConnectionParams.setSocketBufferSize(httpParams,8*1024);

        //用默认的实现方式
        HttpClient httpClient = new DefaultHttpClient(httpParams);

        // 下面使用Http客户端发送请求，并获取响应内容
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            int stateCode = httpResponse.getStatusLine().getStatusCode();
            if(stateCode == 200){
                //说明 成功获取返回结果
                result = getResponseString(httpResponse);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }

    public static String getResponseString(HttpResponse response) {
        String result = null;
        if (null == response) {
            return result;
        }

        HttpEntity httpEntity = response.getEntity();
        InputStream inputStream = null;
        try {
            inputStream = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            result = "";
            String line;
            while (null != (line = reader.readLine())) {
                result += line;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }
}
