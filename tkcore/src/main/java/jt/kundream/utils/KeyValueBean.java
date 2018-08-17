package jt.kundream.utils;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/4/14/014 16
 * @e-mail: 245086168@qq.com
 * @desc:用于设置微信支付等的签名的keyvalue的实体bean
 */

public class KeyValueBean {
    private String key;
    private String value;

    public KeyValueBean(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
