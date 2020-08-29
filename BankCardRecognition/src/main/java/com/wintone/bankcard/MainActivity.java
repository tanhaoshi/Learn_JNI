package com.wintone.bankcard;

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
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.wintone.bankcard.camera.CameraHelper;
import com.wintone.bankcard.camera.CameraListener;
import com.wintone.bankcard.layout.ViewfinderView;
import com.wintone.bankcard.permissions.EasyPermission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EasyPermission.PermissionCallback,ViewTreeObserver.OnGlobalLayoutListener
, SurfaceHolder.Callback ,Camera.PreviewCallback{

    public static final String TAG = "MainActivity";

    private static double NORMAL_CARD_SCALE = 1.58577d;
    private boolean isFatty = false;

    private FrameLayout frameLayout;
    private ViewfinderView mView;
    private SurfaceView surfaceView;

    private SurfaceHolder mSurfaceHolder;

    List<Camera.Size> list;
    Camera mCamera;

    private int preHeight = 0;
    private int preWidth = 0;

    public int srcHeight;
    public int srcWidth;

    private int width;
    private int height;

    private boolean isShowBorder = false;

    private BankCardAPI mBankCardAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        requestPermission();

        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);

        this.width = metric.widthPixels;
        this.height = metric.heightPixels;

//        setScreenSize(this);

        surfaceView.getViewTreeObserver().addOnGlobalLayoutListener(this);
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

    private static final String[] LIBRARIES = new String[]{
            "libAndroidBankCard.so"
    };

    /**
     * 检查能否找到动态链接库，如果找不到，请修改工程配置
     *
     * @param libraries 需要的动态链接库
     * @return 动态库是否存在
     */
    private boolean checkSoFile(String[] libraries) {
        File dir = new File(getApplicationInfo().nativeLibraryDir);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return false;
        }
        List<String> libraryNameList = new ArrayList<>();
        for (File file : files) {
            libraryNameList.add(file.getName());
        }
        boolean exists = true;
        for (String library : libraries) {
            exists &= libraryNameList.contains(library);
        }
        return exists;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(checkSoFile(LIBRARIES)){
            mBankCardAPI = new BankCardAPI();
            mBankCardAPI.WTInitCardKernal("",0);
        }else{
            throw new IllegalArgumentException(" not't find so file ...");
        }
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

    private void requestPermission() {
        if (!EasyPermission.hasPermissions(this, permissions)) {
            EasyPermission.with(this)
                    .rationale("否则无法打开相机")
                    .addRequestCode(REQUEST_PERMISSIONS)
                    .permissions(permissions)
                    .request();
        }
    }

    @Override
    public void onGlobalLayout() {
        surfaceView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        mSurfaceHolder = this.surfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(3);

        mCamera = Camera.open();

        Camera.Parameters parameters = mCamera.getParameters();
        getCameraPreParameters(mCamera);

        int t = this.height / 10;
        int b = this.height - t;
        int l = (this.width - ((int) (((double) (b - t)) * NORMAL_CARD_SCALE))) / 2;
        int r = this.width - l;
        l += 30;
        t += 19;
        r -= 30;
        b -= 19;
        if (this.isFatty) {
            t = this.height / 5;
            b = this.height - t;
            l = (this.width - ((int) (((double) (b - t)) * NORMAL_CARD_SCALE))) / 2;
            r = this.width - l;
        }
        double proportion = ((double) this.width) / ((double) this.preWidth);
        l = (int) (((double) l) / proportion);
        t = (int) (((double) t) / proportion);
        r = (int) (((double) r) / proportion);
        b = (int) (((double) b) / proportion);
        Log.i(TAG,"look at l = " + l);
        Log.i(TAG,"look at t = " + t);
        Log.i(TAG,"look at r = " + r);
        Log.i(TAG,"look at b = " + b);
        Log.i(TAG,"look at preWidth = " + preWidth);
        Log.i(TAG,"look at preHeight = " + preHeight);
        this.mBankCardAPI.WTSetROI(new int[]{l, t, r, b}, this.preWidth, this.preHeight);

        this.mView = new ViewfinderView(this, this.width, this.height);
        this.frameLayout.addView(this.mView);

        parameters.setPreviewFormat(ImageFormat.NV21);

        parameters.setPreviewSize(preWidth, preHeight);
        //对焦模式设置
        List<String> supportedFocusModes = parameters.getSupportedFocusModes();
        if (supportedFocusModes != null && supportedFocusModes.size() > 0) {
            if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            } else if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            } else if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
        }
        mCamera.setParameters(parameters);
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.setPreviewCallback(this);
        mCamera.startPreview();
    }

    public void getCameraPreParameters(Camera camera) {
        this.isShowBorder = false;
        if ("PLK-TL01H".equals(Build.MODEL)) {
            this.preWidth = 1920;
            this.preHeight = 1080;
        } else if ("MI 3".equals(Build.MODEL)) {
            this.preWidth = 1024;
            this.preHeight = 576;
        } else {
            this.list = camera.getParameters().getSupportedPreviewSizes();

            float ratioScreen = ((float) this.srcWidth) / ((float) this.srcHeight);

            int i = 0;

            while (i < this.list.size()) {
                Log.i(TAG,"look at camera preview width = " + (list.get(i)).width);
                Log.i(TAG,"look at camera preview height = " + (list.get(i)).height);
                if (ratioScreen == ((float) (this.list.get(i)).width) / ((float) (this.list.get(i)).height)
                        && ((this.list.get(i)).width >= 1280 || (this.list.get(i)).height >= 720)) {
                    if (this.preWidth == 0 && this.preHeight == 0) {
                        Log.i(TAG,"look at preview count 1 = " + i);
                        this.preWidth = (this.list.get(i)).width;

                        this.preHeight = (this.list.get(i)).height;
                    }
                    if ((this.list.get(0)).width > (this.list.get(this.list.size() - 1)).width) {
                        Log.i(TAG,"look at preview count 2 = " + i);
                        if (this.preWidth > (this.list.get(i)).width || this.preHeight > (this.list.get(i)).height) {
                            Log.i(TAG,"look at preview count 3 = " + i);
                            this.preWidth = (this.list.get(i)).width;
                            this.preHeight = (this.list.get(i)).height;
                        }
                    } else if ((this.preWidth < (this.list.get(i)).width || this.preHeight < (this.list.get(i)).height) && this.preWidth < 1280 && this.preHeight < 720) {
                        Log.i(TAG,"look at preview count 4 = " + i);
                        this.preWidth = (this.list.get(i)).width;
                        this.preHeight = (this.list.get(i)).height;
                    }
                }
                i++;
            }

            if (this.preWidth == 0 || this.preHeight == 0) {
                Log.i(TAG,"look at preview count 5 ");
                this.isShowBorder = true;
                this.preWidth = (this.list.get(0)).width;
                this.preHeight = (this.list.get(0)).height;
                i = 0;
                while (i < this.list.size()) {
                    if ((this.list.get(0)).width > (this.list.get(this.list.size() - 1)).width) {
                        if ((this.preWidth >= (this.list.get(i)).width || this.preHeight >= (this.list.get(i)).height) && (this.list.get(i)).width >= 1280) {
                            this.preWidth = (this.list.get(i)).width;
                            this.preHeight = (this.list.get(i)).height;
                        }
                    } else if ((this.preWidth <= (this.list.get(i)).width || this.preHeight <= (this.list.get(i)).height) && this.preWidth < 1280 && this.preHeight < 720 && (this.list.get(i)).width >= 1280) {
                        this.preWidth = (this.list.get(i)).width;
                        this.preHeight = (this.list.get(i)).height;
                    }
                    i++;
                }
            }
            if (this.preWidth == 0 || this.preHeight == 0) {
                Log.i(TAG,"look at preview count 6 ");
                this.isShowBorder = true;
                if ((this.list.get(0)).width > (this.list.get(this.list.size() - 1)).width) {
                    this.preWidth = (this.list.get(0)).width;
                    this.preHeight = (this.list.get(0)).height;
                } else {
                    this.preWidth = (this.list.get(this.list.size() - 1)).width;
                    this.preHeight = (this.list.get(this.list.size() - 1)).height;
                }
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(mCamera != null){
            try{
                mCamera.setPreviewDisplay(holder);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private int counter = 0;

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        int[] isBorders = new int[4];
        counter++;
        if (counter == 2) {
            counter = 0;
            char[] recognition = new char[30];
            int[] bRotated = new int[1];
            int[] pLineWarp = new int[32000];

            int result = mBankCardAPI.RecognizeNV21(data, 1280, 720, isBorders, recognition, 30, bRotated, pLineWarp);

            if (result == 0) {
                camera.stopPreview();
                mBankCardAPI.WTUnInitCardKernal();
                String bankCard = String.valueOf(recognition);
                Log.i(TAG,"recognition bank info card of = " + bankCard);
                camera.setPreviewCallback(null);
            }
        }
    }
}
