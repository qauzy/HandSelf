package cn.iyunbei.handself.adapter;

import android.content.Context;
import android.widget.TextView;

import java.util.List;

import cn.iyunbei.handself.R;
import cn.iyunbei.handself.bean.OrderDetailBean;
import jt.kundream.adapter.rviewadapter.CommonAdapter;
import jt.kundream.adapter.rviewadapter.base.ViewHolder;
import jt.kundream.utils.TextViewUtils;

public class OrderDetailAdapter extends CommonAdapter<OrderDetailBean.DataBean.OrdersBean> {

    public OrderDetailAdapter(Context context, int layoutId, List<OrderDetailBean.DataBean.OrdersBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, OrderDetailBean.DataBean.OrdersBean ordersBean, int position) {
        TextView tvName = holder.getView(R.id.tv_goods_name);
        TextView tvGuiGe = holder.getView(R.id.tv_goods_guige);
        TextView tvCode = holder.getView(R.id.tv_goods_code);
        TextView tvMoney = holder.getView(R.id.tv_money);
        TextView tvNum = holder.getView(R.id.tv_num);

        TextViewUtils.setText2Tv(ordersBean.getGoods_name(), tvName);
        TextViewUtils.setText2Tv("规格："+ordersBean.getSpec(), tvGuiGe);
        TextViewUtils.setText2Tv(String.valueOf(ordersBean.getBarcode()), tvCode);
        TextViewUtils.setText2Tv("￥" + ordersBean.getGoods_price(), tvMoney);
        TextViewUtils.setText2Tv("X" + ordersBean.getGoods_num(), tvNum);

    }
}
