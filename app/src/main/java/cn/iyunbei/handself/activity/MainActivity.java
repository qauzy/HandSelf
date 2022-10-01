package cn.iyunbei.handself.activity;
import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.king.mlkit.vision.barcode.analyze.BarcodeScanningAnalyzer;
import com.king.mlkit.vision.camera.AnalyzeResult;
import com.king.mlkit.vision.camera.BaseCameraScan;
import com.king.mlkit.vision.camera.CameraScan;
import com.king.mlkit.vision.camera.analyze.Analyzer;
import com.king.mlkit.vision.camera.util.LogUtils;
import com.king.mlkit.vision.camera.util.PermissionUtils;
import com.king.mlkit.vision.camera.config.ResolutionCameraConfig;


import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.adapter.GoodsAdapter;
import cn.iyunbei.handself.bean.TempOrderBean;
import cn.iyunbei.handself.contract.MainContract;
import cn.iyunbei.handself.greendao.GreenDaoHelper;
import cn.iyunbei.handself.presenter.MainPresenter;
import cn.iyunbei.handself.presenter.SpeechUtils;
import cn.iyunbei.handself.service.LiveService;
import cn.iyunbei.handself.utils.aboutclick.AntiShake;
import com.fiberhome.duotellib.HumidityControlUtil;
import jt.kundream.base.BaseActivity;
import jt.kundream.bean.EventBusBean;
import jt.kundream.utils.ActivityUtil;
import jt.kundream.utils.CommonUtil;
import jt.kundream.utils.CurrencyUtils;

import static android.widget.ListPopupWindow.MATCH_PARENT;

import androidx.annotation.NonNull;
import androidx.camera.view.PreviewView;

/**
 * @author YangTianKun
 */

public class MainActivity extends BaseActivity<MainContract.View, MainPresenter> implements MainContract.View,CameraScan.OnScanResultCallback<List<Barcode>>{
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int REQUEST_CAMERA = 100;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 0X86;

    protected PreviewView previewView;
    private boolean mHasCamera;
    private boolean mHasDot;
    private CameraScan<List<Barcode>> mCameraScan;
    private SpeechUtils spk;
    private int readBytes=0, writtenBytes=0;
    private HumidityControlUtil humidityControlUtil;


    private AlarmManager mAlarmManager;
    private PendingIntent mPendingIntent;

