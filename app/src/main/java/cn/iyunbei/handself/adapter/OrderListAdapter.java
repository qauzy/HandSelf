package cn.iyunbei.handself.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.iyunbei.handself.R;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.activity.OrderDetailActivity;
import cn.iyunbei.handself.bean.OrderListBean;
import jt.kundream.adapter.rviewadapter.CommonAdapter;
import jt.kundream.adapter.rviewadapter.base.ViewHolder;
import jt.kundream.utils.ActivityUtil;
import jt.kundream.utils.TextViewUtils;
import jt.kundream.utils.ToastUtils;

public class OrderListAdapter extends CommonAdapter<OrderListBean.DataBean> implements View.OnClickListener {

    private RequestCallback.ItemViewOnClickListener clickListener;
    private OrderListDetailAdapter orderDetailAdapter;
    private Map<String, Boolean> orderIsOpen = new HashMap<>();

    public OrderListAdapter(Context context, int layoutId, List<OrderListBean.DataBean> datas, RequestCallback.ItemViewOnClickListener clickListener) {
        super(context, layoutId, datas);
        this.clickListener = clickListener;
        for (int i = 0; i < datas.size(); i++) {
            orderIsOpen.put(datas.get(i).getDate(), false);
        }
    }

    @Override
    protected void convert(ViewHolder holder, final OrderListBean.DataBean dataBean, int position) {
        TextView tvDate = holder.getView(R.id.tv_date);
        TextView tvSellMoney = holder.getView(R.id.tv_sell_money);
        TextView tvSellNum = holder.getView(R.id.tv_sell_num);
        LinearLayout llBottom = holder.getView(R.id.ll_bottom);
        RecyclerView rvOrders = holder.getView(R.id.rv_orders_num);
        ImageView ivZhankai = holder.getView(R.id.iv_zhankai);

        TextViewUtils.setText2Tv(dataBean.getDate(), tvDate);
        TextViewUtils.setText2Tv("销售总额  ￥" + dataBean.getTotal_amount(), tvSellMoney);
        TextViewUtils.setText2Tv("销售笔数 " + dataBean.getOrder_count() + "单", tvSellNum);

        // TODO: 2018/8/31 这里还需要一个向上箭头的切图
        if (orderIsOpen.get(dataBean.getDate())) {
            ivZhankai.setImageResource(R.mipmap.bihe);
        } else {
            ivZhankai.setImageResource(R.mipmap.zhankai);
        }

        llBottom.setOnClickListener(this);
        llBottom.setTag(position);


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
            if (orderIsOpen.get(dataBean.getDate())) {
                order.addAll(dataBean.getOrder());
            } else {
                for (int i = 0; i < 3; i++) {
                    order.add(dataBean.getOrder().get(i));
                }
            }
            llBottom.setVisibility(View.VISIBLE);
            rvOrders.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rvOrders.setLayoutManager(linearLayoutManager);
        orderDetailAdapter = new OrderListDetailAdapter(mContext, R.layout.item_order_list_detail, order);
        orderDetailAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //点击之后 将订单号携带到新的界面 请求这个订单的详细数据
                int order_id = dataBean.getOrder().get(position).getOrder_id();
                ActivityUtil.startActivity(mContext, OrderDetailActivity.class, new Intent().putExtra("order_id", order_id));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        rvOrders.setAdapter(orderDetailAdapter);

    }

    @Override
    public void onClick(View view) {
        clickListener.itemViewClick(view);
    }

    /**
     * 此处为设置订单是否为展开状态
     *
     * @param position
     */
    public void setOrderIsOpen(int position) {
        Boolean aBoolean = orderIsOpen.get(mDatas.get(position).getDate());

//        if (aBoolean) {
//            //如果不是展开的
//            AnimationUtils.Up2DownAnimation(0f, 180f, ivZhankai);
//
//        } else {
//            //如果是展开的
//            AnimationUtils.Up2DownAnimation(180f, 0f, ivZhankai);
//        }
        orderIsOpen.put(mDatas.get(position).getDate(), !aBoolean);
        notifyDataSetChanged();
    }
}
