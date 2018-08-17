package jt.kundream.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

/**
*  @date:   创建时间： 2017/11/15 15:10
*  @author:  YangTiankun
*  @Describe :使用DisplayMetrics 获取设备屏幕的一些信息
*/
public class ScreenUtil {
	private static ScreenUtil screenUtil;
	private DisplayMetrics displayMetrics;
	private Display display;
	private Context context;

	public ScreenUtil(Context context) {
		displayMetrics = new DisplayMetrics();
		display = ((Activity) context).getWindowManager().getDefaultDisplay();
		display.getMetrics(displayMetrics);
		this.context = context.getApplicationContext();
	}
	public DisplayMetrics displayMetrics(Activity activity) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics;
	}
	public static ScreenUtil getInstance(Context context) {
		if (screenUtil == null) {
			screenUtil = new ScreenUtil(context);
		}
		return screenUtil;
	}

	/**
	 * 获取屏幕宽度 ，单位px
	 * 
	 * @return
	 */
	public int getWidth() {
		return display.getWidth();
	}

	/**
	 * 获取屏幕宽度 ，单位dip
	 * 
	 * @return
	 */
	public int getDipWidth() {
		return (int) (display.getWidth() / displayMetrics.density);
	}

	/**
	 * 获取屏幕高度，单位px
	 * 
	 * @return
	 */
	public int getHeight() {
		return display.getHeight();
	}

	/**
	 * 获取屏幕高度 ，单位dip
	 * 
	 * @return
	 */
	public int getDipHeight() {
		return (int) (display.getHeight() / displayMetrics.density);
	}

	/**
	 * 将px值转换成dip值
	 * 
	 * @param px
	 * @return
	 */
	public int px2dip(int px) {
		return (int) (px / displayMetrics.density);
	}

	/**
	 * 将dip值转换成px值
	 * 
	 * @param dip
	 * @return
	 */
	public int dip2px(int dip) {
		return (int) (dip * displayMetrics.density);
	}

	/**
	 * 将sp值转换成px值
	 * 
	 * @param spValue
	 * @return
	 */
	public int sp2px(int spValue) {
		return (int) (spValue * context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
	}

	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	/**
	 * Company: YunBeiTeac
	 * Copyright: Copyright (c) 2017-2018
	 *
	 * @author Created by YangTianKun at 2018/5/28 and 14:24
	 * @Email 245086168@qq.com
	 * describe:动态设置控件在布局中的margin
	 */
	public static void setMargins (View group, int l, int t, int r, int b) {
		if (group.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) group.getLayoutParams();
			p.setMargins(l, t, r, b);
			group.requestLayout();
		}
		// TODO: 下面为实例
//		TextView ceshiTv = (TextView) findViewById(R.id.ceshi_tv);
//		LinearLayout.LayoutParams lp = (LayoutParams) ceshiTv.getLayoutParams();
//		lp.setMargins(30, 50, 22, 10);
//		ceshiTv.setLayoutParams(lp);
	}
}
