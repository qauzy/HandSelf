package jt.kundream.utils;

import android.view.View;

/**
 * 版权所有，违法必究！
 * @author YangTianKun
 */

public class ClickViewsUtils {

    public static void setViewsOnclickListener(View.OnClickListener listener, View... views) {
        for (int i = 0; i < views.length; i++) {
            if (views[i] != null){
                views[i].setOnClickListener(listener);
            }
        }
    }
}
