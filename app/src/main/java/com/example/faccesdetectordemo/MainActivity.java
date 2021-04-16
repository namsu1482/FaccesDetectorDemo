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

public class MainActivity extends AppCompatActivity {
    Context mContext;
    Bitmap bitmap;

    TextView mTvFaceCount;
    ImageView mImageFaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();


        Glide.with(mContext)
                .asBitmap()
                .load("https://cdn.vox-cdn.com/thumbor/CMJs1AJyAmf27RUd2UI5WBSZpy4=/0x0:3049x2048/920x613/filters:focal(1333x1562:1819x2048):format(webp)/cdn.vox-cdn.com/uploads/chorus_image/image/63058104/fake_ai_faces.0.png")
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        bitmap = resource;
                        mImageFaces.setImageBitmap(bitmap);

                        FaceAnalyzer faceAnalyzer = new FaceAnalyzer();
                        faceAnalyzer.analyze(bitmap, new FaceAnalyzer.mappingImageListener() {
                            @Override
                            public void onComplete(Bitmap bitmap) {
                                mImageFaces.setImageBitmap(bitmap);
                            }
                        });
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

}
