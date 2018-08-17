package jt.kundream.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @date: 创建时间： 2017/11/15 11:56
 * @author: YangTiankun
 * @Describe :  主要是获取Android设备信息的一些工具方法
 */
public final class AndroidUtil {
    private AndroidUtil() {
    }

    private static List<String> INVALID_IMEIs = new ArrayList<>();

    static {
        INVALID_IMEIs.add("358673013795895");
        INVALID_IMEIs.add("004999010640000");
        INVALID_IMEIs.add("00000000000000");
        INVALID_IMEIs.add("000000000000000");
    }

    /**
     * 判断IMEI是否合法
     *
     * @param imei
     * @return
     */
    private static boolean isValidIMEI(String imei) {
        if (TextUtils.isEmpty(imei)) {
            return false;
        }
        if (imei.length() < 10) {
            return false;
        }
        if (INVALID_IMEIs.contains(imei)) {
            return false;
        }
        return true;
    }

    private static final String INVALID_ANDROID_ID = "9774d56d682e549c";

    /**
     * 取得设备的唯一标识
     * <p>
     * imei -> androidId -> mac address -> uuid saved in sdcard
     *
     * @param context
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String getUUID(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
//            return TODO;
        }
        String imei = telephonyManager.getDeviceId();

        if (AndroidUtil.isValidIMEI(imei)) {
            return imei;
        }

        String androidId = Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
        if (!TextUtils.isEmpty(androidId)
                && !INVALID_ANDROID_ID.equals(androidId.toLowerCase())) {
            return androidId;
        }

        String macAddress = AndroidUtil.getWifiMacAddress(context);
        if (!TextUtils.isEmpty(macAddress)) {
            String udid = null;
            try {
                udid = AndroidUtil.toMD5(macAddress + Build.MODEL
                        + Build.MANUFACTURER + Build.ID + Build.DEVICE);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return udid;
        }

        return null;
    }

    /**
     * 获取wifi的mac地址
     *
     * @param context 上下文
     * @return mac地址
     */
    private static String getWifiMacAddress(final Context context) {
        try {
            WifiManager wifimanager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            String mac = wifimanager.getConnectionInfo().getMacAddress();
            if (TextUtils.isEmpty(mac)) {
                return null;
            }
            return mac;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static String toMD5(String source) {
//        if (null == source || "".equals(source)) {
//            return null;
//        }
//        try {
//            MessageDigest digest = MessageDigest
//                        .getInstance("MD5");
//            digest.update(source.getBytes());
//            return toHex(digest.digest());
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    private static String toHex(byte[] buf) {
        if (buf == null) {
            return "";
        }
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private final static String HEX = "0123456789ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    /**
     * 获取项目包名
     *
     * @param context 上下文
     * @return 项目包名
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 获取项目版本名称
     *
     * @param context 上下文
     * @return 版本名称
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取手机IMEI
     *
     * @param context
     * @return
     */
    public static final String getIMEI(Context context) {
        try {
            //实例化TelephonyManager对象
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //获取IMEI号
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                /**
                 * 此处可以是权限检验失败的处理
                 */
//                return ;
            }
            String imei = telephonyManager.getDeviceId();
            //在次做个验证，也不是什么时候都能获取到的啊
            if (imei == null) {
                imei = "";
            }
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * Company: YunBeiTeac
     * Copyright: Copyright (c) 2017-2018
     *
     * @author Created by YangTianKun at 2018/5/28 and 9:42
     * @Email 245086168@qq.com
     * describe:  具体可参考：https://blog.csdn.net/sunsteam/article/details/73189268
     * AndroidId 和 Serial Number 的通用性都较好，并且不受权限限制，如果刷机和恢复出厂设置会导致设备标识符重置这一点可以接受的话，那么将他们组合使用时
     * ，唯一性就可以应付绝大多数设备了。
     */

    public static String getUniqueId(Context context) {
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String id = androidID + Build.SERIAL;
        try {
            return toMD5(id);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return id;
        }
    }


    private static String toMD5(String text) throws NoSuchAlgorithmException {
        //获取摘要器 MessageDigest
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        //通过摘要器对字符串的二进制字节数组进行hash计算
        byte[] digest = messageDigest.digest(text.getBytes());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            //循环每个字符 将计算结果转化为正整数;
            int digestInt = digest[i] & 0xff;
            //将10进制转化为较短的16进制
            String hexString = Integer.toHexString(digestInt);
            //转化结果如果是个位数会省略0,因此判断并补0
            if (hexString.length() < 2) {
                sb.append(0);
            }
            //将循环结果添加到缓冲区
            sb.append(hexString);
        }
        //返回整个结果
        return sb.toString();
    }


}
