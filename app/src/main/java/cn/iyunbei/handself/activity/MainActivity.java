package cn.iyunbei.handself.activity;

import cn.iyunbei.handself.R;
import cn.iyunbei.handself.contract.MainContract;
import cn.iyunbei.handself.presenter.MainPresenter;
import jt.kundream.base.BaseActivity;

/**
 * @author YangTianKun
 */
public class MainActivity extends BaseActivity<MainContract.View, MainPresenter> {

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {

    }

//    15917694315
//
//    123456


    @Override
    public MainPresenter initPresenter() {
        return null;
    }
}
