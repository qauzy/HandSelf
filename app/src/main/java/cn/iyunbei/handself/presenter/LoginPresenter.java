package cn.iyunbei.handself.presenter;

import android.content.Context;
import android.text.TextUtils;

import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.contract.LoginContract;
import cn.iyunbei.handself.model.LoginModel;
import jt.kundream.base.BasePresenter;
import jt.kundream.utils.CommonUtil;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    private RequestCallback.LoginCallback loginCallback = new RequestCallback.LoginCallback() {
        @Override
        public void succ(String token) {
            mView.loginSucc(token);
        }

        @Override
        public void fail(String errMsg) {
            mView.showToast(errMsg);
        }
    };

    @Override
    public void login(String phone, String pswd) {
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(pswd)) {
            mView.showToast("请输入手机号和密码");
        } else {
            LoginModel.login(phone, pswd, loginCallback);
        }
    }

    @Override
    public void checkToken(Context ctx) {
        if (!TextUtils.isEmpty(CommonUtil.getString(ctx, "token"))) {
            mView.skipLogin();
        }
    }
}
