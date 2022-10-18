package cn.iyunbei.handself.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.bean.TempOrderBean;
import cn.iyunbei.handself.contract.ScanPayContract;
import cn.iyunbei.handself.presenter.ScanPayPresenter;
import jt.kundream.base.BaseActivity;
import jt.kundream.bean.EventBusBean;
import jt.kundream.utils.ActivityUtil;
import jt.kundream.utils.CommonUtil;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/9/4
 * @e-mail: 245086168@qq.com
 * @desc:结算状态页面
 **/
public class ScanPayActivity extends BaseActivity<ScanPayContract.View, ScanPayPresenter> implements ScanPayContract.View {
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
    @Bind(R.id.tv_money)
    TextView tvMoney;
    @Bind(R.id.btn_sele_pay_type_again)
    Button btnSelePayTypeAgain;
    private List<TempOrderBean.TempGoodsBean> goodsList = new ArrayList<>();
    private String tolMoney;
    private String realMoney;
    private int payType;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_scan_pay;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        tvTitle.setText("结算状态");
        ivLeft.setVisibility(View.GONE);
        ivRight.setVisibility(View.GONE);
        tvLeft.setVisibility(View.GONE);
        tvRight.setVisibility(View.GONE);
        tolMoney = getIntent().getStringExtra("tolMoney");
        realMoney = getIntent().getStringExtra("realMoney");
        goodsList = (List<TempOrderBean.TempGoodsBean>) getIntent().getSerializableExtra("goods");
        payType = getIntent().getIntExtra("payType", -1);
        tvMoney.setText(realMoney);
        //此处需要发送一个消息，告诉主页面：你现在扫描之后接收的结果我来处理(页面会有个默认值，当主页面不onPause的时候，主页面的所有扫码结果分2个页面处理)
//        EventBus.getDefault().post(new EventBusBean(substring));

    }

    @OnClick(R.id.btn_sele_pay_type_again)
    public void onClick(View view) {
        //此页面只有一个点击事件,点击之后回到上一个页面，重新选择支付方式。
        finish();
        Intent intent = new Intent();
        ActivityUtil.startActivity(getContext(), MainActivity.class, intent);
    }

    @Override
    public ScanPayPresenter initPresenter() {
        return new ScanPayPresenter();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusBean bean) {
        /**
         * 接受到扫码的用户支付码之后，请求信息。
         */
        String event = bean.getEvent();
        if (!event.substring(0,8).equals("closeAct")){
            String authCode = event;

            presenter.startPay(CommonUtil.getString(getContext(), "token"), goodsList, payType,realMoney, authCode);
        }

    }

    @Override
    public void paySucc() {
        Intent intent = new Intent();
        intent.putExtra("goodsNum", goodsList.size());
        intent.putExtra("realMoney", realMoney);
        intent.putExtra("tolMon", tolMoney);
        ActivityUtil.startActivity(this, PaySuccActivity.class, intent, true);
    }
}
