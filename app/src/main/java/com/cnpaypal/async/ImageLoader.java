package com.cnpaypal.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import com.cnpaypal.home.R;
import com.cnpaypal.util.CommentUtil;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import libcore.io.DiskLruCache;

/**
 * 1 DiskLruCache的本地缓存文件路径为:
 *   /sdcard/Android/data//cache
 *   DiskLruCache
 *   1.1 remove(String key) 删除某个已缓存的对象.
 *   1.2 size() 得到缓存路径下缓存的总大小.这个很常见.许多APP都提示当前有多少缓存.
 *   1.3 flush() 将内存中的操作记录同步到日志文件(也就是journal文件)中
 *       一般在Activity的onPause()中调用该方法.
 *   1.4 close() 关闭DiskLruCache.该方法与open方法对应.
 *       一般在Activity的onDestory()中调用该方法.
 *   1.5 delete() 将缓存数据全部删除.
 *
 *
 * 学习资源:
 * http://blog.csdn.net/guolin_blog/article/details/28863651
 */
public class ImageLoader {
    private static final int DISK_CACHE_DEFAULT_SIZE = 10 * 1024 * 1024;
    private static final int DEFAULT_VALUECOUNT = 1;

    private Context mContext;
    private ImageView mImageView;
    private String mUrl;
    private LruCache<String,Bitmap> lruCache;
    private ListView mListView;
    private Set<ImageLoaderTask> mTasks;
    private DiskLruCache mDiskLruCache;

    public ImageLoader(Context context,ListView listView){
        mContext = context;
        mListView = listView;
        mTasks = new HashSet<>();

        initLruCache();
        initDiskLruCache();
    }

