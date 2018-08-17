package jt.kundream.utils;

import com.tencent.mm.opensdk.modelpay.PayReq;

import java.util.LinkedList;
import java.util.List;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/4/16/016 10
 * @e-mail: 245086168@qq.com
 * @desc:
 */

public class WechatPayUtils {

    /**
     * Company: YunBeiTeac
     * Copyright: Copyright (c) 2017-2018
     *
     * @author Created by YangTianKun at 2018/4/14/014 and 16:26
     * @Email 245086168@qq.com
     * describe:设置微信签名
     */
    public static List<KeyValueBean> setSign(PayReq req) {
        List<KeyValueBean> list = new LinkedList<>();
        list.add(new KeyValueBean("appid", req.appId));
        list.add(new KeyValueBean("noncestr", req.nonceStr));
        list.add(new KeyValueBean("package", req.packageValue));
        list.add(new KeyValueBean("partnerid", req.partnerId));
        list.add(new KeyValueBean("prepayid", req.prepayId));
        list.add(new KeyValueBean("timestamp", req.timeStamp));
        return list;
    }

    /**
     * 生成微信签名
     *
     * @return
     */
    private String genAppSign(List<KeyValueBean> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).getKey());
            sb.append('=');
            sb.append(list.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        //此处需要appkey生成签名  but此项目
//        sb.append(Constants.WECHAT_AK);
        String appSign = MD5Utils.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return appSign;
    }
}
