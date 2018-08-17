package jt.kundream.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 版权所有，违法必究！
 * Created by YangTianKun on 2017.4.26.
 * 拨打电话工具类
 */

public class CallPhoneUtils {
    /**
     * 之调用拨号界面，不播出
     *
     * @param num
     */
    public static void call(Context ctx, String num) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    /**
     * 点击电话号码，直接拨打电话
     * 注意 这里需要动态权限了  尽量不这样做
     * @param ctx
     * @param phone
     */
//    public static void callPhone(Context ctx,String phone) {
//        Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));
//        ctx.startActivity(intent);
//    }
}
