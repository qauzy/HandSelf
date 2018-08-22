package cn.iyunbei.handself.model;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import cn.iyunbei.handself.Constants;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.contract.LoginContract;
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
public class LoginModel {

    public static void login(String phone, String pswd, final RequestCallback.LoginCallback loginCallback) {
        OkGo.<String>post(Constants.LOGIN)
                .params("account", phone)
                .params("pwd", pswd)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String s = response.body().toString();
                        if (JsonUtils.checkToken(s) == 200){
                            loginCallback.succ(JsonUtils.getInnerStr(s, "_token"));
                        }else{
                            loginCallback.fail(JsonUtils.getMsg(s));
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        loginCallback.fail("网络错误");
                    }
                });
    }
}
