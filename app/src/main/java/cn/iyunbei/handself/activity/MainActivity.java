package cn.iyunbei.handself.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.posapi.PosApi;
import android.posapi.PrintQueue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.OnClick;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.contract.MainContract;
import cn.iyunbei.handself.presenter.MainPresenter;
import cn.iyunbei.handself.utils.aboutclick.AntiShake;
import jt.kundream.base.BaseActivity;
import jt.kundream.utils.ActivityUtil;

/**
 * @author YangTianKun
 */
public class MainActivity extends BaseActivity<MainContract.View, MainPresenter> {

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
    @Bind(R.id.tv_hand_input)
    TextView tvHandInput;
    @Bind(R.id.iv_jiesuan)
    ImageView ivJiesuan;
    @Bind(R.id.tv_jiesuan)
    TextView tvJiesuan;
    @Bind(R.id.tv_totalnum)
    TextView tvTotalnum;

    private PosApi mPosApi = null;
    //GPIO电源的控制
    private static byte mGpioPower = 0x1E;// PB14
    private static byte mGpioTrig = 0x29;// PC9
    //SCAN 按键
    private ScanBroadcastReceiver scanBroadcastReceiver;
    private static int mCurSerialNo = 3; // usart3
    private static int mBaudrate = 4; // 9600
    private boolean isScan = false;
    private Handler handler = new Handler();
    Runnable run = new Runnable() {
        @Override
        public void run() {
            //到一定时间后拉低扫描头电压，关掉扫描光
            mPosApi.gpioControl(mGpioTrig, 0, 1);
            isScan = false;
        }
    };
    private MediaPlayer player;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
//        initConstants();
        tvTitle.setText("结算");
        tvRight.setVisibility(View.GONE);
        mPosApi = PosApi.getInstance(this);
        player = MediaPlayer.create(getApplicationContext(), R.raw.beep);
        initPrintQueue();
        registerListener();
        openScan();

    }


//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mPosApi = PosApi.getInstance(this);
//        player = MediaPlayer.create(getApplicationContext(), R.raw.beep);
//    }
//
//    /**
//     * 初始化一些必须的一些常量值
//     */
//    private void initConstants() {
//        mPosApi = PosApi.getInstance(this);
//        player = MediaPlayer.create(getApplicationContext(), R.raw.beep);
//    }

