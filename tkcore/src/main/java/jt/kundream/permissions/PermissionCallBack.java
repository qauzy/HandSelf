package jt.kundream.permissions;

import java.util.List;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @author: by 杨天坤 on 2018/1/4/004 14
 * @e-mail: 245086168@qq.com
 * @desc: 权限申请回调
 */

public interface PermissionCallBack {

    void onSuccessful();

    void onFailure(List<String> permissions);
}
