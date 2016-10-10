package com.meitu.makeuppicker.util;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;

/**
 * @author ysq 2016/9/30.
 */
public class SurfaceCallback implements SurfaceHolder.Callback {
    private Camera mCamera;
    private Camera.Parameters mCameraParameters = null;
    // 拍照状态变化时调用该方法
    private Activity mActivity;

    public SurfaceCallback(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
            mCameraParameters = mCamera.getParameters(); // 获取各项参数
            mCameraParameters.setPictureFormat(PixelFormat.JPEG); // 设置图片格式
            mCameraParameters.setPreviewSize(width, height); // 设置预览大小
            mCameraParameters.setPreviewFrameRate(5); // 设置每秒显示4帧
            mCameraParameters.setPictureSize(width, height); // 设置保存的图片尺寸
            mCameraParameters.setJpegQuality(80); // 设置照片质量
        } catch (Exception e) {
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
            }
        }
    }

    // 开始拍照时调用该方法
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera = Camera.open(); // 打开摄像头
            mCamera.setPreviewDisplay(holder); // 设置用于显示拍照影像的SurfaceHolder对象
            mCamera.setDisplayOrientation(getPreviewDegree(mActivity));
            mCamera.startPreview(); // 开始预览
        } catch (Exception e) {
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
            }
            e.printStackTrace();
        }

    }

    // 停止拍照时调用该方法
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.release(); // 释放照相机
            mCamera = null;
        }
    }

    // 提供一个静态方法，用于根据手机方向获得相机预览画面旋转的角度
    private int getPreviewDegree(Activity activity) {
        // 获得手机的方向
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        // 根据手机的方向计算相机预览画面应该选择的角度
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }
}
