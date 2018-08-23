package cn.iyunbei.handself;

import cn.iyunbei.handself.bean.GoodsBean;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public interface RequestCallback {

    interface GetGoodsCallback {
        /**
         * 成功的话 回调一个java实体bean给页面显示
         */
        void succ(GoodsBean.DataBean bean);

        /**
         * 失败情况 提示用户
         *
         * @param err
         */
        void fial(String err);


    }

    /**
     * 登陆接口回调监听
     */
    interface LoginCallback {

        void succ(String token);

        void fail(String errMsg);

    }
}
