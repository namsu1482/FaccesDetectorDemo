package com.example.faccesdetectordemo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import com.example.faccesdetectordemo.R;

import java.io.File;

public class IntroActivity extends AppCompatActivity {
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        mContext = this;

    }

    private void initView() {

    }

    // 얼굴인식 대상 이미지를 저장할 디렉토리 생성
    private void makeImgDir(String name) {
        String imageDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/images";
        File imagesDir = new File(imageDirPath);
        if (!imagesDir.exists()) {
            imagesDir.mkdirs();
        }

        File userImagesDir = new File(imageDirPath + "/" + name);
        if (!userImagesDir.exists()) {
            userImagesDir.mkdirs();
        }
    }

}
