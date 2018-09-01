package cn.iyunbei.handself.adapter;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.iyunbei.handself.R;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.PanDianBean;
import jt.kundream.adapter.rviewadapter.CommonAdapter;
import jt.kundream.adapter.rviewadapter.base.ViewHolder;
import jt.kundream.utils.TextViewUtils;

public class PanDianAdapter extends CommonAdapter<PanDianBean.DataBean> {

    private RequestCallback.ItemViewOnClickListener itemClickListener;

    public PanDianAdapter(Context context, int layoutId, List<PanDianBean.DataBean> datas, RequestCallback.ItemViewOnClickListener itemClickListener) {
        super(context, layoutId, datas);
        this.itemClickListener = itemClickListener;
    }

    @Override
    protected void convert(ViewHolder holder, PanDianBean.DataBean dataBean, int position) {
        /**
         * 请忽略掉id的不正规写法，因为这个地方的有些id 是直接拿的别的地方的复制的
         */
        TextView tvPanDianID = holder.getView(R.id.tv_pandian_id);
        TextView tvTime = holder.getView(R.id.tv_time);
        TextView tvKuCun = holder.getView(R.id.tv_order_num);
        TextView tvYing = holder.getView(R.id.tv_goods_num);
        TextView tvKui = holder.getView(R.id.tv_sell_money);
        LinearLayout pandian = holder.getView(R.id.ll_counting);

        TextViewUtils.setText2Tv("单号：" + position, tvPanDianID);
        TextViewUtils.setText2Tv("下周写：" + position, tvTime);
        TextViewUtils.setText2Tv(String.valueOf(dataBean.getNums()), tvKuCun);
        TextViewUtils.setText2Tv(String.valueOf(dataBean.getYing()), tvYing);
        TextViewUtils.setText2Tv(String.valueOf(dataBean.getKui()), tvKui);

    }
}
