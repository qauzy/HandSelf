package cn.iyunbei.handself.presenter;

import android.content.Context;

import java.util.List;

import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.PanDianBean;
import cn.iyunbei.handself.contract.PanDianContract;
import cn.iyunbei.handself.model.PanDianModel;
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
public class PanDianPresenter extends BasePresenter<PanDianContract.View> implements PanDianContract.Presenter {

    private RequestCallback.GetPanDianListCallback pandianCallback = new RequestCallback.GetPanDianListCallback() {
        @Override
        public void succ(List<PanDianBean.DataBean> list) {
            mView.hideProgress();
            if (list.size() < 1) {
            } else {
                mView.showData(list);
            }

        }

        @Override
        public void Fail(String errMsg) {
            mView.hideProgress();

        }
    };

    @Override
    public void getPanDianList(Context ctx) {
        mView.showProgress();
        new PanDianModel().getPanDianList(CommonUtil.getString(ctx, "token"), pandianCallback);
    }
}
