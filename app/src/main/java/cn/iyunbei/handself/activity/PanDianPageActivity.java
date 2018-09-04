package cn.iyunbei.handself.activity;

import android.app.AlertDialog;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.adapter.PanDianingAdapter;
import cn.iyunbei.handself.bean.PanDianingBean;
import cn.iyunbei.handself.contract.PanDianPageContract;
import cn.iyunbei.handself.presenter.PanDianPagePresenter;
import jt.kundream.base.BaseActivity;
import jt.kundream.utils.ActivityUtil;
import jt.kundream.utils.CommonUtil;

import static android.widget.ListPopupWindow.MATCH_PARENT;

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
    SwipeMenuRecyclerView rvPandianGoods;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @Bind(R.id.ll_pandian_head)
    LinearLayout llPDHead;
    private int pd_id;
    private int page = 1;
    private List<PanDianingBean.DataBeanX.ListBean.DataBean> pdList = new ArrayList<>();
    private PanDianingAdapter mAdapter;
    //修改盘点数据成功之后需要先在界面中修改的集合的位置
    private int tempPosi;
    //需要修改的集合的临时值
    private String tempNum;

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
        presenter.reqPanDianing(this, pd_id, page);

        smartRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                page++;
                presenter.reqPanDianing(getContext(), pd_id, page);
                smartRefreshLayout.finishLoadmore(1500);
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                page = 1;

                presenter.reqPanDianing(getContext(), pd_id, page);
                smartRefreshLayout.finishRefresh(1500);
            }
        });

    }

    @OnClick({R.id.iv_left, R.id.iv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();

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
        View contentView = mLayoutInflater.inflate(R.layout.pop, null);
        final PopupWindow pop = new PopupWindow(contentView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);

        //显示在正左下方
        pop.showAsDropDown(ivRight);
        /**
         * pop的显示和消失动画 暂略
         */

        //设置2个点击事件
        TextView tvPause = (TextView) contentView.findViewById(R.id.tv_pause);
        tvPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("上传盘点状态信息，改为此盘点单暂停盘点");
                pop.dismiss();

            }
        });


        TextView tvEnd = (TextView) contentView.findViewById(R.id.tv_end);

        tvEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showToast("上传盘点状态信息，改为此盘点单盘点完成");

                pop.dismiss();
            }
        });


    }

    @Override
    public PanDianPagePresenter initPresenter() {
        return new PanDianPagePresenter();
    }

    /**
     * 如果是一个新的盘点单 那么显示为空  直到用户添加商品
     */
    @Override
    public void showEmpty() {
        llPDHead.setVisibility(View.GONE);
        smartRefreshLayout.setVisibility(View.GONE);
        showToast("请扫描添加商品");
    }

    @Override
    public void showContent(PanDianingBean bean) {
        tvOrderId.setText("订单编号：" + bean.getData().getProfit().getProfit_id());
        tvStaff.setText("收银员：" + bean.getData().getProfit().getMember_name());

        if (page == 1) {
            pdList.clear();
            pdList.addAll(bean.getData().getList().getData());
        } else {
            pdList.addAll(bean.getData().getList().getData());
        }

        if (mAdapter == null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mAdapter = new PanDianingAdapter(this, R.layout.item_order_list_detail, pdList);
            rvPandianGoods.setItemAnimator(new DefaultItemAnimator());
            rvPandianGoods.setSwipeMenuCreator(mSwipeMenuCreator);
            //设置侧滑出来的菜单的删除监听
            rvPandianGoods.setSwipeMenuItemClickListener(mMenuItemClickListener);
            rvPandianGoods.setLayoutManager(linearLayoutManager);
            rvPandianGoods.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private SwipeMenuItem editItem;
    private SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeMenu, SwipeMenu swipeMenu1, int i) {
            editItem = new SwipeMenuItem(getContext());
            // 可以设置各种文字和图标了
            editItem.setHeight(MATCH_PARENT);
            editItem.setWidth(80);
//            editItem.setText("删除");
//            editItem.setTextColorResource(R.color.white);
            // TODO: 2018/9/4 需要换成编辑的图标
            editItem.setImage(R.mipmap.bianji);
            editItem.setBackground(R.color.gray_bg);
            swipeMenu1.addMenuItem(editItem);
        }
    };
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge swipeMenuBridge) {
            // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
            swipeMenuBridge.closeMenu();
            /**
             * 获取这些侧滑的必要信息
             */
            // 左侧还是右侧菜单。
            int direction = swipeMenuBridge.getDirection();
            // RecyclerView的Item的position。
            int adapterPosition = swipeMenuBridge.getAdapterPosition();
            // 菜单在RecyclerView的Item中的Position。
            int menuPosition = swipeMenuBridge.getPosition();
            // TODO: 2018/9/4 此处需要弹出编辑数量的弹窗
            tempPosi = menuPosition;
            showPdGoodsDlg(pdList.get(tempPosi).getGoods_name(), pdList.get(tempPosi).getBarcode());
//            goodsList.remove(menuPosition);
//            mAdapter.notifyDataSetChanged();
//            presenter.calcTotal(goodsList, numMap);
//            presenter.setSingleGood(goodsList, adapterPosition, AndroidUtil.getUniqueId(mContext));
        }
    };

    /**
     * 显示盘点的商品输入对话框
     *
     * @param goods_name
     * @param barcode
     */
    private void showPdGoodsDlg(String goods_name, final String barcode) {
        ActivityUtil.backgroundAlpha(0.6f, this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(this, R.layout.dialog_pd, null);
        // dialog.setView(view);// 将自定义的布局文件设置给dialog
        // 设置边距为0,保证在2.x的版本上运行没问题
        dialog.setView(view, 0, 0, 0, 0);

        final EditText etNum = (EditText) view.findViewById(R.id.et_goods_code);
        TextView tvName = (TextView) view.findViewById(R.id.tv_goods_name);
        TextView tvBar = (TextView) view.findViewById(R.id.tv_goods_bar);
        Button btnAdd = (Button) view.findViewById(R.id.btn_add_goods);

        tvName.setText(goods_name);
        tvBar.setText(barcode);

        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tempNum = etNum.getText().toString();
                presenter.saveGoodsNum(pd_id, barcode, tempNum, CommonUtil.getString(PanDianPageActivity.this, "token"));
                dialog.dismiss();
                ActivityUtil.backgroundAlpha(1f, PanDianPageActivity.this);
            }
        });
        dialog.show();
    }

    @Override
    public void editNumSucc() {
        pdList.get(tempPosi).setReal_number(tempNum);
        mAdapter.notifyDataSetChanged();
    }
}