    private static final int INTERVAL = 1000 * 60;
    private static final int DELAY = 5000;
//    com.king.mlkit.vision.barcode.BarcodeCameraScanActivity
    @Bind(R.id.iv_left)
    ImageView ivLeft;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.head)
    RelativeLayout head;
    @Bind(R.id.tv_1)
    TextView tv1;
    @Bind(R.id.rl_middle)
    RelativeLayout rlMiddle;
    @Bind(R.id.tv_digit_price)
    TextView tvDigitPrice;
    @Bind(R.id.tv_hand_input)
    TextView tvHandInput;
    @Bind(R.id.rl_scan)
    RelativeLayout rlScan;
    @Bind(R.id.iv_jiesuan)
    ImageView ivJiesuan;
    @Bind(R.id.tv_jiesuan)
    TextView tvJiesuan;
    @Bind(R.id.tv_totalnum)
    TextView tvTotalnum;
    @Bind(R.id.iv)
    ImageView iv;
    @Bind(R.id.ll_bottom)
    LinearLayout llBottom;
    @Bind(R.id.rv_goods)
    SwipeMenuRecyclerView rvGoods;
    @Bind(R.id.tv_left)
    TextView tvLeft;
    @Bind(R.id.rl_input)
    RelativeLayout rlInput;
    @Bind(R.id.ly_digit_keyboard)        //手动输入数字键盘
    LinearLayout lyDigitKeyboard;
    @Bind(R.id.rl_jiesuan)
    RelativeLayout rlJiesuan;

    private double toaMon = 0;
    private int toaNum = 0;

    //variables
    private int audioSource = MediaRecorder.AudioSource.VOICE_COMMUNICATION;
    private int samplingRate = 44100; /* in Hz*/
    private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int bufferSize = AudioRecord.getMinBufferSize(samplingRate, channelConfig, audioFormat);
    private int sampleNumBits = 16;
    private int numChannels = 1;

    /**
     * SCAN 按键
     */
    private ScanBroadcastReceiver scanBroadcastReceiver;

    /**
     * 要展示的商品列表集合
     */
    private List<TempOrderBean.TempGoodsBean> goodsList = new ArrayList<>();
    /**
     * 是否是主页面的判断，如果为true，所有的扫码结果都给主页面处理
     * 如果是false,看下一个变量
     */
    private boolean isMain = true;

    private Thread newThread; //声明一个子线程


    private Handler handler = new Handler();
    Runnable run = new Runnable() {
        @Override
        public void run() {

        }
    };
    private MediaPlayer player;
    private MediaPlayer di;
    private GoodsAdapter mAdapter = null;
    private SwipeMenuItem deleteItem;
    private SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeMenu, SwipeMenu swipeMenu1, int i) {
            deleteItem = new SwipeMenuItem(getContext());
            // 可以设置各种文字和图标了
            deleteItem.setHeight(MATCH_PARENT);
            deleteItem.setWidth(80);
            deleteItem.setText("删除");
            deleteItem.setTextColorResource(R.color.white);
            deleteItem.setBackground(R.color.yunbei_bg);
            swipeMenu1.addMenuItem(deleteItem);
        }
    };
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge swipeMenuBridge) {
            // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
            swipeMenuBridge.closeMenu();
            /**
             * 获取这些侧滑的必要信息
             */
            // 左侧还是右侧菜单。
            int direction = swipeMenuBridge.getDirection();
            // RecyclerView的Item的position。
            int adapterPosition = swipeMenuBridge.getAdapterPosition();
            // 菜单在RecyclerView的Item中的Position。
            int menuPosition = swipeMenuBridge.getPosition();
            // TODO: 2018/8/23   删除某一条目  刷新列表
            numMap.remove(goodsList.get(adapterPosition).getGoods_id());
            goodsList.remove(adapterPosition);
            mAdapter.notifyDataSetChanged();
            presenter.calcTotal(goodsList, numMap);
//            presenter.setSingleGood(goodsList, adapterPosition, AndroidUtil.getUniqueId(mContext));
        }
    };
    /**
     * item内部点击时间处理
     */
    private RequestCallback.ItemViewOnClickListener itemViewOnClickListener = new RequestCallback.ItemViewOnClickListener() {

        @Override
        public void itemViewClick(View view) {
            int posi = (int) view.getTag();
            int goodsId = goodsList.get(posi).getGoods_id();
            int num = numMap.get(goodsId);
            switch (view.getId()) {
                case R.id.tv_add:
                    numMap.put(goodsId, ++num);
                    mAdapter.notifyDataSetChanged();
                    presenter.calcTotal(goodsList, numMap);
                    break;
                case R.id.tv_min:
                    if (num < 2) {
                        showToast("删除请侧滑商品");
                    } else {
                        numMap.put(goodsId, --num);
                    }
                    mAdapter.notifyDataSetChanged();
                    presenter.calcTotal(goodsList, numMap);
                    break;

                default:
                    break;
            }
        }
    };


    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        tvTitle.setText("结算");
        tvRight.setVisibility(View.GONE);
        ivLeft.setImageResource(R.mipmap.time);
        ivRight.setImageResource(R.mipmap.person);
        registerListener();
        //延迟一秒打开串口，这个为了初始化扫描头，必须延迟一秒执行，否则会出现延迟打印或者打印不出的现象，需注意
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //打开扫描串口  但是延迟1秒执行的话，会出现进入之后立即点扫码无法实现功能的问题

            }
        }, 1000);
        player = MediaPlayer.create(getApplicationContext(), R.raw.beep);
        di = MediaPlayer.create(getApplicationContext(), R.raw.di);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        CommonUtil.put(this, "tempCount", 0);
        //注销物理scan按键的接受广播
        this.unregisterReceiver(scanBroadcastReceiver);
        //释放相机
