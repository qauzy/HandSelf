package cn.iyunbei.handself.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import androidx.annotation.NonNull;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.baidu.ai.edge.core.base.BaseConfig;
import com.baidu.ai.edge.core.base.BaseException;
import com.baidu.ai.edge.core.base.Consts;
import com.baidu.ai.edge.core.classify.ClassificationResultModel;
import com.baidu.ai.edge.core.classify.ClassifyException;
import com.baidu.ai.edge.core.classify.ClassifyInterface;
import com.baidu.ai.edge.core.classify.ClassifyOnline;
import com.baidu.ai.edge.core.ddk.DDKConfig;
import com.baidu.ai.edge.core.ddk.DDKDaVinciConfig;
import com.baidu.ai.edge.core.ddk.DDKManager;
import com.baidu.ai.edge.core.ddk.DavinciManager;
import com.baidu.ai.edge.core.detect.DetectInterface;
import com.baidu.ai.edge.core.detect.DetectOnline;
import com.baidu.ai.edge.core.detect.DetectionResultModel;
import com.baidu.ai.edge.core.infer.ArmGpuConfig;
import com.baidu.ai.edge.core.infer.InferConfig;
import com.baidu.ai.edge.core.infer.InferManager;
import com.baidu.ai.edge.core.ocr.OcrInterface;
import com.baidu.ai.edge.core.ocr.OcrResultModel;
import com.baidu.ai.edge.core.pose.PoseInterface;
import com.baidu.ai.edge.core.pose.PoseResultModel;
import com.baidu.ai.edge.core.segment.SegmentInterface;
import com.baidu.ai.edge.core.segment.SegmentationResultModel;
import com.baidu.ai.edge.core.snpe.SnpeConfig;
import com.baidu.ai.edge.core.snpe.SnpeGpuConfig;
import com.baidu.ai.edge.core.snpe.SnpeManager;
import com.baidu.ai.edge.core.util.Util;
import com.baidu.ai.edge.ui.activity.ResultListener;
import com.baidu.ai.edge.ui.util.ThreadPoolManager;
import com.baidu.ai.edge.ui.util.UiLog;
import com.baidu.ai.edge.ui.view.model.BasePolygonResultModel;
import com.baidu.ai.edge.ui.view.model.ClassifyResultModel;
import com.baidu.ai.edge.ui.view.model.DetectResultModel;
import com.baidu.ai.edge.ui.view.model.OcrViewResultModel;
import com.baidu.ai.edge.ui.view.model.PoseViewResultModel;
import com.baidu.ai.edge.ui.view.model.SegmentResultModel;
import com.baidu.ai.edge.ui.activity.MainActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.iyunbei.handself.MyApp;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.bean.GoodsDataBean;
import cn.iyunbei.handself.presenter.SpeechUtils;
import cn.iyunbei.handself.utils.EditTextWithText;
import jt.kundream.utils.ActivityUtil;
import jt.kundream.utils.ToastUtils;


/**
 * Created by ruanshimin on 2018/10/31.
 */

public class CameraActivity extends MainActivity {
    final String TAG = "CameraActivity";
    private MediaPlayer player;

    private String serialNum;
    private Integer step = 0;

    private final String[] StepList = {"???????????????","????????????","???????????????"};
    private final String[] SayWaht = {"????????????????????????","?????????????????????","????????????????????????"};
    private HashMap InfoMap;

    ClassifyInterface mClassifyDLManager;
    ClassifyInterface mOnlineClassify;
    DetectInterface mDetectManager;
    DetectInterface mOnlineDetect;
    SegmentInterface mSegmentManager;
    OcrInterface mOcrManager;
    PoseInterface mPoseManager;
    GoodsDataBean goodsInfo;    //????????????OCR????????????????????????

    private static final int CODE_FOR_WRITE_PERMISSION = 0;

    private int platform = Consts.TYPE_INFER;

    private boolean isInitializing = false;

    private boolean hasOnlineApi = false;
    // ??????????????????
    private boolean modelLoadStatus = false;

