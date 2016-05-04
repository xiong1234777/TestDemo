package com.xiong.mdapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.xiong.mdapplication.bean.ImageInfo;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Xiong on 2016/5/4.
 */
public class PhotoService extends Service {
    private final IBinder mBinder = new MyBinder();
    private UploadListener mListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void upload(UploadListener listener, ArrayList<ImageInfo> images) {
        mListener = listener;
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        for (Parcelable image : images) {
            threadPool.execute(new PhotoTask((ImageInfo) image));
        }

    }

    public interface UploadListener {
        void onUpload(int position, int progress);

        void onFinish(int position);
    }

    public class MyBinder extends Binder {
        public PhotoService getService() {
            return PhotoService.this;
        }
    }

    class PhotoTask extends Thread {
        private ImageInfo info;

        public PhotoTask(ImageInfo info) {
            this.info = info;
        }

        @Override
        public void run() {

            while (info.progress < 100000) {
                info.progress+=20;
//                info.progress = info.progress > 100 ? 100 : info.progress;
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                    if (mListener != null) {
                        mListener.onUpload(info.position, info.progress);
                    }
            }
            mListener.onFinish(info.position);
//            info.progress += Math.random() * 20
        }
    }
}
