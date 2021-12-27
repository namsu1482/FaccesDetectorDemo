package com.example.faccesdetectordemo.facerecognizer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class BoundingBoxOverlay extends SurfaceView implements SurfaceHolder.Callback {
    Context context;
    AttributeSet attributeSet;
    private DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

    private int dpHeight = displayMetrics.heightPixels;
    private int dpWidth = displayMetrics.widthPixels;

    // Our boxes will be predicted on a 640 * 480 image. So, we need to scale the boxes to the device screen's width and
    // height
    private float xfactor = dpWidth / 480f;
    private float yfactor = dpHeight / 640f;

    ArrayList<Prediction> faceBoundingBoxes = null;

    boolean addPostScaleTransform = false;

    // Create a Matrix for scaling the bbox coordinates ( for REAR camera )
    private Matrix output2OverlayTransformRearLens = new Matrix();

    {
        output2OverlayTransformRearLens.preScale(xfactor, yfactor);

    }

    private Matrix output2OverlayTransformFrontLens = new Matrix();

    private void setOutput2OverlayTransformFrontLens() {
        output2OverlayTransformFrontLens.preScale(xfactor, yfactor);
//        postScale(-1f, 1f, dpWidth / 2f, dpHeight / 2f);
    }

    public BoundingBoxOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private Paint textPaint = new Paint();

    private void setTextPaint() {
        textPaint.setStrokeWidth(2.0f);
        textPaint.setTextSize(32f);
        textPaint.setColor(Color.WHITE);
    }

    private Paint boxPaint = new Paint();

    private void setBoxPaint() {
        boxPaint.setColor(Color.parseColor("#4D90caf9"));
        boxPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (faceBoundingBoxes != null) {
            for (Prediction face : faceBoundingBoxes) {
                RectF processedBbox = processBBox(face.bbox);
                // Draw boxes and text
                canvas.drawRoundRect(processedBbox, 16f, 16f, boxPaint);
                canvas.drawText(face.label, processedBbox.centerX(), processedBbox.centerY(), textPaint);
                Log.e("Info", String.format("Rect received %s", processedBbox.toShortString()));


            }
        }
    }

    private RectF processBBox(Rect bbox) {
        RectF rectf = new RectF(bbox);
        // Add suitable Matrix transform
        if (addPostScaleTransform) {
            output2OverlayTransformFrontLens.mapRect(rectf);
        } else {
            output2OverlayTransformRearLens.mapRect(rectf);
        }

        return rectf;
    }

}
