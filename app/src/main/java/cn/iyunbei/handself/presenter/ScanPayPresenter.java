package cn.iyunbei.handself.presenter;

import com.google.gson.Gson;

import java.util.List;

import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.TempOrderBean;
import cn.iyunbei.handself.contract.ScanPayContract;
import cn.iyunbei.handself.model.ScanPayModel;
import jt.kundream.base.BasePresenter;

public class ScanPayPresenter extends BasePresenter<ScanPayContract.View> implements ScanPayContract.Presenter {

    private RequestCallback.PayCallback payCallback = new RequestCallback.PayCallback() {
        @Override
        public void succ(String succMsg) {
            mView.showToast(succMsg);

//            mView.paySucc();
        }

        @Override
        public void Fail(String errMsg) {
            mView.showToast(errMsg);
        }
    };

    /**
     * 开始支付，首先需要把集合数据转化为json
     *
     * @param token
     * @param goodsList
     * @param payMode
     * @param authCode
     */
    public void startPay(String token, List<TempOrderBean.TempGoodsBean> goodsList, int payMode, String authCode) {
        //将集合转化为后台规定的数据格式
        String s = new Gson().toJson(goodsList);

        new ScanPayModel().aliPay(token, s, payMode, authCode, payCallback);

    }
}
