package cn.iyunbei.handself.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.bean.UserBean;
import cn.iyunbei.handself.contract.UserCenterContract;
import cn.iyunbei.handself.presenter.UserCenterPresenter;
import jt.kundream.base.BaseActivity;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/8/30
 * @e-mail: 245086168@qq.com
 * @desc:个人中心
 **/
class UserCenterActivity extends BaseActivity<UserCenterContract.View, UserCenterPresenter> implements UserCenterContract.View {
    @Bind(R.id.iv_left)
    ImageView ivLeft;
    @Bind(R.id.tv_left)
    TextView tvLeft;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.tv_name1)
    TextView tvName1;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_tel)
    TextView tvTel;
    @Bind(R.id.tv_sell_today)
    TextView tvSellToday;
    @Bind(R.id.tv_sell_month)
    TextView tvSellMonth;
    @Bind(R.id.tv_order_num)
    TextView tvOrderNum;
    @Bind(R.id.tv_goods_num)
    TextView tvGoodsNum;
    @Bind(R.id.tv_sell_money)
    TextView tvSellMoney;
    @Bind(R.id.ll_order_list)
    LinearLayout llOrderList;
    @Bind(R.id.ll_goods_count)
    LinearLayout llGoodsCount;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_user_center;
    }

    @Override
    public void initView() {
        tvLeft.setVisibility(View.GONE);
        ivRight.setVisibility(View.GONE);
        tvRight.setVisibility(View.GONE);

        tvTitle.setText("个人中心");

        showProgress();
        presenter.getUserMsg(this);
        presenter.getUserOrderData(this);
    }

    @Override
    public UserCenterPresenter initPresenter() {
        return new UserCenterPresenter();
    }

    @Override
    public void showUserMsg(UserBean bean) {
        String name = bean.getData().getMember_name();
        tvName.setText(name);
        tvName1.setText(String.valueOf(name.charAt(name.length() - 1)));
        tvTel.setText(bean.getData().getMember_mobile());
    }
}
