package com.yizhitong.learnjni;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yizhitong.learnjni.facedetect.FaceDetectBody;
import com.yizhitong.learnjni.permissions.EasyPermission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class FaceDetectActivity extends AppCompatActivity implements EasyPermission.PermissionCallback {

    private FaceDetectBody mFaceDetectBody;

    private AppCompatImageView startIdCard,disposeIdCard;

    private String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
    };

    private final int REQUEST_PERMISSIONS = 2;

    // 保存到本地图片 名称
    private static final String IMAGE_PATH = "/storage/emulated/0/Android/data/com.tomcat.ocr.idcard/cache/id_card.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detect);

        mFaceDetectBody = new FaceDetectBody();

        startIdCard   = findViewById(R.id.startIdCard);
        disposeIdCard = findViewById(R.id.disposeIdCard);

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
        String newFileName = "/storage/emulated/0/Android/data/com.tomcat.ocr.idcard/cache/dispose_id_card.jpg";

        File file = new File(IMAGE_PATH);
        if(file.exists()){
            mFaceDetectBody.disposFaceDetect(IMAGE_PATH,newFileName);
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

            mFaceDetectBody.disposFaceDetect(IMAGE_PATH,newFileName);
        }

        Glide.with(FaceDetectActivity.this).load(IMAGE_PATH).into(startIdCard);

        Glide.with(FaceDetectActivity.this).load(newFileName).into(disposeIdCard);
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermission();
    }

    @Override
    public void onPermissionGranted(int requestCode, List<String> perms) {
        //打开成功!
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
