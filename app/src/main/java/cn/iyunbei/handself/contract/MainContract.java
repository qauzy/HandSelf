package cn.iyunbei.handself.contract;

import android.content.Context;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cn.iyunbei.handself.bean.GoodsBean;
import jt.kundream.base.IBaseView;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public interface MainContract {
    interface Model {
    }

    interface View extends IBaseView {
        /**
         * 扫码或者添加商品之后，处理得到的商品数据，之后再展示在界面中
         *
         * @param bean
         */
        void manageData(GoodsBean.DataBean bean);

        /**
         * 设置单品数量  存储在map中
         *
         * @param goodsId
         * @param num
         */
        void setNumMap(int goodsId, int num);

        void addList(GoodsBean.DataBean bean);

        /**
         * 设置底部显示的总价钱和总件数
         *
         * @param totalMoney
         * @param totalNum
         */
        void setToalData(BigDecimal totalMoney, int totalNum);

        /**
         * 显示空商品的布局
         */
        void showEmptyView();

        void setThisOrderTemp(long count);
    }

    interface Presenter {
        /**
         * 此页面的必须方法
         *
         * @param s
         */
        void addGoods(String s, String token);

        void checkGoodsIsSame(Map<Integer, Integer> numMap, List<GoodsBean.DataBean> list, GoodsBean.DataBean bean);

        /**
         * 计算订单总
         *
         * @param goodsList
         * @param numMap
         */
        void calcTotal(List<GoodsBean.DataBean> goodsList, Map<Integer, Integer> numMap);

        /**
         * 点击back之后 将数据存储在本地数据库  这里是判断数据的
         *
         * @param goodsList
         * @param numMap
         */
        void saveOrderDatas(List<GoodsBean.DataBean> goodsList, Map<Integer, Integer> numMap, Context ctx, double tolMon, int tolNum);
    }
}
