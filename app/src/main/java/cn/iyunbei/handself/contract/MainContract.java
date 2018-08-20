package cn.iyunbei.handself.contract;

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
public interface MainContract {
    interface Model {
    }

    interface View extends IBaseView {
    }

    interface Presenter {
        /**
         * 此页面的必须方法
         *
         * @param s
         */
        void addGoods(String s);

    }
}
