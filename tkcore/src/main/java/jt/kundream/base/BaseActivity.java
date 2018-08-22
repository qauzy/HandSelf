package jt.kundream.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.ButterKnife;
import jt.kundream.R;
import jt.kundream.utils.LogEncapsulation;
import jt.kundream.view.CustomProgressDialog;
import jt.kundream.view.HeadWidget;


/**
 * @date: 创建时间： 2017/11/15 11:33
 * @author: YangTiankun
 * @Describe : Activity基类，所有的act都要继承于此.此类实现了IBaseView，因此子类中不用再重写
 * BaseView的方法
 */
public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity implements IBaseView {

    public T presenter;
    private HeadWidget headWidget;
    private CustomProgressDialog customProgressDialog;
    private Toast mToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (isNeedHead()) {
            initToolBar(getLayoutResId());
        } else {
            super.setContentView(getLayoutResId());
        }
        //透明状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //mmersionBar依赖的默认使用  默认透明状态栏和黑色导航栏
//        ImmersionBar.with(this)
//                .transparentStatusBar()  //透明状态栏  默认透明色
//                .fitsSystemWindows(true) //解决状态栏和布局重叠问题 写完之后 会把状态栏高度留出来 在状态栏下面加载布局
////                .statusBarColor(getStatusBarColor())
////                .navigationBarColor(R.color.green) //导航栏颜色，不写默认黑色
//                .init();
//        presenter = initPresenter();
//        presenter.attach((V) this);
        ButterKnife.bind(this);
//        EventBus.getDefault().register(this);
        customProgressDialog = new CustomProgressDialog(this, R.style.loading_style);
        // customProgressDialog.setCancelable(false); // 设置不响应返回按钮点击事件
        initView();
    }

    /**
     * @author YangTianKun
     * @date 创建时间 2017/11/23 11:08
     * Describe：增加设置状态栏背景色的方法  一般来讲一个app的标题栏颜色，增加这个方法只是为了防止部分傻逼UI
     */
    public int getStatusBarColor() {
        return R.color.yunbei_bg;
    }

    private void initToolBar(int layoutResID) {
        headWidget = new HeadWidget(this, layoutResID);
        super.setContentView(headWidget.getContentView());
    }


    /**
     * get layoutResId
     *
     * @return
     */
    public abstract int getLayoutResId();

    /***
     * init view data
     */
    public abstract void initView();

    /**
     * init presenter
     *
     * @return
     */
    public abstract T initPresenter();


    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
//        MobclickAgent.onPageStart(this.getClass().getSimpleName());
        /**
         * 设置所有Activity为竖屏显示
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
//        MobclickAgent.onPause(this);
//        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        presenter.detach();
        ButterKnife.unbind(this);
        //如果不销毁ImmersionBar的话，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        ImmersionBar.with(this).destroy();
//        EventBus.getDefault().register(this);
        hideProgress();
        super.onDestroy();
//        Glide.with(this).pauseRequests();
    }

    /**
     * 是否需要使用 通用的header
     */
    protected boolean isNeedHead() {
        return false;
    }

    /**
     * @return 返回hearer 操作类
     */
    public HeadWidget getHeadWidget() {
        return headWidget;
    }

    /**
     * 打印日志
     *
     * @param logContent 日志内容
     */
    public void Lg(String logContent) {
        LogEncapsulation.d(this.getClass().getSimpleName(), logContent);
    }

    @Override
    public void showProgress() {
        if (customProgressDialog != null) {
            customProgressDialog.show();
        }
    }

    @Override
    public void hideProgress() {
        if (customProgressDialog != null && customProgressDialog.isShowing()) {
            customProgressDialog.dismiss();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showToast(String content) {
        hideProgress();
        //Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
        showToast(this, content, 100);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 弹出toast
     *
     * @param context
     * @param msg
     * @param duration
     */
    public void showToast(Context context, String msg, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, duration);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}

