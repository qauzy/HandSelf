package cn.iyunbei.handself.contract;

import android.content.Context;

import java.util.List;

import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.activity.TempOrderActivity;
import cn.iyunbei.handself.bean.OrderIdDao;
import cn.iyunbei.handself.bean.TempOrderBean;
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
public interface TempOrderContract {
    interface Model {
        /**
         * 查询存入的订单表
         *
         * @param ctx
         */
        void quaryOrderIdList(Context ctx, RequestCallback.QuarySqlCallback callback);
    }

    interface View extends IBaseView {
        /**
         * 显示临时订单
         *
         * @param orderIdList
         */
        void showTempOrder(List<OrderIdDao> orderIdList);
    }

    interface Presenter {
        /**
         * 进入临时订单页面之后  开始查询存在数据库中的数据
         *
         * @param ctx
         */
        void queryData(Context ctx);
    }
}
