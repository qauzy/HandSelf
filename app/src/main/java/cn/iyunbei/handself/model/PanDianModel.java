package cn.iyunbei.handself.model;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import cn.iyunbei.handself.Constants;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.PanDianBean;
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
public class PanDianModel implements PanDianContract.Model {

    /**
     * 获取盘点列表
     *
     * @param token
     * @param pandianCallback
     */
    public void getPanDianList(String token, final RequestCallback.GetPanDianListCallback pandianCallback) {
        OkGo.<String>post(Constants.PANDIAN_LIST)
                .params("_token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String s = response.body().toString();
                        if (JsonUtils.checkToken(s) == 200){
                            PanDianBean bean = new Gson().fromJson(s, PanDianBean.class);
                            pandianCallback.succ(bean.getData());
                        }else{
                            pandianCallback.Fail(JsonUtils.getMsg(s));
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        pandianCallback.Fail("数据获取失败");
                    }
                });
    }
}
