package cn.iyunbei.handself.bean;

import java.util.List;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:存储临时订单数据
 **/

public class Single {

    private static volatile Single instance;

    private List<TempOrderBean> tempList;

    public List<TempOrderBean> getTempList() {
        return this.tempList;
    }

    public void setTempList(List<TempOrderBean> tempList) {
        this.tempList = tempList;
    }


    private Single() {
    }

    public static Single getInstance() {

        if (instance == null) {

            synchronized (Single.class) {

                if (instance == null) {

                    instance = new Single();

                }

            }

        }

        return instance;
    }

}
