package com.example.faccesdetectordemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.face.Contour;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceContour;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;

import java.util.List;

public class FaceAnalyzer {
    private static final String TAG = "FaceAnalyzer";
    Bitmap facesImage;

    Bitmap mappedImage;

    private FaceDetector faceDetector;

    interface mappingImageListener {
        void onComplete(Bitmap bitmap);
    }

    //https://developers.google.com/ml-kit/vision/face-detection/android?hl=ko#4.-process-the-image


    private void init() {
        FaceDetectorOptions highAccuracyOpts =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                        .build();

        faceDetector = FaceDetection.getClient(highAccuracyOpts);

    }

    public void analyze(Bitmap bitmap, final mappingImageListener mappingImageListener) {
        init();
        facesImage = bitmap;
        final InputImage inputImage = InputImage.fromBitmap(facesImage, 0);
        faceDetector.process(inputImage)
                .addOnSuccessListener(new OnSuccessListener<List<Face>>() {
                    @Override
                    public void onSuccess(@NonNull List<Face> faces) {
                        Log.i(TAG, "Face Detect Success");
                        Log.i(TAG, String.format("Face Count : %d", faces.size()));
                        mappingImageListener.onComplete(analyze(faces));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "Face Detect Failed " + e);

                    }
                });

    }

    private Bitmap analyze(List<Face> faces) {
        mappedImage = facesImage.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mappedImage);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#99ff0000"));

        int faceNo = 0;
        for (Face face : faces) {
            List<FaceContour> contours = face.getAllContours();
            Log.i(TAG, String.format("Face [%d] contour count : %d", faceNo, contours.size()));

            for (FaceContour contour : contours) {
                Path path = new Path();
                path.moveTo(contour.getPoints().get(0).x, contour.getPoints().get(0).y);
                for (PointF pointF : contour.getPoints()) {
                    Log.i(TAG, String.format("[face %d] contour point: %s", faceNo, pointF));
                    path.lineTo(pointF.x, pointF.y);
                    canvas.drawPath(path, paint);
                }
            }

//            Rect bounds = face.getBoundingBox();
//            float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
//            float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees
//
//            // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
//            // nose available):
//            FaceLandmark leftEar = face.getLandmark(FaceLandmark.LEFT_EAR);
//            if (leftEar != null) {
//                PointF leftEarPos = leftEar.getPosition();
//            }
//
//            // If contour detection was enabled:
//            List<PointF> leftEyeContour =
//                    face.getContour(FaceContour.LEFT_EYE).getPoints();
//            List<PointF> upperLipBottomContour =
//                    face.getContour(FaceContour.UPPER_LIP_BOTTOM).getPoints();
//
//            // If classification was enabled:
//            if (face.getSmilingProbability() != null) {
//                float smileProb = face.getSmilingProbability();
//            }
//            if (face.getRightEyeOpenProbability() != null) {
//                float rightEyeOpenProb = face.getRightEyeOpenProbability();
//            }
//
//            // If face tracking was enabled:
//            if (face.getTrackingId() != null) {
//                int id = face.getTrackingId();
//            }
            faceNo++;
        }
        return mappedImage;
    }

    private void mapContoursToBitmap() {


    }

    public Bitmap getMappedImage() {
        return mappedImage;
    }

}
