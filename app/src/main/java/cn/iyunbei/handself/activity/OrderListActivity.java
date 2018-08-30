package cn.iyunbei.handself.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.contract.OrderListContract;
import cn.iyunbei.handself.presenter.OrderListPresenter;
import jt.kundream.base.BaseActivity;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/8/30
 * @e-mail: 245086168@qq.com
 * @desc:订单列表页面
 **/
public class OrderListActivity extends BaseActivity<OrderListContract.View, OrderListPresenter> implements OrderListContract.View {
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
    @Bind(R.id.rv_orders)
    RecyclerView rvOrders;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_order_list;
    }

    @Override
    public void initView() {
        tvLeft.setVisibility(View.GONE);
        tvRight.setVisibility(View.GONE);
        tvTitle.setText("订单列表");
        ivRight.setImageResource(R.mipmap.rili);

        presenter.getOrderList(this);

    }

    @Override
    public OrderListPresenter initPresenter() {
        return new OrderListPresenter();
    }

    @OnClick({R.id.iv_left, R.id.iv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;

            case R.id.iv_right:

                break;

            default:
                break;
        }

    }

}
