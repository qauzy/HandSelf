package cn.iyunbei.handself.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.iyunbei.handself.R;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.GoodsDataBean;
import cn.iyunbei.handself.bean.GoodsListBean;
import jt.kundream.adapter.rviewadapter.CommonAdapter;
import jt.kundream.adapter.rviewadapter.base.ViewHolder;
import jt.kundream.utils.TextViewUtils;

public class GoodsListAdapter extends CommonAdapter<GoodsDataBean> implements View.OnClickListener {

    private RequestCallback.ItemViewOnClickListener itemClickListener;

    public GoodsListAdapter(Context context, int layoutId, List<GoodsDataBean> datas, RequestCallback.ItemViewOnClickListener itemClickListener) {
        super(context, layoutId, datas);
        this.itemClickListener = itemClickListener;
    }

    @Override
    protected void convert(ViewHolder holder, GoodsDataBean dataBean, int position) {
        /**
         * 请忽略掉id的不正规写法，因为这个地方的有些id 是直接拿的别的地方的复制的
         */
        RelativeLayout rlItem = holder.getView(R.id.rl_goods_item);

        TextView tvName = holder.getView(R.id.tv_goods_name);
        TextView tvGuiGe = holder.getView(R.id.tv_goods_guige);
        TextView tvCode = holder.getView(R.id.tv_goods_code);
        TextView tvMoney = holder.getView(R.id.tv_money);
        TextView tvSupplier = holder.getView(R.id.tv_supplier);

        TextViewUtils.setText2Tv(dataBean.getGoodsName(), tvName);
        TextViewUtils.setText2Tv("规格："+dataBean.getSpec(), tvGuiGe);
        TextViewUtils.setText2Tv(String.valueOf(dataBean.getBarcode()), tvCode);
        String price = "无价格信息";
        if(!dataBean.getPrice().isEmpty()){
            price =  "￥" + dataBean.getPrice();
        }
        TextViewUtils.setText2Tv(price, tvMoney);
        TextViewUtils.setText2Tv(dataBean.getSupplier(), tvSupplier);
        //设置item数据 -- > position作为商品ID

        rlItem.setTag(position);
        rlItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.itemViewClick(view);
    }

}
