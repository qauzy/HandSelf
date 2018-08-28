package cn.iyunbei.handself.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.iyunbei.handself.R;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.GoodsBeanDao;
import cn.iyunbei.handself.bean.OrderBeanDao;
import cn.iyunbei.handself.bean.OrderIdDao;
import cn.iyunbei.handself.bean.TempOrderBean;
import cn.iyunbei.handself.greendao.GoodsBeanDaoDao;
import jt.kundream.adapter.rviewadapter.CommonAdapter;
import jt.kundream.adapter.rviewadapter.base.ViewHolder;
import jt.kundream.utils.TextViewUtils;
import jt.kundream.utils.TimeUtil;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public class TempOrderAdapter extends CommonAdapter<OrderIdDao> implements View.OnClickListener {

    private final RequestCallback.ItemViewOnClickListener clickListener;
    private Map<Long, Integer> numMap = new HashMap<>();
    private List<GoodsBeanDao> mList = new ArrayList<>();


    public TempOrderAdapter(Context context, int layoutId, List<OrderIdDao> datas, RequestCallback.ItemViewOnClickListener clickListener) {
        super(context, layoutId, datas);
        this.clickListener = clickListener;
    }

    @Override
    protected void convert(ViewHolder holder, OrderIdDao tempOrderBean, int position) {
        TextView tvOrderId = holder.getView(R.id.tv_order_id);
        TextView tvOrderTime = holder.getView(R.id.tv_order_time);
        TextView tvGoodsNum = holder.getView(R.id.tv_goods_num);
        TextView tvOrderMoney = holder.getView(R.id.tv_order_money);
        RecyclerView rvGoods = holder.getView(R.id.rv_goods);
        LinearLayout llBottom1 = holder.getView(R.id.ll_bottom_1);
        LinearLayout llPayAgain = holder.getView(R.id.ll_pay_again);
        LinearLayout llPayCancle = holder.getView(R.id.ll_pay_cancel);

        TextViewUtils.setText2Tv("订单编号：" + tempOrderBean.getOrderId(), tvOrderId);
        /**
         * 拿我自己的这个时间戳去获取时间  之后还要tmd格式化  我tmd是给自己挖了多少坑
         */
        String time = TimeUtil.stampToDate(Long.valueOf(tempOrderBean.getOrderId()), "MM-dd HH:mm");
        TextViewUtils.setText2Tv(time, tvOrderTime);

        TextViewUtils.setText2Tv("共" + tempOrderBean.getTotalNum() + "件商品  合计", tvGoodsNum);
        TextViewUtils.setText2Tv(String.valueOf(tempOrderBean.getTotalMoney()), tvOrderMoney);

        /**
         * 设置内部的几个点击事件
         */
        llBottom1.setOnClickListener(this);
        llBottom1.setTag(position);
        llPayAgain.setOnClickListener(this);
        llPayAgain.setTag(position);
        llPayCancle.setOnClickListener(this);
        llPayCancle.setTag(position);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rvGoods.setLayoutManager(linearLayoutManager);
        //这里是每个单号里面有多少个订单id为 上一步的id的集合  其实也就是有多少个商品
        List<OrderBeanDao> list = tempOrderBean.daoSession.queryBuilder(OrderBeanDao.class).build().list();
        if (list.size() > 2) {
            for (int i = 0; i < 2; i++) {

                list.get(i).daoSession.queryBuilder(GoodsBeanDaoDao.class).build();

                mList.add(list.get(i).getGoodsBeanDao());

                numMap.put(list.get(i).getGoodsId(), list.get(i).getGoodsNum());
            }
        }else{
            mList.add(list.get(0).getGoodsBeanDao());
            numMap.put(list.get(0).getGoodsId(), list.get(0).getGoodsNum());
        }
        TempGoodsAdapter mGoodsAdapter = new TempGoodsAdapter(mContext, R.layout.item_temp_goods, mList,numMap);
        rvGoods.setAdapter(mGoodsAdapter);
    }

    @Override
    public void onClick(View view) {

    }
}
