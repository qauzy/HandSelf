package cn.iyunbei.handself.activity;

import android.app.Activity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.contract.MainContract;
import cn.iyunbei.handself.presenter.MainPresenter;
import cn.iyunbei.handself.utils.aboutclick.AntiShake;
import jt.kundream.base.BaseActivity;
import jt.kundream.utils.ActivityUtil;

/**
 * @author YangTianKun
 */
public class MainActivity extends BaseActivity<MainContract.View, MainPresenter> {

    @Bind(R.id.iv_left)
    ImageView ivLeft;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.head)
    RelativeLayout head;
    @Bind(R.id.tv_1)
    TextView tv1;
    @Bind(R.id.rl_middle)
    RelativeLayout rlMiddle;
    @Bind(R.id.tv_hand_input)
    TextView tvHandInput;
    @Bind(R.id.iv_jiesuan)
    ImageView ivJiesuan;
    @Bind(R.id.tv_jiesuan)
    TextView tvJiesuan;
    @Bind(R.id.tv_totalnum)
    TextView tvTotalnum;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        tvTitle.setText("结算");
        tvRight.setVisibility(View.GONE);
    }

//    15917694315
//
//    123456

    @Override
    public MainPresenter initPresenter() {
        return new MainPresenter();
    }

    @OnClick({R.id.iv_left, R.id.iv_right, R.id.tv_hand_input, R.id.tv_jiesuan})
    public void onClick(View view) {
        //判断是否多次点击
        if (AntiShake.check(view.getId())) {
            return;
        }

        switch (view.getId()) {
            case R.id.iv_left:

                break;

            case R.id.iv_right:

                break;

            case R.id.tv_hand_input:
                //点击弹出输入框
                ActivityUtil.backgroundAlpha(0.6f, this);
                showInputDialog();

                break;

            case R.id.tv_jiesuan:

                break;

            default:
                break;

        }
    }

    /**
     * 输入商品条码的dialog
     */
    private void showInputDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.dialog_input, null);
    }

}
