package cn.iyunbei.handself.activity;

import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.adapter.OrderListDetailAdapter;
import cn.iyunbei.handself.bean.DayOrder;
import cn.iyunbei.handself.bean.OrderListBean;
import cn.iyunbei.handself.contract.MoreOrderListContract;
import cn.iyunbei.handself.presenter.MoreOrderListPresenter;
import jt.kundream.adapter.rviewadapter.MultiItemTypeAdapter;
import jt.kundream.base.BaseActivity;
import jt.kundream.utils.ActivityUtil;

public class MoreOrderListActivity extends BaseActivity<MoreOrderListContract.View, MoreOrderListPresenter> implements MoreOrderListContract.View {

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
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.tv_sell_money)
    TextView tvSellMoney;
    @Bind(R.id.tv_sell_num)
    TextView tvSellNum;
    @Bind(R.id.rv_orders_num)
    RecyclerView rvOrdersNum;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    private OrderListDetailAdapter mAdapter;
    private List<DayOrder> order;
    private int page = 1;
    private String date;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_more_list;
    }

    @Override
    public void initView() {
        OrderListBean.DataBean bean = (OrderListBean.DataBean) getIntent().getSerializableExtra("dateList");
        order = bean.getOrder();
        tvLeft.setVisibility(View.GONE);
        tvRight.setVisibility(View.GONE);
        ivRight.setVisibility(View.GONE);
        tvTitle.setText("全部订单");
        date = bean.getDate();
        tvDate.setText(date);
        tvSellMoney.setText("销售总额  ￥" + bean.getTotal_amount());
        tvSellNum.setText("销售笔数 " + bean.getOrder_count() + "单");
        showList();

        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                page++;
                presenter.reqMoreListData(getContext(), date, page);
                smartRefreshLayout.finishLoadmore(1500);
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {

            }
        });

    }

    @OnClick(R.id.iv_left)
    public void onClick(View v) {
        finish();
    }

    /**
     * 显示列表
     */
    private void showList() {
        if (mAdapter == null) {

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rvOrdersNum.setLayoutManager(linearLayoutManager);
            mAdapter = new OrderListDetailAdapter(this, R.layout.item_order_list_detail, order);
            mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    //点击之后 将订单号携带到新的界面 请求这个订单的详细数据
                    int order_id = order.get(position).getOrder_id();
                    ActivityUtil.startActivity(getContext(), OrderDetailActivity.class, new Intent().putExtra("order_id", order_id));
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            rvOrdersNum.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public MoreOrderListPresenter initPresenter() {
        return new MoreOrderListPresenter();
    }

    @Override
    public void addNewData(List<DayOrder> data) {
        if (data.size() < 1) {
            showToast("没有更多订单了");
        } else {
            order.addAll(data);
            showList();
        }
    }
}
