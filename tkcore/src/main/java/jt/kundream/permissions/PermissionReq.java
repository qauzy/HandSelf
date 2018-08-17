package jt.kundream.permissions;

import android.app.Activity;
import android.content.Context;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @author: by 杨天坤 on 2018/1/4/004 14
 * @e-mail: 245086168@qq.com
 * @desc: 权限申请的简易封装
 */

public class PermissionReq {

    private Context mContext;
    private PermissionCallBack mCallback;

    public PermissionReq(Context mContext, PermissionCallBack mCallBack) {
        this.mContext = mContext;
        this.mCallback = mCallBack;
    }

    public void request(String permission) {
        AndPermission.with(mContext)
                .requestCode(120)
                .permission(permission)
                .callback(this)
                .start();
    }


    /**
     * Company: YunBeiTeac
     *
     * @author Created by YangTianKun at 2018/1/4/004 and 14:49
     * @Email 245086168@qq.com
     * describe:对相应的权限进行申请  传入参数为所请求的权限
     */
    public void request(final Activity ctx, String... permissions) {
        AndPermission.with(mContext)
                .requestCode(110)
                .permission(permissions)
                .callback(this)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(ctx, rationale).show();
                    }
                })
                .start();
    }

    public void request(final Activity ctx, String[]... permissions) {
        AndPermission.with(mContext)
                .requestCode(110)
                .permission(permissions)
                .callback(this)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(ctx, rationale).show();
                    }
                })
                .start();
    }

    @PermissionYes(120)
    public void yes(List<String> permissions) {
        this.mCallback.onSuccessful();
    }

    @PermissionNo(120)
    public void no(List<String> permissions) {
        this.mCallback.onFailure(permissions);
    }
}
