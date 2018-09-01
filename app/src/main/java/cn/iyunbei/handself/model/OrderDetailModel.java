package cn.iyunbei.handself.model;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import cn.iyunbei.handself.Constants;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.OrderDetailBean;
import cn.iyunbei.handself.contract.OrderDetailContract;
import jt.kundream.utils.JsonUtils;

public class OrderDetailModel implements OrderDetailContract.Model {
    public void getOrderDetail(String token, int order_id, final RequestCallback.GetOrderDetailCallback getOrderDetailCallback) {
        OkGo.<String>post(Constants.GET_ORDER_DETAIL)
                .params("_token", token)
                .params("order_id", order_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String s = response.body().toString();
                        OrderDetailBean bean = new Gson().fromJson(s, OrderDetailBean.class);
                        if (JsonUtils.checkToken(s) == 200) {
                            getOrderDetailCallback.getOrderDetailSucc(bean);
                        } else {
                            getOrderDetailCallback.Fail(JsonUtils.getMsg(s));
                        }


                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        getOrderDetailCallback.Fail("获取失败");
                    }
                });

    }
}
