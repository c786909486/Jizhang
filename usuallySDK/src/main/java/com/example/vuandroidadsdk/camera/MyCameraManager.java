package com.example.vuandroidadsdk.camera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by CKZ on 2017/8/1.
 */

public class MyCameraManager {
    private static final String TAG = "MyCameraManager";

    private static MyCameraManager manager;

    private Context mContext;

    private Camera camera;

    private int cameraType = 0;

    /**
     * 相机闪光状态
     */
    private int cameraFlash;

    /**
     * 前后置状态
     */
    private int cameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;

    public static MyCameraManager getInstance(Context mContext){
        if (manager == null){
            synchronized (MyCameraManager.class){
                if (manager == null){
                    manager = new MyCameraManager(mContext);
                }
            }
        }
        return manager;
    }
    private MyCameraManager(Context mContext){
        this.mContext = mContext;
    }

    /**
     * 打开相机
     */
    public void openCamera(SurfaceHolder surfaceHolder){
        if (camera == null){
            Log.d(TAG,"打开相机");
            camera = Camera.open(cameraFacing);
            try {
                Camera.Parameters parameters = camera.getParameters();
                parameters.set("orientation","portrait");
//                parameters.set("rottion",90);
                parameters.setPreviewSize(640,480);
                parameters.setPictureSize(640,480);
                camera.setParameters(parameters);
                camera.setDisplayOrientation(90);
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG,e.toString());
                camera.release();
                camera = null;
            }
        }else {
            Log.d(TAG,"相机已经存在");
        }
    }

    public void openCamera(SurfaceTexture surfaceTexture, int width, int height){
        if (camera == null){
            camera = Camera.open(cameraFacing);
            try {
                camera.setDisplayOrientation(90);
                camera.setPreviewTexture(surfaceTexture);
                initCameraParameters(cameraFacing,width,height);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG,e.toString());
                camera.release();
                camera = null;
            }

        }
    }

    /**
     * 关闭相机
     */
    public void closeCamera(){
        this.cameraType = 0;
        if (camera!=null){
            try {
                camera.stopPreview();
                camera.release();
                camera = null;
//                cameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
            }catch (Exception e){
                Log.i(TAG,e.toString());
                camera.release();
                camera = null;
//                cameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
            }
        }
    }

    public void resetCamera(){
        closeCamera();
        cameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
    }

    /**
     * 更换摄像头
     * @param surfaceHolder
     */

    public void changeCamera(SurfaceHolder surfaceHolder) {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int i = 0;i<cameraCount;i++){
            Camera.getCameraInfo(i,cameraInfo);
            if (cameraFacing == Camera.CameraInfo.CAMERA_FACING_BACK){
                //现在后后置，更改为前置
                if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    closeCamera();
                    cameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT;
                    openCamera(surfaceHolder);
                    Log.d(TAG,"切换到前置摄像头");
                    break;
                }

            }else {
                if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    closeCamera();
                    cameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
                    openCamera(surfaceHolder);
                    Log.d(TAG,"切换到后置摄像头");
                    break;
                }
            }
        }
    }

    public void changeCamera(SurfaceTexture surfaceTexture, int width, int height) {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int i = 0;i<cameraCount;i++){
            Camera.getCameraInfo(i,cameraInfo);
            if (cameraFacing == Camera.CameraInfo.CAMERA_FACING_BACK){
                //现在后后置，更改为前置
                if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    closeCamera();
                    cameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT;
                    openCamera(surfaceTexture,width,height);
                    Log.d(TAG,"切换到前置摄像头");
                    break;
                }

            }else {
                if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    closeCamera();
                    cameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
                    openCamera(surfaceTexture,width,height);
                    Log.d(TAG,"切换到后置摄像头");
                    break;
                }
            }
        }
    }

    /**
     * 拍照
     * @param callback
     */
    public void takePhoto(Camera.PictureCallback callback){
        if (camera!=null){
            try {
                camera.takePicture(null,null,callback);
            }catch (Exception e){
                Toast.makeText(mContext,"拍照失败", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void initCameraParameters(int cameraId, int width, int height) {
        Camera.Parameters parameters = camera.getParameters();
        if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes != null) {
                if (cameraType == 0) {
                    if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                    }
                } else {
                    if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                    }
                }
            }
        }
        parameters.setRotation(90);//设置旋转代码,
        switch (cameraFlash) {
            case 0:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                break;
            case 1:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                break;
            case 2:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                break;
        }
        List<Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        if (!isEmpty(pictureSizes) && !isEmpty(previewSizes)) {
            /*for (Camera.Size size : pictureSizes) {
                LogUtils.i("pictureSize " + size.width + "  " + size.height);
            }
            for (Camera.Size size : pictureSizes) {
                LogUtils.i("previewSize " + size.width + "  " + size.height);
            }*/
            Camera.Size optimalPicSize = getOptimalCameraSize(pictureSizes, width, height);
            Camera.Size optimalPreSize = getOptimalCameraSize(previewSizes, width, height);
//            Camera.Size optimalPicSize = getOptimalSize(pictureSizes, width, height);
//            Camera.Size optimalPreSize = getOptimalSize(previewSizes, width, height);
            Log.i(TAG,"TextureSize "+width+" "+height+" optimalSize pic " + optimalPicSize.width + " " + optimalPicSize.height + " pre " + optimalPreSize.width + " " + optimalPreSize.height);
            parameters.setPictureSize(optimalPicSize.width, optimalPicSize.height);
            parameters.setPreviewSize(optimalPreSize.width, optimalPreSize.height);

        }
        camera.setParameters(parameters);
    }

    /**
     *
     * @param sizes 相机support参数
     * @param w
     * @param h
     * @return 最佳Camera size
     */
    private Camera.Size getOptimalCameraSize(List<Camera.Size> sizes, int w, int h){
        sortCameraSize(sizes);
        int position = binarySearch(sizes, w*h);
        return sizes.get(position);
    }

    /**
     *
     * @param sizes
     * @param targetNum 要比较的数
     * @return
     */
    private int binarySearch(List<Camera.Size> sizes, int targetNum){
        int targetIndex;
        int left = 0,right;
        int length = sizes.size();
        for (right = length-1;left != right;){
            int midIndex = (right + left)/2;
            int mid = right - left;
            Camera.Size size = sizes.get(midIndex);
            int midValue = size.width * size.height;
            if (targetNum == midValue){
                return midIndex;
            }
            if (targetNum > midValue){
                left = midIndex;
            }else {
                right = midIndex;
            }

            if (mid <= 1){
                break;
            }
        }
        Camera.Size rightSize = sizes.get(right);
        Camera.Size leftSize = sizes.get(left);
        int rightNum = rightSize.width * rightSize.height;
        int leftNum = leftSize.width * leftSize.height;
        targetIndex = Math.abs((rightNum - leftNum)/2) > Math.abs(rightNum - targetNum) ? right : left;
        return targetIndex;
    }

    /**
     * 排序
     * @param previewSizes
     */
    private void sortCameraSize(List<Camera.Size> previewSizes){
        Collections.sort(previewSizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size size1, Camera.Size size2) {
                int compareHeight = size1.height - size2.height;
                if (compareHeight == 0){
                    return (size1.width == size2.width ? 0 :(size1.width > size2.width ? 1:-1));
                }
                return compareHeight;
            }
        });
    }

    /**
     * 集合不为空
     *
     * @param list
     * @param <E>
     * @return
     */
    private <E> boolean isEmpty(List<E> list) {
        return list == null || list.isEmpty();
    }
}
