package cn.iyunbei.handself.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.adapter.PanDianAdapter;
import cn.iyunbei.handself.adapter.TempOrderAdapter;
import cn.iyunbei.handself.bean.PanDianBean;
import cn.iyunbei.handself.bean.Single;
import cn.iyunbei.handself.contract.PanDianContract;
import cn.iyunbei.handself.presenter.PanDianPresenter;
import jt.kundream.base.BaseActivity;

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
    private RequestCallback.ItemViewOnClickListener itemClickListener = new RequestCallback.ItemViewOnClickListener() {
        @Override
        public void itemViewClick(View view) {
            int position = (int) view.getTag();
            switch (view.getId()) {
                case R.id.ll_bottom:
                    /**
                     *  1.展开商品列表,将箭头切换为向上  就是做一个箭头的旋转动画  这个在云贝生活中已经有了  直接拿过来就行
                     *  2.修改TempOrderAdapter中的tempGoodsList  然后notify
                     */
//                    mAdapter.openOrCloseGoodsList(position);
                    break;

                default:
                    break;

            }

        }
    };


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
    public void showData(List<PanDianBean.DataBean> list) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPandian.setLayoutManager(linearLayoutManager);
        rvPandian.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new PanDianAdapter(this, R.layout.item_goods_count, list, itemClickListener);
        rvPandian.setAdapter(mAdapter);
    }
}
