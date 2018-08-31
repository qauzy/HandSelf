package cn.iyunbei.handself.model;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import cn.iyunbei.handself.Constants;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.OrderListBean;
import cn.iyunbei.handself.contract.OrderListContract;
import jt.kundream.utils.JsonUtils;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/8/30
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public class OrderListModel implements OrderListContract.Model {

    /**
     * 获取用户订单列表
     */
    public void getOrderList(String token, String time, final RequestCallback.GetOrderListCallback callback) {
        OkGo.<String>post(Constants.GET_ORDER_LIST)
                .params("_token", token)
                .params("date", time)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String s = response.body().toString();
                        if (JsonUtils.checkToken(s) == 200) {
                            OrderListBean orderListBean = new Gson().fromJson(s, OrderListBean.class);
                            callback.getOrderListSucc(orderListBean);
                        } else {
                            callback.Fail(JsonUtils.getMsg(s));
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        callback.Fail("网络错误");
                    }
                });

    }
}
