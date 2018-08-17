package jt.kundream.utils;

/**
 * 版权所有,违法必究！！！
 *
 * @Company:NanYangYunBeiTeac
 * @Copyright:©2017-2018
 * @Author:YangTianKun Created at 2018/5/19 11
 * @e-mail:245086168@qq.com
 * @desc:用于微信分享的一些接口回调
 */

public interface OnResponseListener {
    void onSuccess();

    void onCancel();

    void onFail(String message);
}
