package com.example.faccesdetectordemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

public class MainActivity extends AppCompatActivity {
    Context mContext;
    Bitmap bitmap;

    TextView mTvFaceCount;
    ImageView mImageFaces;

    // 링크 https://developers.google.com/vision/android/face-tracker-tutorial
    // https://developers.google.com/ml-kit/vision/face-detection/android
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();


        Glide.with(mContext)
                .asBitmap()
                .load("https://museum.wa.gov.au/sites/default/files/imagecache/wam_v2_page_full/new-museum-wafaces-banner-v2.jpg")
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        bitmap = resource;
                        mImageFaces.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                })
        ;

    }

    private void initView() {
        mImageFaces = findViewById(R.id.image_faces);
        mTvFaceCount = findViewById(R.id.tv_face_count);

    }

    private void initFaceDetector() {
        // High-accuracy landmark detection and face classification
        FaceDetectorOptions highAccuracyOpts =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .build();

        FaceDetector faceDetector = FaceDetection.getClient(highAccuracyOpts);

        InputImage inputImage = InputImage.fromBitmap(bitmap,0);

    }
}
