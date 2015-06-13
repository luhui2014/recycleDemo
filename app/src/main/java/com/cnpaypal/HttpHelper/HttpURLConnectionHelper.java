package com.cnpaypal.HttpHelper;

import android.util.Log;
import com.cnpaypal.util.StringUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * HttpURLConnection  学习具体过程
 * android jdk 封装好了一个网络工具类
 * 在2.0 以后做了性能的优化
 * 需要权限: <uses-permission android:name="android.permission.INTERNET" />
 *
 * http://developer.android.com/reference/java/net/HttpURLConnection.html
 */
public class HttpURLConnectionHelper {
    private static final String TAG = "Http->JDK";
    private static final int CONNECT_TIME_OUT = 3000;
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_CONTENT_LENGTH = "Content-Length";

    /**
     * Default encoding for POST or PUT parameters. See
     * {@link #getParamsEncoding()}.
     */
    private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";

    /**
     * get default encoding style
     */
    public static String getParamsEncoding(){
        return DEFAULT_PARAMS_ENCODING;
    }

    /**
     * 没有对文件上传的类型进行处理，基本x-www-form-urlencoded的类型
     */
    public static String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset="
                + getParamsEncoding();
    }

    /**
     * 处理get请求
     * @return 返回处理的结果
     */
    public static String doGet(String baseUrl){
        String result = null;
        HttpURLConnection httpURLConnection = null;

        if(StringUtils.isBlank(baseUrl)){
            Log.e(TAG," baseUrl is blank");
            return result;
        }

        try {
            //将String的url路径转化为url的格式
            URL url = new URL(baseUrl);
            //打开链接获取对象
            httpURLConnection = (HttpURLConnection)url.openConnection();

            //设置连接超时时间
            httpURLConnection.setConnectTimeout(CONNECT_TIME_OUT);
            httpURLConnection.setReadTimeout(CONNECT_TIME_OUT);

            //设置为具体的请求
            httpURLConnection.setRequestMethod("GET");

            //请求体(optionally)。如果有请求体那么setDoOutput(true)必须为true，然后把输入放在getOutputStream()流中。
            //读取响应。响应的headers一般包括了一些metadata比如响应体的内容类型和长度，修改日期以及session cookies。响应体可以从 getInputStream()流中读出。
            httpURLConnection.setDoInput(true);

            //处理返回
            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                //请求成功，正确返回
                InputStream inputStream = httpURLConnection.getInputStream();
                result = readStream(inputStream);
            }else {
                Log.e(TAG,"Connection failed,response code="+httpURLConnection.getResponseCode());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
        }
        return result;
    }

    public static String doPost(String baseUrl,Map<String,String> params){
        String result = null;
        HttpURLConnection httpURLConnection;

        try {
            URL url = new URL(baseUrl);
            httpURLConnection = (HttpURLConnection)url.openConnection();

            //开始设置具体的参数
            httpURLConnection.setConnectTimeout(CONNECT_TIME_OUT);
            httpURLConnection.setReadTimeout(CONNECT_TIME_OUT);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            //用于设置头属性
            // connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary); 文件上传的头属性
            httpURLConnection.setRequestProperty(HEADER_CONTENT_TYPE,getBodyContentType());

            /**
             * For best performance, you should call either setFixedLengthStreamingMode(int) when the body length is known in advance,
             * or setChunkedStreamingMode(int) when it is not.
             * Otherwise HttpURLConnection will be forced to buffer the complete request body in memory before it is transmitted,
             */
            byte[] data = getParamsData(params);
            if(data != null){
                httpURLConnection.setFixedLengthStreamingMode(data.length);
                httpURLConnection.setRequestProperty(HEADER_CONTENT_TYPE,String.valueOf(data.length));
                httpURLConnection.getOutputStream().write(data);
            }

            //处理返回
            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                //请求成功，正确返回
                InputStream inputStream = httpURLConnection.getInputStream();
                result = readStream(inputStream);
            }else {
                Log.e(TAG,"Connection failed,response code="+httpURLConnection.getResponseCode());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 放到map中，作为post的body 处理
     * @param params
     * @return
     */
    private static byte[] getParamsData(Map<String, String> params){
        byte[] data = null;

        if(null != params && !params.isEmpty()){
            try {
                StringBuffer stringBuffer = new StringBuffer();
                for(Map.Entry<String,String> entry : params.entrySet()){
                    stringBuffer.append(entry.getKey())
                            .append("=")
                            .append(URLEncoder.encode(entry.getValue(),getParamsEncoding()))
                            .append("&");// 请求的参数之间使用&分割。
                }

                // 最后一个&要去掉
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                data = stringBuffer.toString().getBytes(getParamsEncoding());

            }catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else {
            Log.e(TAG,"null == params || params.isEmpty()");
        }
        return data;
    }


    //doput doDelete
    //-----
    //----
    //----
    //----
    //-----
    //-----



    /**
     * 将对应的流转化为String
     * @param inputStream 输入流
     * @return 转换的结果
     */
    private static String readStream(InputStream inputStream){
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
