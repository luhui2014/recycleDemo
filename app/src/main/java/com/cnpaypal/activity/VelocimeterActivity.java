package com.cnpaypal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;

import com.cnpaypal.home.R;
import com.github.glomadrian.velocimeterlibrary.VelocimeterView;

/**
 * Created by Administrator on 2015/6/24.
 */
public class VelocimeterActivity extends Activity{
    private SeekBar seek;
    private VelocimeterView velocimeter;
    private VelocimeterView velocimeter2;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_velocimeter_layout);
        seek = (SeekBar) findViewById(R.id.seek);
        seek.setOnSeekBarChangeListener(new SeekListener());
        velocimeter = (VelocimeterView) findViewById(R.id.velocimeter);
        velocimeter2 = (VelocimeterView) findViewById(R.id.velocimeter2);
    }

    private class SeekListener implements SeekBar.OnSeekBarChangeListener {

        @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            velocimeter.setValue(progress, true);
            velocimeter2.setValue(progress, true);
        }

        @Override public void onStartTrackingTouch(SeekBar seekBar) {
            //Empty
        }

        @Override public void onStopTrackingTouch(SeekBar seekBar) {
            //Empty
        }
    }



}
