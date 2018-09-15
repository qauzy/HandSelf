package cn.iyunbei.handself.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.bean.UserBean;
import cn.iyunbei.handself.contract.UserCenterContract;
import cn.iyunbei.handself.presenter.UserCenterPresenter;
import cn.iyunbei.handself.utils.aboutclick.AntiShake;
import jt.kundream.base.BaseActivity;
import jt.kundream.bean.EventBusBean;
import jt.kundream.utils.ActivityUtil;
import jt.kundream.utils.CommonUtil;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/8/30
 * @e-mail: 245086168@qq.com
 * @desc:个人中心
 **/
public class UserCenterActivity extends BaseActivity<UserCenterContract.View, UserCenterPresenter> implements UserCenterContract.View {
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
    @Bind(R.id.ll_exit)
    LinearLayout llExit;

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
        tvSellToday.setSelected(true);
        tvSellMonth.setSelected(false);

        showProgress();
        presenter.getUserMsg(this);
        presenter.getDayOrderData(this);
    }

    @Override
    public UserCenterPresenter initPresenter() {
        return new UserCenterPresenter();
    }

    @Override
    public void showUserMsg(UserBean bean) {
        String name = bean.getData().getMember_name();
        CommonUtil.put(this, "username", name);
        tvName.setText(name);
        tvName1.setText(String.valueOf(name.charAt(name.length() - 1)));
        tvTel.setText(bean.getData().getMember_mobile());
    }

    @Override
    public void showSellData(String total_amount, String order_num, String goods_num) {
        tvOrderNum.setText(order_num + "笔");
        tvGoodsNum.setText(goods_num + "件");
        tvSellMoney.setText(total_amount + "元");
    }

    @OnClick({R.id.iv_left, R.id.ll_order_list, R.id.ll_goods_count, R.id.ll_exit, R.id.tv_sell_today, R.id.tv_sell_month})
    public void onClick(View v) {
        //判断是否多次点击
        if (AntiShake.check(v.getId())) {
            return;
        }

        switch (v.getId()) {

            case R.id.iv_left:
                finish();
                break;

            case R.id.ll_order_list:
                //订单列表
                ActivityUtil.startActivity(this, OrderListActivity.class);

                break;

            case R.id.ll_goods_count:
                //货品盘点  进入盘点页面
                ActivityUtil.startActivity(this, PanDianActivity.class);
                break;
            case R.id.ll_exit:
                //退出app
                CommonUtil.clearSp(this);
                EventBus.getDefault().post(new EventBusBean("Main"));
                ActivityUtil.startActivity(this, LoginActivity.class, true);
                break;

            case R.id.tv_sell_today:
                //今日订单情况
                tvSellToday.setSelected(true);
                tvSellMonth.setSelected(false);
                presenter.getDayOrderData(this);

                break;

            case R.id.tv_sell_month:
                //本月订单情况
                tvSellToday.setSelected(false);
                tvSellMonth.setSelected(true);
                presenter.getMonthOrderData(this);
                break;

            default:
                break;


        }
    }

}
