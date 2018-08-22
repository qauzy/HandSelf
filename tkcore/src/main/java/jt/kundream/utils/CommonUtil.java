package jt.kundream.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import jt.kundream.base.BaseApplication;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
public class CommonUtil {

    /**
     * 在主线程执行一段任务
     *
     * @param r
     */
    public static void runOnUIThread(Runnable r) {
        BaseApplication.mMainHandler.post(r);
    }

    /**
     * 把json字符串变成集合 params: new TypeToken<List<yourbean>>(){}.getType(),
     *
     * @param json
     * @param type new TypeToken<List<yourbean>>(){}.getType()
     * @return
     */
    public static List<?> parseJsonToList(String json, Type type) {
        Gson gson = new Gson();
        List<?> list = gson.fromJson(json, type);
        return list;
    }

    //========================== sp相关操作方法 =======================================

    /**
     * 向sp中保存数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void put(Context context, String key, Object value) {
        SharedPreferences sp = context.getSharedPreferences("pda", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (int) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (boolean) value);
        }
        edit.commit();
    }

    /**
     * 获取String
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("pda", Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    /**
     * 获取int值
     *
     * @param context
     * @param key
     * @return
     */
    public static int getInt(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("pda", Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    /**
     * 获取boolean值
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("pda", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static void clearSp(Context context) {
        SharedPreferences sp = context.getSharedPreferences("pda", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.commit();
    }

    /**
     * app使用 主要用于获取user_id
     */
    public static String getUd(Context context) {
        SharedPreferences sp = context.getSharedPreferences("pda", Context.MODE_PRIVATE);
        return sp.getString("user_id", "");
    }

    /**
     * app使用 主要用于获取user_token
     */
    public static String getToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("pda", Context.MODE_PRIVATE);
        return sp.getString("user_token", "");
    }


//    /**
//     * zxing 生产二维码
//     *
//     * @param str
//     * @return
//     * @throws WriterException
//     */
//    public static Bitmap create2DCode(String str) throws WriterException {
//        //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
//        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 264, 264);
//        int width = matrix.getWidth();
//        int height = matrix.getHeight();
//        //二维矩阵转为一维像素数组,也就是一直横着排了
//        int[] pixels = new int[width * height];
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                if (matrix.get(x, y)) {
//                    pixels[y * width + x] = 0xff000000;
//                }
//            }
//        }
//        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        //通过像素数组生成bitmap,具体参考api
//        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//
//
//        return bitmap;
//    }

    /**
     * 在二维码中间添加Logo图案
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }


    public static List activityList = new ArrayList();

    public static void exit() {
        int siz = activityList.size();

        for (int i = 0; i < siz; i++) {

            if (activityList.get(i) != null) {

                ((Activity) activityList.get(i)).finish();

            }
        }
    }

    /**
     * 判断网络状态,并引导用户设置网络
     * @param context
     * @return
     */

//    public static boolean getNetWorkStatus(final Context context) {
//        boolean netSataus = false;
//        ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        cwjManager.getActiveNetworkInfo();
//
//        if (cwjManager.getActiveNetworkInfo() != null) {
//            netSataus = cwjManager.getActiveNetworkInfo().isAvailable();
//        }
//
//        if (!netSataus) {
//            new AlertDialog(context).builder().setTitle("没有可用的网络")
//                    .setMsg("是否对网络进行设置？")
//                    .setPositiveButton("是", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent mIntent = new Intent("/");
//                            ComponentName comp = new ComponentName(
//                                    "com.android.settings",
//                                    "com.android.settings.WirelessSettings");
//                            mIntent.setComponent(comp);
//                            mIntent.setAction("android.intent.action.VIEW");
//                            if (android.os.Build.VERSION.SDK_INT > 13) {
//                                //3.2以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
//                                context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
//                            } else {
//                                context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
//                            }
//
//                            // context.startActivity(mIntent);
//                        }
//                    }).setNegativeButton("否", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                        }
//                    }).show();
//
//        }
//        return netSataus;
//    }

    /**
     * 判断当前网络状态
     *
     * @param context
     * @return
     */
    public static boolean checkNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            //没有网络
            return false;
        }
        int type = activeNetworkInfo.getType();
        //WIFI 移动数据
        if (type == ConnectivityManager.TYPE_WIFI || type == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

//    /**
//     * 提示网络状态
//     */
//    public static boolean showNetWorkStatus(Context context) {
//
//        boolean netSataus = false;
//        ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        cwjManager.getActiveNetworkInfo();
//
//        if (cwjManager.getActiveNetworkInfo() != null) {
//            netSataus = cwjManager.getActiveNetworkInfo().isAvailable();
//        }
//
//        if (!netSataus) {
//
//            new AlertDialog(context).builder()
//                    .setMsg("没有找到可用网络。请到设置里面启动数据网络或打开WLAN")
//                    .setNegativeButton("确定", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                        }
//                    }).show();
//        }
//        return netSataus;
//    }

    public static String getFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }


    /**
     * 判断是不是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric00(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 获取文件的md5值，用做检验apk的正确完整性
     *
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len = -1;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16).toUpperCase();
    }
}
