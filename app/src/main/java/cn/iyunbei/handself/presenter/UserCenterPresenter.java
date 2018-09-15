package cn.iyunbei.handself.presenter;

import android.content.Context;

import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.UserBean;
import cn.iyunbei.handself.contract.UserCenterContract;
import cn.iyunbei.handself.model.UserCenterModel;
import jt.kundream.base.BasePresenter;
import jt.kundream.utils.CommonUtil;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/8/30
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public class UserCenterPresenter extends BasePresenter<UserCenterContract.View> implements UserCenterContract.Presenter {


    private RequestCallback.GetUserMsgCallback callback = new RequestCallback.GetUserMsgCallback() {
        @Override
        public void getUserMsgSucc(UserBean bean) {
            mView.hideProgress();
            mView.showUserMsg(bean);
        }

        @Override
        public void getUserMsgFail(String errMsg) {
            mView.hideProgress();
            mView.showToast(errMsg);
        }
    };

    @Override
    public void getUserMsg(Context ctx) {
        new UserCenterModel().getUserMsg(CommonUtil.getString(ctx, "token"), callback);
    }


    private RequestCallback.SellCallback sellCallback = new RequestCallback.SellCallback() {
        @Override
        public void succ(String total_amount, String order_num, String goods_num) {
            mView.showSellData(total_amount,order_num,goods_num);
        }

        @Override
        public void Fail(String errMsg) {
            mView.showToast(errMsg);
        }
    };

    @Override
    public void getDayOrderData(Context ctx) {
        new UserCenterModel().reqDaySell(CommonUtil.getString(ctx, "token"), sellCallback);
    }

    @Override
    public void getMonthOrderData(Context ctx) {
        new UserCenterModel().reqMonthSell(CommonUtil.getString(ctx, "token"), sellCallback);
    }

}
