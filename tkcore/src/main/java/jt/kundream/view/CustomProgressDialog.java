package jt.kundream.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import jt.kundream.R;


/**
 * @date: 创建时间： 2017/11/15 10:21
 * @author: YangTiankun
 * @Describe : 自定义全局网络加载框
 */
public class CustomProgressDialog extends ProgressDialog {

    private AnimationDrawable mAnimation;
    private Context mContext;
    private ProgressBar mProgressBar;
    private String mLoadingTip;
    private TextView mLoadingTv;
    private int count = 0;
    private String oldLoadingTip;
    private int mResid;

    public CustomProgressDialog(Context context, String content, int theme, int id) {
        super(context, theme);
        this.mContext = context;
        this.mLoadingTip = content;
        this.mResid = id;
        setCanceledOnTouchOutside(false);
    }

    public CustomProgressDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }

    public CustomProgressDialog(Context context, String content, int id) {
        super(context);
        this.mContext = context;
        this.mLoadingTip = content;
        this.mResid = id;
        setCanceledOnTouchOutside(false); // 设置不能点击对话框外边取消当前对话框
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
//        initData();
        init();
    }

    /**
     * Company: YunBeiTeac
     *
     * @author Created by YangTianKun at 2017/12/20/020 and 16:27
     * @Email 245086168@qq.com
     * describe:使用progressBar进度框的时候的加载
     */
    private void init() {
//设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        setContentView(R.layout.progress_loading_dialog);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }


//    private void initData() {
//        mProgressBar.setBackgroundResource(mResid);
//        // 通过ImageView对象拿到背景显示的AnimationDrawable
//        mAnimation = (AnimationDrawable) mProgressBar.getBackground();
//        // 为了防止在onCreate方法中只显示第一帧的解决方案之一
//        mProgressBar.post(new Runnable() {
//            @Override
//            public void run() {
//                mAnimation.start();
//            }
//        });
//        mLoadingTv.setText(mLoadingTip);
//    }

    public void setContent(String str) {
        mLoadingTv.setText(str);
    }

    private void initView() {
        setContentView(R.layout.progress_loading_dialog);
        mLoadingTv = (TextView) findViewById(R.id.loadingTv);
        mProgressBar = (ProgressBar) findViewById(R.id.loadingIv);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    //    @Override
//    public void dismiss() {
//        //dialog消失，则停止动画
//        if (mAnimation != null) {
//            mAnimation.stop();
//        }
//        super.dismiss();
//    }
}