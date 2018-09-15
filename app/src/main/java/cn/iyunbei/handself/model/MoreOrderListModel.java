package cn.iyunbei.handself.model;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

import cn.iyunbei.handself.Constants;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.DayOrder;
import cn.iyunbei.handself.bean.OrderListPageBean;
import cn.iyunbei.handself.contract.MoreOrderListContract;
import jt.kundream.utils.CommonUtil;
import jt.kundream.utils.JsonUtils;
import jt.kundream.utils.ToastUtils;

public class MoreOrderListModel implements MoreOrderListContract.Model {
    public void reqMoreListData(String token, String date, int page, final RequestCallback.MoreListCallback moreListCallback) {
        OkGo.<String>post(Constants.LIST_NEXT)
                .params("_token", token)
                .params("date", date)
                .params("page", page)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String s = response.body().toString();
                        if (JsonUtils.checkToken(s) == 200) {
                            OrderListPageBean orderListPageBean = new Gson().fromJson(s, OrderListPageBean.class);

                            List<DayOrder> data = orderListPageBean.getData().getOrder().getData();
                            moreListCallback.succ(data);
                        } else {
                            moreListCallback.Fail(JsonUtils.getMsg(s));
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        moreListCallback.Fail("网络连接错误");
                    }
                });
    }
}
