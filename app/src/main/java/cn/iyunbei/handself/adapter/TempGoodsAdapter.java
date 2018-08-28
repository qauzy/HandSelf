package cn.iyunbei.handself.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.iyunbei.handself.R;
import cn.iyunbei.handself.bean.GoodsBeanDao;
import cn.iyunbei.handself.bean.OrderBeanDao;
import cn.iyunbei.handself.bean.OrderIdDao;
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
public class TempGoodsAdapter extends CommonAdapter<GoodsBeanDao> {

    private  Map<Long, Integer> numMap = new HashMap<>();
//    private  List<GoodsBeanDao> mList = new ArrayList<>();

    public TempGoodsAdapter(Context context, int layoutId, List<GoodsBeanDao> datas,Map<Long,Integer> numMap) {
        super(context, layoutId, datas);

        this.numMap = numMap;

        // TODO: 2018/8/28 这里的集合长度  指的是本个订单中包含有多少商品
//        ((OrderIdDao) ((ArrayList) result).get(0)).daoSession.queryBuilder(OrderBeanDao.class).build()
//                      .list().get(8).daoSession.queryBuilder(GoodsBeanDao.class).build().list();

//        if (mDatas.size() > 2){
//
//            for (int i = 0; i < 2; i++) {
//                mList.add(datas.get(i).getGoodsBeanDao());
//                numMap.put(datas.get(i).getGoodsId(),datas.get(i).getGoodsNum());
//            }
//
////            long goodsId = datas.get(0).getGoodsId();
////            GoodsBeanDao goodsBeanDao = datas.get(0).getGoodsBeanDao();
////
////            mList.add(mDatas.get(0));
////            mList.add(mDatas.get(1));
//        }
    }

    @Override
    protected void convert(ViewHolder holder, GoodsBeanDao goodsBeanDao, int position) {
        TextView tvName = holder.getView(R.id.tv_goods_name);
        TextView tvGuiGe = holder.getView(R.id.tv_goods_guige);
        TextView tvCode = holder.getView(R.id.tv_goods_code);
        TextView tvMoney = holder.getView(R.id.tv_money);
        TextView tvNum = holder.getView(R.id.tv_num);

        TextViewUtils.setText2Tv(goodsBeanDao.getName(),tvName);
        TextViewUtils.setText2Tv(goodsBeanDao.getGoodsGuige(),tvGuiGe);
        TextViewUtils.setText2Tv(String.valueOf(goodsBeanDao.getGoodsBarCode()),tvCode);
        TextViewUtils.setText2Tv("￥"+goodsBeanDao.getPrice(),tvMoney);
        TextViewUtils.setText2Tv("X"+numMap.get(goodsBeanDao.getGoodsId()),tvNum);
    }
}
