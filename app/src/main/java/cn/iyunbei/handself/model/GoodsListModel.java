package cn.iyunbei.handself.model;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.nio.charset.StandardCharsets;

import cn.iyunbei.handself.Constants;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.GoodsBean;
import cn.iyunbei.handself.bean.GoodsListBean;
import cn.iyunbei.handself.bean.PanDianBean;
import cn.iyunbei.handself.contract.GoodsContract;
import cn.iyunbei.handself.contract.PanDianContract;
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
public class GoodsListModel implements GoodsContract.Model {

    /**
     * 获取盘点列表
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
                        if (JsonUtils.checkToken(s) == 0){
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
}
