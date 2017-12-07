package com.example.vuandroidadsdk.camera;

import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.vuandroidadsdk.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by CKZ on 2017/7/29.
 */

public class BelowLollipopCameraActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "BelowLollipopCamera";
    private TextureView mSurface;
    private ImageView mClose;
    private ImageView mTake;
    private ImageView mPic;
    private ImageView mBack;
    private ImageView mSucess;
    private ImageView mChange;

    private MyCameraManager manager;


    private File mFile = null;
    private boolean isToken = false;



    private TextureView.SurfaceTextureListener listener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            manager.openCamera(surfaceTexture,i,i1);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            manager.resetCamera();
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_camera);
        File dir = new File(Environment.getExternalStorageDirectory(),"images");
        if(dir.exists()){
            dir.mkdirs();
        }
        manager = MyCameraManager.getInstance(this);
        mFile = new File(dir,"pic.jpg");
        initView();
        showCamera();
        setClick();
//        setTouch();
    }
    private void initView() {
        mSurface = (TextureView) findViewById(R.id.surface);
        mTake = (ImageView) findViewById(R.id.take_photo);
        mClose = (ImageView) findViewById(R.id.close_camera);
        mPic = (ImageView) findViewById(R.id.picture);
        mBack = (ImageView) findViewById(R.id.camera_back);
        mSucess = (ImageView) findViewById(R.id.camera_sucess);
        mChange = (ImageView) findViewById(R.id.camera_change);

//        mSurface.getHolder().addCallback(cpHolderCallback);
        mSurface.setSurfaceTextureListener(listener);

    }

    private void setClick() {
        mClose.setOnClickListener(this);
        mTake.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mSucess.setOnClickListener(this);
        mChange.setOnClickListener(this);
    }
    //预览时显示的控件
    private void showCamera(){
        mSurface.setVisibility(View.VISIBLE);
        mTake.setVisibility(View.VISIBLE);
        mClose.setVisibility(View.VISIBLE);
        mChange.setVisibility(View.VISIBLE);

        mPic.setVisibility(View.GONE);
        mBack.setVisibility(View.GONE);
        mSucess.setVisibility(View.GONE);
        isToken = false;
    }
    //完成拍照时显示的控件
    private void showPicture(){
        mSurface.setVisibility(View.GONE);
        mTake.setVisibility(View.GONE);
        mClose.setVisibility(View.GONE);
        mChange.setVisibility(View.GONE);

        mPic.setVisibility(View.VISIBLE);
        mBack.setVisibility(View.VISIBLE);
        mSucess.setVisibility(View.VISIBLE);
        mBack.startAnimation(AnimationUtils.loadAnimation(this,R.anim.to_left));
        mSucess.startAnimation(AnimationUtils.loadAnimation(this,R.anim.to_right));
        isToken = true;
    }

    //保存临时文件的方法
    private String saveFile(byte[] bytes){
        try {
            mFile = File.createTempFile("img","");
            FileOutputStream fos = new FileOutputStream(mFile);
            fos.write(bytes);
            fos.flush();
            fos.close();
//            mPic.setImageURI(CameraHelper.getInstance(this).uriChange(mFile));
            return mFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }



    @Override
    public void onBackPressed() {
        if (isToken){
            showCamera();
        }else {
            finish();

        }
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0,R.anim.camera_activity_out);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.close_camera) {
            Intent cancleIntent = new Intent();
            setResult(RESULT_CANCELED, cancleIntent);
            finish();

        } else if (i == R.id.take_photo) {//拍照
//                takePhotos();
            manager.takePhoto(new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] bytes, Camera camera) {
                    saveFile(bytes);
                    mPic.setImageURI(Uri.fromFile(mFile));
                    showPicture();
                }
            });

        } else if (i == R.id.camera_back) {
            manager.closeCamera();
            manager.openCamera(mSurface.getSurfaceTexture(), mSurface.getWidth(), mSurface.getHeight());
            showCamera();

        } else if (i == R.id.camera_sucess) {//返回数据
            Intent intent = new Intent();
//                intent.putExtra("imageUri",CameraHelper.getInstance(this).uriChange(mFile).toString());
            intent.putExtra("imageUri", Uri.fromFile(mFile).toString());
            setResult(RESULT_OK, intent);
            finish();

        } else if (i == R.id.camera_change) {//更改摄像头
//                changeCamera();
            manager.changeCamera(mSurface.getSurfaceTexture(), mSurface.getWidth(), mSurface.getHeight());
        }
    }


    /**
     * 手动聚焦
     *
     * @param point 触屏坐标
//     */
//    protected boolean onFocus(Point point, Camera.AutoFocusCallback callback) {
//        if (camera == null) {
//            return false;
//        }
//
//        Camera.Parameters parameters = null;
//        try {
//            parameters = camera.getParameters();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        //不支持设置自定义聚焦，则使用自动聚焦，返回
//
//        if(Build.VERSION.SDK_INT >= 14) {
//
//            if (parameters.getMaxNumFocusAreas() <= 0) {
//                return focus(callback);
//            }
//
//            Log.d(TAG, "onCameraFocus:" + point.x + "," + point.y);
//
//            //定点对焦
//            List<Camera.Area> areas = new ArrayList<Camera.Area>();
//            int left = point.x - 300;
//            int top = point.y - 300;
//            int right = point.x + 300;
//            int bottom = point.y + 300;
//            left = left < -1000 ? -1000 : left;
//            top = top < -1000 ? -1000 : top;
//            right = right > 1000 ? 1000 : right;
//            bottom = bottom > 1000 ? 1000 : bottom;
//            areas.add(new Camera.Area(new Rect(left, top, right, bottom), 100));
//            parameters.setFocusAreas(areas);
//            try {
//                //本人使用的小米手机在设置聚焦区域的时候经常会出异常，看日志发现是框架层的字符串转int的时候出错了，
//                //目测是小米修改了框架层代码导致，在此try掉，对实际聚焦效果没影响
//                camera.setParameters(parameters);
//            } catch (Exception e) {
//                // TODO: handle exception
//                e.printStackTrace();
//                return false;
//            }
//        }
//
//
//        return focus(callback);
//    }
//
//    private boolean focus(Camera.AutoFocusCallback callback) {
//        try {
//            camera.autoFocus(callback);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//    private void setTouch(){
//        mSurface.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                Point point = new Point((int)motionEvent.getX(),(int)motionEvent.getY());
//                if (onFocus(point,callback)){
//                    camera.cancelAutoFocus();
//                }
//                return true;
//            }
//        });
//    }
}
