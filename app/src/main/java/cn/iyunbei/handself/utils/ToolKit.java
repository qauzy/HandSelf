package cn.iyunbei.handself.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import java.util.UUID;

public class ToolKit {
    /**
     * 得到全局唯一UUID,有权限时
     * @param context NameActivity.this
     * @return 返回UUID字符串
     */
    public static String getUniqueID(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

        }
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();

        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }
    /**
     * 得到全局唯一UUID,无权限时通过UUID.randomUUID().toString()随机产生一个UUID
     */
    public static String getUUID(Context context) {
        return UUID.randomUUID().toString();
    }
}
