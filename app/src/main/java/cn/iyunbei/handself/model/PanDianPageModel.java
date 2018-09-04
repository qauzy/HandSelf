package cn.iyunbei.handself.model;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import cn.iyunbei.handself.Constants;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.PanDianingBean;
import cn.iyunbei.handself.contract.PanDianPageContract;
import jt.kundream.utils.JsonUtils;

public class PanDianPageModel implements PanDianPageContract.Model {

    public void reqPanDianing(String token, int pd_id, int page, final RequestCallback.PanDianingCallback callback) {

        OkGo.<String>post(Constants.PANDIAN_ING)
                .params("_token", token)
                .params("profit_id", pd_id)
                .params("page", page)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String s = response.body().toString();
                        if (JsonUtils.checkToken(s) == 200) {
                            PanDianingBean bean = new Gson().fromJson(s, PanDianingBean.class);
                            callback.succ(bean);
                        } else {
                            callback.Fail(JsonUtils.getMsg(s));
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        callback.Fail("网络连接错误");
                    }
                });

    }

    public void saveGoodsNum(int pd_id, String barcode, String num, String token, final RequestCallback.PanDianGoodsCallback pdGoodsCallback) {
        OkGo.<String>post(Constants.PD_SAVE)
                .params("_token", token)
                .params("barcode", barcode)
                .params("profit_id", pd_id)
                .params("real_number", num)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String s = response.body().toString();
                        if (JsonUtils.checkToken(s) == 200) {
                            pdGoodsCallback.succ(JsonUtils.getMsg(s));
                        } else {
                            pdGoodsCallback.Fail(JsonUtils.getMsg(s));
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        pdGoodsCallback.Fail("网络请求失败");
                    }
                });
    }
}
