package jt.kundream.utils;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2015-10-10 - 14:13
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import android.view.WindowManager;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2015-10-10 - 14:10
 */
public class ActivityUtil {

    /**
     * Company: YunBeiTeac
     * Copyright: Copyright (c) 2017-2018
     *
     * @author Created by YangTianKun at 2018/5/25 and 10:33
     * @Email 245086168@qq.com
     * describe:为页面添加一个遮罩层  1为全透明  0 为纯黑色
     */
    public static void backgroundAlpha(float bgAlpha,Activity ctx) {
        WindowManager.LayoutParams lp = ctx.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ctx.getWindow().setAttributes(lp);
    }

    public static void startActivity(Fragment fragment, Class<?> cls) {
        fragment.startActivityForResult(getIntent(fragment.getActivity(), cls, null), 1);
    }

    public static void startActivity(Fragment fragment, Class<?> cls, Intent intent) {
        fragment.startActivityForResult(getIntent(fragment.getActivity(), cls, intent), 1);
    }

    // --------
    public static void startActivity(Context context, Class<?> cls) {
        startActivity(context, cls, false);
    }

    public static void startActivity(Context context, Class<?> cls, Intent intent) {
        startActivity(context, cls, intent, false);
    }

    public static void startActivity(Context context, Class<?> cls, boolean isFinish) {
        startActivity(context, cls, null, isFinish);
    }

    public static void startActivity(Context context, Class<?> cls, Intent intent, boolean isFinish) {
        context.startActivity(getIntent(context, cls, intent));
        if (isFinish && context instanceof Activity) {
            finish((Activity) context);
        }
    }

    // ------- activity. startActivityForResult
    public static void startActivityForResult(
                Context context, Class<?> cls, int code) {
        startActivityForResult(context, cls, null, code, false);
    }

    public static void startActivityForResult(
                Context context, Intent intent, int code) {
        startActivityForResult(context, null, intent, code, false);
    }

    public static void startActivityForResult(
                Context context, Class<?> cls, Intent intent, int code) {
        startActivityForResult(context, cls, intent, code, false);
    }

    public static void startActivityForResult(
                Context context, Class<?> cls, Intent intent, int code, boolean isFinish) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.startActivityForResult(getIntent(context, cls, intent), code);
            if (isFinish) {
                finish(activity);
            }
        }
    }

    // -------
    public static void startActivityForResult(
                Fragment fragment, Class<?> cls, int code) {
        startActivityForResult(fragment, cls, null, code, false);
    }

    public static void startActivityForResult(
                Fragment fragment, Intent intent, int code) {
        startActivityForResult(fragment, null, intent, code, false);
    }

    public static void startActivityForResult(
                Fragment fragment, Class<?> cls, Intent intent, int code) {
        startActivityForResult(fragment, cls, intent, code, false);
    }

    public static void startActivityForResult(
                Fragment fragment, Class<?> cls, Intent intent, int code, boolean isFinish) {
        fragment.startActivityForResult(getIntent(fragment.getActivity(), cls, intent), code);
        if (isFinish) {
            finish(fragment.getActivity());
        }
    }

    //
    public static void finish(Context context) {
        if (context instanceof Activity) {
            finish((Activity) context);
        }
    }

    public static void finish(Context context, int code) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.setResult(code);
            activity.finish();
        }
    }

    public static void finish(Context context, Intent data, int code) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.setResult(code, data);
            activity.finish();
        }
    }

    private static void finish(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
    }

    /**
     * @param context
     * @param cls
     * @param intent
     * @return 获取intent
     */
    private static Intent getIntent(Context context, Class<?> cls, Intent intent) {
        if (intent == null) {
            intent = new Intent();
        }
        if (cls != null) {
            intent.setClass(context, cls);
        }
        return intent;
    }
}