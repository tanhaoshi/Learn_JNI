package com.yizhitong.learnjni;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yizhitong.learnjni.facedetect.FaceDetectBody;
import com.yizhitong.learnjni.permissions.EasyPermission;

import org.opencv.android.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FaceDetectActivity extends AppCompatActivity implements EasyPermission.PermissionCallback {

    private FaceDetectBody mFaceDetectBody;

    private AppCompatImageView startIdCard,disposeIdCard,idCardImage;

    private String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
    };

    private final int REQUEST_PERMISSIONS = 2;

    // 保存到本地图片 名称
    private static final String IMAGE_PATH = "/storage/emulated/0/Android/data/com.inj.ocr/cache/id_card.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detect);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        mFaceDetectBody = new FaceDetectBody();

        startIdCard   = findViewById(R.id.startIdCard);
        disposeIdCard = findViewById(R.id.disposeIdCard);
        idCardImage   = findViewById(R.id.idCardImage);
    }

    private void requestPermission(){
        if(EasyPermission.hasPermissions(FaceDetectActivity.this, permissions)){
            startCV();
        }else{
            EasyPermission.with(this)
                    .rationale("需要打开权限才能使用!")
                    .addRequestCode(REQUEST_PERMISSIONS)
                    .permissions(permissions)
                    .request();
        }
    }

    private void startCV(){
        String path = getAssetsCacheFile(this,"haarcascade_frontalface_alt2.xml");

        Log.i("FaceDetectActivity","look at current path = " + path);

        String newFileName = "/storage/emulated/0/Android/data/com.inj.ocr/cache/dispose_id_card.jpg";

        File file = new File(IMAGE_PATH);

        Bitmap headBitmap = null;

        if(file.exists()){
            headBitmap = mFaceDetectBody.disposeFaceDetect(IMAGE_PATH,newFileName,path);
        }else{
            file.getParentFile().mkdirs();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.idcard);

            FileOutputStream out = null;
            try{
                out = new FileOutputStream(file);
                if(bitmap.compress(Bitmap.CompressFormat.PNG,100,out)){
                    out.flush();
                    out.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(out != null){
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                bitmap.recycle();
            }

            headBitmap = mFaceDetectBody.disposeFaceDetect(IMAGE_PATH,newFileName,path);
        }

        Glide.with(FaceDetectActivity.this).load(IMAGE_PATH).into(startIdCard);

        Glide.with(FaceDetectActivity.this).load(newFileName).into(disposeIdCard);

        if(headBitmap != null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            headBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

            byte[] bytes=baos.toByteArray();

            Glide.with(FaceDetectActivity.this).load(bytes).into(idCardImage);
        }
    }

    public String getAssetsCacheFile(Context context, String fileName){
        File cacheFile = new File(context.getCacheDir(), fileName);
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            try {
                FileOutputStream outputStream = new FileOutputStream(cacheFile);
                try {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buf)) > 0) {
                        outputStream.write(buf, 0, len);
                    }
                } finally {
                    outputStream.close();
                }
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cacheFile.getAbsolutePath();
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermission();
    }

    @Override
    public void onPermissionGranted(int requestCode, List<String> perms) {
        startCV();
    }

    @Override
    public void onPermissionDenied(int requestCode, List<String> perms) {
        Toast.makeText(this,"需要打开权限才能使用!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermission.onRequestPermissionsResult(this,REQUEST_PERMISSIONS,permissions,grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EasyPermission.SETTINGS_REQ_CODE){
            if(EasyPermission.hasPermissions(FaceDetectActivity.this, permissions)){
                startCV();
            }else{
                EasyPermission.with(this)
                        .rationale("需要打开权限才能使用!")
                        .addRequestCode(REQUEST_PERMISSIONS)
                        .permissions(permissions)
                        .request();
            }
        }
    }
}
