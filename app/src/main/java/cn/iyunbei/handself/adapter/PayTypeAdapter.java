package cn.iyunbei.handself.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.iyunbei.handself.R;
import cn.iyunbei.handself.bean.PayTypeBean;
import jt.kundream.adapter.rviewadapter.CommonAdapter;
import jt.kundream.adapter.rviewadapter.base.ViewHolder;
import jt.kundream.utils.TextViewUtils;

public class PayTypeAdapter extends CommonAdapter<PayTypeBean.DataBean> {

    Map<Integer, Integer> selectMap = new HashMap<>();

    public PayTypeAdapter(Context context, int layoutId, List<PayTypeBean.DataBean> datas) {
        super(context, layoutId, datas);
        for (int i = 0; i < datas.size(); i++) {
            selectMap.put(datas.get(i).getPayment_type(), datas.get(i).getDefaultX());
        }
    }

    @Override
    protected void convert(ViewHolder holder, PayTypeBean.DataBean dataBean, int position) {
        ImageView ivPaypic = holder.getView(R.id.iv_pay_pic);
        TextView tvPayName = holder.getView(R.id.tv_cash);
        ImageView ivSelect = holder.getView(R.id.iv_select);

        TextViewUtils.setText2Tv(dataBean.getPayment_type_name(), tvPayName);
        if (dataBean.getPayment_mode() == 0) {
            ivPaypic.setImageResource(R.mipmap.xjzf);
        } else if (dataBean.getPayment_mode() == 1) {
            ivPaypic.setImageResource(R.mipmap.wechat);
        } else if (dataBean.getPayment_mode() == 2) {
            ivPaypic.setImageResource(R.mipmap.zfbzf);
        }

        if (selectMap.get(dataBean.getPayment_type()) == 1) {
            ivSelect.setImageResource(R.mipmap.xuanze);
        } else {
            ivSelect.setImageResource(R.mipmap.weixuanze);
        }
    }

    public void setSelectType(int position) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (i == position){
                selectMap.put(mDatas.get(i).getPayment_type(),1);
            }else{
                selectMap.put(mDatas.get(i).getPayment_type(),0);
            }
        }
        notifyDataSetChanged();
    }
}