//    15917694315
//
//    123456

    @Override
    public MainPresenter initPresenter() {
        return new MainPresenter();
    }

    @OnClick({R.id.iv_left, R.id.iv_right, R.id.tv_hand_input, R.id.tv_jiesuan})
    public void onClick(View view) {
        //判断是否多次点击
        if (AntiShake.check(view.getId())) {
            return;
        }

        switch (view.getId()) {
            case R.id.iv_left:
                showToast("临时订单");
                break;

            case R.id.iv_right:
                showToast("个人中心");
                break;

            case R.id.tv_hand_input:
                //点击弹出输入框
                ActivityUtil.backgroundAlpha(0.6f, this);
                showInputDialog();

                break;

            case R.id.tv_jiesuan:
                showToast("进入付款页");
                break;

            default:
                break;

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
                presenter.addGoods(s);
                showToast("请求网络，添加商品=======" + s);
                // TODO: 2018/8/20 成功之后添加商品，dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 扫码复上电位
     */
    private void openScan() {
        // GPIO控制器初始化
        mPosApi.gpioControl(mGpioPower, 0, 1);
        //扫描串口初始化
        mPosApi.extendSerialInit(mCurSerialNo, mBaudrate, 1, 1, 1, 1);
    }

    /**
     * 注册广播接收器
     */
    private void registerListener() {
        //注册扫描信息的接收器
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(PosApi.ACTION_POS_COMM_STATUS);
        registerReceiver(receiver_, mFilter);
        //SCAN按键按下时候广播的接收器
        scanBroadcastReceiver = new ScanBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ismart.intent.scandown");
        this.registerReceiver(scanBroadcastReceiver, intentFilter);
    }

    private PrintQueue mPrintQueue = null;

    private void initPrintQueue() {
        //打印队列赋值
        mPrintQueue = new PrintQueue(getApplicationContext(), mPosApi);
        //打印队列初始化
        mPrintQueue.init();
        //打印队列设置监听
        mPrintQueue.setOnPrintListener(new PrintQueue.OnPrintListener() {
            //打印完成
            @Override
            public void onFinish() {
                //打印完成
                new Thread() {

                    @Override
                    public void run() {
                        //...你的业务逻辑；
                        Message message = new Message();//发送一个消息，该消息用于在handleMessage中区分是谁发过来的消息；
                        message.what = 1;
//                        handlerui.sendMessage(message);
                    }
                }.start();

                Toast.makeText(getApplicationContext(), "打印完成", Toast.LENGTH_SHORT).show();
            }

            //打印失败
            @Override
            public void onFailed(int state) {

                switch (state) {
                    case PosApi.ERR_POS_PRINT_NO_PAPER:
                        // 打印缺纸
                        Toast.makeText(getApplicationContext(), "打印缺纸", Toast.LENGTH_SHORT).show();
                        break;
                    case PosApi.ERR_POS_PRINT_FAILED:
                        // 打印失败
                        Toast.makeText(getApplicationContext(), "打印失败", Toast.LENGTH_SHORT).show();
                        break;
                    case PosApi.ERR_POS_PRINT_VOLTAGE_LOW:
                        // 电压过低
                        Toast.makeText(getApplicationContext(), "电压过低", Toast.LENGTH_SHORT).show();
                        break;
                    case PosApi.ERR_POS_PRINT_VOLTAGE_HIGH:
                        // 电压过高
                        Toast.makeText(getApplicationContext(), "电压过高", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onGetState(int arg0) {
            }

            //打印设置
            @Override
            public void onPrinterSetting(int state) {
                switch (state) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "持续有纸", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        //缺纸
                        Toast.makeText(getApplicationContext(), "缺纸", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "检测到黑标", Toast.LENGTH_SHORT).show();
                        //检测到黑标
                        break;
                }
            }
        });
    }

    //SCAN按键的监听
    class ScanBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isScan) {
                //扫描头未处于扫描状态
                //打开扫描头
                mPosApi.gpioControl(mGpioTrig, 0, 0);
                isScan = true;
                handler.removeCallbacks(run);
                handler.postDelayed(run, 1000);
            } else {
                //扫描头处于扫描头状态，先关掉扫描头光
                mPosApi.gpioControl(mGpioTrig, 0, 1);
                //打开扫描头
                mPosApi.gpioControl(mGpioTrig, 0, 0);
                isScan = true;
                handler.removeCallbacks(run);
                handler.postDelayed(run, 1000);
            }
        }
    }


    /**
     * 扫描头扫描信息接收器
     */
    BroadcastReceiver receiver_ = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equalsIgnoreCase(PosApi.ACTION_POS_COMM_STATUS)) {
                int cmdFlag = intent.getIntExtra(PosApi.KEY_CMD_FLAG, -1);
                byte[] buffer = intent
                        .getByteArrayExtra(PosApi.KEY_CMD_DATA_BUFFER);
                switch (cmdFlag) {
                    // 传输扫描信息的串口
                    case PosApi.POS_EXPAND_SERIAL3:
                        //如果为空，返回
                        if (buffer == null) {
                            return;
                        }
                        //播放扫描音，提示已经扫描到信息
                        player.start();
                        try {

                            //把扫描头传过来的byte字节转成字符串
                            String str = new String(buffer, "GBK");
//                            presenter.getPickGoods(tuid, token, toid_cotent + "", str.trim());
                            presenter.addGoods(str);
                            //准备通过广播发送扫描信息，如果是集成进自己项目，此段可忽略
                            isScan = false;
                            //拉低扫描头电压，使扫描头熄灭
                            //  mPosApi.gpioControl(mGpioTrig, 0, 1);
                            //移除扫描头熄灭线程
                            handler.removeCallbacks(run);
                            ;

                        } catch (UnsupportedEncodingException e) {

                            e.printStackTrace();
                        }
                        break;
                }
                buffer = null;
            }
        }
    };


}
