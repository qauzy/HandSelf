package jt.kundream.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import jt.kundream.R;
import jt.kundream.utils.LogEncapsulation;
import jt.kundream.view.CustomProgressDialog;

/**
 * @date: 创建时间： 2017/11/22 14:35
 * @author: YangTiankun*
 * @Company NanYangYunBeiTeac
 * @Describe : BaseFragment  所有fragment的基类
 */
public abstract class BaseFragment<V, T extends BasePresenter<V>> extends Fragment implements IBaseView {
    public T presenter;
    public Context mContext;
    private Toast mToast;
    private CustomProgressDialog customProgressDialog;

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//    }

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    /*
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    /*
     * Called when the fragment attaches to the context
     */
    protected void onAttachToContext(Context context) {
        presenter = initPresenter();
        presenter.attach((V) this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        customProgressDialog = new CustomProgressDialog(getActivity(), R.style.loading_style);
        getBundleData();

    }

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
//        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(setLayoutId(), container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }


    /**
     * Company: YunBeiTeac
     *
     * @author Created by YangTianKun at 2017/11/25 0025  15:26
     * @Email 245086168@qq.com
     * describe:  根据布局id加载fragment
     */
    protected abstract int setLayoutId();


    /**
     * Company: YunBeiTeac
     *
     * @author Created by YangTianKun at 2017/11/25 0025  15:25
     * @Email 245086168@qq.com
     * describe:  用于获取onCreate方法里面参数的数据  主要用于fragment之间的数据传递
     * 感觉这里不用抽象方法就行  todo  待闲暇时研究下去除这里的   abstract
     */
    protected abstract void getBundleData();


    /**
     * Company: YunBeiTeac
     *
     * @author Created by YangTianKun at 2017/11/25 0025  15:26
     * @Email 245086168@qq.com
     * describe:初始化view和数据
     */
    protected abstract void initView();


    /**
     * Company: YunBeiTeac
     *
     * @author Created by YangTianKun at 2017/11/25 0025  15:28
     * @Email 245086168@qq.com
     * describe: 通过下面抽象方法，拿到具体的presenter
     */
    public abstract T initPresenter();

    /**
     * Company: YunBeiTeac
     *
     * @author Created by YangTianKun at 2017/12/6 0006  16:00
     * @Email 245086168@qq.com
     * describe: 设置顶部标题  这里如果fragment有顶部标题的话  就必须设置
     */
    public abstract void initHeadWidget();

    /**
     * 打印日志
     *
     * @param message
     */
    public void Lg(String message) {
        LogEncapsulation.d(this.getClass().getSimpleName(), message);
    }

    @Override
    public void onDestroyView() {
        presenter.detach();
        ButterKnife.unbind(this);//解绑
        hideProgress();
        super.onDestroyView();
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
    public void showToast(String content) {
        showToast(getActivity(), content, 100);
    }


    public void showToast(Context ctx, String msg, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(ctx, msg, duration);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
