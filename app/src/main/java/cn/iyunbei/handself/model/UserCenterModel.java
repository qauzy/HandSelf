package cn.iyunbei.handself.model;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import cn.iyunbei.handself.Constants;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.UserBean;
import cn.iyunbei.handself.contract.UserCenterContract;
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
public class UserCenterModel implements UserCenterContract.Model {

    public void getUserMsg(String token, final RequestCallback.GetUserMsgCallback callback) {
        OkGo.<String>post(Constants.USER_MSG)
                .params("_token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String s = response.body().toString();

                        if (JsonUtils.checkToken(s) == 200) {
                            UserBean userBean = new Gson().fromJson(s, UserBean.class);
                            callback.getUserMsgSucc(userBean);
                        } else {
                            callback.getUserMsgFail(JsonUtils.getMsg(s));
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        callback.getUserMsgFail("网络错误");
                    }
                });
    }

    public void reqDaySell(String token, final RequestCallback.SellCallback sellCallback) {
        OkGo.<String>post(Constants.DAY_SELL)
                .params("_token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String s = response.body().toString();
                        if (JsonUtils.checkToken(s) == 200){
                            String total_amount = JsonUtils.getInnerStr(s, "total_amount");
                            String order_num = JsonUtils.getInnerStr(s, "order_num");
                            String goods_num = JsonUtils.getInnerStr(s, "goods_num");
                            sellCallback.succ(total_amount,order_num,goods_num);
                        }else{
                            sellCallback.Fail(JsonUtils.getMsg(s));
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        sellCallback.Fail("网络连接错误");
                    }
                });
    }

    public void reqMonthSell(String token, final RequestCallback.SellCallback sellCallback) {
        OkGo.<String>post(Constants.MONTH_SELL)
                .params("_token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String s = response.body().toString();
                        if (JsonUtils.checkToken(s) == 200){
                            String total_amount = JsonUtils.getInnerStr(s, "total_amount");
                            String order_num = JsonUtils.getInnerStr(s, "order_num");
                            String goods_num = JsonUtils.getInnerStr(s, "goods_num");
                            sellCallback.succ(total_amount,order_num,goods_num);
                        }else{
                            sellCallback.Fail(JsonUtils.getMsg(s));
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        sellCallback.Fail("网络连接错误");
                    }
                });
    }
}
