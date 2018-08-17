package jt.kundream.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 版权所有，违法必究！！！
 *
 * @author YangTianK on 2017/11/18 09:45;
 * @Company NanYangYunBeiTeac
 * 邮箱：245086168@qq.com
 * 说明： 参数校验工具类
 */
public class CheckParamsUtils {

    /**
     * 校验参数不能为Null
     *
     * @param obj
     */
    public static void notNull(Object... obj) {
        notNull(null, obj);
    }

    /**
     * 校验参数不能为Null
     *
     * @param msg 提示信息
     * @param obj
     */
    public static void notNull(String msg, Object... obj) {
        if (!isNotNull(obj)) {
            throw new NullPointerException(msg);
        }
    }

    /**
     * 校验参数是否全部不为null
     *
     * @param obj
     * @return
     */
    public static boolean isNotNull(Object... obj) {

        if (obj == null) {
            return false;
        }

        for (Object o : obj) {
            if (o == null) {
                return false;
            }
        }

        return true;
    }


    /**
     * 对手机号码进行判断
     */
    public static boolean isMobile(String mobiles) {
        Pattern p = Pattern.compile("^(13[0-9]|14[57]|15[0-35-9]|16[6]|17[0-9]|18[0-9]|19[9])[0-9]{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }


}
