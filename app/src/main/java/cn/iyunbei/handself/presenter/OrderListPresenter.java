
package cn.iyunbei.handself.presenter;

import android.content.Context;

import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.OrderListBean;
import cn.iyunbei.handself.contract.OrderListContract;
import cn.iyunbei.handself.model.OrderListModel;
import jt.kundream.base.BasePresenter;
import jt.kundream.utils.CommonUtil;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/8/30
 * @e-mail: 245086168@qq.com
 * @desc:
 **/

public class OrderListPresenter extends BasePresenter<OrderListContract.View> implements OrderListContract.Presenter {


    private RequestCallback.GetOrderListCallback getOrderCallback = new RequestCallback.GetOrderListCallback() {
        @Override
        public void getOrderListSucc(OrderListBean bean) {
            mView.hideProgress();
            mView.showOrderList(bean.getData());
        }

        @Override
        public void Fail(String errMsg) {
            mView.hideProgress();
            mView.showToast(errMsg);
        }
    };

    @Override
    public void getOrderList(Context ctx, String time) {

        new OrderListModel().getOrderList(CommonUtil.getString(ctx, "token"), time, getOrderCallback);
    }
}
