package cn.iyunbei.handself.activity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.adapter.TempOrderAdapter;
import cn.iyunbei.handself.bean.TempOrderBean;
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
public class TempOrderActivity extends BaseActivity<TempOrderContract.View, TempOrderPresenter> implements TempOrderContract.View {
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
    @Bind(R.id.rv_temp_orders)
    RecyclerView rvTempOrders;
    private TempOrderAdapter mAdapter;
    private List<TempOrderBean> mDatas = new ArrayList<>();

    private RequestCallback.ItemViewOnClickListener itemClickListener = new RequestCallback.ItemViewOnClickListener() {
        @Override
        public void itemViewClick(View view) {

        }
    };

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

    @Override
    public void showTempOrder(List<TempOrderBean> tempOrderBeanList) {
        mDatas.clear();
        mDatas.addAll(tempOrderBeanList);
        if (mAdapter == null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rvTempOrders.setLayoutManager(linearLayoutManager);
            rvTempOrders.setItemAnimator(new DefaultItemAnimator());
            mAdapter = new TempOrderAdapter(this, R.layout.item_temp_order, mDatas, itemClickListener);
            rvTempOrders.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }
}