    /**
     * 一级内存缓存
     */
    private void initLruCache(){
        int cacheMaxSize = (int)Runtime.getRuntime().maxMemory()/4;

        lruCache = new LruCache<String,Bitmap>(cacheMaxSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    /**
     * 二级文件缓存
     */
    private void initDiskLruCache(){
        try {
            File cacheDir = CommentUtil.getDiskCacheDir(mContext, "bitmap");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheDir, CommentUtil.getAppVersion(mContext), DEFAULT_VALUECOUNT, DISK_CACHE_DEFAULT_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mImageView.getTag().equals(mUrl)){
                mImageView.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };

    public void addImageToCache(String url,Bitmap bitmap){
        if(getBitmapFromCache(url) == null){
            lruCache.put(url,bitmap);
        }
    }

    public Bitmap getBitmapFromCache(String url){
        return lruCache.get(url);
    }


    /**
     * 通过线程下载网络图片
     * @param imageView
     * @param imageUrl
     */
    public void showImageByThread(ImageView imageView,final String imageUrl){
        mImageView = imageView;
        mUrl = imageUrl;

        new Thread(){
            @Override
            public void run() {
//                Bitmap bitmap = getBitmapFormUrl(imageUrl);
//                Message message = Message.obtain();
//                message.obj = bitmap;
//                mHandler.sendMessage(message);

            }
        }.start();
    }

    /**
     * 通过异步任务获取网络图片
     * @param imageView
     * @param imageUrl
     */
    public void showImageByTask(ImageView imageView,final String imageUrl){
        Bitmap bitmap = getBitmapFromCache(imageUrl);
        if(bitmap == null){
            imageView.setImageResource(R.drawable.ic_launcher);
        }else {
            imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * 二级缓存，重disk 和 本地缓存中获取
     * @param imageUrl
     * @param outputStream
     * @return
     */
    public Bitmap getBitmapFormUrl(String imageUrl,OutputStream outputStream){
        Bitmap bitmap;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        try {
            URL url = new URL(imageUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            bitmap = BitmapFactory.decodeStream(bufferedInputStream);

//            HttpGet httpRequest = new HttpGet(imageUrl);
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
//            HttpEntity entity = response.getEntity();
//            BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(entity);
//            InputStream is = bufferedHttpEntity.getContent();
//            bufferedInputStream = new BufferedInputStream(is);
//            bitmap = BitmapFactory.decodeStream(is);
//            bufferedOutputStream = new BufferedOutputStream(outputStream);


            int byteRead;
            while ((byteRead = bufferedInputStream.read()) != -1) {
                bufferedOutputStream.write(byteRead);
            }

            Log.e("AAAA", "getBitmapFormUrl 成功了 bitmap="+bitmap);

            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                //关闭流
                if(bufferedInputStream!=null){
                    bufferedInputStream.close();
                }

                if(bufferedOutputStream!=null){
                    bufferedOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     *
     * 一级缓存，重网络中获取
     * @param imageUrl
     * @return
     */
    public Bitmap getBitmapFormUrl(String imageUrl){
        Bitmap bitmap;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        try {
            URL url = new URL(imageUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            bitmap = BitmapFactory.decodeStream(bufferedInputStream);

            //关闭当前的链接
            httpURLConnection.disconnect();
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                //关闭流
                if(bufferedInputStream!=null){
                    bufferedInputStream.close();
                }

                if(bufferedOutputStream!=null){
                    bufferedOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private class ImageLoaderTask extends AsyncTask<String,Void,Bitmap>{
        private String urlInTask;

        public ImageLoaderTask(String url) {
            this.urlInTask = url;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];

            //一级缓存处理
//            Bitmap bitmap = getBitmapFormUrl(url);
//            if(bitmap == null){
//                Log.e("AAAA", "ImageLoaderTask 没有下载成功");
//                bitmap = getBitmapFormUrl(url);
//            }

            //增加二级缓存处理
            Bitmap bitmap = downloadToDiskCache(url);

            if(bitmap!=null){
                addImageToCache(url,bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageViewInTask = (ImageView)mListView.findViewWithTag(urlInTask);
            if(bitmap!=null && imageViewInTask!=null && imageViewInTask.getTag().equals(urlInTask)){
                imageViewInTask.setImageBitmap(bitmap);
            }

            //图片下载完成之后，需要删除任务
            mTasks.remove(this);
        }
    }

    private Bitmap downloadToDiskCache(String url){
        Bitmap bitmap = null;
        String key = hashKeyForDisk(url);
        DiskLruCache.Editor editor = null;
        try {
            editor = mDiskLruCache.edit(key);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                bitmap = getBitmapFormUrl(url,outputStream);
                if(bitmap!=null){
                    Log.d("AAAA","bitmap!=null 放到二级缓存文件中,成功了 key="+key+",bitmap.size="+bitmap.getByteCount());
                    editor.commit();
                }else {
                    editor.abort();
                }
                mDiskLruCache.flush();
            }
        } catch (IOException e) {
            bitmap = null;
        }

        return bitmap;
    }

    public void loadImage(int start,int end){
        for(int i=start;i<end;i++){
            String url = ListAdapterWithCommentViewHolder.URLS[i];
            Bitmap bitmap = getBitmapFromCache(url);

//            if(bitmap == null){
//                ImageLoaderTask task = new ImageLoaderTask(url);
//                task.execute(url);
//                mTasks.add(task);
//            }else {
//                ((ImageView)mListView.findViewWithTag(url)).setImageBitmap(bitmap);
//            }

            if(bitmap == null){
                Log.d("AAAA"," loadImage 缓存中没有找到 对应的数据");
                String key = hashKeyForDisk(url);
                DiskLruCache.Snapshot snapShot = null;
                try {
                    snapShot = mDiskLruCache.get(key);
                    if (snapShot != null) {
                        InputStream is = snapShot.getInputStream(0);
                        Log.d("AAAA","loadImage 找到对应的key="+key +",is="+is.toString());
                        bitmap = BitmapFactory.decodeStream(is);
                    }
                } catch (IOException e) {
                    Log.d("AAAA","loadImage 没有找到对应的key="+key);
                    bitmap = null;
                }

                if(bitmap == null){
                    Log.d("AAAA","loadImage 文件缓存没有找到对应的bitmap == null");
                    ImageLoaderTask task = new ImageLoaderTask(url);
                    task.execute(url);
                    mTasks.add(task);
                }else {
                    ((ImageView)mListView.findViewWithTag(url)).setImageBitmap(bitmap);
                }
            }else {
                ((ImageView)mListView.findViewWithTag(url)).setImageBitmap(bitmap);
            }

        }
    }

    public void cancelAllTask(){
        if(mTasks!=null){
            for(ImageLoaderTask imageLoaderTask : mTasks){
                imageLoaderTask.cancel(false);
            }
        }
    }

    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }


    /**
     * 将字符串用MD5编码.
     * 比如在改示例中将url进行MD5编码
     */
    public String getStringByMD5(String string) {
        String md5String = null;
        try {
            // Create MD5 Hash
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(string.getBytes());
            byte messageDigestByteArray[] = messageDigest.digest();
            if (messageDigestByteArray == null || messageDigestByteArray.length == 0) {
                return md5String;
            }

            // Create hexadecimal String
            StringBuffer hexadecimalStringBuffer = new StringBuffer();
            int length = messageDigestByteArray.length;
            for (int i = 0; i < length; i++) {
                hexadecimalStringBuffer.append(Integer.toHexString(0xFF & messageDigestByteArray[i]));
            }
            md5String = hexadecimalStringBuffer.toString();
            return md5String;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5String;
    }


    /**
     * 清除本地的所以缓存
     */
    public void deleteDisCache(){
        try {
            mDiskLruCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



