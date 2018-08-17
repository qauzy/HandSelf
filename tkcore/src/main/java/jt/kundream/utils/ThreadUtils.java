package jt.kundream.utils;

import android.os.Handler;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @author: by 杨天坤 on 2018/1/2/002 15
 * @e-mail: 245086168@qq.com
 * @desc: 线程池使用工具类  用此工具类去统一对线程进行管理操作
 */

public class ThreadUtils {
    /**
     * 在子线程执行
     * @param runnable
     */
    public static  void runOnBackThread(Runnable runnable){
        ThreadPoolManager.getInstance().createThreadPool().execture(runnable);
    }

    private static Handler handler = new Handler();

    /**
     * 在主线程执行
     * @param runnable
     */
    public static  void runOnUiThread(Runnable runnable){
        handler.post(runnable);
    }
}
