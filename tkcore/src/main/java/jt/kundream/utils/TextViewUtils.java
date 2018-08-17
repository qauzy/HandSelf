package jt.kundream.utils;

import android.text.TextUtils;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/16/016 15
 * @e-mail: 245086168@qq.com
 * @desc:对数据进行判断并且非空的时候设置到textView中
 */

public class TextViewUtils {

    public static void setText2Tv(String s, TextView textView) {
        if (!TextUtils.isEmpty(s)) {
            textView.setText(s);
        } else {
            textView.setText("0");
        }
    }

    public static boolean isPhoneNum(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {

            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            return isMatch;
        }
    }
}
