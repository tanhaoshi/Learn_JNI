package com.ym.idcard.reg;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.ym.idcard.reg.engine.OcrEngine;
import com.ym.idcard.reg.layout.ViewfinderView;
import com.ym.idcard.reg.permissions.EasyPermission;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class IdCardActivity extends AppCompatActivity implements EasyPermission.PermissionCallback, ViewTreeObserver.OnGlobalLayoutListener {

    public static final String TAG = "IdCardActivity";

    private int width;
    private int height;

    private FrameLayout frameLayout;
    private ViewfinderView mView;
    private SurfaceView    surfaceView;

    private CameraHelper cameraHelper;
    private Integer rgbCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

    OcrEngine ocrEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_id_card);

        requestPermission();

        findView();

        frameLayout = (FrameLayout)findViewById(R.id.frameLayout);
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);

        ocrEngine = new OcrEngine();
    }

    @Override
    protected void onResume() {
        super.onResume();
        surfaceView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    private void findView() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        this.width = metric.widthPixels;
        this.height = metric.heightPixels;
    }

    @Override
    public void onPermissionGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionDenied(int requestCode, List<String> perms) {

    }

    private String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private final int REQUEST_PERMISSIONS = 2;

    private void requestPermission(){
        if(!EasyPermission.hasPermissions(this, permissions)){
            EasyPermission.with(this)
                    .rationale("否则无法打开相机")
                    .addRequestCode(REQUEST_PERMISSIONS)
                    .permissions(permissions)
                    .request();
        }
    }

    private void initCamera() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        CameraListener cameraListener = new CameraListener() {
            @Override
            public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {
            }

            @Override
            public void onPreview(byte[] nv21, Camera camera) {
                    /*身份证扫描核心代码*/
                //这里代码肯定会执行的很快 如何控制速度呢
                IDCard idCard = ocrEngine.recognize(IdCardActivity.this, 2, saveCurrentPreView(nv21), null);
                Log.d("hezd","idcard info:"+idCard.toString());
            }

            @Override
            public void onCameraClosed(){
                Log.i(TAG, "onCameraClosed: ");
            }

            @Override
            public void onCameraError(Exception e) {
                Log.i(TAG, "onCameraError: " + e.getMessage());
            }

            @Override
            public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {
                Log.i(TAG, "onCameraConfigurationChanged: " + cameraID + "  " + displayOrientation);
            }
        };

        this.mView = new ViewfinderView(this, this.width, this.height);
        this.frameLayout.addView(this.mView);

        cameraHelper = new CameraHelper.Builder()
                .previewViewSize(new Point(surfaceView.getMeasuredWidth(), surfaceView.getMeasuredHeight()))
                .rotation(getWindowManager().getDefaultDisplay().getRotation())
                .specificCameraId(rgbCameraId != null ? rgbCameraId : Camera.CameraInfo.CAMERA_FACING_FRONT)
                .isMirror(false)
                .previewOn(surfaceView)
                .cameraListener(cameraListener)
                .build();
        cameraHelper.init();
        cameraHelper.start();
    }

    private byte[] saveCurrentPreView(byte[] data){
        Camera.Size previewSize = cameraHelper.previewSize;
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        YuvImage yuvimage = new YuvImage(
                data,
                ImageFormat.NV21,
                previewSize.width,
                previewSize.height,
                null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 100, baos);// 80--JPG图片的质量[0-100],100最高
        byte[] rawImage = baos.toByteArray();
        return rawImage;
    }


    @Override
    public void onGlobalLayout() {
        surfaceView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        initCamera();
    }

}
