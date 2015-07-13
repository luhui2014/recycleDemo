package cirmellntent.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cnpaypal.home.R;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 控制相机界面的fragment的显示
 */
public class CrimeCameraFragment extends Fragment {
    public static final String EXTAR_PHOTO_FILENAME = "com.cnpaypal.cirmellntent.photo_filename";
    private Camera mCamera;
    private SurfaceView surfaceView;
    private View layoutView;
    private View mProgressContainer;

    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            //在相机捕获图像的时候调用，这是图片还没有处理完成,显示等待加载框
            mProgressContainer.setVisibility(View.VISIBLE);
        }
    };

    private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //这个回调的方法，在jpeg版本的图像可用时调用

            //创建文件名
            String fileName = UUID.randomUUID().toString() + ".jpg";
            //保存文件到disk
            FileOutputStream outputStream = null;
            boolean success = true;

            try {
                outputStream = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
                outputStream.write(data);
            } catch (Exception e) {
                Log.d("AAAA", "PictureCallback Exception", e);
                success = false;
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        success = false;
                    }
                }
            }

            if (success) {
                Log.d("AAAA", "PictureCallback save fileName" + fileName);
                Intent intent = new Intent();
                intent.putExtra(EXTAR_PHOTO_FILENAME,fileName);
                getActivity().setResult(Activity.RESULT_OK,intent);
            }else {
                getActivity().setResult(Activity.RESULT_CANCELED);
            }

            getActivity().finish();

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_crime_camera_layout, container, false);
        initView();
        return layoutView;
    }

    @SuppressWarnings("deprecation")
    private void initView() {
        surfaceView = (SurfaceView) layoutView.findViewById(R.id.crime_camera_surfaceView);
        mProgressContainer = layoutView.findViewById(R.id.crime_camera_progressContainer);

        Button takePictureBtn = (Button) layoutView.findViewById(R.id.crime_camera_takePicture_btn);
        takePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().finish();
                if (mCamera != null) {
                    mCamera.takePicture(mShutterCallback, null, mJpegCallback);
                }
            }
        });

        SurfaceHolder holder = surfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //告诉相机使用这块区域作为他的显示区域
                if (mCamera != null) {
                    try {
                        mCamera.setPreviewDisplay(holder);
                    } catch (IOException e) {
                        Log.d("AAAA", "error setting up preview display", e);
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (mCamera == null) return;
                //update the camera preview size
                Camera.Parameters parameters = mCamera.getParameters();
                Camera.Size size = getBestSupportedSize(parameters.getSupportedPreviewSizes());
                parameters.setPreviewSize(size.width, size.height);

                //get size again to set pic size like preview size
                size = getBestSupportedSize(parameters.getSupportedPreviewSizes());
                parameters.setPreviewSize(size.width, size.height);

                mCamera.setParameters(parameters);

                try {
                    mCamera.startPreview();
                } catch (Exception e) {
                    mCamera.release();
                    mCamera = null;
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mCamera != null) {
                    mCamera.stopPreview();
                }
            }
        });
    }

    @TargetApi(9)
    @Override
    public void onResume() {
        super.onResume();
        //camera的api 版本不同调用的方法也不同
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mCamera = Camera.open(0);
        } else {
            mCamera = Camera.open();
        }
    }

    /**
     * 在回收内存之前会先调用onPause的方法
     * 一般处理，释放xxxx
     */
    @Override
    public void onPause() {
        super.onPause();
        if (mCamera != null) {
            //及时释放，是其他的可以打开相机
            mCamera.release();
            //清空，内存的释放，防止内存泄露
            mCamera = null;
        }
    }

    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes) {
        Camera.Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for (Camera.Size size : sizes) {
            int area = size.width * size.height;
            if (area > largestArea) {
                bestSize = size;
                largestArea = area;
            }
        }
        return bestSize;
    }

}
