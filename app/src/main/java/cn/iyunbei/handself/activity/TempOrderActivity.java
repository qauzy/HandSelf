package cn.iyunbei.handself.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.contract.TempOrderContract;
import cn.iyunbei.handself.presenter.TempOrderPresenter;
import jt.kundream.base.BaseActivity;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:临时订单的activity
 **/
public class TempOrderActivity extends BaseActivity<TempOrderContract.View,TempOrderPresenter> implements TempOrderContract.View {
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
    @Bind(R.id.rv_goods)
    RecyclerView rvGoods;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_temp_order;
    }

    @Override
    public void initView() {
        tvTitle.setText("临时订单");
        tvLeft.setVisibility(View.GONE);
        ivRight.setVisibility(View.GONE);
        tvRight.setVisibility(View.GONE);
        /**
         * 查询数据库存储的数据
         */
        presenter.queryData(this);

    }

    @Override
    public TempOrderPresenter initPresenter() {
        return new TempOrderPresenter();
    }
}
