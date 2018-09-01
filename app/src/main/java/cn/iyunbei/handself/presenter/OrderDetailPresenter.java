package cn.iyunbei.handself.presenter;

import android.content.Context;

import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.OrderDetailBean;
import cn.iyunbei.handself.contract.OrderDetailContract;
import cn.iyunbei.handself.model.OrderDetailModel;
import jt.kundream.base.BasePresenter;
import jt.kundream.utils.CommonUtil;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/9/1
 * @e-mail: 245086168@qq.com
 * @desc:订单详情
 **/
public class OrderDetailPresenter extends BasePresenter<OrderDetailContract.View> implements OrderDetailContract.Presenter {

    private RequestCallback.GetOrderDetailCallback getOrderDetailCallback = new RequestCallback.GetOrderDetailCallback() {
        @Override
        public void getOrderDetailSucc(OrderDetailBean bean) {
            mView.hideProgress();
            mView.showOrderMsg(bean.getData());
        }

        @Override
        public void Fail(String errMsg) {
            mView.hideProgress();

        }
    };

    @Override
    public void getOrderDetail(Context ctx, int order_id) {
        new OrderDetailModel().getOrderDetail(CommonUtil.getString(ctx, "token"), order_id, getOrderDetailCallback);
    }
}
