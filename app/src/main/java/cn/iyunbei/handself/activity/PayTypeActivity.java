package cn.iyunbei.handself.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.bean.TempOrderBean;
import cn.iyunbei.handself.contract.PayTypeContract;
import cn.iyunbei.handself.presenter.PayTypePresenter;
import jt.kundream.base.BaseActivity;
import jt.kundream.bean.EventBusBean;
import jt.kundream.utils.ActivityUtil;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/9/4
 * @e-mail: 245086168@qq.com
 * @desc:支付方式的选择
 **/
public class PayTypeActivity extends BaseActivity<PayTypeContract.View, PayTypePresenter> implements PayTypeContract.View {

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
    @Bind(R.id.ll_money)
    LinearLayout llMoney;
    @Bind(R.id.ll_real_money)
    LinearLayout llRealMoney;
    @Bind(R.id.iv_cash)
    ImageView ivCash;
    @Bind(R.id.ll_cash)
    LinearLayout llCash;
    @Bind(R.id.iv_wecaht)
    ImageView ivWecaht;
    @Bind(R.id.ll_wechat)
    LinearLayout llWechat;
    @Bind(R.id.iv_ali)
    ImageView ivAli;
    @Bind(R.id.ll_ali_pay)
    LinearLayout llAliPay;
    @Bind(R.id.ll_pay_type)
    LinearLayout llPayType;
    @Bind(R.id.tv_prev)
    TextView tvPrev;
    @Bind(R.id.tv_next)
    TextView tvNext;
    private String tolMoney;
    //支付方式的选择，默认现金
    private int payMode = 0;
    private List<TempOrderBean.TempGoodsBean> goodsList = new ArrayList<>();

    @Override
    public int getLayoutResId() {
        return R.layout.activity_jiesuan_type;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        tolMoney = getIntent().getStringExtra("tolMoney");
        goodsList = (List<TempOrderBean.TempGoodsBean>) getIntent().getSerializableExtra("goods");
        ivLeft.setVisibility(View.GONE);
        ivRight.setVisibility(View.GONE);
        tvLeft.setVisibility(View.GONE);
        tvRight.setVisibility(View.GONE);
        tvTitle.setText("结算方式");
        tvMoney.setText(tolMoney);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusBean bean) {
        /**
         * 接受到扫码的用户支付码之后，请求信息。
         */
        if (bean.getEvent().equals("closeAct")) {
            finish();
        }
    }

    @OnClick({R.id.tv_next, R.id.tv_prev, R.id.ll_cash, R.id.ll_wechat, R.id.ll_ali_pay})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_prev:
                finish();

                break;
            case R.id.tv_next:
//                presenter.payMoney(getContext(),goodsList,payMode);
                if (payMode == 0) {
                    presenter.useCashPay(getContext(), goodsList);
                } else {
                    userNetPay();
                }
                break;

            // TODO: 2018/9/8 此处红恩说可以有N种支付方式，比如可以设置有多个支付宝方式，然后返回的是一个列表，点击列表，选择某种方式去支付。what fuck
            case R.id.ll_cash:
                ivCash.setImageResource(R.mipmap.xuanze);
                ivAli.setImageResource(R.mipmap.weixuanze);
                ivWecaht.setImageResource(R.mipmap.weixuanze);
                payMode = 0;

                break;
            case R.id.ll_wechat:
                ivCash.setImageResource(R.mipmap.weixuanze);
                ivAli.setImageResource(R.mipmap.weixuanze);
                ivWecaht.setImageResource(R.mipmap.xuanze);
                payMode = 1;
                break;
            case R.id.ll_ali_pay:
                ivCash.setImageResource(R.mipmap.weixuanze);
                ivWecaht.setImageResource(R.mipmap.weixuanze);
                ivAli.setImageResource(R.mipmap.xuanze);
                payMode = 2;
                break;

            default:
                break;
        }

    }

    private void userNetPay() {
        Intent intent = new Intent();
        intent.putExtra("tolMoney", tolMoney);
        intent.putExtra("payMode", payMode);
        intent.putExtra("goods", (Serializable) goodsList);
        ActivityUtil.startActivity(getContext(), ScanPayActivity.class, intent);
    }

    @Override
    public PayTypePresenter initPresenter() {
        return new PayTypePresenter();
    }

    @Override
    public void cashPaySucc() {
        Intent intent = new Intent();
        intent.putExtra("goodsNum", goodsList.size());
        intent.putExtra("tolMon", tolMoney);
        ActivityUtil.startActivity(this, PaySuccActivity.class, intent, true);
    }
}
