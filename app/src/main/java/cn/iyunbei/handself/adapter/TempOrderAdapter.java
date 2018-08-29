package cn.iyunbei.handself.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.iyunbei.handself.R;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.TempOrderBean;
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
public class TempOrderAdapter extends CommonAdapter<TempOrderBean> implements View.OnClickListener {

    private final RequestCallback.ItemViewOnClickListener clickListener;
    private List<TempOrderBean.TempGoodsBean> tempGoodsList;
//    private Map<Long, Integer> numMap = new HashMap<>();
//    private List<GoodsBeanDao> mList = new ArrayList<>();
//    private List<GoodsBeanDao> list1 = new ArrayList<>();


    public TempOrderAdapter(Context context, int layoutId, List<TempOrderBean> datas, RequestCallback.ItemViewOnClickListener clickListener) {
        super(context, layoutId, datas);
        this.clickListener = clickListener;
    }

    @Override
    protected void convert(ViewHolder holder, TempOrderBean tempOrderBean, int position) {
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

        /**
         * 设置内部的商品列表
         */
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rvGoods.setLayoutManager(linearLayoutManager);
        List<TempOrderBean.TempGoodsBean> goodsList = tempOrderBean.getGoodsList();
        if (goodsList.size() > 2) {
            tempGoodsList = goodsList.subList(0, 2);
        } else {
            tempGoodsList.addAll(goodsList);
        }
        TempGoodsAdapter mGoodsAdapter = new TempGoodsAdapter(mContext, R.layout.item_temp_goods, tempGoodsList);

        rvGoods.setAdapter(mGoodsAdapter);


        // TODO: 2018/8/29 因个人更换实现思路，故以下暂时作废，but以下内容很有参考意义，很多时候，东西难，就是难在了实现的思路上面
//        /**
//         *这里是每个单号里面有多少个订单id为 上一步的id的集合  其实也就是有多少个商品
//         *
//         * 1.这里查询的是第二张表里面，也就是存储了多少订单号，每个订单号对应一个商品，如，订单1中有1商品，那么就是对应1=1,2中有3个商品  对应2 = 3
//         *
//         */
//        List<OrderBeanDao> list = tempOrderBean.daoSession.queryBuilder(OrderBeanDao.class).build().list();
//        /**
//         * 截止上一行 其实得到的是第二张表(订单表，内含订单id，商品id和单个商品的number)的所有内容，实质上就是指现在的临时订单中，包含多少种商品。那么下面的逻辑进行梳理
//         * 1.将所有的订单号拿出来，放在一个新集合中newOrderIdList,(将所有的goodsid也拿出来放入一个集合newgoodsIdList)
//         *      把list中 orderId相同的goodsid
//         * 2.将数据库中所有的商品拿出来，放入一个集合中 goodsInDataList
//         * 3.比对：遍历goodsInDataList，如果newGoodsIdList中含有遍历时的元素，那么将这个元素的属性值放入需要显示的集合中goodsShowList
//         * 4.对adapter进行展示的时候，进行判断  如果新的集合goodsShowList长度大于2，那么就只截取前2个，否则，全部传入
//         */
//
////        List<Integer> newOrderIdList = new ArrayList<>();
//        List<Integer> newgoodsIdList = new ArrayList<>();  //订单表中存储的所有的goodsid
//
//        for (int i = 0; i < list.size(); i++) {
//            newgoodsIdList.add((int) list.get(i).getGoodsId());
//        }
//
//        //数据库中存储的所有的商品
//        List<GoodsBeanDao> goodsInDataList = MyApp.getDaoSession().getGoodsBeanDaoDao().queryBuilder().build().list();
//        //将所有的数据库中的商品
//
//        for (int i = 0; i < goodsInDataList.size(); i++) {
//            //如果遍历的订单的goodsid集合中  包含某个商品id
//            if (newgoodsIdList.contains(goodsInDataList.get(i).getGoodsId())) {
//
//            } else {
//                continue;
//            }
//        }
//
//
//        if (list != null && list.size() > 0) {
//
//            if (list.size() > 2) {
//                for (int i = 0; i < 2; i++) {
//
//                    /**
//                     * 1.查出商品集合
//                     * 2.拿到商品集合的前2条数据
//                     * 3.
//                     */
//                    list1 = list.get(i).daoSession.queryBuilder(GoodsBeanDao.class).build().list();
//
//                    list1.subList(0, 2);
//
////                mList.add(list.get(i).getGoodsBeanDao());
//
//                    numMap.put(list.get(i).getGoodsId(), list.get(i).getGoodsNum());
//                }
//            } else {
//                // TODO: 2018/8/29 问题出在这里 一直到.list()的时候得出来的结果都是null的  是否要异步查询？
//                list1.add(list.get(0).daoSession.queryBuilder(GoodsBeanDao.class).list().get(0));
//                list1.add(list.get(1).daoSession.queryBuilder(GoodsBeanDao.class).list().get(1));
////            mList.add(list.get(0).getGoodsBeanDao());
//                numMap.put(list.get(0).getGoodsId(), list.get(0).getGoodsNum());
//            }
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
//            rvGoods.setLayoutManager(linearLayoutManager);
//
//            TempGoodsAdapter mGoodsAdapter = new TempGoodsAdapter(mContext, R.layout.item_temp_goods, list1, numMap);
//            rvGoods.setAdapter(mGoodsAdapter);
//        } else {
//        }
    }

    @Override
    public void onClick(View view) {

    }
}
