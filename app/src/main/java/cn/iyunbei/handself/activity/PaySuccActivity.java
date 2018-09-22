package cn.iyunbei.handself.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iyunbei.handself.R;
import jt.kundream.bean.EventBusBean;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/9/5
 * @e-mail: 245086168@qq.com
 * @desc:支付成功页面，逻辑比较少，不用mvp了
 **/
public class PaySuccActivity extends AppCompatActivity {

    @Bind(R.id.iv_left)
    ImageView ivLeft;
    @Bind(R.id.tv_left)
    TextView tvLeft;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.tv_pay_result)
    TextView tvPayResult;
    @Bind(R.id.tv_total)
    TextView tvTotal;
    @Bind(R.id.tv_time_back)
    TextView tvTimeBack;
    @Bind(R.id.rl_bottom)
    RelativeLayout rl_bottom;
    private int goodsNum;
    private String tolMon;
    private String realMoney;

    private final int MSG_WHAT_START = 1;

    /**
     * 剩余倒计时时间
     */
    private long mLastMillis = 5000;
    /**
     * 间隔时间差 也就是发送handler消息的间隔时间
     */
    private long mIntervalMills = 1000;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_WHAT_START:
                    if (mLastMillis > 0) {
                        mLastMillis -= mIntervalMills;
                        handler.sendEmptyMessageDelayed(MSG_WHAT_START, mIntervalMills);
                        tvTimeBack.setText("(" + mLastMillis / 1000 + ")");
                    } else {
                        finish();
//                        EventBus.getDefault().post(new EventBusBean("closeAct"));
                    }

                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiesuan_succ);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
        goodsNum = getIntent().getIntExtra("goodsNum", -1);
        tolMon = getIntent().getStringExtra("tolMon");
        realMoney = getIntent().getStringExtra("realMoney");
        initView();
    }

    private void initView() {
        tvTitle.setText("结算状态");
        tvLeft.setVisibility(View.GONE);
        tvRight.setVisibility(View.GONE);
        ivLeft.setVisibility(View.GONE);
        ivRight.setVisibility(View.GONE);
        tvTotal.setText("共" + goodsNum + "件商品，共支付" + realMoney + "元");
        handler.sendEmptyMessage(MSG_WHAT_START);
        EventBus.getDefault().post(new EventBusBean("closeAct"+realMoney));
    }

    @OnClick(R.id.rl_bottom)
    public void onClick(View view) {
        handler.removeCallbacksAndMessages(null);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
