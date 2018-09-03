package cn.iyunbei.handself.activity;

import android.app.ActionBar;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.contract.PanDianPageContract;
import cn.iyunbei.handself.presenter.PanDianPagePresenter;
import jt.kundream.base.BaseActivity;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/9/3
 * @e-mail: 245086168@qq.com
 * @desc:盘点进行中的界面，也就是盘点列表中，点击顶部+号和盘点中的蓝色点击之后的跳转界面
 **/
public class PanDianPageActivity extends BaseActivity<PanDianPageContract.View, PanDianPagePresenter> implements PanDianPageContract.View {
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
    @Bind(R.id.tv_order_id)
    TextView tvOrderId;
    @Bind(R.id.tv_staff)
    TextView tvStaff;
    @Bind(R.id.rv_pandian_goods)
    RecyclerView rvPandianGoods;
    private int pd_id;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_add_pandian;
    }

    @Override
    public void initView() {
        tvLeft.setVisibility(View.GONE);
        tvTitle.setText("添加盘点");
        tvRight.setVisibility(View.GONE);
        ivRight.setImageResource(R.mipmap.more);
        //获取上一个页面 传递过来的状态  如果是正在盘点中的订单  那么显示盘点的情况  否则 不显示
        pd_id = getIntent().getIntExtra("pd_id", -1);
        //此处逻辑中 不再是携带的订单号过来 直接携带的应该是数据
        presenter.reqPanDianing(this, pd_id);

    }

    @OnClick({R.id.iv_left, R.id.iv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:

                break;

            case R.id.iv_right:
                showPopWindow();

                break;

            default:

                break;
        }

    }

    /**
     * 弹出popwindow
     */
    private void showPopWindow() {
        LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = mLayoutInflater.inflate(R.layout.pop,null);
        PopupWindow pop = new PopupWindow(contentView, ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);

        //显示在正左下方
        pop.showAsDropDown(ivRight);
        /**
         * pop的显示和消失动画
         */



    }

    @Override
    public PanDianPagePresenter initPresenter() {
        return new PanDianPagePresenter();
    }

    /**
     * 如果是一个新的盘点单 那么显示为空  知道用户添加商品
     */
    @Override
    public void showEmpty() {

    }
}
