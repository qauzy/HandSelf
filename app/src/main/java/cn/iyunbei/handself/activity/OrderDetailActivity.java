package cn.iyunbei.handself.activity;

import cn.iyunbei.handself.contract.OrderDetailContract;
import cn.iyunbei.handself.presenter.OrderDetailPresenter;
import jt.kundream.base.BaseActivity;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/8/31
 * @e-mail: 245086168@qq.com
 * @desc:订单详情
 **/
public class OrderDetailActivity extends BaseActivity<OrderDetailContract.View,OrderDetailPresenter> implements OrderDetailContract.View {
    @Override
    public int getLayoutResId() {
        return 0;
    }

    @Override
    public void initView() {

    }

    @Override
    public OrderDetailPresenter initPresenter() {
        return null;
    }
}
