package cn.iyunbei.handself.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import cn.iyunbei.handself.bean.DayOrder;
import cn.iyunbei.handself.bean.OrderListBean;
import jt.kundream.adapter.rviewadapter.CommonAdapter;
import jt.kundream.adapter.rviewadapter.base.ViewHolder;
import jt.kundream.utils.ActivityUtil;
import jt.kundream.utils.TextViewUtils;

public class OrderListAdapter extends CommonAdapter<OrderListBean.DataBean> implements View.OnClickListener {

    private RequestCallback.ItemViewOnClickListener clickListener;
    private OrderListDetailAdapter orderDetailAdapter;
    private Map<String, Boolean> orderIsOpen = new HashMap<>();
    private int page = 1;
    //    private List<DayOrder> order = new ArrayList<>();
    private String date;

    public OrderListAdapter(Context context, int layoutId, List<OrderListBean.DataBean> datas, RequestCallback.ItemViewOnClickListener clickListener) {
        super(context, layoutId, datas);
        this.clickListener = clickListener;
        setMapFalse(datas);
    }

    public void setMapFalse(List<OrderListBean.DataBean> datas) {
        for (int i = 0; i < datas.size(); i++) {
            orderIsOpen.put(datas.get(i).getDate(), false); 
        }
    }

    @Override
    protected void convert(ViewHolder holder, final OrderListBean.DataBean dataBean, int position) {
        page = 1;
        TextView tvDate = holder.getView(R.id.tv_date);
        TextView tvSellMoney = holder.getView(R.id.tv_sell_money);
        TextView tvSellNum = holder.getView(R.id.tv_sell_num);
        TextView tvSeeAll = holder.getView(R.id.tv_see_all);
        LinearLayout llBottom = holder.getView(R.id.ll_bottom);
        final RecyclerView rvOrders = holder.getView(R.id.rv_orders_num);
        ImageView ivZhankai = holder.getView(R.id.iv_zhankai);
//        SmartRefreshLayout smartRefresh = holder.getView(R.id.smart_refresh_layout);


        TextViewUtils.setText2Tv(dataBean.getDate(), tvDate);
        TextViewUtils.setText2Tv("????????????  ???" + dataBean.getTotal_amount(), tvSellMoney);
        TextViewUtils.setText2Tv("???????????? " + dataBean.getOrder_count() + "???", tvSellNum);

        // TODO: 2018/8/31 ??????????????????????????????????????????
        if (orderIsOpen.get(dataBean.getDate())) {
            ivZhankai.setImageResource(R.mipmap.bihe);
            TextViewUtils.setText2Tv("????????????????????????", tvSeeAll);
        } else {
            ivZhankai.setImageResource(R.mipmap.zhankai);
            TextViewUtils.setText2Tv("????????????????????????", tvSeeAll);
        }




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

//        7160249472      100   ????????????
//        });
//        order = dataBean.getOrder();
        final List<DayOrder> order = new ArrayList<>();
        date = dataBean.getDate();
        if (dataBean.getOrder().size() < 3) {
            if (dataBean.getOrder().size() > 0) {
                rvOrders.setVisibility(View.VISIBLE);
                // TODO: 2018/8/31 ????????????????????????????????????????????????
                llBottom.setVisibility(View.GONE);
                order.addAll(dataBean.getOrder());

            } else {
                llBottom.setVisibility(View.GONE);
                rvOrders.setVisibility(View.GONE);
            }
        } else {
            // TODO: 2018/8/31 ?????????????????????  ??????????????????3?????????
            if (orderIsOpen.get(dataBean.getDate())) {
                order.addAll(dataBean.getOrder());
            } else {
                for (int i = 0; i < 3; i++) {
                    order.add(dataBean.getOrder().get(i));
                }
            }
            page = 1;
            llBottom.setVisibility(View.VISIBLE);
            rvOrders.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rvOrders.setLayoutManager(linearLayoutManager);
        orderDetailAdapter = new OrderListDetailAdapter(mContext, R.layout.item_order_list_detail, order);
        orderDetailAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //???????????? ????????????????????????????????? ?????????????????????????????????
                int order_id = dataBean.getOrder().get(position).getOrder_id();
                ActivityUtil.startActivity(mContext, OrderDetailActivity.class, new Intent().putExtra("order_id", order_id));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        rvOrders.setAdapter(orderDetailAdapter);

        llBottom.setOnClickListener(this);
        llBottom.setTag(position);
    }
//        smartRefresh.setEnableRefresh(false);
//        if (!orderIsOpen.get(dataBean.getDate())) {
//            smartRefresh.setEnableLoadmore(false);
//        } else {
//            smartRefresh.setEnableLoadmore(true);
//
//            smartRefresh.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
//                @Override
//                public void onLoadmore(RefreshLayout refreshLayout) {
//                    page++;
//
//                    OkGo.<String>post(Constants.LIST_NEXT)
//                            .params("_token", CommonUtil.getString(mContext, "token"))
//                            .params("date", date)
//                            .params("page", page)
//                            .execute(new StringCallback() {
//                                @Override
//                                public void onSuccess(Response<String> response) {
//                                    String s = response.body().toString();
//                                    if (JsonUtils.checkToken(s) == 200) {
//                                        OrderListPageBean orderListPageBean = new Gson().fromJson(s, OrderListPageBean.class);
//
////                                    ?????????????????????????????????????????????
//                                        List<DayOrder> data = orderListPageBean.getData().getOrder().getData();
////                                        loadMoreSucc(data);
//                                        order.addAll(data);
//                                        orderDetailAdapter.notifyDataSetChanged();
//                                    } else {
//                                        ToastUtils.showShort(mContext, JsonUtils.getMsg(s));
//                                    }
//                                }
//
//                                @Override
//                                public void onError(Response<String> response) {
//                                    super.onError(response);
//                                    ToastUtils.showShort(mContext, "??????????????????");
//                                }
//                            });
//
//                }
//
//
//                @Override
//                public void onRefresh(RefreshLayout refreshLayout) {
//
//                }
//
//            });
//            smartRefresh.finishLoadmore(1500);

//            rvOrders.setOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                    super.onScrollStateChanged(recyclerView, newState);
//                    /**
//                     * ??????????????????????????????????????? && ???????????????item???????????????????????????????????????item???????????????????????????????????????
//                     * ?????????recyclerView???????????????
//                     * ?????????2??????????????????????????????  ???????????????recyclerView??????
//                     */
//
//                    if (orderIsOpen.get(dataBean.getDate()) && !(lastVisibleItemPosition == order.size() - 1)) {
//                        // TODO: 2018/9/15
//                        rvOrders.setOnTouchListener(new View.OnTouchListener() {
//                            @Override
//                            public boolean onTouch(View view, MotionEvent motionEvent) {
//                                return true;
//                            }
//                        });
//                    } else {
//                        rvOrders.setOnTouchListener(new View.OnTouchListener() {
//                            @Override
//                            public boolean onTouch(View view, MotionEvent motionEvent) {
//                                return false;
//                            }
//                        });
//                    }
//                }
//
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                }
//            });
    //???????????????????????????
//        int itemCount = linearLayoutManager.getItemCount();
//        //????????????recyclerView????????????????????????????????????
//        int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
//        final boolean isCurrentCost = lastVisibleItemPosition == (order.size() - 1);
//        smartRefresh.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (orderIsOpen.get(dataBean.getDate()) && !isCurrentCost) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });


//    }

//    /**
//     * ????????????????????????????????????
//     *
//     * @param data
//     */
//    private void loadMoreSucc(List<DayOrder> data) {
//        order.addAll(data);
//        notifyDataSetChanged();
//    }

    @Override
    public void onClick(View view) {
        clickListener.itemViewClick(view);
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param position
     */
    public void setOrderIsOpen(int position) {
        Boolean aBoolean = orderIsOpen.get(mDatas.get(position).getDate());

//        if (aBoolean) {
//            //?????????????????????
//            AnimationUtils.Up2DownAnimation(0f, 180f, ivZhankai);
//
//        } else {
//            //??????????????????
//            AnimationUtils.Up2DownAnimation(180f, 0f, ivZhankai);
//        }
        orderIsOpen.put(mDatas.get(position).getDate(), !aBoolean);
        notifyDataSetChanged();
    }
}
