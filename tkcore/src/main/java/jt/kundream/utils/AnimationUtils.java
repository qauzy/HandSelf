package jt.kundream.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:动画工具类
 **/
public class AnimationUtils {


    /**
     * 旋转动画  多用于界面中箭头的反转
     * @param from
     * @param to
     * @param view
     */
    public static void Up2DownAnimation(float from, float to, View view) {
        RotateAnimation rotate = new RotateAnimation(from, to, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        rotate.setDuration(500);//设置动画持续周期
        rotate.setRepeatCount(0);//设置重复次数
        rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        view.startAnimation(rotate);
    }


}
