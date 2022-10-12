package cn.iyunbei.handself.model;

import android.util.Log;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import cn.iyunbei.handself.Constants;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.GoodsBean;
import cn.iyunbei.handself.bean.GoodsListBean;
import cn.iyunbei.handself.bean.PanDianBean;
import cn.iyunbei.handself.contract.GoodsContract;
import cn.iyunbei.handself.contract.PanDianContract;
import jt.kundream.utils.JsonUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/8/30
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public class GoodsListModel implements GoodsContract.Model {

    /**
     * 获取商品列表
     *
     * @param page
     * @param pageSize
     * @param goodsListCallback
     */
    public void getGoodsList(Integer page,Integer pageSize, final RequestCallback.GetGoodsListCallback goodsListCallback) {
        Gson gson = new Gson();

//把类模型对象转化成json的数据模式
        ShareEntity saveObject = new ShareEntity(page,pageSize);
        String contentJson = gson.toJson(saveObject);
        OkGo.<String>post(Constants.GET_GOODS_LIST)
                .upBytes(contentJson.getBytes(StandardCharsets.UTF_8))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String s = response.body();
                        if (JsonUtils.checkToken(s) == 200){
                            GoodsListBean bean = new Gson().fromJson(s, GoodsListBean.class);
                            goodsListCallback.succ(bean.getData());
                        }else{
                            goodsListCallback.Fail(JsonUtils.getMsg(s));
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        goodsListCallback.Fail("数据获取失败");
                    }
                });
    }

    /**
     * 获取更新商品信息
     *
     * @param barcode
     * @param goodsName
     * @param supplier
     * @param price
     * @param psec
     */
    public void saveGoodsInfo(Integer position, String barcode, String goodsName, String supplier, String price,String psec,final RequestCallback.UpdateInfoCallback updateInfoCallback) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("position", position);
            jsonObj.put("barcode", barcode);
            jsonObj.put("goodsName",goodsName);
            jsonObj.put("supplier",supplier);
            jsonObj.put("price",price);
            jsonObj.put("psec",psec);
        }catch (JSONException e){
            updateInfoCallback.Fail("参数错误");
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObj.toString());
        OkGo.<String>post(Constants.GOODS_SAVE)
                .upRequestBody(body)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String s = response.body();
                        if (JsonUtils.checkToken(s) == 200) {
                            GoodsBean bean = new Gson().fromJson(s, GoodsBean.class);
                            updateInfoCallback.succ(bean.getData());
                        } else {
                            updateInfoCallback.Fail(JsonUtils.getMsg(s));
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        updateInfoCallback.Fail("网络请求失败");
                    }
                });
    }
}
