package cn.iyunbei.handself.activity;
import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTouch;
import cn.iyunbei.handself.Constants;
import cn.iyunbei.handself.MyApp;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.adapter.GoodsAdapter;
import cn.iyunbei.handself.bean.GoodsDataBean;
import cn.iyunbei.handself.bean.TempOrderBean;
import cn.iyunbei.handself.contract.MainContract;
import cn.iyunbei.handself.greendao.GreenDaoHelper;
import cn.iyunbei.handself.presenter.MainPresenter;
import cn.iyunbei.handself.presenter.SpeechUtils;
import cn.iyunbei.handself.service.BleGattClient;
import cn.iyunbei.handself.service.BleGattServer;
import cn.iyunbei.handself.service.BluetoothService;
import cn.iyunbei.handself.service.LiveService;
import cn.iyunbei.handself.service.MynahAI;
import cn.iyunbei.handself.service.ScanService;
import cn.iyunbei.handself.service.TemperatureService;
import cn.iyunbei.handself.utils.EditTextWithText;
import cn.iyunbei.handself.utils.aboutclick.AntiShake;
import jt.kundream.base.BaseActivity;
import jt.kundream.bean.EventBusBean;
import jt.kundream.utils.ActivityUtil;
import jt.kundream.utils.CommonUtil;
import jt.kundream.utils.CurrencyUtils;
import jt.kundream.utils.ToastUtils;

import static android.widget.ListPopupWindow.MATCH_PARENT;

import androidx.annotation.NonNull;

/**
 * @author YangTianKun
 */

public class MainActivity extends BaseActivity<MainContract.View, MainPresenter> implements MainContract.View{
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int REQUEST_CAMERA = 100;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 0X86;

    private boolean mHasCamera;


    private BleGattServer mBleGattServer = null;          //Ble??????????????????????????????
    private  BleGattClient mBleGattClient= null;          //Ble??????????????????????????????



    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final String DEVICE_NAME = "device_name";
    private String mConnectedDeviceName = null;
    private static final int REQUEST_CONNECT_DEVICE = 300;  //??????????????????
    private static final int REQUEST_ENABLE_BLUETOOTH = 301;  //??????????????????

    private static final int REQUEST_CONNECT_BLE = 302;  //????????????BLE?????????????????????

