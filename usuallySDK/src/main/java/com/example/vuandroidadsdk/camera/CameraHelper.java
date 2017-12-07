package com.example.vuandroidadsdk.camera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by CKZ on 2017/7/25.
 */

public class CameraHelper {
    public static final String PHOTO_FILE_NAME = "fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE = 200;
    public static final int ALBUM_REQUEST_CODE = 100;
    public static final int OPEN_MYCAMERA = 300;
    public static final int CLIP_PIC = 400;

    private Activity activity;
    public CameraHelper (Activity activity){
        this.activity = activity;
    }
    private static CameraHelper helper;
    public static CameraHelper getInstance(Activity activity){
        if (helper == null){
            helper = new CameraHelper(activity);
        }
        return helper;
    }

    /**
     * 打开相册
     */
    public void openAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, ALBUM_REQUEST_CODE);
    }


    /**
     * 打开相机
     * @return
     */

    /*
       <provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="${applicationId}.provider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/provider_paths" />
        </provider>
     */
    public Uri openCamera() {
        File dir = new File(Environment.getExternalStorageDirectory(),"images");
        if(dir.exists()){
            dir.mkdirs();
        }
        File file = new File(dir, System.currentTimeMillis()+".jpg");
//        File file = new File(dir,PHOTO_FILE_NAME);


        Uri photoURI = null;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            photoURI = FileProvider.getUriForFile(activity,
                    activity.getPackageName()+".provider",
                    file);
            // 申请临时访问权限
            intent.setFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

        }else {
            //  intent.setDataAndType(getImageContentUri(UserSetActivity.this, newFile), "image/*");
            // 自己使用Content Uri替换File Uri
            photoURI = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

        }
        activity.startActivityForResult(intent, CAMERA_REQUEST_CODE);

        return photoURI;
    }

    public void openMyCamera(){
        Intent intent = new Intent(activity,BelowLollipopCameraActivity.class);
        activity.startActivityForResult(intent,OPEN_MYCAMERA);
    }

    public Uri uriChange(File file){
        Uri photoURI = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            photoURI = FileProvider.getUriForFile(activity,
                    activity.getPackageName()+".provider",
                    file);
        }else {
            //  intent.setDataAndType(getImageContentUri(UserSetActivity.this, newFile), "image/*");
            // 自己使用Content Uri替换File Uri
            photoURI = Uri.fromFile(file);
        }
        return photoURI;
    }
}
