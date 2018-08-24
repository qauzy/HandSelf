package jt.kundream.base;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public abstract class BaseModle<P> {

    public P mPresenter;

    public void attachPresenter(P mPresenter) {
        this.mPresenter = mPresenter;
    }

    public void detach() {
        mPresenter = null;
    }
}
