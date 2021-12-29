package com.example.faccesdetectordemo.facerecognizer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class FaceRecognizer {
    private static final String TAG = "FaceRecognizer";
    Context context;

    private Interpreter interpreter;

    public FaceRecognizer(Context context) throws IOException {
        this.context = context;
        init();
    }

    //default height,width
    private int imageSize = 160;

    private int embeddingDim = 128;

    private ImageProcessor imageProcessor = new ImageProcessor.Builder()
            .add(new ResizeOp(imageSize, imageSize, ResizeOp.ResizeMethod.BILINEAR))
            .add(new NormalizeOp(127.5f, 127.5f))
            .build();

    private void init() throws IOException {
        Interpreter.Options options = new Interpreter.Options().setNumThreads(4);
        interpreter = new Interpreter(FileUtil.loadMappedFile(context, "facenet_int8.tflite"), options);
    }

    public float[] getFaceEmbedding(Bitmap image, Rect crop, Boolean preRotate, Boolean isRearCameraOn) {
        return runFaceNet(
                convertBitmapToBuffer(
                        cropRectFromBitmap(image, crop, preRotate, isRearCameraOn)
                )
        ).get(0);
    }

    private ByteBuffer convertBitmapToBuffer(Bitmap image) {
        return imageProcessor.process(TensorImage.fromBitmap(image)).getBuffer();
    }


    // Crop the given bitmap with the given rect.
    private Bitmap cropRectFromBitmap(Bitmap source, Rect rect, Boolean preRotate, Boolean isRearCameraOn) {
        int width = rect.width();
        int height = rect.height();
        if ((rect.left + width) > source.getWidth()) {
            width = source.getWidth() - rect.left;
        }
        if ((rect.top + height) > source.getHeight()) {
            height = source.getHeight() - rect.top;
        }
        Bitmap croppedBitmap;
        if (preRotate) {
            croppedBitmap = Bitmap.createBitmap(rotateBitmap(source, -90f));
        } else {
            croppedBitmap = Bitmap.createBitmap(source, rect.left, rect.top, width, height);
        }

        // Add a 180 degrees rotation if the rear camera is on.
        if (isRearCameraOn) {
            croppedBitmap = rotateBitmap(croppedBitmap, 180f);
        }

        // Uncomment the below line if you want to save the input image.
        // Make sure the app has the `WRITE_EXTERNAL_STORAGE` permission.
        //saveBitmap( croppedBitmap , "image")

        return croppedBitmap;
    }

    private Bitmap rotateBitmap(Bitmap source, Float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
    }

    // Run the FaceNet model.
    private ArrayList<float[]> runFaceNet(Object inputs) {
        long t1 = System.currentTimeMillis();
        ArrayList<float[]> outputs = new ArrayList<>(1);
        outputs.add(new float[embeddingDim]);
        interpreter.run(inputs, outputs);
        Log.i(TAG, "FaceNet Inference Speed in ms : ${System.currentTimeMillis() - t1}");
        return outputs;
    }
}


