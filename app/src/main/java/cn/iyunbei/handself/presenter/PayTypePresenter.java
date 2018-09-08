package cn.iyunbei.handself.presenter;

import android.content.Context;

import com.google.gson.Gson;

import java.util.List;

import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.TempOrderBean;
import cn.iyunbei.handself.contract.PayTypeContract;
import cn.iyunbei.handself.model.PayTypeModel;
import jt.kundream.base.BasePresenter;
import jt.kundream.utils.CommonUtil;

public class PayTypePresenter extends BasePresenter<PayTypeContract.View> implements PayTypeContract.Presenter {

    private RequestCallback.PayCallback payCallback = new RequestCallback.PayCallback() {
        @Override
        public void succ(String succMsg) {
            mView.showToast(succMsg);
            mView.cashPaySucc();
        }

        @Override
        public void Fail(String errMsg) {
            mView.showToast(errMsg);
        }
    };

    public void useCashPay(Context context, List<TempOrderBean.TempGoodsBean> goodsList) {
        String s = new Gson().toJson(goodsList);
        new PayTypeModel().useCashPay(CommonUtil.getString(context, "token"), s, payCallback);
    }
}
