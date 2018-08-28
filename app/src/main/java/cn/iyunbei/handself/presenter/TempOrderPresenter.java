package cn.iyunbei.handself.presenter;

import android.content.Context;

import java.util.List;

import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.GoodsBeanDao;
import cn.iyunbei.handself.bean.OrderBeanDao;
import cn.iyunbei.handself.bean.OrderIdDao;
import cn.iyunbei.handself.bean.TempOrderBean;
import cn.iyunbei.handself.contract.TempOrderContract;
import cn.iyunbei.handself.model.TempOrderModel;
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


    private RequestCallback.QuarySqlCallback callback = new RequestCallback.QuarySqlCallback() {
        @Override
        public void quarySqlOrderIdSucc(List<TempOrderBean> orderIdList) {

        }

        @Override
        public void quarySqlFail(String errMsg) {

        }
    };

    @Override
    public void queryData(Context ctx) {
        new TempOrderModel().quaryOrderIdList(ctx, callback);
    }
}
