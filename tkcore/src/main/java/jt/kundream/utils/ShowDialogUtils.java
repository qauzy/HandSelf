package jt.kundream.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

/**
 * 版权所有，违法必究！
 * Created by YangTianKun on 2017.4.13.
 */

public class ShowDialogUtils {



    /**
     * 显示是否确定拨打电话的对话框
     */
    public static void showCallSelect(final Activity ctx, final String num) {
        new AlertDialog.Builder(ctx).setTitle("拨号提示")  //设置对话框标题
                    .setMessage("是否拨打电话？")
                    .setCancelable(false)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CallPhoneUtils.call(ctx, num);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
    }

    /**
     * 极光发送自定义消息之后，将得到的自定义消息展示在app中
     *
     * @param activity
     * @param string
     */
    public static void showReceiveMsg(final Activity activity, String string) {
        new AlertDialog.Builder(activity).setTitle("通知")  //设置对话框标题
                    .setMessage(string)
                    .setCancelable(false)
                    .setPositiveButton("确认收到", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            //在弹窗消失之后，应该将sp中保存的JPushMsg给删除，否则每次都会弹窗
                            SharedPreferences sp = activity.getSharedPreferences("user_state", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            if (sp.contains("JPushMsg")) {
                                editor.remove("JPushMsg").commit();
                            }
                        }
                    }).show();
    }


}
