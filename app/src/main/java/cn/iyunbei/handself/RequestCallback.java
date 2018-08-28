package cn.iyunbei.handself;

import android.view.View;

import java.util.List;

import cn.iyunbei.handself.bean.GoodsBean;
import cn.iyunbei.handself.bean.GoodsBeanDao;
import cn.iyunbei.handself.bean.OrderBeanDao;
import cn.iyunbei.handself.bean.OrderIdDao;
import cn.iyunbei.handself.bean.TempOrderBean;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public interface RequestCallback {

    interface GetGoodsCallback {
        /**
         * 成功的话 回调一个java实体bean给页面显示
         */
        void succ(GoodsBean.DataBean bean);

        /**
         * 失败情况 提示用户
         *
         * @param err
         */
        void fial(String err);


    }

    /**
     * 登陆接口回调监听
     */
    interface LoginCallback {

        void succ(String token);

        void fail(String errMsg);

    }

    /**
     * item内部控件点击处理
     */
    interface ItemViewOnClickListener {
        /**
         * 用于结算商品时候的数量点击
         */
        void clickAddOrMin(View view);

    }

    /**
     * 点击临时订单之后，查询数据库拿到的数据，个人在此处做处理。
     */
    interface QuarySqlCallback {

        void quarySqlOrderIdSucc(List<TempOrderBean> orderIdList);

        void quarySqlFail(String errMsg);
    }
}
