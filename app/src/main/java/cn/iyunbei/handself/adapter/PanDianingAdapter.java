package cn.iyunbei.handself.adapter;

import android.content.Context;

import java.util.List;

import cn.iyunbei.handself.bean.PanDianingBean;
import jt.kundream.adapter.rviewadapter.CommonAdapter;
import jt.kundream.adapter.rviewadapter.base.ViewHolder;

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

    }
}
