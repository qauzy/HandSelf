package jt.kundream.base;

import android.content.Context;

/**
*  @date:   创建时间： 2017/11/15 11:35
*  @author:  YangTiankun
*  @Describe :
 *      界面的接口规范
*/
public interface IBaseView {
    /**
     * 显示进度条
     */
    void showProgress();

    /**
     * 隐藏进度条
     */
    void hideProgress();

    Context getContext();

    /**
     * 显示toast
     *
     * @param content toast内容
     */
    void showToast(String content);
}
