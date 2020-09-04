package com.yizhitong.learnjni;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageProcessActivity extends AppCompatActivity {
    
    private static final String TAG = "ImageProcessActivity";

    ImageView startIdCard,resizeIdCard,grayIdCard,thresholdIdCard,erodeIdCard,divisionIdCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_process);

        initView();
    }

    private void initView(){
        startIdCard  = findViewById(R.id.startIdCard);
        resizeIdCard = findViewById(R.id.resizeIdCard);
        grayIdCard   = findViewById(R.id.grayIdCard);
        thresholdIdCard = findViewById(R.id.thresholdIdCard);
        erodeIdCard  = findViewById(R.id.erodeIdCard);
        divisionIdCard = findViewById(R.id.divisionIdCard);

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mBaseLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mBaseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    private BaseLoaderCallback mBaseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            super.onManagerConnected(status);
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    initData();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }

        @Override
        public void onPackageInstall(int operation, InstallCallbackInterface callback) {
            super.onPackageInstall(operation, callback);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Glide.with(this).load(R.drawable.idcard).into(startIdCard);
    }

    private void initData(){
        Mat newMat = null;
        try {
            newMat = Utils.loadResource(this,R.drawable.idcard);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(newMat == null){
            return;
        }

        Log.i(TAG,"The new mat is not null ... ");

        Size size     = new Size(640,400);
        Mat  imageMat = new Mat();
        // 归一化
        Imgproc.resize(newMat,imageMat,size);

        if(imageMat != null){
            Log.i(TAG,"The resize mat is success ... ");

            Bitmap bitmap = Bitmap.createBitmap(imageMat.cols(),imageMat.rows(),Bitmap.Config.ARGB_8888);

            Utils.matToBitmap(imageMat,bitmap);

            if(bitmap != null){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bytes=baos.toByteArray();

                Glide.with(this).load(bytes).into(resizeIdCard);
            }
        }

        //灰度化
        Mat disposeMat = new Mat();
        Imgproc.cvtColor(imageMat,disposeMat,Imgproc.COLOR_RGB2GRAY);

        if(disposeMat != null){
            Log.i(TAG,"The cvtColor mat is success ... ");

            Bitmap bitmap = Bitmap.createBitmap(disposeMat.cols(),disposeMat.rows(),Bitmap.Config.ARGB_8888);

            Utils.matToBitmap(disposeMat,bitmap);

            if(bitmap != null){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bytes=baos.toByteArray();

                Glide.with(this).load(bytes).into(grayIdCard);
            }
        }

        //二值化
        Imgproc.threshold(disposeMat,disposeMat,100,255,Imgproc.THRESH_BINARY);

        if(disposeMat != null){
            Log.i(TAG,"The threshold mat is success ... ");

            Bitmap bitmap = Bitmap.createBitmap(disposeMat.cols(),disposeMat.rows(),Bitmap.Config.ARGB_8888);

            Utils.matToBitmap(disposeMat,bitmap);

            if(bitmap != null){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bytes=baos.toByteArray();

                Glide.with(this).load(bytes).into(thresholdIdCard);
            }
        }

        //膨胀
        Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(20,10));
        Imgproc.erode(disposeMat,disposeMat,erodeElement);

        if(disposeMat != null){
            Log.i(TAG,"The Structuring mat is success ... ");

            Bitmap bitmap = Bitmap.createBitmap(disposeMat.cols(),disposeMat.rows(),Bitmap.Config.ARGB_8888);

            Utils.matToBitmap(disposeMat,bitmap);

            if(bitmap != null){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bytes=baos.toByteArray();

                Glide.with(this).load(bytes).into(erodeIdCard);
            }
        }

        //轮廓检测
        List<MatOfPoint> contours = new ArrayList<>();
        Mat mat = new Mat();
        Imgproc.findContours(disposeMat,contours,mat,Imgproc.RETR_TREE,Imgproc.CHAIN_APPROX_SIMPLE,new Point(0,0));

        Log.i(TAG,"look at current list size = " + contours.size());

        if(contours.size() > 0){
            for(int i=0; i<contours.size(); i++){
                Rect rect = Imgproc.boundingRect(contours.get(i));
                Log.i(TAG,"rect x = " + rect.x + " -- rect y = " + rect.y + " -- rect width = " + rect.width + " -- rect height = " + rect.height);
            }
        }
    }
}

