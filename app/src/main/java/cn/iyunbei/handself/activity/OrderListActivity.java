package cn.iyunbei.handself.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.adapter.OrderListAdapter;
import cn.iyunbei.handself.adapter.TempOrderAdapter;
import cn.iyunbei.handself.bean.OrderListBean;
import cn.iyunbei.handself.bean.Single;
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
    private String time = null;
    private OrderListAdapter mAdapter;
    private List<OrderListBean.DataBean> orderList = new ArrayList<>();


    private RequestCallback.ItemViewOnClickListener itemClickListener = new RequestCallback.ItemViewOnClickListener() {
        @Override
        public void itemViewClick(View view) {
            int position = (int) view.getTag();
            switch (view.getId()) {
                case R.id.ll_bottom_1:
                    break;

                case R.id.ll_pay_again:
                    break;

                case R.id.ll_pay_cancel:

                    break;

                default:
                    break;

            }

        }
    };


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
        /**
         * 直接拿当前时间去请求数据
         */
        presenter.getOrderList(this, new SimpleDateFormat("yyyy-MM-dd").format(new Date().getTime()));

    }

    @Override
    public OrderListPresenter initPresenter() {
        return new OrderListPresenter();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.iv_left, R.id.iv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;

            case R.id.iv_right:
                showDatePickDlg(DateType.TYPE_YMD);

                break;

            default:
                break;
        }

    }

    /**
     * 显示一个简单的日历选择
     */
    private void showDatePickDlg(DateType type) {
        DatePickDialog dialog = new DatePickDialog(this);
        //设置上下年分限制
        dialog.setYearLimt(3);
        //设置标题
        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(type);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd");
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调
        dialog.setOnSureLisener(new OnSureLisener() {
            @Override
            public void onSure(Date date) {
                long msec = date.getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                time = simpleDateFormat.format(msec);
                showToast(time);
            }
        });
        dialog.show();
    }

    @Override
    public void showOrderList(List<OrderListBean.DataBean> backList) {
        orderList.clear();
        orderList.addAll(backList);

        if (mAdapter == null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rvOrders.setLayoutManager(linearLayoutManager);
            rvOrders.setItemAnimator(new DefaultItemAnimator());
            mAdapter = new OrderListAdapter(this, R.layout.item_orders_list, orderList, itemClickListener);
            rvOrders.setAdapter(mAdapter);

        } else {
            mAdapter.notifyDataSetChanged();
        }
    }
}
