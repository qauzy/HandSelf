package cn.iyunbei.handself.contract;

import android.content.Context;

import cn.iyunbei.handself.activity.LoginActivity;
import jt.kundream.base.IBaseView;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public interface LoginContract {
    interface Model {
    }

    interface View extends IBaseView {
        void loginSucc(String token);

        void skipLogin();
    }

    interface Presenter {
        void login(String phone, String pswd);

        /**
         * 检验用户是否登陆过
         * @param ctx
         */
        void checkToken(Context ctx);
    }
}
