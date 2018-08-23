package cn.iyunbei.handself.model;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import cn.iyunbei.handself.Constants;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.GoodsBean;
import cn.iyunbei.handself.contract.MainContract;
import jt.kundream.utils.JsonUtils;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public class MainModel implements MainContract.Model {

    public static void requestGoods(String s, String token, final RequestCallback.GetGoodsCallback callback) {
        OkGo.<String>post(Constants.GET_GOODS)
                .params("barcode", s)
                .params("_token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = response.body().toString();
                        if (JsonUtils.checkToken(result) == 200) {
                            GoodsBean goodsBean = new Gson().fromJson(result, GoodsBean.class);
                            callback.succ(goodsBean.getData());
                        } else {
                            callback.fial(JsonUtils.getMsg(result));
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        callback.fial("请求完成");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        callback.fial("网络错误");
                    }
                });
    }
}
