package cn.iyunbei.handself.model;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import cn.iyunbei.handself.Constants;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.contract.PayTypeContract;
import jt.kundream.utils.JsonUtils;

public class PayTypeModel implements PayTypeContract.Model {
    public void useCashPay(String token, final String s, final RequestCallback.PayCallback payCallback) {
        OkGo.<String>post(Constants.CASH_PAY)
                .params("_token", token)
                .params("json_goods", s)
                .params("payment_type", 0)
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
}
