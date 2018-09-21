package cn.iyunbei.handself.presenter;

import android.content.Context;

import java.util.List;

import cn.iyunbei.handself.bean.Single;
import cn.iyunbei.handself.bean.TempOrderBean;
import cn.iyunbei.handself.contract.TempOrderContract;
import jt.kundream.base.BasePresenter;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public class TempOrderPresenter extends BasePresenter<TempOrderContract.View> implements TempOrderContract.Presenter {



    /**
     * 这里实质上是需要拿到保存的数据
     *
     * @param ctx
     */
    @Override
    public void queryData(Context ctx) {
        List<TempOrderBean> tempList = Single.getInstance().getTempList();
        if (tempList != null && tempList.size() > 0) {
            mView.showTempOrder(tempList);
        } else {
            mView.showToast("暂无临时订单");
        }
    }


}
