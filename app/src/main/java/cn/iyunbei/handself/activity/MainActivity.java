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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
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

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.adapter.GoodsAdapter;
import cn.iyunbei.handself.bean.GoodsBean;
import cn.iyunbei.handself.contract.MainContract;
import cn.iyunbei.handself.presenter.MainPresenter;
import cn.iyunbei.handself.utils.aboutclick.AntiShake;
import jt.kundream.base.BaseActivity;
import jt.kundream.utils.ActivityUtil;
import jt.kundream.utils.CommonUtil;

import static android.widget.ListPopupWindow.MATCH_PARENT;

/**
 * @author YangTianKun
 */
public class MainActivity extends BaseActivity<MainContract.View, MainPresenter> implements MainContract.View {

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
    @Bind(R.id.iv)
    ImageView iv;
    @Bind(R.id.ll_bottom)
    LinearLayout llBottom;
    @Bind(R.id.rv_goods)
    SwipeMenuRecyclerView rvGoods;
    @Bind(R.id.tv_left)
    TextView tvLeft;

    private double toaMon = 0;
    private int toaNum = 0;

    private PosApi mPosApi = null;
    //GPIO电源的控制
    private static byte mGpioPower = 0x1E;// PB14
    private static byte mGpioTrig = 0x29;// PC9
    /**
     * SCAN 按键
     */
    private ScanBroadcastReceiver scanBroadcastReceiver;
    // usart3
    private static int mCurSerialNo = 3;
    // 9600
    private static int mBaudrate = 4;
    private boolean isScan = false;
    /**
     * 要展示的商品列表集合
     */
    private List<GoodsBean.DataBean> goodsList = new ArrayList<>();
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
            goodsList.remove(menuPosition);
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
        public void clickAddOrMin(View view) {
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
//        initConstants();
        tvTitle.setText("结算");
        tvRight.setVisibility(View.GONE);
        ivLeft.setImageResource(R.mipmap.time);
        ivRight.setImageResource(R.mipmap.person);
        mPosApi = PosApi.getInstance(this);
        openScan();
        player = MediaPlayer.create(getApplicationContext(), R.raw.beep);
        initPrintQueue();
        registerListener();
    }


//    /**
//     * 初始化一些必须的一些常量值
//     */
//    private void initConstants() {
//        mPosApi = PosApi.getInstance(this);
//        player = MediaPlayer.create(getApplicationContext(), R.raw.beep);
//    }

//    15917694315
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
//                showToast("临时订单");
                ActivityUtil.startActivityForResult(this, TempOrderActivity.class, 200);
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
                presenter.quaryAllData();
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
                presenter.addGoods(s, CommonUtil.getString(MainActivity.this, "token"));
                dialog.dismiss();
            }
        });
        ActivityUtil.backgroundAlpha(1f, this);
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

    private Map<Integer, Integer> numMap = new HashMap<>();

    @Override
    public void manageData(GoodsBean.DataBean bean) {
        // TODO: 2018/8/23 如果这里是相同的goodsId，那么需要的是更改数量就行  如果不是相同的goodsId，那么做的就是集合中添加一个新数据
//        goodsList.add(bean);
        presenter.checkGoodsIsSame(numMap, goodsList, bean);
    }

    @Override
    public void setNumMap(int goodsId, int num) {
        numMap.put(goodsId, num);
        setAdapter();
    }

    @Override
    public void addList(GoodsBean.DataBean bean) {
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
    public void setThisOrderTemp(long count) {
        hideProgress();
        tvLeft.setText(count + "");
        /**
         * 将原本的数据存储在本地数据库之后  清空商品集合
         */
        goodsList.clear();
        numMap.clear();
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
                            String substring = str.substring(0, str.length() - 2);
//                            presenter.getPickGoods(tuid, token, toid_cotent + "", str.trim());
                            presenter.addGoods(substring, CommonUtil.getString(MainActivity.this, "token"));
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
//        return super.onKeyDown(keyCode, event);
    }
}
