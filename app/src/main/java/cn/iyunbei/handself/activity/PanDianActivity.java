package cn.iyunbei.handself.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import butterknife.Bind;
import butterknife.OnClick;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.adapter.PanDianAdapter;
import cn.iyunbei.handself.bean.PanDianBean;
import cn.iyunbei.handself.contract.PanDianContract;
import cn.iyunbei.handself.presenter.PanDianPresenter;
import jt.kundream.base.BaseActivity;
import jt.kundream.utils.ActivityUtil;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/8/30
 * @e-mail: 245086168@qq.com
 * @desc:盘点页面
 **/
public class PanDianActivity extends BaseActivity<PanDianContract.View, PanDianPresenter> implements PanDianContract.View {
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
    @Bind(R.id.rv_pandian)
    RecyclerView rvPandian;
    private PanDianAdapter mAdapter;
    private List<PanDianBean.DataBean> mDatas = new ArrayList<>();
    private RequestCallback.ItemViewOnClickListener itemClickListener = new RequestCallback.ItemViewOnClickListener() {
        @Override
        public void itemViewClick(View view) {
            int position = (int) view.getTag();
            switch (view.getId()) {
                case R.id.ll_counting:
                    // TODO: 2018/9/3 点击之后  跳入新的界面
                    ActivityUtil.startActivity(PanDianActivity.this, PanDianPageActivity.class, new Intent().putExtra("pd_id", mDatas.get(position).getProfit_id()), false);

                    break;

                default:
                    break;

            }

        }
    };

    @OnClick({R.id.iv_left, R.id.iv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;

            case R.id.iv_right:
                /**
                 * 添加盘点单的时候 进入界面  数据直接为空
                 */
                ActivityUtil.startActivity(PanDianActivity.this, PanDianPageActivity.class, new Intent().putExtra("pd_id",-1 ), false);
                break;

            default:
                break;

        }
    }


    @Override
    public int getLayoutResId() {
        return R.layout.activity_pandian;
    }

    @Override
    public void initView() {
        tvLeft.setVisibility(View.GONE);
        tvTitle.setText("盘点货品");
        tvRight.setVisibility(View.GONE);
        ivRight.setImageResource(R.mipmap.add);

        presenter.getPanDianList(this);

    }

    @Override
    public PanDianPresenter initPresenter() {
        return new PanDianPresenter();
    }


    @Override
    protected void onStart() {
        super.onStart();
        presenter.getPanDianList(this);
    }

    @Override
    public void showData(List<PanDianBean.DataBean> list) {
        mDatas.clear();
        mDatas.addAll(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPandian.setLayoutManager(linearLayoutManager);
        rvPandian.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new PanDianAdapter(this, R.layout.item_goods_count, mDatas, itemClickListener);
        rvPandian.setAdapter(mAdapter);
    }
}