    //Check Permissions
    private static final int REQUEST_CODE_PERMISSION = 800;
    private static String[] PERMISSIONS_REQ = {
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_PRIVILEGED,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private BluetoothAdapter mBluetoothAdapter = null;

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
    @Bind(R.id.ly_digit_keyboard)        //????????????????????????
    LinearLayout lyDigitKeyboard;
    @Bind(R.id.rl_jiesuan)
    RelativeLayout rlJiesuan;

    private double toaMon = 0;
    private int toaNum = 0;



    /**
     * SCAN ??????
     */
    private ScanBroadcastReceiver scanBroadcastReceiver;

    /**
     * ??????????????????????????????
     */
    private List<TempOrderBean.TempGoodsBean> goodsList = new ArrayList<>();
    /**
     * ???????????????????????????????????????true?????????????????????????????????????????????
     * ?????????false,??????????????????
     */
    private boolean isMain = true;

    private Thread newThread; //?????????????????????
    //????????????
    private ScanService mScanService;

    private MynahAI mMynahAI;
    private  Thread thread;

    /**
     * ??????????????????????????????
     * @param data
     */
    @Override
    public void showResult(GoodsDataBean data) {
        //???????????????????????????????????????
        if(data.getGoodsName() != null && !data.getGoodsName().isEmpty()){
            TempOrderBean.TempGoodsBean goodsBean = new TempOrderBean.TempGoodsBean();
            goodsBean.setGoods_id(data.getId());
            goodsBean.setSpec(data.getSpec());
            goodsBean.setGoods_price(data.getPrice());
            goodsBean.setGoods_name(data.getGoodsName());
            goodsBean.setBarcode(data.getBarcode());
            goodsBean.setSupplier(data.getSupplier());
            goodsBean.setGoods_number(1);
            presenter.checkGoodsIsSame(numMap, goodsList, goodsBean);

            //?????????????????????
        }else{
            MyApp.getInstance().say("??????????????????????????????????????????????????????????????????");
            showInputDialog(data);
        }

    }

    /**
     * ??????????????????dialog
     */
    private void showInputDialog(GoodsDataBean data) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(this, R.layout.dialog_input, null);
        // dialog.setView(view);// ????????????????????????????????????dialog
        // ???????????????0,?????????2.x???????????????????????????
        dialog.setView(view, 0, 0, 0, 0);

        TextView etCode = (TextView) view.findViewById(R.id.tv_goods_code);
        etCode.setText(data.getBarcode());
        EditText etGuide = (EditText) view.findViewById(R.id.et_goods_guige);
        etGuide.setText(data.getSpec());
        EditText etName = (EditText) view.findViewById(R.id.et_goods_name);
        etName.setText(data.getGoodsName());
        EditTextWithText etMoney = (EditTextWithText) view.findViewById(R.id.et_money);
        if(data.getPrice() == null || data.getPrice().isEmpty()){
            etMoney.setLeadText("???");
            etMoney.setText("");
        }else{
            etMoney.setLeadText("???");
            etMoney.setText(data.getPrice());
        }

        EditText etSupplier = (EditText) view.findViewById(R.id.et_supplier);
        etSupplier.setText(data.getSupplier());


        Log.d(TAG,"??????position="+data.getPosition());
        Button btnModify = (Button) view.findViewById(R.id.btn_add_goods);

        btnModify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String barcode = etCode.getText().toString();
                String goodsName = etName.getText().toString();
                String supplier = etSupplier.getText().toString();
                String price = etMoney.getText().toString();
                String psec = etGuide.getText().toString();
                Log.d(TAG,"??????psec="+psec);
                if(barcode.isEmpty() || goodsName.isEmpty() || supplier.isEmpty() || price.isEmpty() || psec.isEmpty()){
                    ToastUtils.showShort(getApplicationContext(), "??????????????????");
                    return;
                }
                presenter.saveGoodsInfo(data.getPosition(),barcode,goodsName,supplier,price,psec,MainActivity.this);

                dialog.dismiss();
            }
        });
        ActivityUtil.backgroundAlpha(1f, this);
        dialog.show();
    }
    private MediaPlayer player;
    private MediaPlayer di;
    private GoodsAdapter mAdapter = null;
    private SwipeMenuItem deleteItem;
    private SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeMenu, SwipeMenu swipeMenu1, int i) {
            deleteItem = new SwipeMenuItem(getContext());
            // ????????????????????????????????????
            deleteItem.setHeight(MATCH_PARENT);
            deleteItem.setWidth(80);
            deleteItem.setText("??????");
            deleteItem.setTextColorResource(R.color.white);
            deleteItem.setBackground(R.color.yunbei_bg);
            swipeMenu1.addMenuItem(deleteItem);
        }
    };
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge swipeMenuBridge) {
            // ??????????????????????????????????????????????????????Item???????????????????????????
            swipeMenuBridge.closeMenu();
            /**
             * ?????????????????????????????????
             */
            // ???????????????????????????
            int direction = swipeMenuBridge.getDirection();
            // RecyclerView???Item???position???
            int adapterPosition = swipeMenuBridge.getAdapterPosition();
            // ?????????RecyclerView???Item??????Position???
            int menuPosition = swipeMenuBridge.getPosition();
            // TODO: 2018/8/23   ??????????????????  ????????????
            numMap.remove(goodsList.get(adapterPosition).getGoods_id());
            goodsList.remove(adapterPosition);
            mAdapter.notifyDataSetChanged();
            presenter.calcTotal(goodsList, numMap);
//            presenter.setSingleGood(goodsList, adapterPosition, AndroidUtil.getUniqueId(mContext));
        }
    };
    /**
     * item????????????????????????
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
                        showToast("?????????????????????");
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
        tvTitle.setText("??????");
        tvRight.setVisibility(View.GONE);
        ivLeft.setImageResource(R.mipmap.time);
        ivRight.setImageResource(R.mipmap.person);
        registerListener();
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //??????????????????  ????????????1?????????????????????????????????????????????????????????????????????????????????

            }
        }, 1000);
        player = MediaPlayer.create(getApplicationContext(), R.raw.beep);
        di = MediaPlayer.create(getApplicationContext(), R.raw.di);
    }




    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusBean bean) {
        /**
         * ?????????????????????????????????????????????????????????
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
    @OnTouch({R.id.btn_scan})
    public boolean onTouch(View view, MotionEvent event) {
        switch (view.getId()) {
            case R.id.btn_scan:

                if(event.getAction() == MotionEvent.ACTION_UP){
                    Log.d(TAG, "cansal button ---> cancel");
                    if(mHasCamera){
                        mScanService.stop();
                    }

                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //??????????????????????????????????????????
                    if(MyApp.getInstance().mUseConnecting && mBleGattClient.getState() != Constants.STATE_CONNECTED){
                        MyApp.getInstance().say("??????????????????????????????????????????");
                        Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
                        ActivityUtil.startActivityForResult(this, BleDeviceActivity.class, REQUEST_CONNECT_BLE);
                        return false;
                    }

                    Log.d(TAG, "cansal button ---> down");
                    if(mHasCamera){
                        mScanService.start();

                    }

                }


        }

        return false;
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

                //????????????????????????(??????????????????)
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
                //??????????????????????????????
                //?????????????????????????????????(????????????)
                if(View.VISIBLE == lyDigitKeyboard.getVisibility()) {
//                    if(mHasCamera){
//                        rlScan.setVisibility(View.VISIBLE);//?????????????????????
//                    }
                    lyDigitKeyboard.setVisibility(View.GONE);

                    tvHandInput.setText(getString(R.string.hand_input));
                }else{
//                    if(mHasCamera){
//                        rlScan.setVisibility(View.GONE);//?????????????????????
//                    }
                    lyDigitKeyboard.setVisibility(View.VISIBLE);
                    lyDigitKeyboard.bringToFront();
                    tvHandInput.setText(getString(R.string.scan_input));
                }

                break;

        }


    }

    @OnClick({R.id.iv_left, R.id.iv_right, R.id.rl_input, R.id.rl_jiesuan,R.id.plus})
    public void onClick(View view) {
        //????????????????????????
        if (AntiShake.check(view.getId())) {
            return;
        }

        switch (view.getId()) {
            case R.id.plus:
                String priceText = tvDigitPrice.getText().toString();

                if(!isStandardNumeric(priceText) || new BigDecimal(priceText).compareTo(BigDecimal.valueOf(0.0))<=0){
                    MyApp.getInstance().say("??????????????????????????????");
                    break;
                }

                //????????????????????????
                if(priceText.endsWith(".")){
                    priceText = priceText.substring(0,priceText.length()-1);
                }

                //???????????????????????????????????????????????????????????????????????????????????????????????????????????????
                TempOrderBean.TempGoodsBean goodsBean = new TempOrderBean.TempGoodsBean();
                int random = (int) ((Math.random() * 9 + 1) * (6));
                goodsBean.setGoods_id(-random);
                goodsBean.setSpec("???");
                goodsBean.setGoods_price(priceText);
                goodsBean.setGoods_name("????????????");
                goodsBean.setBarcode("????????????");
                goodsBean.setGoods_number(1);
                manageData(goodsBean);
                MyApp.getInstance().say(priceText+"?????????????????????");
                //????????????
                tvDigitPrice.setText("");
                break;
            case R.id.iv_left:
//                showToast("????????????");

                if (goodsList.size() < 1) {
                    ActivityUtil.startActivityForResult(this, TempOrderActivity.class, 200);
                } else {
                    showToast("???????????????????????????");
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
                    ActivityUtil.startActivityForResult(this, PayTypeActivity.class, intent,500);
                } else {
                    showToast("??????????????????????????????");
                }
                break;

            default:
                break;

        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //???????????????
        if(data == null){
            return;
        }
        if (requestCode == 200) {
            //?????????????????????????????????
                TempOrderBean tempOrder = (TempOrderBean) data.getSerializableExtra("tempOrder");
                goodsList.addAll(tempOrder.getGoodsList());
                toaMon = tempOrder.getTotalMoney();
                toaNum = tempOrder.getTotalNum();
                for (int i = 0; i < goodsList.size(); i++) {
                    numMap.put(goodsList.get(i).getGoods_id(), goodsList.get(i).getGoods_number());
                }
                setAdapter();
        }else if(requestCode == REQUEST_ENABLE_BLUETOOTH){
                setupBluetoothDev();
        }else if(requestCode == REQUEST_CONNECT_BLE){
            String address = data.getExtras().getString(DeviceList.EXTRA_DEVICE_ADDRESS);
            Toast.makeText(this, "????????????:"+address, Toast.LENGTH_SHORT).show();
            mBleGattClient.connect(address);
            //???????????????????????????
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().putString(getString(R.string.default_dev),address).apply();

        }else if(requestCode == 500){
            if (resultCode == Activity.RESULT_OK) {
                goodsList.clear();
                numMap.clear();
                mAdapter.notifyDataSetChanged();
                setToalData(CurrencyUtils.toBigDecimal("0"), 0);

            }

        }

    }



    /**
     * ?????????????????????
     */
    private void registerListener() {
        //SCAN????????????????????????????????????
        scanBroadcastReceiver = new ScanBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ismart.intent.scandown");
        this.registerReceiver(scanBroadcastReceiver, intentFilter);
    }


    private Map<Integer, Integer> numMap = new HashMap<>();

    @Override
    public void manageData(TempOrderBean.TempGoodsBean bean) {
        // TODO: 2018/8/23 ????????????????????????goodsId???????????????????????????????????????  ?????????????????????goodsId???????????????????????????????????????????????????
        // TODO: 2022/10/12 ???????????????????????????????????????????????????????????????????????????
        if(bean.getGoods_price().isEmpty()){
            MyApp.getInstance().say("??????????????????????????????????????????,?????????????????????");
            GoodsDataBean data = new GoodsDataBean();
            data.setBarcode(bean.getBarcode());
            data.setId(bean.getGoods_id());
            data.setGoodsName(bean.getGoods_name());
            data.setPrice(bean.getGoods_price());
            data.setSpec(bean.getSpec());
            data.setSupplier(bean.getSupplier());
            showInputDialog(data);
            return;

        }
        MyApp.getInstance().say(bean.getGoods_name());
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
        tvJiesuan.setText("??????(???" + totalMoney + ")");
        tvTotalnum.setText("??????" + totalNum + "?????????");
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
         * ????????????????????????????????????????????????  ??????????????????
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
            //??????????????????????????????????????????
            rvGoods.setSwipeMenuCreator(mSwipeMenuCreator);
            //??????????????????????????????????????????
            rvGoods.setSwipeMenuItemClickListener(mMenuItemClickListener);
            mAdapter = new GoodsAdapter(this, R.layout.item_getmoneying, goodsList, numMap, itemViewOnClickListener);
            rvGoods.setAdapter(mAdapter);
            rlMiddle.setVisibility(View.GONE);
        } else {
            mAdapter.notifyDataSetChanged();
        }

        presenter.calcTotal(goodsList, numMap);

    }

    //SCAN???????????????
    class ScanBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if (!isScan) {
//                //??????????????????????????????
//                //???????????????
//                mPosApi.gpioControl(mGpioTrig, 0, 0);
//                isScan = true;
//                handler.removeCallbacks(run);
//                handler.postDelayed(run, 1000);
//            } else {
//                //??????????????????????????????????????????????????????
//                mPosApi.gpioControl(mGpioTrig, 0, 1);
//                //???????????????
//                mPosApi.gpioControl(mGpioTrig, 0, 0);
//                isScan = true;
//                handler.removeCallbacks(run);
//                handler.postDelayed(run, 1000);
//            }
        }
    }





    /**
     * ???activity?????????????????????isMain??????????????????  ??????Main????????????  ????????????????????????????????????????????????  ??????  ?????????????????????  ?????????????????????
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
     * ???????????????  ?????????????????????????????????????????????????????????
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /**
         * ?????????????????????????????????  ????????????????????????????????????????????????????????????
         */
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (goodsList.size() < 1) {
                showToast("??????????????????????????????");
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
//?????????SQL??????
        String stu_table="create table IF NOT EXISTS goods(id integer primary key autoincrement,goods_name text,barcode text,price text,spec text,supplier text)";
//??????SQL??????
        db.execSQL(stu_table);
    }

    /**
     * ?????????
     */
    public void initUI()  {

        //TODO ???????????????????????????????????????????????????????????????????????????--????????????????????????????????????
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String[] cameraIdList = new String[0];
        try {
            cameraIdList = cameraManager.getCameraIdList();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        if (cameraIdList.length>0) {
            mHasCamera = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//??????
        }else{
            mHasCamera = false;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//??????
        }

    }



    //????????????????????????
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


    private void setupBluetoothDev() {
        // ???????????????????????????
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "???????????????", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // ??????????????????
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ActivityUtil.startActivityForResult(this,enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
            return;
        }


        //Ble????????????????????? ---------->
        mBleGattServer = new BleGattServer(this,mHandler);

        //Ble????????????????????? ---------->?????????
        mBleGattClient = new  BleGattClient(this);
        //????????????????????????????????????????????????????????????
        if(MyApp.getInstance().mUseConnecting){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String address = prefs.getString(getString(R.string.default_dev), "");
            if(!address.isEmpty()){
                mBleGattClient.connect(address);
            }
        }
    }
    //??????Handler?????????UI???????????????????????????????????????
    private final Handler mHandler = new Handler() {   //????????????
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case Constants.STATE_CONNECTED:
                            if(mConnectedDeviceName == null) {
                                tvTitle.setText("??????(?????????)");
                                MyApp.getInstance().say("?????????");
                            }else{
                                tvTitle.setText("??????(?????????"+mConnectedDeviceName+")");
                                MyApp.getInstance().say("?????????"+mConnectedDeviceName);
                            }

                            hideProgress();
                            break;
                        case Constants.STATE_CONNECTING:
                            MyApp.getInstance().say("???????????????");
                            break;
                        case Constants.STATE_LISTEN:
                        case Constants.STATE_NONE:
                            MyApp.getInstance().say("??????????????????");
                            tvTitle.setText("??????(?????????)");
                            break;
                    }
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    //??????????????????????????????????????????
                    if(!isForeground(MainActivity.this)){
                        Log.d(TAG, "????????????");
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);

                        intent.addCategory(Intent.CATEGORY_LAUNCHER);

                        intent.setAction(Intent.ACTION_MAIN);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        MainActivity.this.startActivity(intent);
                    }

                    presenter.addGoods(readMessage);
                    break;
                case MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    break;

            }
        }
    };

    private boolean sendMessage(String message) {
        //?????????????????????????????????????????????true
        if(!MyApp.getInstance().mUseConnecting){
            return true;
        }

        if (message.length() > 0) {
            mBleGattClient.SendBarcode(message);
            return true;
        }
        return false;
    }

    private void showPermissionDenyDialog() {
        PermissionDialog dialog = new PermissionDialog();
        dialog.show(getSupportFragmentManager(), "PermissionDeny");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Permission.REQUEST_CODE) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    showPermissionDenyDialog();
                    return;
                }
            }
        }
    }


    /**
     * Checks if the app has permission to write to device storage or open camera
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    private static boolean verifyPermissions(Activity activity) {
        // Check if we have write permission

        int internet_permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET);
        int bluetooth_permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH);
        int location_permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int bluetooth_a_permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_ADMIN);
        int bluetooth_p_permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_PRIVILEGED);
        int write_permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read_permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int camera_permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        int phone_state_permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);

        if (write_permission != PackageManager.PERMISSION_GRANTED ||
                read_permission != PackageManager.PERMISSION_GRANTED ||
                bluetooth_permission !=PackageManager.PERMISSION_GRANTED ||
                bluetooth_a_permission !=PackageManager.PERMISSION_GRANTED ||
                bluetooth_p_permission !=PackageManager.PERMISSION_GRANTED ||
                phone_state_permission != PackageManager.PERMISSION_GRANTED ||
                internet_permission != PackageManager.PERMISSION_GRANTED ||
                location_permission != PackageManager.PERMISSION_GRANTED ||
                camera_permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "JNI read_permission" );
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_REQ,
                    REQUEST_CODE_PERMISSION
            );
            return false;
        } else {
            return true;
        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupBluetoothDev();

        GreenDaoHelper.initDatabase();
        createTable(GreenDaoHelper.getDb());
        initUI();


        Intent intent = new Intent(this, LiveService.class);
        if (Build.VERSION.SDK_INT>=26) {
            startForegroundService(intent);
        }else startService(intent);



        verifyPermissions(this);



        mScanService = new ScanService(this) {
            @Override
            public void onScanResult(String code) {
                Log.d(TAG,code);

                //??????????????????????????????????????????????????????
                if(sendMessage(code) && !MyApp.getInstance().mEnableScanOnly){
                    presenter.addGoods(code);
                }
            }
        };


        //?????????????????????
        if(MyApp.getInstance().isFiberHome()){
            //Android 6.0??????????????????????????????,??????????????????
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !Settings.canDrawOverlays(getApplicationContext())) {
                startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + this.getPackageName())));
            }
            Intent serviceIntent= new Intent(MainActivity.this, TemperatureService.class);
            startService(serviceIntent);
        }


        mMynahAI = new MynahAI(){

            @Override
            public void onScanResult(int code) {
                MyApp.getInstance().say("????????????"+code+"??????");
                Log.d(TAG,"????????????"+code+"??????");

            }
        };
        String md  = MynahAI.copyFromAssetsToCache("picodet_320_lite.nb",this);
        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                mMynahAI.loadModel(md);
            }
        });
        thread.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        CommonUtil.put(this, "tempCount", 0);
        //????????????scan?????????????????????
        this.unregisterReceiver(scanBroadcastReceiver);
        System.exit(0);


    }

    //??????????????????????????????
    private boolean isForeground(Context context) {
        if (context != null) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> processes = activityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo: processes) {
                if (processInfo.processName.equals(context.getPackageName())) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
