package cirmellntent.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cirmellntent.util.PictureUtils;

/**
 * Created by Administrator on 2015/7/13.
 */
public class ImageDialogFragment extends DialogFragment {
    public static final String EXTRA_IMAGE_PATH = "com.cnpaypal.cirmellntent.image_path";

    public static ImageDialogFragment newInstance(String imagePath){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_IMAGE_PATH,imagePath);

        ImageDialogFragment dialogFragment = new ImageDialogFragment();
        dialogFragment.setArguments(args);
        dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE,0);

        return dialogFragment;
    }

    private ImageView mImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mImageView = new ImageView(getActivity());
        String path = (String)getArguments().getSerializable(EXTRA_IMAGE_PATH);
        BitmapDrawable bitmapDrawable = PictureUtils.getScaledDrawable(getActivity(),path);
        mImageView.setBackground(bitmapDrawable);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return mImageView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PictureUtils.clearImageView(mImageView);
    }
}
