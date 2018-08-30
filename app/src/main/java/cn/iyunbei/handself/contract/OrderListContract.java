package cn.iyunbei.handself.contract;

import android.content.Context;

import jt.kundream.base.IBaseView;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/8/30
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public interface OrderListContract {
    interface Model {
    }

    interface View extends IBaseView {
    }

    interface Presenter {
        /**
         * 查询订单列表
         * @param ctx
         */
        void getOrderList(Context ctx);
    }
}