//        releaseCamera();
//        System.exit(0);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusBean bean) {
        /**
         * 接受到扫码的用户支付码之后，请求信息。
         */
        String event = bean.getEvent();
        if (event.length() > 4) {
            if (event.substring(0, 8).equals("closeAct")) {
                String realMoney = event.substring(8, event.length());
                goodsList.clear();
                numMap.clear();
                mAdapter.notifyDataSetChanged();
                setToalData(CurrencyUtils.toBigDecimal("0"), 0);
            }
        } else {
            finish();
        }
    }


    @Override
    public MainPresenter initPresenter() {
        return new MainPresenter();
    }

    @OnClick({R.id.rl_input,R.id.digit0,R.id.digit1,R.id.digit2,R.id.digit3,R.id.digit4,R.id.digit5,R.id.digit6,R.id.digit7,R.id.digit8,R.id.digit9,R.id.dot,R.id.del})
    public void onDigitClick(View view) {
        String viewText = tvDigitPrice.getText().toString();
        switch (view.getId()) {

            case R.id.digit0:
            case R.id.digit1:
            case R.id.digit2:
            case R.id.digit3:
            case R.id.digit4:
            case R.id.digit5:
            case R.id.digit6:
            case R.id.digit7:
            case R.id.digit8:
            case R.id.digit9:
            case R.id.dot:
                if(view.getId() == R.id.dot){
                    if(viewText.length()<=0 || viewText.indexOf(".") != -1){
                        break;
                    }
                }
                String inputText = ((TextView)view).getText().toString();

                //判断输入是否合法(最多两位小数)
                if(!isStandardNumeric(tvDigitPrice.getText().toString() + inputText)){
                    break;
                }


                if(viewText.length()>16){
                    break;
                }
                di.start();
                tvDigitPrice.setText(tvDigitPrice.getText().toString() + inputText);
                break;
            case R.id.del:

                if(viewText.length()<=0){
                    break;
                }
                tvDigitPrice.setText(viewText.substring(0,viewText.length()-1));
                break;
            case R.id.rl_input:
                //点击显示手动输入界面
                //切换扫码添加和手动添加(无码商品)
                if(View.VISIBLE == lyDigitKeyboard.getVisibility()) {
                    if(mHasCamera){
                        rlScan.setVisibility(View.VISIBLE);//隐藏摄像头扫描
                    }
                    lyDigitKeyboard.setVisibility(View.GONE);

                    tvHandInput.setText(getString(R.string.hand_input));
                }else{
                    if(mHasCamera){
                        rlScan.setVisibility(View.GONE);//隐藏摄像头扫描
                    }
                    lyDigitKeyboard.setVisibility(View.VISIBLE);
                    lyDigitKeyboard.bringToFront();
                    tvHandInput.setText(getString(R.string.scan_input));
                }

                break;

        }


    }

    @OnClick({R.id.iv_left, R.id.iv_right, R.id.rl_input, R.id.rl_jiesuan,R.id.plus})
    public void onClick(View view) {
        //判断是否多次点击
        if (AntiShake.check(view.getId())) {
            return;
        }

        switch (view.getId()) {
            case R.id.plus:
                String priceText = tvDigitPrice.getText().toString();

                if(!isStandardNumeric(priceText) || new BigDecimal(priceText).compareTo(BigDecimal.valueOf(0.0))<=0){
                    spk.speak("请输入正确的商品价格");
                    break;
                }

                //点结尾的，去掉点
                if(priceText.endsWith(".")){
                    priceText = priceText.substring(0,priceText.length()-1);
                }

                //此处的逻辑处理，应该是将单个商品，放进集合中，将集合返给页面，在页面中展示
                TempOrderBean.TempGoodsBean goodsBean = new TempOrderBean.TempGoodsBean();
                int random = (int) ((Math.random() * 9 + 1) * (6));
                goodsBean.setGoods_id(-random);
                goodsBean.setSpec("件");
                goodsBean.setGoods_price(priceText);
                goodsBean.setGoods_name("无码商品");
                goodsBean.setBarcode("无条形码");
                goodsBean.setGoods_number(1);
                manageData(goodsBean);
//                spk.speak(priceText+"元商品添加成功");
                spk.speak("现在温度是"+humidityControlUtil.getTemperature()+"度，相对湿度百分之"+humidityControlUtil.getHumidity());
                //清空价款
                tvDigitPrice.setText("");
                break;
            case R.id.iv_left:
//                showToast("临时订单");

                if (goodsList.size() < 1) {
                    ActivityUtil.startActivityForResult(this, TempOrderActivity.class, 200);
                } else {
                    showToast("请优先处理当前订单");
                }

                break;
            case R.id.iv_right:
                ActivityUtil.startActivity(this, UserCenterActivity.class);
                break;

            case R.id.rl_jiesuan:
                if (goodsList != null && goodsList.size() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("tolMoney", String.valueOf(toaMon));
                    intent.putExtra("goods", (Serializable) goodsList);
                    ActivityUtil.startActivity(this, PayTypeActivity.class, intent);
                } else {
                    showToast("你还没有添加任何商品");
                }
                break;

            default:
                break;

        }
    }

    /**
     * 临时订单点击之后回传的数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode == 200) {
                TempOrderBean tempOrder = (TempOrderBean) data.getSerializableExtra("tempOrder");
                goodsList.addAll(tempOrder.getGoodsList());
                toaMon = tempOrder.getTotalMoney();
                toaNum = tempOrder.getTotalNum();
                for (int i = 0; i < goodsList.size(); i++) {
                    numMap.put(goodsList.get(i).getGoods_id(), goodsList.get(i).getGoods_number());
                }
                setAdapter();
            }
        }

    }

    /**
     * 输入商品条码的dialog
     */
    private void showInputDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(this, R.layout.dialog_input, null);
        // dialog.setView(view);// 将自定义的布局文件设置给dialog
        // 设置边距为0,保证在2.x的版本上运行没问题
        dialog.setView(view, 0, 0, 0, 0);

        final EditText etCode = (EditText) view.findViewById(R.id.et_goods_code);
        Button btnAdd = (Button) view.findViewById(R.id.btn_add_goods);

        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String s = etCode.getText().toString();
                presenter.addGoods(s, spk);
                dialog.dismiss();
            }
        });
        ActivityUtil.backgroundAlpha(1f, this);
        dialog.show();
    }


    /**
     * 注册广播接收器
     */
    private void registerListener() {
        //SCAN按键按下时候广播的接收器
        scanBroadcastReceiver = new ScanBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ismart.intent.scandown");
        this.registerReceiver(scanBroadcastReceiver, intentFilter);
    }


    private Map<Integer, Integer> numMap = new HashMap<>();

    @Override
    public void manageData(TempOrderBean.TempGoodsBean bean) {
        // TODO: 2018/8/23 如果这里是相同的goodsId，那么需要的是更改数量就行  如果不是相同的goodsId，那么做的就是集合中添加一个新数据
        presenter.checkGoodsIsSame(numMap, goodsList, bean);
    }

    @Override
    public void setNumMap(int goodsId, int num) {
        numMap.put(goodsId, num);
        setAdapter();
    }

    @Override
    public void addList(TempOrderBean.TempGoodsBean bean) {
        goodsList.add(bean);
    }

    @Override
    public void setToalData(BigDecimal totalMoney, int totalNum) {
        tvJiesuan.setText("结算(￥" + totalMoney + ")");
        tvTotalnum.setText("共计" + totalNum + "件商品");
        toaMon = totalMoney.doubleValue();
        toaNum = totalNum;
    }

    @Override
    public void showEmptyView() {
        rlMiddle.setVisibility(View.VISIBLE);
        rvGoods.setVisibility(View.GONE);
    }


    @Override
    public void setThisOrderTemp(int count) {
        hideProgress();
        CommonUtil.put(this, "tempCount", count);
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setText(count + "");
        /**
         * 将原本的数据存储在本地数据库之后  清空商品集合
         */
        goodsList.clear();
        numMap.clear();
        toaMon = 0;
        toaNum = 0;
        setToalData(BigDecimal.valueOf(toaMon), toaNum);
        rlMiddle.setVisibility(View.VISIBLE);
        rvGoods.setVisibility(View.GONE);
    }

    private void setAdapter() {
        rlMiddle.setVisibility(View.GONE);
        rvGoods.setVisibility(View.VISIBLE);
        if (mAdapter == null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rvGoods.setLayoutManager(linearLayoutManager);
            rvGoods.setItemAnimator(new DefaultItemAnimator());
            //设置侧滑时候弹出侧滑删除菜单
            rvGoods.setSwipeMenuCreator(mSwipeMenuCreator);
            //设置侧滑出来的菜单的删除监听
            rvGoods.setSwipeMenuItemClickListener(mMenuItemClickListener);
            mAdapter = new GoodsAdapter(this, R.layout.item_getmoneying, goodsList, numMap, itemViewOnClickListener);
            rvGoods.setAdapter(mAdapter);
            rlMiddle.setVisibility(View.GONE);
        } else {
            mAdapter.notifyDataSetChanged();
        }

        presenter.calcTotal(goodsList, numMap);

    }

    //SCAN按键的监听
    class ScanBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if (!isScan) {
//                //扫描头未处于扫描状态
//                //打开扫描头
//                mPosApi.gpioControl(mGpioTrig, 0, 0);
//                isScan = true;
//                handler.removeCallbacks(run);
//                handler.postDelayed(run, 1000);
//            } else {
//                //扫描头处于扫描头状态，先关掉扫描头光
//                mPosApi.gpioControl(mGpioTrig, 0, 1);
//                //打开扫描头
//                mPosApi.gpioControl(mGpioTrig, 0, 0);
//                isScan = true;
//                handler.removeCallbacks(run);
//                handler.postDelayed(run, 1000);
//            }
        }
    }





    /**
     * 在activity的生命周期中对isMain进行状态更改  如果Main是显示的  那么就让扫描接受的值在本页面处理  否则  就发送一个消息  让其它页面处理
     * 30669 - 11971 = 18698 -4955 - 9987  -
     */

    @Override
    protected void onStart() {
        super.onStart();
        int tempCount = CommonUtil.getInt(this, "tempCount");
        if (tempCount > 0) {
            tvLeft.setText(tempCount + "");
        } else {
            tvLeft.setVisibility(View.GONE);
        }
        isMain = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isMain = false;
    }

    /**
     * 返回键监听  这里是为了将正在结算的单子挂为临时订单
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /**
         * 如果用户按下的是返回键  此时需要将原来的订单和数据存在本地数据库
         */
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (goodsList.size() < 1) {
                showToast("您还没有添加任何订单");
            } else {
                showProgress();
                presenter.saveOrderDatas(goodsList, numMap, this, toaMon, toaNum);
            }
        }
        return true;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }
    }
    private void createTable(SQLiteDatabase db){
//创建表SQL语句
        String stu_table="create table IF NOT EXISTS goods(id integer primary key autoincrement,goods_name text,barcode text,price text,spec text,supplier text)";
//执行SQL语句
        db.execSQL(stu_table);
    }
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    @Override
    public void onScanResultCallback(@NonNull AnalyzeResult<List<Barcode>> result) {
        mCameraScan.setAnalyzeImage(false);

        for(Barcode it : result.getResult()){
            //过滤不全是数字的结果
           if(!isNumeric(it.getRawValue())){
               continue;
           }
            presenter.addGoods(it.getRawValue(), spk);

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

//   com.king.mlkit.vision.barcode.BarcodeCameraScanActivity
    /**
     * Get {@link CameraScan}
     * @return
     */
    public CameraScan<List<Barcode>> getCameraScan(){
        return mCameraScan;
    }

    /**
     * 创建{@link CameraScan}
     * @param previewView
     * @return
     */
    public CameraScan<List<Barcode>> createCameraScan(PreviewView previewView){
        return new BaseCameraScan<>(this,previewView);
    }

    /**
     * 创建分析器，默认分析所有条码格式
     * @return
     */
    @Nullable
    public Analyzer<List<Barcode>> createAnalyzer(){
        return new BarcodeScanningAnalyzer(Barcode.FORMAT_ALL_FORMATS);
    }
    /**
     * 初始化
     */
    public void initUI()  {
        previewView = findViewById(R.id.previewView);

        initCameraScan();

        //TODO 这里判断下，如果没有摄像头就影藏扫描框，设置成横屏--说明是在智能音箱上运行的


        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String[] cameraIdList = new String[0];
        try {
            cameraIdList = cameraManager.getCameraIdList();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        if (cameraIdList.length>0) {
            mHasCamera = true;
            rlScan.setVisibility(View.VISIBLE);//显示摄像头扫描
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
            startCamera();

        }else{
            mHasCamera = false;
            rlScan.setVisibility(View.GONE);//隐藏摄像头扫描
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
        }

    }

    /**
     * 初始化CameraScan
     */
    public void initCameraScan(){
        mCameraScan = createCameraScan(previewView)
                .setDarkLightLux(45f)//设置光线足够暗的阈值（单位：lux），需要通过{@link #bindFlashlightView(View)}绑定手电筒才有效
                .setBrightLightLux(100f)//设置光线足够明亮的阈值（单位：lux），需要通过{@link #bindFlashlightView(View)}绑定手电筒才有效
                .setAnalyzer(createAnalyzer())
                .setPlayBeep(true)
                .setVibrate(true)
                .setCameraConfig(new ResolutionCameraConfig(this))//设置CameraConfig
                .setOnScanResultCallback(this);
    }


    /**
     * 启动相机预览
     */
    public void startCamera(){
        if(mCameraScan != null){
            if(PermissionUtils.checkPermission(this,Manifest.permission.CAMERA)){
                mCameraScan.startCamera();
            }else{
                LogUtils.d("checkPermissionResult != PERMISSION_GRANTED");
                PermissionUtils.requestPermission(this,Manifest.permission.CAMERA,CAMERA_PERMISSION_REQUEST_CODE);
            }
        }
    }


    /**
     * 释放相机
     */
    private void releaseCamera(){
        if(mCameraScan != null){
            mCameraScan.release();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GreenDaoHelper.initDatabase();
        createTable(GreenDaoHelper.getDb());
        initUI();
        Context ctx =  getApplicationContext();
        spk = new SpeechUtils(ctx);

        Intent intent = new Intent(this, LiveService.class);
        if (Build.VERSION.SDK_INT>=26) {
            startForegroundService(intent);
        }else startService(intent);

        humidityControlUtil = new HumidityControlUtil();


        bufferSize += 2048;
        newThread = new Thread(new Runnable() {
            @Override
            public void run() {
                AudioRecord recorder = new AudioRecord(audioSource, samplingRate, channelConfig, audioFormat, bufferSize);
                recorder.startRecording();

                AudioTrack audioPlayer = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                        AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);

                if(audioPlayer.getPlayState() != AudioTrack.PLAYSTATE_PLAYING)
                    audioPlayer.play();

//capture data and record to file
                byte[] data = new byte[bufferSize];

                do{
                    readBytes = recorder.read(data, 0, bufferSize);

                    if(AudioRecord.ERROR_INVALID_OPERATION != readBytes){
                        writtenBytes += audioPlayer.write(data, 0, readBytes);
                    }

                }
                while(true);
            }
        });

//        newThread.start(); //启动线程



    }

    //判断是否合法数据
    public  boolean isStandardNumeric(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]{1,2})?");
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;
        }

        Matcher isNum = pattern.matcher(bigStr);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }



}
