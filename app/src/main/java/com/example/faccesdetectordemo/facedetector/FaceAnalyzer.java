package com.example.faccesdetectordemo.facedetector;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;

import java.util.List;

public class FaceAnalyzer {
    //TODO
    /*
    이미지에서 사람 인식 분류 -> faceAnalyzer 사용
    만약 인식이 안될시 초 해상도 이미지 작업

    tensorFlow 참조 필요
     */
    private static final String TAG = "FaceAnalyzer";
    Bitmap facesImage;

    Bitmap mappedImage;
    List<Face> list;

    private FaceDetector faceDetector;

    public interface mappingImageListener {
        void onComplete();
    }
    //https://developers.google.com/ml-kit/vision/face-detection/android?hl=ko#4.-process-the-image

    private void init() {
        FaceDetectorOptions highAccuracyOpts =
                new FaceDetectorOptions.Builder()
                        // performance setting (default:Fast)
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        // landmark recognize(ex. nose,ear,cheek,mouse)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        //
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
                        list = faces;
                        Log.i(TAG, String.format("Face Count : %d", faces.size()));
                        mappingImageListener.onComplete();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "Face Detect Failed " + e);

                    }
                });
    }

    // 인식된 이미지에 point mapping
    private Bitmap analyzeFaces(List<Face> faces) {
        mappedImage = facesImage.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mappedImage);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#99ff0000"));

        int faceNo = 0;
        for (Face face : faces) {
            List<FaceLandmark> landmarks = face.getAllLandmarks();
            Path path = new Path();
            path.moveTo(face.getLandmark(0).getPosition().x, face.getLandmark(0).getPosition().y);
            for (FaceLandmark faceLandmark : landmarks) {
                path.lineTo(faceLandmark.getPosition().x, faceLandmark.getPosition().y);
                Log.i(TAG, String.format("Face Count [%d] landmark pointF [ x:%s y:%s]", faceNo, faceLandmark.getPosition().x, faceLandmark.getPosition().y));
                canvas.drawPath(path, paint);
            }
//            List<FaceContour> contours = face.getAllContours();
//            Log.i(TAG, String.format("Face [%d] landmark count : %d", faceNo, face.getAllLandmarks().size()));

//            for (FaceContour contour : contours) {
//                Path path = new Path();
//                path.moveTo(contour.getPoints().get(0).x, contour.getPoints().get(0).y);
//                for (PointF pointF : contour.getPoints()) {
//                    Log.i(TAG, String.format("[face %d] contour point: %s", faceNo, pointF));
//                    path.lineTo(pointF.x, pointF.y);
//                    canvas.drawPath(path, paint);
//                }
//            }

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

    public Bitmap getMappedImage() {
        analyzeFaces(list);
        return mappedImage;
    }
}
