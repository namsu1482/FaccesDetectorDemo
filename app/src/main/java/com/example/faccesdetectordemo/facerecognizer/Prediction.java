package com.example.faccesdetectordemo.facerecognizer;

import android.graphics.Rect;

public class Prediction {
    Rect bbox;
    String label;

    public Prediction(Rect bbox, String label) {
        this.bbox = bbox;
        this.label = label;
    }
}
