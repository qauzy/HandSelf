package cn.iyunbei.handself.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import cn.iyunbei.handself.R;
import cn.iyunbei.handself.bean.PanDianingBean;
import jt.kundream.adapter.rviewadapter.CommonAdapter;
import jt.kundream.adapter.rviewadapter.base.ViewHolder;
import jt.kundream.utils.TextViewUtils;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/9/4
 * @e-mail: 245086168@qq.com
 * @desc:盘点中的列表
 **/
public class PanDianingAdapter extends CommonAdapter<PanDianingBean.DataBeanX.ListBean.DataBean> {

    public PanDianingAdapter(Context context, int layoutId, List<PanDianingBean.DataBeanX.ListBean.DataBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, PanDianingBean.DataBeanX.ListBean.DataBean dataBean, int position) {
        TextView goodsName = holder.getView(R.id.tv_goods_name);
        TextView goodsSpec = holder.getView(R.id.tv_goods_guige);
        TextView goodsBar= holder.getView(R.id.tv_goods_code);
        TextView goodsRealNum = holder.getView(R.id.tv_money);
        TextView goodsStoreNum = holder.getView(R.id.tv_num);

        TextViewUtils.setText2Tv(dataBean.getGoods_name(),goodsName);
        TextViewUtils.setText2Tv(dataBean.getSpec(),goodsSpec);
        TextViewUtils.setText2Tv(dataBean.getBarcode(),goodsBar);
        TextViewUtils.setText2Tv("实际数量："+dataBean.getReal_number(),goodsRealNum);
        TextViewUtils.setText2Tv("账存数量："+dataBean.getGoods_number(),goodsStoreNum);
    }
}
