package cn.iyunbei.handself.adapter;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.iyunbei.handself.R;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.PanDianBean;
import jt.kundream.adapter.rviewadapter.CommonAdapter;
import jt.kundream.adapter.rviewadapter.base.ViewHolder;
import jt.kundream.utils.TextViewUtils;

public class PanDianAdapter extends CommonAdapter<PanDianBean.DataBean> implements View.OnClickListener {

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
        ImageView ivNext = holder.getView(R.id.iv_next);


        int profit_status = dataBean.getProfit_status();
        if (profit_status == 0) {
            //盘点数据生成中
            tvTime.setTextColor(mContext.getResources().getColor(R.color.gray));
            ivNext.setVisibility(View.GONE);
            TextViewUtils.setText2Tv("盘点生成中", tvTime);
            pandian.setClickable(false);
        } else if (profit_status == 1) {
            //盘点中(盘点数据生成完毕)
            tvTime.setTextColor(mContext.getResources().getColor(R.color.pandian));
            ivNext.setVisibility(View.VISIBLE);
            TextViewUtils.setText2Tv("盘点中", tvTime);
            pandian.setClickable(true);
        } else if (profit_status == 2) {
            tvTime.setTextColor(mContext.getResources().getColor(R.color.gray));
            ivNext.setVisibility(View.GONE);
            //盘点完成，此时显示盘点完成的时间
            TextViewUtils.setText2Tv(dataBean.getOk_time(), tvTime);
            pandian.setClickable(false);
        }

        TextViewUtils.setText2Tv("单号：" + dataBean.getProfit_id(), tvPanDianID);
        TextViewUtils.setText2Tv(String.valueOf(dataBean.getNums()), tvKuCun);
        TextViewUtils.setText2Tv(String.valueOf(dataBean.getYing()), tvYing);
        TextViewUtils.setText2Tv(String.valueOf(dataBean.getKui()), tvKui);

        pandian.setOnClickListener(this);
        pandian.setTag(position);

    }

    @Override
    public void onClick(View view) {
        itemClickListener.itemViewClick(view);
    }
}
