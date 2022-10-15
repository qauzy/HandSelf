package cn.iyunbei.handself.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Point;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.List;

import cn.iyunbei.handself.R;
import cn.iyunbei.handself.bean.GoodsDataBean;
import cn.iyunbei.handself.service.BluetoothService;
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

    private final String[] StepList = {"获取条形码","获取商品名","获取规格","获取生产商"};
    ClassifyInterface mClassifyDLManager;
    ClassifyInterface mOnlineClassify;
    DetectInterface mDetectManager;
    DetectInterface mOnlineDetect;
    SegmentInterface mSegmentManager;
    OcrInterface mOcrManager;
    PoseInterface mPoseManager;


    private static final int CODE_FOR_WRITE_PERMISSION = 0;

    private int platform = Consts.TYPE_INFER;

    private boolean isInitializing = false;

    private boolean hasOnlineApi = false;
    // 模型加载状态
    private boolean modelLoadStatus = false;


    //使用Handler对象在UI主线程与子线程之间传递消息
    private final Handler mHandler = new Handler() {   //消息处理
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                default:
                    player.start();
                AlertDialog alertDialog1 = new AlertDialog.Builder(CameraActivity.this)
                        .setTitle(StepList[step])//标题
                        .setMessage(msg.getData().getString("净含量"))//内容
                        .setCancelable(false) //点击弹框外部不会消失
                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();

                                step++;
                                //获取完了所有信息，结束扫描
                                if(step>3){
                                    CameraActivity.this.finish();
                                }else{
                                    //继续识别
                                    if(!isRealtimeStatusRunning){
                                        toggleRealtimeStatus();
                                    }
                                }
                            }
                        })
                        .setNegativeButton("关闭", new DialogInterface.OnClickListener() {//添加取消
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                //继续识别
                                if(!isRealtimeStatusRunning){
                                    toggleRealtimeStatus();
                                }

                            }
                        })
                        .create();
                alertDialog1.show();

            }
        }
    };


    /*
      onCreate中调用
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
        // paddleLite需要保证初始化与预测在同一线程保证速度
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
     * 此处简化，建议一个mDetectManager对象在同一线程中调用
     */
    @Override
    public void onActivityDestory() {
        releaseEasyDL();
    }


    /**
     * 新线程中调用 ，从照相机中获取bitmap
     *
     * @param bitmap     RGBA格式
     * @param confidence [0-1）
     * @return
     */
    @Override
    public void onDetectBitmap(Bitmap bitmap, float confidence,
                               final ResultListener.DetectListener listener) {

//        if (isOnline) {
//            try {
//                List<DetectionResultModel> result = mOnlineDetect.detect(bitmap, confidence);
//                listener.onResult(fillDetectionResultModel(result));
//            } catch (BaseException e) {
//                listener.onResult(null);
//                showError(e);
//                e.printStackTrace();
//            }
//            return;
//        }

        if (mDetectManager == null) {
            showMessage("模型初始化中，请稍后");
            listener.onResult(null);
            return;
        }
        try {
            List<DetectionResultModel> modelList = mDetectManager.detect(bitmap, confidence);
            listener.onResult(fillDetectionResultModel(modelList));
        } catch (BaseException e) {
            showError(e);
            listener.onResult(null);
        }
    }

    private List<BasePolygonResultModel> fillDetectionResultModel(
            List<DetectionResultModel> modelList) {
        List<BasePolygonResultModel> results = new ArrayList<>();
        for (int i = 0; i < modelList.size(); i++) {
            DetectionResultModel mDetectionResultModel = modelList.get(i);
            DetectResultModel mDetectResultModel = new DetectResultModel();
            mDetectResultModel.setIndex(i + 1);
            mDetectResultModel.setConfidence(mDetectionResultModel.getConfidence());
            mDetectResultModel.setName(mDetectionResultModel.getLabel());
            mDetectResultModel.setBounds(mDetectionResultModel.getBounds());
            results.add(mDetectResultModel);
        }
        return results;
    }

    @Override
    public void onClassifyBitmap(Bitmap bitmap, float confidence,
                                 final ResultListener.ClassifyListener listener) {
//        if (isOnline) {
//
//            try {
//                List<ClassificationResultModel> result = mOnlineClassify.classify(bitmap, confidence);
//                fillClassificationResultModel(result);
//            } catch (BaseException e) {
//                e.printStackTrace();
//                listener.onResult(null);
//                showError(e);
//            }
//            return;
//        }

        if (mClassifyDLManager == null) {
            showMessage("模型初始化中，请稍后");
            listener.onResult(null);
            return;
        }
        try {
            List<ClassificationResultModel> modelList = mClassifyDLManager.classify(bitmap, confidence);
            listener.onResult(fillClassificationResultModel(modelList));
        } catch (BaseException e) {
            showError(e);
            listener.onResult(null);
        }
    }

    @Override
    public void onSegmentBitmap(Bitmap bitmap, float confidence, final ResultListener.SegmentListener listener) {
        if (mSegmentManager == null) {
            showMessage("模型初始化中，请稍后");
            listener.onResult(null);
            return;
        }

        List<SegmentationResultModel> resultModels = null;
        try {
            resultModels = mSegmentManager.segment(bitmap, confidence);
            List<BasePolygonResultModel> results = new ArrayList<>();
            for (int i = 0; i < resultModels.size(); i++) {
                SegmentationResultModel mSegmentationResultModel = resultModels.get(i);
                SegmentResultModel mSegmentResultModel = new SegmentResultModel();
                mSegmentResultModel.setColorId(mSegmentationResultModel.getLabelIndex());
                mSegmentResultModel.setIndex(i + 1);
                mSegmentResultModel.setConfidence(mSegmentationResultModel.getConfidence());
                mSegmentResultModel.setName(mSegmentationResultModel.getLabel());
                mSegmentResultModel.setBounds(mSegmentationResultModel.getBox());
                mSegmentResultModel.setMask(mSegmentationResultModel.getMask());
                if (model == MODEL_SEMANTIC_SEGMENT) {
                    // 语义分割不绘制标签
                    mSegmentResultModel.setRect(false);
                    mSegmentResultModel.setSemanticMask(true);
                }
                results.add(mSegmentResultModel);
            }

            listener.onResult(results);
        } catch (BaseException e) {
            showError(e);
            listener.onResult(null);
        }

    }

    @Override
    public void onOcrBitmap(Bitmap bitmap, float confidence, ResultListener.OcrListener listener) {
        List<OcrResultModel> modelList = null;
        try {
            modelList = mOcrManager.ocr(bitmap, confidence);
            List<BasePolygonResultModel> results = new ArrayList<>();
            for (int i = 0; i < modelList.size(); i++) {
                OcrResultModel mOcrResultModel = modelList.get(i);

                if(mOcrResultModel.getLabel().contains("净含量")){
                    //扫描到目标文字，暂停识别
                    if(isRealtimeStatusRunning){
                        toggleRealtimeStatus();
                    }
                    //发送消息
                    Message msg = mHandler.obtainMessage(BluetoothChat.MESSAGE_DEVICE_NAME);
                    Bundle bundle = new Bundle();
                    bundle.putString("净含量", mOcrResultModel.getLabel());
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                    Log.d(TAG,mOcrResultModel.getLabel());

//                    ToastUtils.showShort(CameraActivity.this,mOcrResultModel.getLabel());
                }
                OcrViewResultModel mOcrViewResultModel = new OcrViewResultModel();
                mOcrViewResultModel.setColorId(mOcrResultModel.getLabelIndex());
                mOcrViewResultModel.setIndex(i + 1);
                mOcrViewResultModel.setConfidence(mOcrResultModel.getConfidence());
                mOcrViewResultModel.setName(mOcrResultModel.getLabel());
                mOcrViewResultModel.setBounds(mOcrResultModel.getPoints());
                mOcrViewResultModel.setTextOverlay(true);
                results.add(mOcrViewResultModel);


            }
            listener.onResult(results);
        } catch (BaseException e) {
            showError(e);
            listener.onResult(null);
        }
    }

    public void onPoseBitmap(Bitmap bitmap, float confidence, ResultListener.PoseListener listener) {
        List<PoseResultModel> modelList = null;
        try {
            modelList = mPoseManager.pose(bitmap);
            List<BasePolygonResultModel> results = new ArrayList<>();
            for (int i = 0; i < modelList.size(); i++) {
                PoseResultModel mPoseResultModel = modelList.get(i);
                PoseViewResultModel mPoseViewResultModel = new PoseViewResultModel();
                mPoseViewResultModel.setColorId(mPoseResultModel.getIndex());
                mPoseViewResultModel.setIndex(i + 1);
                mPoseViewResultModel.setConfidence(mPoseResultModel.getConfidence());
                mPoseViewResultModel.setName(mPoseResultModel.getLabel() + "Line" + mPoseResultModel.getIndex());
                Pair<Point, Point> line = mPoseResultModel.getPoints();
                List<Point> points = new ArrayList<Point>();
                points.add(line.first);
                points.add(line.second);
                mPoseViewResultModel.setBounds(points);
                mPoseViewResultModel.setHasGroupColor(mPoseResultModel.hasGroups());
                mPoseViewResultModel.setColorId(mPoseResultModel.getGroupIndex());
                results.add(mPoseViewResultModel);
            }
            listener.onResult(results);
        } catch (BaseException e) {
            showError(e);
            listener.onResult(null);
        }
    }

    private List<ClassifyResultModel> fillClassificationResultModel(
            List<ClassificationResultModel> modelList) {
        List<ClassifyResultModel> results = new ArrayList<>();
        for (int i = 0; i < modelList.size(); i++) {
            ClassificationResultModel mClassificationResultModel = modelList.get(i);
            ClassifyResultModel mClassifyResultModel = new ClassifyResultModel();
            mClassifyResultModel.setIndex(i + 1);
            mClassifyResultModel.setConfidence(mClassificationResultModel.getConfidence());
            mClassifyResultModel.setName(mClassificationResultModel.getLabel());
            results.add(mClassifyResultModel);
        }
        return results;
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
            showMessage("模型未初始化");
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
                        // 设置openclTune为true可以加快推理速度，但是第一次推理会很慢
                        mArmGpuConfig.setOpenclTune(false);
                        mDetectManager = new InferManager(this, mArmGpuConfig, serialNum);
                        break;
                    case Consts.TYPE_INFER:
                    default:
                        InferConfig mInferConfig = new InferConfig(getAssets(),
                                "infer");
                        // 可修改ARM推断使用的CPU核数
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


    /**
     * 更新商品信息dialog
     */
    private void showInputDialog(GoodsDataBean data) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(this, R.layout.dialog_input, null);
        // dialog.setView(view);// 将自定义的布局文件设置给dialog
        // 设置边距为0,保证在2.x的版本上运行没问题
        dialog.setView(view, 0, 0, 0, 0);

        TextView etCode = (TextView) view.findViewById(R.id.tv_goods_code);
        etCode.setText(data.getBarcode());
        EditText etGuide = (EditText) view.findViewById(R.id.et_goods_guige);
        etGuide.setText(data.getSpec());
        EditText etName = (EditText) view.findViewById(R.id.et_goods_name);
        etName.setText(data.getGoodsName());
        EditTextWithText etMoney = (EditTextWithText) view.findViewById(R.id.et_money);
        if(data.getPrice() == null || data.getPrice().isEmpty()){
            etMoney.setLeadText("￥");
            etMoney.setText("");
        }else{
            etMoney.setLeadText("￥");
            etMoney.setText(data.getPrice());
        }

        EditText etSupplier = (EditText) view.findViewById(R.id.et_supplier);
        etSupplier.setText(data.getSupplier());


        Log.d(TAG,"商品position="+data.getPosition());
        Button btnModify = (Button) view.findViewById(R.id.btn_add_goods);

        btnModify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String barcode = etCode.getText().toString();
                String goodsName = etName.getText().toString();
                String supplier = etSupplier.getText().toString();
                String price = etMoney.getText().toString();
                String psec = etGuide.getText().toString();
                Log.d(TAG,"商品psec="+psec);
                if(barcode.isEmpty() || goodsName.isEmpty() || supplier.isEmpty() || price.isEmpty() || psec.isEmpty()){
                    ToastUtils.showShort(getApplicationContext(), "商品信息不全");
                    return;
                }
//                presenter.saveGoodsInfo(data.getPosition(),barcode,goodsName,supplier,price,psec, cn.iyunbei.handself.activity.MainActivity.this);

                dialog.dismiss();
            }
        });
        ActivityUtil.backgroundAlpha(1f, this);
        dialog.show();
    }
}
