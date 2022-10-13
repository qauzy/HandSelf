package cn.iyunbei.handself.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import cn.iyunbei.handself.activity.MainActivity;

//开机自启动
public class BootBroadcastReceiver extends BroadcastReceiver {
    private final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("BootBroadcastReceiver", intent.getAction());
        Toast.makeText(context, intent.getAction(), Toast.LENGTH_LONG).show();

        /**
         * 如果 系统 启动的消息，则启动 APP 主页活动
         */

        if (ACTION_BOOT.equals(intent.getAction())) {
            Intent intentMainActivity = new Intent(context, MainActivity.class);
            intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentMainActivity);
            Log.e("BootBroadcastReceiver", "开机完毕~------启动MainActivity");
            Toast.makeText(context, "开机完毕~", Toast.LENGTH_LONG).show();
        }else {
            Log.e("BootBroadcastReceiver","Boot 非开机自动启动");
        }

    }

}
