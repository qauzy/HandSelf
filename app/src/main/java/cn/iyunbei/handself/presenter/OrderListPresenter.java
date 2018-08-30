
package cn.iyunbei.handself.presenter;

import android.content.Context;

import cn.iyunbei.handself.contract.OrderListContract;
import cn.iyunbei.handself.model.OrderListModel;
import jt.kundream.base.BasePresenter;

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
    @Override
    public void getOrderList(Context ctx) {
        new OrderListModel().getOrderList();
    }
}
