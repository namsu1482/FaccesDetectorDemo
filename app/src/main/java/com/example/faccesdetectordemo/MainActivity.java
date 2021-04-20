package com.example.faccesdetectordemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.button.MaterialButton;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private static final int REQ_PICK_IMG = 201;
    Context mContext;
    Bitmap bitmap;

    TextView mTvFaceCount;
    ImageView mImageFaces;

    private MaterialButton mBtnGetImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();

//        Glide.with(mContext)
//                .asBitmap()
//                .load("https://cdn.vox-cdn.com/thumbor/CMJs1AJyAmf27RUd2UI5WBSZpy4=/0x0:3049x2048/920x613/filters:focal(1333x1562:1819x2048):format(webp)/cdn.vox-cdn.com/uploads/chorus_image/image/63058104/fake_ai_faces.0.png")
//                .into(new CustomTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        bitmap = resource;
//                        mImageFaces.setImageBitmap(bitmap);
//
//                        final FaceAnalyzer faceAnalyzer = new FaceAnalyzer();
//                        faceAnalyzer.analyze(bitmap, new FaceAnalyzer.mappingImageListener() {
//                            @Override
//                            public void onComplete() {
//                                bitmap = faceAnalyzer.getMappedImage();
//                                mImageFaces.setImageBitmap(bitmap);
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//                    }
//                });

    }

    private void initView() {
        mImageFaces = findViewById(R.id.image_faces);
        mTvFaceCount = findViewById(R.id.tv_face_count);
        mBtnGetImage = findViewById(R.id.btn_get_image);
        mBtnGetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

    }

    private void getImage() {
        Intent imageIntent = new Intent(Intent.ACTION_PICK);
        imageIntent.setType("image/*");
        startActivityForResult(imageIntent, REQ_PICK_IMG);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_PICK_IMG) {
            try {
                Uri imageUri = data.getData();
                InputStream imageStream = null;
                imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                mImageFaces.setImageBitmap(selectedImage);
                bitmap = selectedImage;

                final FaceAnalyzer faceAnalyzer = new FaceAnalyzer();
                faceAnalyzer.analyze(bitmap, new FaceAnalyzer.mappingImageListener() {
                    @Override
                    public void onComplete() {
                        bitmap = faceAnalyzer.getMappedImage();
                        mImageFaces.setImageBitmap(bitmap);
                    }
                });

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
