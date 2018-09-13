package cn.iyunbei.handself.model;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import cn.iyunbei.handself.Constants;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.PayTypeBean;
import cn.iyunbei.handself.contract.PayTypeContract;
import jt.kundream.utils.JsonUtils;

public class PayTypeModel implements PayTypeContract.Model {
    public void useCashPay(String token,int payType, final String s,String realMoney, final RequestCallback.PayCallback payCallback) {
        OkGo.<String>post(Constants.CASH_PAY)
                .params("_token", token)
                .params("json_goods", s)
                .params("payment_type", payType)
                .params("total_amount", realMoney)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String s1 = response.body().toString();
                        if (JsonUtils.checkToken(s1) == 200) {
                            payCallback.succ(JsonUtils.getMsg(s1));
                        } else {
                            payCallback.Fail(JsonUtils.getMsg(s1));
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        payCallback.Fail("网络连接错误");
                    }
                });


    }

    /**
     * 获取收款方式
     *
     * @param token
     * @param payTypeCallback
     */
    public void getPayType(String token, final RequestCallback.PayTypeCallback payTypeCallback) {
        OkGo.<String>post(Constants.PAY_TYPE)
                .params("_token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String s = response.body().toString();
                        if (JsonUtils.checkToken(s) == 200){
                            PayTypeBean payTypeBean = new Gson().fromJson(s, PayTypeBean.class);
                            payTypeCallback.succ(payTypeBean);

                        }else{
                            payTypeCallback.Fail(JsonUtils.getMsg(s));
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        payTypeCallback.Fail("网络连接错误");
                    }
                });
    }
}
