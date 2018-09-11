package cn.iyunbei.handself.model;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import cn.iyunbei.handself.Constants;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.GoodsBean;
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

    public void setPDOk(String token, int pd_id, final RequestCallback.PdOkCallback pdOkCallback) {
        OkGo.<String>post(Constants.SET_PD_OK)
                .params("_token", token)
                .params("profit_id", pd_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String s = response.body().toString();
                        if (JsonUtils.checkToken(s) == 200) {
                            pdOkCallback.succ(JsonUtils.getMsg(s));
                        } else {
                            pdOkCallback.Fail(JsonUtils.getMsg(s));
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        pdOkCallback.Fail("网络连接错误");
                    }
                });
    }

    /**
     * 获取单个商品信息
     *
     * @param barCode
     * @param token
     * @param getGoodsCallback
     */
    public static void requestGoods(String barCode, String token, final RequestCallback.GetGoodsCallback getGoodsCallback) {
        OkGo.<String>post(Constants.GET_GOODS)
                .params("barcode", barCode)
                .params("_token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = response.body().toString();
                        if (JsonUtils.checkToken(result) == 200) {
                            GoodsBean bean = new Gson().fromJson(result, GoodsBean.class);
                            getGoodsCallback.succ(bean);
                        } else {
                            getGoodsCallback.fial(JsonUtils.getMsg(result));
                        }
                    }

//                    @Override
//                    public void onFinish() {
//                        super.onFinish();
//                        callback.fial("请求完成");
//                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        getGoodsCallback.fial("网络错误");
                    }
                });
    }

    /**
     * 创建新的盘点单
     *
     * @param token
     * @param createProfitCallback
     */
    public void createProfit(String token, final RequestCallback.CreateParofitCallback createProfitCallback) {
        OkGo.<String>post(Constants.CREATE_PROFIT)
                .params("_token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String s = response.body().toString();
                        if (JsonUtils.checkToken(s) == 200) {
                            createProfitCallback.succ(JsonUtils.getInnerStr(s, "profit_id"), JsonUtils.getInnerStr(s, "profit_status"));
                        } else {
                            createProfitCallback.Fail(JsonUtils.getMsg(s));
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        createProfitCallback.Fail("");
                    }
                });
    }
}
