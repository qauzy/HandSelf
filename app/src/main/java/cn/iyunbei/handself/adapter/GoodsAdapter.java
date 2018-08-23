package cn.iyunbei.handself.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.iyunbei.handself.R;
import cn.iyunbei.handself.bean.GoodsBean;
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
 * @desc:
 **/
public class GoodsAdapter extends CommonAdapter<GoodsBean.DataBean> {

    Map<Integer, Integer> numMap = new HashMap<>();

    public GoodsAdapter(Context context, int layoutId, List<GoodsBean.DataBean> datas, Map<Integer, Integer> numMap) {
        super(context, layoutId, datas);
        this.numMap = numMap;
    }

    @Override
    protected void convert(ViewHolder holder, GoodsBean.DataBean dataBean, int position) {
        TextViewUtils.setText2Tv(dataBean.getGoods_name(), (TextView) holder.getView(R.id.tv_goods_name));
        TextViewUtils.setText2Tv(dataBean.getGoods_price(), (TextView) holder.getView(R.id.tv_money));
        TextViewUtils.setText2Tv(dataBean.getGoods_id() + "", (TextView) holder.getView(R.id.tv_goods_code));
        TextViewUtils.setText2Tv(dataBean.getSpec(), (TextView) holder.getView(R.id.tv_goods_guige));
        int num = numMap.get(dataBean.getGoods_id());
        TextViewUtils.setText2Tv(String.valueOf(num), (TextView) holder.getView(R.id.tv_num));
    }


}
