package cn.iyunbei.handself.contract;

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
    }

    interface Presenter {
        /**
         * 此页面的必须方法
         *
         * @param s
         */
        void addGoods(String s, String token);

        void checkGoodsIsSame(Map<Integer, Integer> numMap, List<GoodsBean.DataBean> list, GoodsBean.DataBean bean);
    }
}
