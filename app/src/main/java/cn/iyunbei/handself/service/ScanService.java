package cn.iyunbei.handself.service;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.view.PreviewView;
import androidx.fragment.app.FragmentActivity;

import com.baidu.ai.edge.ui.activity.ResultListener;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.king.mlkit.vision.barcode.analyze.BarcodeScanningAnalyzer;
import com.king.mlkit.vision.camera.AnalyzeResult;
import com.king.mlkit.vision.camera.BaseCameraScan;
import com.king.mlkit.vision.camera.CameraScan;
import com.king.mlkit.vision.camera.analyze.Analyzer;
import com.king.mlkit.vision.camera.config.ResolutionCameraConfig;
import com.king.mlkit.vision.camera.util.LogUtils;
import com.king.mlkit.vision.camera.util.PermissionUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.iyunbei.handself.R;

public abstract class ScanService  implements CameraScan.OnScanResultCallback<List<Barcode>>{
    protected PreviewView mPreviewView;
    private boolean mHasCamera;
    protected RelativeLayout rlScan;
    private boolean isRunnning = false;
    public abstract void onScanResult(String code);

    public ScanService(FragmentActivity activity) {
        mActivity = activity;
        mPreviewView = mActivity.findViewById(R.id.previewView);
        rlScan = mActivity.findViewById(R.id.rl_scan);
        initCameraScan();
    }

    private FragmentActivity mActivity;
    private CameraScan<List<Barcode>> mCameraScan;
    @Override
    public void onScanResultCallback(@NonNull AnalyzeResult<List<Barcode>> result) {
        mCameraScan.setAnalyzeImage(false);

        for(Barcode it : result.getResult()){
            //过滤不全是数字的结果
            if(!isNumeric(it.getRawValue())){
                continue;
            }
            onScanResult(it.getRawValue());

        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //延迟重新打开扫码识别
                mCameraScan.setAnalyzeImage(true);
            }
        }, 1000);

    }


    @Override
    public void onScanResultFailure() {

    }

    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * 创建{@link CameraScan}
     * @param previewView
     * @return
     */
    public CameraScan<List<Barcode>> createCameraScan(PreviewView previewView){
        return new BaseCameraScan<>(mActivity,previewView);
    }
    /**
     * 创建分析器，默认分析所有条码格式
     * @return
     */
    @Nullable
    public Analyzer<List<Barcode>> createAnalyzer(){
        return new BarcodeScanningAnalyzer(Barcode.FORMAT_EAN_13);
    }

    /**
     * 初始化CameraScan
     */
    public void initCameraScan(){
        mCameraScan = createCameraScan(mPreviewView)
                .setDarkLightLux(45f)//设置光线足够暗的阈值（单位：lux），需要通过{@link #bindFlashlightView(View)}绑定手电筒才有效
                .setBrightLightLux(100f)//设置光线足够明亮的阈值（单位：lux），需要通过{@link #bindFlashlightView(View)}绑定手电筒才有效
                .setAnalyzer(createAnalyzer())
                .setPlayBeep(true)
                .setVibrate(true)
                .setCameraConfig(new ResolutionCameraConfig(mActivity))//设置CameraConfig
                .setOnScanResultCallback(this);
    }


    /**
     * 启动相机预览
     */
    public void start(){
        if(mCameraScan != null){
            if(PermissionUtils.checkPermission(mActivity,Manifest.permission.CAMERA)){
                rlScan.setVisibility(View.VISIBLE);
                mPreviewView.setVisibility(View.VISIBLE);//显示摄像头扫描
                mCameraScan.startCamera();
                mCameraScan.setAnalyzeImage(true);
                isRunnning = true;
            }else{
                LogUtils.d("checkPermissionResult != PERMISSION_GRANTED");
                PermissionUtils.requestPermission(mActivity,Manifest.permission.CAMERA,0);
            }
        }

    }


    /**
     * 释放相机
     */
    public void stop(){
        if(mCameraScan != null){
            rlScan.setVisibility(View.GONE);
            mCameraScan.setAnalyzeImage(false);
            mPreviewView.setVisibility(View.GONE);//显示摄像头扫描
            mCameraScan.release();
            isRunnning =false;
        }
    }

    public boolean isRunnning() {
        return isRunnning;
    }

    public void setRunnning(boolean runnning) {
        isRunnning = runnning;
    }
}
