package cn.iyunbei.handself.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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
import butterknife.OnClick;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.adapter.PayTypeAdapter;
import cn.iyunbei.handself.bean.PayTypeBean;
import cn.iyunbei.handself.bean.TempOrderBean;
import cn.iyunbei.handself.contract.PayTypeContract;
import cn.iyunbei.handself.presenter.PayTypePresenter;
import jt.kundream.adapter.rviewadapter.MultiItemTypeAdapter;
import jt.kundream.base.BaseActivity;
import jt.kundream.bean.EventBusBean;
import jt.kundream.utils.ActivityUtil;
import jt.kundream.utils.CurrencyUtils;

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
    @Bind(R.id.rv_paytype)
    RecyclerView rvPaytype;
    @Bind(R.id.tv_prev)
    TextView tvPrev;
    @Bind(R.id.tv_next)
    TextView tvNext;
    @Bind(R.id.et_realmoney)
    EditText etRealMoney;
    private String tolMoney;
    //支付方式的选择，默认现金
    private int payType = 0;
    private List<TempOrderBean.TempGoodsBean> goodsList = new ArrayList<>();
    private PayTypeAdapter mAdapter;
    private int payMode = -1;
    private String realMoney;

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
        presenter.getPayType(getContext());
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
        String event = bean.getEvent();
        if (event.substring(0,8).equals("closeAct")) {
            finish();
        }
    }

    @OnClick({R.id.tv_next, R.id.tv_prev})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_prev:
                finish();

                break;
            case R.id.tv_next:
                String str = etRealMoney.getText().toString();
                if (!TextUtils.isEmpty(str) && CurrencyUtils.toBigDecimal(str).doubleValue() > 0) {
                    realMoney = str;
                } else {
                    realMoney = tolMoney;
                }
                if (payMode == 0) {
                    presenter.useCashPay(getContext(), payType, goodsList,realMoney);
                } else {
                    userNetPay(realMoney);
                }
                break;

            default:
                break;
        }

    }

    private void userNetPay(String realMoney) {
        Intent intent = new Intent();
        intent.putExtra("tolMoney", tolMoney);
        intent.putExtra("realMoney", realMoney);
        intent.putExtra("payType", payType);
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

    @Override
    public void showPayTypeList(final List<PayTypeBean.DataBean> data) {

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getDefaultX() == 1) {
                payType = data.get(i).getPayment_type();
                //payMode的值只有0-1-2三种，如果是0则为现金，其余的都跳转到扫码界面
                payMode = data.get(i).getPayment_mode();
            } else {
                continue;
            }
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPaytype.setLayoutManager(linearLayoutManager);
        mAdapter = new PayTypeAdapter(this, R.layout.item_pay_type, data);
        rvPaytype.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                mAdapter.setSelectType(position);
                //此处还要拿到paytype的值
                payType = data.get(position).getPayment_type();
                payMode = data.get(position).getPayment_mode();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

}
