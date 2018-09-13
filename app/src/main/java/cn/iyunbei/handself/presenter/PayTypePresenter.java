package cn.iyunbei.handself.presenter;

import android.content.Context;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.PayTypeBean;
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

    public void useCashPay(Context context,int payType, List<TempOrderBean.TempGoodsBean> goodsList,String realMoney) {
        String s = new Gson().toJson(goodsList);
        new PayTypeModel().useCashPay(CommonUtil.getString(context, "token"),payType, s,realMoney, payCallback);
    }

    /**
     * 获取用户设置的支付类型
     *
     * @param context
     */
    public void getPayType(Context context) {
        new PayTypeModel().getPayType(CommonUtil.getString(context, "token"), payTypeCallback);
    }

    private RequestCallback.PayTypeCallback payTypeCallback = new RequestCallback.PayTypeCallback() {
        @Override
        public void succ(PayTypeBean bean) {
            mView.showPayTypeList(bean.getData());

//            List<Integer> payModeList = new ArrayList<>();
//            int defaultPayType = 0;
//            for (int i = 0; i < bean.getData().size(); i++) {
//                payModeList.add(bean.getData().get(i).getPayment_mode());
//                int defaultX = bean.getData().get(i).getDefaultX();
//                if (defaultX == 1) {
//                    defaultPayType = bean.getData().get(i).getPayment_mode();
//                }
//            }
//            mView.showPayType(defaultPayType, payModeList, bean.getData());

//            if (payModeList.contains(1) && !payModeList.contains(2)) {
//                mView.setPayModeWeChat(bean.get);
//            } else if (!payModeList.contains(1) && payModeList.contains(2)) {
//                mView.setPayModeAli();
//            }else if (!payModeList.contains(1) && !payModeList.contains(2)){
//                mView.setPayModeCash();
//            }else{
//                mView.setPayModeAll();
//            }
        }

        @Override
        public void Fail(String errMsg) {
            mView.showToast(errMsg);
        }
    };
}
