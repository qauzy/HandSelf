package cn.iyunbei.handself.utils.aboutclick;

import java.util.Calendar;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public class OneClick {
    private String methodName;
    private static final int CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    public OneClick(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public boolean check() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            return false;
        } else {
            return true;
        }
    }

}
