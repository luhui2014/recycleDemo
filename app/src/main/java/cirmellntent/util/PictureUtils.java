package cirmellntent.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.widget.ImageView;

/**
 * 图片处理的工具类 on 2015/7/13.
 */
public class PictureUtils {

    @SuppressWarnings("deprecation")
    public static BitmapDrawable getScaledDrawable(Activity activity,String path){
        Display display = activity.getWindowManager().getDefaultDisplay();
        float destWidth = display.getWidth();
        float destHeight = display.getHeight();

        //read in the dimensions of the image of disk
        BitmapFactory.Options options = new BitmapFactory.Options();

        //意思就是说如果该值设为true那么将不返回实际的bitmap对象，不给其分配内存空间但是可以得到一些解码边界信息即图片大小等信息
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;
        if(srcWidth>destWidth || srcHeight>destHeight){
            if(srcWidth > srcHeight){
                inSampleSize = Math.round(srcHeight/destHeight);
            }else {
                inSampleSize = Math.round(srcWidth/destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        Bitmap bitmap = BitmapFactory.decodeFile(path,options);
        return new BitmapDrawable(activity.getResources(),bitmap);
    }

    public static void clearImageView(ImageView imageView){
        if(!(imageView.getDrawable() instanceof BitmapDrawable)){
            return;
        }

        //clear up the view image for the sake of memory
        BitmapDrawable bitmapDrawable = (BitmapDrawable)imageView.getDrawable();
        bitmapDrawable.getBitmap().recycle();
        imageView.setImageDrawable(null);
    }

}
