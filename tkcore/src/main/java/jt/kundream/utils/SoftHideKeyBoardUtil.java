package jt.kundream.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * 版权所有,违法必究！！！
 *
 * @Company:NanYangYunBeiTeac
 * @Copyright:©2017-2018
 * @Author:YangTianKun Created at 2018/5/24 11
 * @e-mail:245086168@qq.com
 * @desc:解决键盘遮挡输入框的通用解决方法
 */

public class SoftHideKeyBoardUtil {


    private final View mChildOfContent;
    private final FrameLayout.LayoutParams frameLayoutParam;
    private int usableHeightPrevious;

    public static void assistActivity(Activity activity) {
        new SoftHideKeyBoardUtil(activity);
    }

    public SoftHideKeyBoardUtil(Activity activity) {
        //1.找到Activity的在外层布局控件，其实是一个DecorView 他所有的控件就是FrameLayout
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        //2.获取到setContentView放进去的view
        mChildOfContent = content.getChildAt(0);
        //3.给act的xml布局设置view树监听 当布局有变化时,如 键盘弹起或者关闭时，都会回调此监听
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //4.软键盘弹起时会使GlobalLayout发生变化
            @Override
            public void onGlobalLayout() {
                //5.当前布局变化时，对act的xml布局进行重绘
                possiblyResizeChildOfContent();
            }
        });
        //6.获取到act的xml布局放置参数
        frameLayoutParam = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent() {
        //获取当前界面可用高度，键盘弹起后，当前界面可用布局会减少键盘的高度
        int usableHeightNow = computeUsableHeight();
        //如果当前可用高度和原始值不一样
        if (usableHeightNow != usableHeightPrevious) {
            //3.获取activity中xml中布局在当前界面显示的高度
            int usableHeightScanKeyboard = mChildOfContent.getRootView().getHeight();
            //4.acti中xml的高度-当前可用高度
            int heightDifference = usableHeightScanKeyboard - usableHeightNow;
            //5.高度大于1/4时，说明键盘弹出
            if (heightDifference > (usableHeightScanKeyboard / 4)) {
                //6.键盘弹出了，activity的xml高度应当减去键盘高度
                frameLayoutParam.height = usableHeightScanKeyboard - heightDifference;
            } else {
                frameLayoutParam.height = usableHeightScanKeyboard;
            }
            //7.重绘activity的布局
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);

        return (r.bottom - r.top);
    }
}
