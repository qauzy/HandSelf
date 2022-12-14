package cn.iyunbei.handself.contract;

import android.content.Context;

import cn.iyunbei.handself.bean.UserBean;
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
public interface UserCenterContract {
    interface Model {
    }

    interface View extends IBaseView {
        /**
         * 显示用户信息
         *
         * @param bean
         */
        void showUserMsg(UserBean bean);

        void showSellData(String total_amount, String order_num, String goods_num);
    }


    interface Presenter {
        /**
         * 获取用户信息
         *
         * @param
         */
        void getUserMsg(Context ctx);

        /**
         * 获取当日销售数据
         * @param ctx
         */
        void getDayOrderData(Context ctx);
        /**
         * 获取本月销售数据
         * @param ctx
         */
        void getMonthOrderData(Context ctx);
    }
}
