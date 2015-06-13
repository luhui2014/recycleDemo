package com.cnpaypal.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import com.cnpaypal.home.R;

import java.io.File;

/**
 * Created by Administrator on 2015/6/4.
 */
public class CommentUtil {

    /**
     * 查找sd卡的目录，如果不存在则用本地目录
     * @param context
     * @param uniqueName
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {

            // 目录地址 /sdcard/Android/data/<application package>/cache
            if(context.getExternalCacheDir()!=null){
                cachePath = context.getExternalCacheDir().getPath();
            }else {
                cachePath = context.getCacheDir().getPath();
            }

        } else {
            // 目录地址 /data/data/<application package>/cache
            cachePath = context.getCacheDir().getPath();
        }

        Log.d("AAAA","创建的文件的目录是="+cachePath+",uniqueName="+cachePath + File.separator + uniqueName);
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获取版本号,每当版本号改变，缓存路径下存储的所有数据都会被清除掉。
     * 因为DiskLruCache认为当应用程序有版本更新的时候，所有的数据都应该从网上重新获取。
     * @param context
     * @return
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static void finishActivityTranslate(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_left_out);
    }

    public static void startActivityTranslate(Activity activity,Class<?> cls){
        Intent intent = new Intent(activity,cls);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.activity_left_in, R.anim.activity_alpha_out);
    }

    public static int dip2px(Context context,float dipValue){
        final float scale=context.getResources().getDisplayMetrics().densityDpi;
        return (int)(dipValue*(scale/160)+0.5f);
    }

    public static int px2dp(Context context,float pxValue){
        final float scale = context.getResources().getDisplayMetrics().densityDpi;
        return (int)((pxValue*160)/scale+0.5f);
    }
}
