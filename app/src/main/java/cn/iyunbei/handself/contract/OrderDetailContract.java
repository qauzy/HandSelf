package cn.iyunbei.handself.contract;

import android.content.Context;

import cn.iyunbei.handself.activity.OrderDetailActivity;
import cn.iyunbei.handself.bean.OrderDetailBean;
import jt.kundream.base.IBaseView;

public interface OrderDetailContract {
    interface Model {
    }

    interface View extends IBaseView {
        void showOrderMsg(OrderDetailBean.DataBean data);
    }

    interface Presenter {
        /**
         * 获取订单详情
         *
         * @param ctx
         * @param order_id
         */
        void getOrderDetail(Context ctx, int order_id);
    }
}
