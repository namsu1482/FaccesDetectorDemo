package com.example.faccesdetectordemo;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.face.FaceDetector;

public class MainActivity extends AppCompatActivity {
    Context mContext;


    // 링크 https://developers.google.com/vision/android/face-tracker-tutorial
    // https://developers.google.com/ml-kit/vision/face-detection/android
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        FaceDetector faceDetector = new FaceDetector.Builder(mContext).build();
    }

    private void initView() {

    }
}
