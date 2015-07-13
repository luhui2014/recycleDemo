package cirmellntent.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cnpaypal.home.R;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.TimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

import cirmellntent.activity.CrimeCameraActivity;
import cirmellntent.model.Crime;
import cirmellntent.model.CrimeLab;
import cirmellntent.model.Photo;
import cirmellntent.util.PictureUtils;

/**
 * 详情页面 on 2015/7/1.
 */
public class CrimeFragment extends Fragment{
    public static final String EXTRA_CRIME_ID = "CRIME_ID";
    private static final int REQUEST_PHOTO = 1;

    private View layoutView;
    private Crime crime;
    private ImageView mPhotoView;

    public static CrimeFragment newInstance(UUID crimeId){
        CrimeFragment crimeFragment = new CrimeFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID,crimeId);
        crimeFragment.setArguments(args);
        return crimeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //构造对象
        UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
        crime = CrimeLab.getCrimeLab(getActivity()).getCrime(crimeId);
        if(crime == null){
            crime = new Crime();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_crime_layout,container,false);

        initView();
        return layoutView;
    }

    private void initView(){
        EditText materialEditText = (EditText)layoutView.findViewById(R.id.crime_title);
        materialEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                builder.title(s.toString()).build();
                crime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        materialEditText.setText(crime.getTitle());

        final Button dataBtn = (Button)layoutView.findViewById(R.id.crime_data_btn);
        dataBtn.setText(crime.getDate());
        dataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showTimePickerDialog();
                showDatePickerDialog(dataBtn);
            }
        });

        CheckBox solvedStateCheckBox = (CheckBox)layoutView.findViewById(R.id.crime_solve_state);
        solvedStateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.setSolved(isChecked);
            }
        });
        solvedStateCheckBox.setChecked(crime.isSolved());

        mPhotoView = (ImageView)layoutView.findViewById(R.id.crime_camera_imageView);
        ImageButton cameraBtn = (ImageButton)layoutView.findViewById(R.id.crime_camera_btn);
        if(!checkCameraAvailable()){
            cameraBtn.setEnabled(false);
        }
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CrimeCameraActivity.class);
                startActivityForResult(intent,REQUEST_PHOTO);
//                CommentUtil.startActivityTranslate(getActivity(),CrimeCameraActivity.class);
            }
        });
    }

    //onCreate onStart onResume
    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    //onPause onStop onDestroyView onDestroy
    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.clearImageView(mPhotoView);
    }

    private void showPhoto(){
        Photo photo = crime.getPhoto();
        BitmapDrawable bitmapDrawable = null;
        if(photo != null){
            String path = getActivity().getFileStreamPath(photo.getFileName()).getAbsolutePath();
            bitmapDrawable = PictureUtils.getScaledDrawable(getActivity(),path);
        }

        mPhotoView.setBackground(bitmapDrawable);
    }

    private void showTimePickerDialog(){
        Dialog.Builder builder = new TimePickerDialog.Builder(R.style.Material_App_Dialog_TimePicker, 24, 00){
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                TimePickerDialog dialog = (TimePickerDialog)fragment.getDialog();
                Toast.makeText(getActivity(), "Time is " + dialog.getFormattedTime(SimpleDateFormat.getTimeInstance()), Toast.LENGTH_SHORT).show();
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                Toast.makeText(getActivity(), "Cancelled" , Toast.LENGTH_SHORT).show();
                super.onNegativeActionClicked(fragment);
            }
        };

        builder.positiveAction("OK")
                .negativeAction("CANCEL");
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getFragmentManager(), null);
    }

    private void showDatePickerDialog(final Button button){
        //R.style.Material_App_Dialog_DatePicker
        Dialog.Builder builder = new DatePickerDialog.Builder(){
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                DatePickerDialog dialog = (DatePickerDialog)fragment.getDialog();
                String date = dialog.getFormattedDate(DateFormat.getDateTimeInstance());
                Toast.makeText(getActivity(), "Date is " + date, Toast.LENGTH_SHORT).show();

                button.setText(date);
                crime.setDate(date);
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                Toast.makeText(getActivity(), "Cancelled" , Toast.LENGTH_SHORT).show();
                super.onNegativeActionClicked(fragment);
            }
        };

        builder.positiveAction("OK").negativeAction("CANCEL");
        DialogFragment fragment = DialogFragment.newInstance(builder);
        // 需要4.0以上才支持 getFragmentManager()
        // getActivity().getSupportFragmentManager() 兼容低版本
        fragment.show(getActivity().getSupportFragmentManager(), null);
    }

    /**
     * 系统回收内存的时候，会调用onPause的方法
     */
    @Override
    public void onPause() {
        Log.d("AAAA", "CrimeFragment 调用 onPause");
        super.onPause();
        CrimeLab.getCrimeLab(getActivity()).saveCrimes();
    }

    public boolean checkCameraAvailable(){
        PackageManager packageManager = getActivity().getPackageManager();
        boolean hasCamera;
        hasCamera = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
                packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
                Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD ||
                Camera.getNumberOfCameras()>0;
        return hasCamera;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("AAAA", "onActivityResult requestCode="+requestCode+" ,resultCode="+resultCode);
        if(resultCode!= Activity.RESULT_OK) return;

        if(requestCode == REQUEST_PHOTO && data!=null){
            String fileName = data.getStringExtra(CrimeCameraFragment.EXTAR_PHOTO_FILENAME);

            Log.d("AAAA", "onActivityResult requestCode=REQUEST_PHOTO ,data!=null  fileName="+fileName);
            if(fileName!= null){
                Photo photo = new Photo(fileName);
                crime.setPhoto(photo);

                //确保用户返回后能显示缩略图
                showPhoto();
            }
        }
    }
}
