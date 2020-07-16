package com.ym.idcard.reg;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ym.idcard.reg.layout.ViewfinderView;
import com.ym.idcard.reg.permissions.EasyPermission;

import java.security.Policy;
import java.util.List;

public class IdCardActivity extends AppCompatActivity implements SurfaceHolder.Callback, Camera.PreviewCallback, EasyPermission.PermissionCallback {

    public int srcHeight;
    public int srcWidth;

    private int width;
    private int height;

    private FrameLayout    frameLayout;
    private ViewfinderView mView;
    private SurfaceView    surfaceView;
    private SurfaceHolder surfaceHolder;

    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_id_card);

        requestPermission();

        frameLayout = (FrameLayout)findViewById(R.id.frameLayout);
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);

        this.surfaceHolder = this.surfaceView.getHolder();
        this.surfaceHolder.addCallback(this);
        this.surfaceHolder.setType(3);

        setScreenSize(this);
        findView();
    }

    private void findView() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        this.width = metric.widthPixels;
        this.height = metric.heightPixels;
    }

    @SuppressLint({"NewApi"})
    private void setScreenSize(Context context) {
        int x;
        int y;
        Display display = ((WindowManager) context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= 13) {
            Point screenSize = new Point();
            if (Build.VERSION.SDK_INT >= 17) {
                display.getRealSize(screenSize);
                x = screenSize.x;
                y = screenSize.y;
            } else {
                display.getSize(screenSize);
                x = screenSize.x;
                y = screenSize.y;
            }
        } else {
            x = display.getWidth();
            y = display.getHeight();
        }
        this.srcWidth = x;
        this.srcHeight = y;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (this.camera == null) {
            try {
                this.camera = Camera.open();
                this.camera.setPreviewDisplay(holder);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "无法启用相机", Toast.LENGTH_SHORT).show();
                return;
            }

            initCamera();

            this.camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {

                }
            });
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onPermissionGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionDenied(int requestCode, List<String> perms) {

    }

    private String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE
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

    private void initCamera(){
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);

        parameters.setPreviewSize(this.width,this.height);

        this.camera.setParameters(parameters);
        this.camera.startPreview();

        mView = new ViewfinderView(this,width,height);
        frameLayout.addView(mView);
    }
}
