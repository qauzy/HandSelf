package cn.iyunbei.handself.adapter;

import android.content.Context;
import android.widget.TextView;

import java.util.List;

import cn.iyunbei.handself.R;
import cn.iyunbei.handself.bean.TempOrderBean;
import jt.kundream.adapter.rviewadapter.CommonAdapter;
import jt.kundream.adapter.rviewadapter.base.ViewHolder;
import jt.kundream.utils.TextViewUtils;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:显示商品
 **/
public class TempGoodsAdapter extends CommonAdapter<TempOrderBean.TempGoodsBean> {


    public TempGoodsAdapter(Context context, int layoutId, List<TempOrderBean.TempGoodsBean> datas) {
        super(context, layoutId, datas);

    }

    @Override
    protected void convert(ViewHolder holder, TempOrderBean.TempGoodsBean goodsBean, int position) {
        TextView tvName = holder.getView(R.id.tv_goods_name);
        TextView tvGuiGe = holder.getView(R.id.tv_goods_guige);
        TextView tvCode = holder.getView(R.id.tv_goods_code);
        TextView tvMoney = holder.getView(R.id.tv_money);
        TextView tvNum = holder.getView(R.id.tv_num);

        TextViewUtils.setText2Tv(goodsBean.getGoods_name(), tvName);
        TextViewUtils.setText2Tv(goodsBean.getSpec(), tvGuiGe);
        TextViewUtils.setText2Tv(String.valueOf(goodsBean.getBarcode()), tvCode);
        TextViewUtils.setText2Tv("￥" + goodsBean.getGoods_price(), tvMoney);
        TextViewUtils.setText2Tv("X" + goodsBean.getGoods_number(), tvNum);
    }
}
