package com.example.faccesdetectordemo;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CustomTask {

    OnCustomTaskListener onCustomTaskListener;

    public interface OnCustomTaskListener {
        void onExecutorComplete();

        void onHandlerComplete();
    }

    public CustomTask(final OnCustomTaskListener onCustomTaskListener) {
        this.onCustomTaskListener = onCustomTaskListener;
        Executor executor = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.myLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // background 처리
                onCustomTaskListener.onExecutorComplete();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // UI 처리
                        onCustomTaskListener.onHandlerComplete();
                    }
                });
            }
        });
    }


}
