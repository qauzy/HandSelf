package cn.iyunbei.handself.activity;

import cn.iyunbei.handself.contract.PanDianContract;
import cn.iyunbei.handself.presenter.PanDianPresenter;
import jt.kundream.base.BaseActivity;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/8/30
 * @e-mail: 245086168@qq.com
 * @desc:盘点页面
 **/
public class PanDianActivity extends BaseActivity<PanDianContract.View,PanDianPresenter>{
    @Override
    public int getLayoutResId() {
        return 0;
    }

    @Override
    public void initView() {

    }

    @Override
    public PanDianPresenter initPresenter() {
        return null;
    }
}