    @Override
    public void onSelectLabel(String label) {

        if(goodsInfo == null){
            goodsInfo = new GoodsDataBean();
        }
        AlertDialog alertDialog1 = new AlertDialog.Builder(CameraActivity.this)
                .setTitle(StepList[step])//??????
                .setMessage(label)//??????
                .setCancelable(false) //??????????????????????????????
                .setPositiveButton("??????",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        if(step == 0){
                            goodsInfo.setGoodsName(label);
                        }else if(step == 1){
                            goodsInfo.setSpec(label);
                        }else if(step == 2){
                            goodsInfo.setSupplier(label);
                        }
                        step++;
                        //???????????????????????????????????????
                        if(step>2){
                            String barcode = getIntent().getStringExtra("barcode");
                            Intent intent = new Intent();
                            intent.putExtra("goodsName", goodsInfo.getGoodsName());  //??????OCR????????????????????????
                            intent.putExtra("barcode", barcode);
                            intent.putExtra("supplier", goodsInfo.getSupplier());
                            intent.putExtra("spec", goodsInfo.getSpec());
                            CameraActivity.this.setResult(Activity.RESULT_OK, intent);
                            CameraActivity.this.finish();
                        }else{
                            //????????????
                            if(!isRealtimeStatusRunning){
                                toggleRealtimeStatus();
                            }
                        }
                    }
                })
                .setNegativeButton("???????????????", new DialogInterface.OnClickListener() {//????????????
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        //????????????
                        if(!isRealtimeStatusRunning){
                            toggleRealtimeStatus();
                        }

                    }
                })
                .create();
        alertDialog1.show();

    }



    @Override
    public void onOcrBitmap(Bitmap bitmap, float confidence, ResultListener.OcrListener listener) {
        List<OcrResultModel> modelList = null;
        try {
            modelList = mOcrManager.ocr(bitmap, confidence);

            List<BasePolygonResultModel> results = new ArrayList<>();
            for (int i = 0; i < modelList.size(); i++) {
                OcrResultModel mOcrResultModel = modelList.get(i);
//
//                //??????????????????????????????
//                GetGoodsInfo(mOcrResultModel.getLabel());

                OcrViewResultModel mOcrViewResultModel = new OcrViewResultModel();
                mOcrViewResultModel.setColorId(mOcrResultModel.getLabelIndex());
                mOcrViewResultModel.setIndex(i + 1);
                mOcrViewResultModel.setConfidence(mOcrResultModel.getConfidence());
                mOcrViewResultModel.setName(mOcrResultModel.getLabel());
                mOcrViewResultModel.setBounds(mOcrResultModel.getPoints());
                mOcrViewResultModel.setTextOverlay(true);
                results.add(mOcrViewResultModel);


            }
            //??????????????????
            listener.onResult(results);
            // ????????????????????????????????????
            if(isRealtimeStatusRunning && modelList.size()>0){
                MyApp.getInstance().say(SayWaht[step]);
                player.start();
                toggleRealtimeStatus();
            }
        } catch (BaseException e) {
            showError(e);
            listener.onResult(null);
        }
    }

    /*
          onCreate?????????
         */
    @Override
    public void onActivityCreate() {
        choosePlatform();
        start();
        player = MediaPlayer.create(getApplicationContext(), R.raw.beep);
    }

    private void choosePlatform() {
        String soc = getIntent().getStringExtra("soc");
        switch (soc) {
            case "dsp":
                platform = Consts.TYPE_SNPE;
                break;
            case "adreno-gpu":
                platform = Consts.TYPE_SNPE_GPU;
                break;
            case "npu-vinci":
                platform = Consts.TYPE_DDK_DAVINCI;
                break;
            case "npu200":
                platform = Consts.TYPE_DDK200;
                break;
            case "arm-gpu":
                platform = Consts.TYPE_ARM_GPU;
                break;
            default:
            case "arm":
                platform = Consts.TYPE_INFER;
        }
    }


    private void start() {
        // paddleLite?????????????????????????????????????????????????????????
        ThreadPoolManager.executeSingle(() -> {
            initManager();
            runOnUiThread(() -> {
                if (((model == MODEL_DETECT || model == MODEL_FACE_DETECT) && mDetectManager != null) ||
                        (model == MODEL_CLASSIFY && mClassifyDLManager != null) ||
                        (model == MODEL_SEGMENT && mSegmentManager != null) ||
                        (model == MODEL_OCR && mOcrManager != null) ||
                        (model == MODEL_POSE && mPoseManager != null)) {
                    modelLoadStatus = true;
                    updateTakePictureButtonStatus();
                }
            });
        });
    }

    private void updateTakePictureButtonStatus() {
        setTakePictureButtonAvailable(modelLoadStatus);
    }

    /**
     * ???????????????????????????mDetectManager??????????????????????????????
     */
    @Override
    public void onActivityDestory() {
        releaseEasyDL();
    }


    @Override
    public void dumpDetectResult(List<DetectResultModel> model, Bitmap bitmap, float min) {

    }

    @Override
    public void dumpClassifyResult(List<ClassifyResultModel> model, Bitmap bitmap, float min) {

    }

    private void showError(BaseException e) {
        showMessage(e.getErrorCode(), e.getMessage());
        Log.e("CameraActivity", e.getMessage(), e);
    }

    private void releaseEasyDL() {
        if (model == MODEL_DETECT || model == MODEL_FACE_DETECT) {
            if (mDetectManager != null) {
                try {
                    mDetectManager.destroy();
                } catch (BaseException e) {
                    showError(e);
                }
            }
        }
        if (model == MODEL_CLASSIFY) {
            if (mClassifyDLManager != null) {
                try {
                    mClassifyDLManager.destroy();
                } catch (ClassifyException e) {
                    showError(e);
                } catch (BaseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (model == MODEL_SEGMENT || model == MODEL_SEMANTIC_SEGMENT) {
            if (mSegmentManager != null) {
                try {
                    mSegmentManager.destroy();
                } catch (ClassifyException e) {
                    showError(e);
                } catch (BaseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (model == MODEL_OCR) {
            if (mOcrManager != null) {
                try {
                    mOcrManager.destroy();
                } catch (ClassifyException e) {
                    showError(e);
                } catch (BaseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (pageCode == PAGE_CAMERA && !isInitializing) {
            showMessage("??????????????????");
        } else {
            super.onBackPressed();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onSetMenu(PopupMenu actionBarMenu) {

    }

    @Override
    protected void onSetMenuItem(boolean isOnline) {

    }

    private void initManager() {
        serialNum = getIntent().getStringExtra("serial_num");
        String apiUrl = getIntent().getStringExtra("apiUrl");
        String ak = getIntent().getStringExtra("ak");
        String sk = getIntent().getStringExtra("sk");

        float threshold = BaseConfig.DEFAULT_THRESHOLD;

        if (apiUrl != null) {
            hasOnlineApi = true;
        }
        UiLog.info("model type is " + model);
        if (model == MODEL_DETECT || model == MODEL_FACE_DETECT) {
            if (hasOnlineApi) {
                mOnlineDetect = new DetectOnline(apiUrl, ak, sk, this);
            }
            try {
                switch (platform) {
                    case Consts.TYPE_DDK200:
                        DDKConfig ddkConfig = new DDKConfig(getAssets(), "ddk");
                        threshold = ddkConfig.getRecommendedConfidence();
                        mDetectManager = new DDKManager(this, ddkConfig, serialNum);
                        break;
                    case Consts.TYPE_SNPE:
                        SnpeConfig mSnpeClassifyConfig = new SnpeConfig(this.getAssets(),
                                "snpe");
                        threshold = mSnpeClassifyConfig.getRecommendedConfidence();
                        mDetectManager = new SnpeManager(this, mSnpeClassifyConfig, serialNum);
                        break;
                    case Consts.TYPE_SNPE_GPU:
                        SnpeGpuConfig mSnpeGpuClassifyConfig = new SnpeGpuConfig(this.getAssets(),
                                "snpe-gpu");
                        threshold = mSnpeGpuClassifyConfig.getRecommendedConfidence();
                        mDetectManager = new SnpeManager(this, mSnpeGpuClassifyConfig, serialNum);
                        break;
                    case Consts.TYPE_DDK_DAVINCI:
                        DDKDaVinciConfig config = new DDKDaVinciConfig(this.getAssets(), "davinci");
                        threshold = config.getRecommendedConfidence();
                        mDetectManager = new DavinciManager(this, config, serialNum);
                        break;
                    case Consts.TYPE_ARM_GPU:
                        ArmGpuConfig mArmGpuConfig = new ArmGpuConfig(getAssets(),
                                "infer");
                        threshold = mArmGpuConfig.getRecommendedConfidence();
                        // ??????openclTune???true?????????????????????????????????????????????????????????
                        mArmGpuConfig.setOpenclTune(false);
                        mDetectManager = new InferManager(this, mArmGpuConfig, serialNum);
                        break;
                    case Consts.TYPE_INFER:
                    default:
                        InferConfig mInferConfig = new InferConfig(getAssets(),
                                "infer");
                        // ?????????ARM???????????????CPU??????
                        mInferConfig.setThread(Util.getInferCores());
                        threshold = mInferConfig.getRecommendedConfidence();
                        mDetectManager = new InferManager(this, mInferConfig, serialNum);
                        break;
                }

                canAutoRun = true;
                isInitializing = true;
            } catch (BaseException e) {
                showError(e);
            }
        }
        if (model == MODEL_CLASSIFY) {
            if (hasOnlineApi) {
                mOnlineClassify = new ClassifyOnline(apiUrl, ak, sk, this);
            }
            try {
                switch (platform) {
                    case Consts.TYPE_DDK200:
                        DDKConfig ddkConfig = new DDKConfig(getAssets(), "ddk");
                        threshold = ddkConfig.getRecommendedConfidence();
                        mClassifyDLManager = new DDKManager(this, ddkConfig, serialNum);
                        break;
                    case Consts.TYPE_DDK_DAVINCI:
                        DDKDaVinciConfig mDDKDaVinciConfig = new DDKDaVinciConfig(this.getAssets(),
                                "davinci");
                        threshold = mDDKDaVinciConfig.getRecommendedConfidence();
                        mClassifyDLManager = new DavinciManager(this, mDDKDaVinciConfig, serialNum);
                        break;
                    case Consts.TYPE_SNPE:
                        SnpeConfig mSnpeClassifyConfig = new SnpeConfig(this.getAssets(),
                                "snpe");
                        threshold = mSnpeClassifyConfig.getRecommendedConfidence();
                        mClassifyDLManager = new SnpeManager(this, mSnpeClassifyConfig, serialNum);
                        break;
                    case Consts.TYPE_SNPE_GPU:
                        SnpeGpuConfig mSnpeGpuClassifyConfig = new SnpeGpuConfig(this.getAssets(),
                                "snpe-gpu");
                        threshold = mSnpeGpuClassifyConfig.getRecommendedConfidence();
                        mClassifyDLManager = new SnpeManager(this, mSnpeGpuClassifyConfig, serialNum);
                        break;
                    case Consts.TYPE_ARM_GPU:
                        ArmGpuConfig mArmGpuConfig = new ArmGpuConfig(getAssets(),
                                "infer");
                        threshold = mArmGpuConfig.getRecommendedConfidence();
                        mClassifyDLManager = new InferManager(this, mArmGpuConfig, serialNum);
                        break;
                    case Consts.TYPE_INFER:
                    default:
                        threshold = initInfer();
                        break;
                }

                canAutoRun = true;
                isInitializing = true;
            } catch (BaseException e) {
                showError(e);
                Log.e("CameraActivity", e.getClass().getSimpleName() + ":" + e.getErrorCode() + ":" + e.getMessage());
            }
        }

        if (model == MODEL_SEGMENT || model == MODEL_OCR || model == MODEL_POSE || model == MODEL_SEMANTIC_SEGMENT) {
            InferConfig mInferConfig = null;
            try {
                mInferConfig = new InferConfig(getAssets(), "infer");
                mInferConfig.setThread(Util.getInferCores());
                threshold = mInferConfig.getRecommendedConfidence();
                switch (model) {
                    case MODEL_SEGMENT:
                    case MODEL_SEMANTIC_SEGMENT: {
                        mSegmentManager = new InferManager(this, mInferConfig, serialNum);
                        break;
                    }
                    case MODEL_OCR: {
                        mOcrManager = new InferManager(this, mInferConfig, serialNum);
                        break;
                    }
                    case MODEL_POSE: {
                        mPoseManager = new InferManager(this, mInferConfig, serialNum);
                        break;
                    }
                }

                canAutoRun = true;
                isInitializing = true;
            } catch (BaseException e) {
                showError(e);
            }
        }

        setConfidence(threshold);
    }

    private float initInfer() throws BaseException {
        InferConfig mInferConfig = new InferConfig(getAssets(),
                "infer");
        mInferConfig.setThread(Util.getInferCores());
        mClassifyDLManager = new InferManager(this, mInferConfig, serialNum);
        return mInferConfig.getRecommendedConfidence();
    }


}
