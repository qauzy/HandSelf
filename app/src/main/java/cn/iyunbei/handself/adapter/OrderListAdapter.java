package cn.iyunbei.handself.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.iyunbei.handself.Constants;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.activity.OrderDetailActivity;
import cn.iyunbei.handself.bean.DayOrder;
import cn.iyunbei.handself.bean.OrderListBean;
import cn.iyunbei.handself.bean.OrderListPageBean;
import jt.kundream.adapter.rviewadapter.CommonAdapter;
import jt.kundream.adapter.rviewadapter.base.ViewHolder;
import jt.kundream.utils.ActivityUtil;
import jt.kundream.utils.CommonUtil;
import jt.kundream.utils.JsonUtils;
import jt.kundream.utils.TextViewUtils;
import jt.kundream.utils.ToastUtils;

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
        TextViewUtils.setText2Tv("销售总额  ￥" + dataBean.getTotal_amount(), tvSellMoney);
        TextViewUtils.setText2Tv("销售笔数 " + dataBean.getOrder_count() + "单", tvSellNum);

        // TODO: 2018/8/31 这里还需要一个向上箭头的切图
        if (orderIsOpen.get(dataBean.getDate())) {
            ivZhankai.setImageResource(R.mipmap.bihe);
            TextViewUtils.setText2Tv("点击隐藏多余订单", tvSeeAll);
        } else {
            ivZhankai.setImageResource(R.mipmap.zhankai);
            TextViewUtils.setText2Tv("点击查看全部订单", tvSeeAll);
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

//        7160249472      100   黄什么英
//        });
//        order = dataBean.getOrder();
        final List<DayOrder> order = new ArrayList<>();
        date = dataBean.getDate();
        if (dataBean.getOrder().size() < 3) {
            if (dataBean.getOrder().size() > 0) {
                rvOrders.setVisibility(View.VISIBLE);
                // TODO: 2018/8/31 进入这个判断，直接显示所有的数据
                llBottom.setVisibility(View.GONE);
                order.addAll(dataBean.getOrder());

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
////                                    这是新得到的加载更多的订单集合
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
//                                    ToastUtils.showShort(mContext, "网络连接错误");
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
//                     * 如果订单列表是在展开的状态 && 当前显示的item不是某一天的订单的最后一个item的时候，滑动事件内部处理，
//                     * 外部的recyclerView中断处理，
//                     * 否则，2个条件任何一个不满足  就让外层的recyclerView处理
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
    //获取所有的条目个数
//        int itemCount = linearLayoutManager.getItemCount();
//        //获取当前recyclerView显示的最后一个条目的位置
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
//     * 加载更多，成功时候的操作
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
