package cn.iyunbei.handself.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.iyunbei.handself.R;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.OrderListBean;
import jt.kundream.adapter.rviewadapter.CommonAdapter;
import jt.kundream.adapter.rviewadapter.base.ViewHolder;
import jt.kundream.utils.TextViewUtils;

public class OrderListAdapter extends CommonAdapter<OrderListBean.DataBean> {

    private RequestCallback.ItemViewOnClickListener clickListener;
    private OrderListDetailAdapter orderDetailAdapter;

    public OrderListAdapter(Context context, int layoutId, List<OrderListBean.DataBean> datas, RequestCallback.ItemViewOnClickListener clickListener) {
        super(context, layoutId, datas);
        this.clickListener = clickListener;
    }

    @Override
    protected void convert(ViewHolder holder, OrderListBean.DataBean dataBean, int position) {
        TextView tvDate = holder.getView(R.id.tv_date);
        TextView tvSellMoney = holder.getView(R.id.tv_sell_money);
        TextView tvSellNum = holder.getView(R.id.tv_sell_num);
        LinearLayout llBottom = holder.getView(R.id.ll_bottom);
        RecyclerView rvOrders = holder.getView(R.id.rv_orders_num);

        TextViewUtils.setText2Tv(dataBean.getDate(), tvDate);
        TextViewUtils.setText2Tv("销售总额  ￥" + dataBean.getTotal_amount(), tvSellMoney);
        TextViewUtils.setText2Tv("销售笔数 " + dataBean.getOrder_count() + "单", tvSellNum);

//        llBottom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Boolean aBoolean = isShowAllMap.get(tempOrderBean.getOrderId());
//                if (!aBoolean) {
//                    AnimationUtils.Up2DownAnimation(0f, 180f, ivZhankai);
//                } else {
//                    AnimationUtils.Up2DownAnimation(180f, 0f, ivZhankai);
//                }
//                isShowAllMap.put(tempOrderBean.getOrderId(), !aBoolean);
//                setGoodsAdapter(tempOrderBean,rvGoods,llBottom1);
//            }
//        });

//        List<OrderListBean.DataBean.DayOrder> order = dataBean.getOrder();
        List<OrderListBean.DataBean.DayOrder> order = new ArrayList<>();
        int size = dataBean.getOrder().size();
        if (size < 3) {
            if (size > 0) {
                rvOrders.setVisibility(View.VISIBLE);
                // TODO: 2018/8/31 进入这个判断，直接显示所有的数据
                llBottom.setVisibility(View.GONE);

            } else {
                llBottom.setVisibility(View.GONE);
                rvOrders.setVisibility(View.GONE);
            }
        } else {
            // TODO: 2018/8/31 如果数据比较多  那么之加载前3条数据
            for (int i = 0; i < 3; i++) {
                order.add(dataBean.getOrder().get(i));
            }

            llBottom.setVisibility(View.VISIBLE);
            rvOrders.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rvOrders.setLayoutManager(linearLayoutManager);
        orderDetailAdapter = new OrderListDetailAdapter(mContext, R.layout.item_order_list_detail, order);

        rvOrders.setAdapter(orderDetailAdapter);

    }
}
