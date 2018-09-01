package cn.iyunbei.handself.activity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.adapter.OrderDetailAdapter;
import cn.iyunbei.handself.adapter.OrderListAdapter;
import cn.iyunbei.handself.bean.OrderDetailBean;
import cn.iyunbei.handself.contract.OrderDetailContract;
import cn.iyunbei.handself.presenter.OrderDetailPresenter;
import jt.kundream.base.BaseActivity;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/8/31
 * @e-mail: 245086168@qq.com
 * @desc:订单详情
 **/
public class OrderDetailActivity extends BaseActivity<OrderDetailContract.View, OrderDetailPresenter> implements OrderDetailContract.View {
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
    @Bind(R.id.tv_user)
    TextView tvUser;
    @Bind(R.id.tv_order_num)
    TextView tvOrderNum;
    @Bind(R.id.tv_order_date)
    TextView tvOrderDate;
    @Bind(R.id.tv_goods_num)
    TextView tvGoodsNum;
    @Bind(R.id.tv_order_money)
    TextView tvOrderMoney;
    @Bind(R.id.rv_goods)
    RecyclerView rvGoods;
    private OrderDetailAdapter mAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initView() {
        tvLeft.setVisibility(View.GONE);
        tvRight.setVisibility(View.GONE);
        tvTitle.setText("订单详情");
        ivRight.setVisibility(View.GONE);

        int order_id = this.getIntent().getIntExtra("order_id", -1);

        showProgress();
        presenter.getOrderDetail(this, order_id);

    }

    @Override
    public OrderDetailPresenter initPresenter() {
        return new OrderDetailPresenter();
    }

    @OnClick(R.id.iv_left)
    public void onClick(View view) {
        finish();
    }

    @Override
    public void showOrderMsg(OrderDetailBean.DataBean data) {
        tvUser.setText("收银员:" + data.getOrder().getInventory_record_no());
        tvOrderNum.setText("订单编号：" + data.getOrder().getOrder_id());
        tvOrderDate.setText(data.getOrder().getOk_time());
        tvGoodsNum.setText("商品总数:" + data.getOrder().getNum() + "件");
        tvOrderMoney.setText("实际付款：￥" + data.getOrder().getTotal_amount());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvGoods.setLayoutManager(linearLayoutManager);
        rvGoods.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new OrderDetailAdapter(this, R.layout.item_temp_goods, data.getOrders());
        rvGoods.setAdapter(mAdapter);

    }
}

