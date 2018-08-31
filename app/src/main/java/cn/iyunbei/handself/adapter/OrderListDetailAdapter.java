package cn.iyunbei.handself.adapter;


import android.content.Context;
import android.widget.TextView;

import java.util.List;

import cn.iyunbei.handself.R;
import cn.iyunbei.handself.bean.OrderListBean;
import jt.kundream.adapter.rviewadapter.CommonAdapter;
import jt.kundream.adapter.rviewadapter.base.ViewHolder;
import jt.kundream.utils.TextViewUtils;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/8/31
 * @e-mail: 245086168@qq.com
 * @desc:订单列表内部的每个订单的大概内容
 **/
public class OrderListDetailAdapter extends CommonAdapter<OrderListBean.DataBean.DayOrder> {


    public OrderListDetailAdapter(Context context, int layoutId, List<OrderListBean.DataBean.DayOrder> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, OrderListBean.DataBean.DayOrder dayOrder, int position) {
        TextView tvOrderId = holder.getView(R.id.tv_order_id);
        TextView tvOrderTime = holder.getView(R.id.tv_order_time);
        TextView tvMoney = holder.getView(R.id.tv_money);
        TextView tvGoodsNum = holder.getView(R.id.tv_goods_num);

        TextViewUtils.setText2Tv("订单号：" + dayOrder.getOrder_id(),tvOrderId);
        TextViewUtils.setText2Tv("￥" + dayOrder.getTotal_amount(),tvMoney);
        TextViewUtils.setText2Tv("商品数  " + dayOrder.getNum(),tvGoodsNum);
        String ok_time = dayOrder.getOk_time();
        TextViewUtils.setText2Tv(ok_time.substring(5,ok_time.length()),tvOrderTime);
    }
}
