package cn.iyunbei.handself.activity;

import static android.widget.ListPopupWindow.MATCH_PARENT;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.adapter.GoodsListAdapter;
import cn.iyunbei.handself.bean.GoodsBean;
import cn.iyunbei.handself.bean.GoodsDataBean;
import cn.iyunbei.handself.bean.GoodsListBean;
import cn.iyunbei.handself.contract.GoodsContract;
import cn.iyunbei.handself.presenter.GoodsPresenter;
import cn.iyunbei.handself.utils.EditTextWithText;
import jt.kundream.base.BaseActivity;
import jt.kundream.utils.ActivityUtil;
import jt.kundream.utils.ToastUtils;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/8/30
 * @e-mail: 245086168@qq.com
 * @desc:盘点页面
 **/
public class GoodsPageActivity extends BaseActivity<GoodsContract.View, GoodsPresenter> implements GoodsContract.View {
    final String TAG = "GoodsPageActivity";
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
    @Bind(R.id.rv_goods_list)
    SwipeMenuRecyclerView rvGoodsList;
    private  Integer page = 1;
    private SwipeMenuItem deleteItem;

    private GoodsListAdapter mAdapter;
    private List<GoodsDataBean> mDatas = new ArrayList<>();
    private RequestCallback.ItemViewOnClickListener itemClickListener = new RequestCallback.ItemViewOnClickListener() {
        @Override
        public void itemViewClick(View view) {
            //

            switch (view.getId()) {

                //点击修改商品信息
                case R.id.rl_goods_item:
                    int position =  (int)view.getTag();
                    GoodsDataBean data = mDatas.get(position);
                    data.setPosition(position);
                    showInputDialog(data);
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
//                ActivityUtil.startActivity(GoodsPageActivity.this, PanDianPageActivity.class, new Intent().putExtra("pd_id",-1 ), false);
                break;

            default:
                break;

        }
    }


    @Override
    public int getLayoutResId() {
        return R.layout.activity_goods;
    }

    @Override
    public void initView() {
        tvLeft.setVisibility(View.GONE);
        tvTitle.setText("货品列表");
        tvRight.setVisibility(View.GONE);
        ivRight.setImageResource(R.mipmap.add);

        presenter.getGoodsList(page,5,this);
        //设置侧滑时候弹出侧滑删除菜单
        rvGoodsList.setSwipeMenuCreator(mSwipeMenuCreator);
        SwipeMenuRecyclerView.LoadMoreListener loadMore = new SwipeMenuRecyclerView.LoadMoreListener(){

            @Override
            public void onLoadMore() {
                //设置还有更多数据
                page++;
                presenter.getGoodsList(page,5,GoodsPageActivity.this);
                rvGoodsList.loadMoreFinish(false,true);

            }
        };
        rvGoodsList.setLoadMoreListener(loadMore);
        //rvGoodsList.setAutoLoadMore(true);
        rvGoodsList.loadMoreFinish(false,true);
    }
    private SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeMenu, SwipeMenu swipeMenu1, int i) {
            deleteItem = new SwipeMenuItem(getContext());
            // 可以设置各种文字和图标了
            deleteItem.setHeight(MATCH_PARENT);
            deleteItem.setWidth(80);
            deleteItem.setText("删除");
            deleteItem.setTextColorResource(R.color.white);
            deleteItem.setBackground(R.color.yunbei_bg);
            swipeMenu1.addMenuItem(deleteItem);
        }
    };
    @Override
    public GoodsPresenter initPresenter() {
        return new GoodsPresenter();
    }


    @Override
    protected void onStart() {
        super.onStart();
        presenter.getGoodsList(page,5,this);
    }

    @Override
    public void showData(List<GoodsDataBean> list) {
//        mDatas.clear();
        mDatas.addAll(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvGoodsList.setLayoutManager(linearLayoutManager);
        rvGoodsList.setItemAnimator(new DefaultItemAnimator());
        //数据和控件之前的映射
        mAdapter = new GoodsListAdapter(this, R.layout.item_goods, mDatas, itemClickListener);

        rvGoodsList.setAdapter(mAdapter);

    }
    @Override
    public void showResult(GoodsDataBean data) {
        mDatas.set(data.getPosition(),data);
        mAdapter.notifyItemChanged(data.getPosition());
        ToastUtils.showShort(getApplicationContext(), "信息更新成功");
    }

    /**
     * 更新商品信息dialog
     */
    private void showInputDialog(GoodsDataBean data) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(this, R.layout.dialog_input, null);
        // dialog.setView(view);// 将自定义的布局文件设置给dialog
        // 设置边距为0,保证在2.x的版本上运行没问题
        dialog.setView(view, 0, 0, 0, 0);

        TextView etCode = (TextView) view.findViewById(R.id.tv_goods_code);
        etCode.setText(data.getBarcode());
        EditText etGuide = (EditText) view.findViewById(R.id.et_goods_guige);
        etGuide.setText(data.getSpec());
        EditText etName = (EditText) view.findViewById(R.id.et_goods_name);
        etName.setText(data.getGoodsName());
        EditTextWithText etMoney = (EditTextWithText) view.findViewById(R.id.et_money);
        if(data.getPrice().isEmpty()){
            etMoney.setLeadText("￥");
            etMoney.setText("");
        }else{
            etMoney.setLeadText("￥");
            etMoney.setText(data.getPrice());
        }

        EditText etSupplier = (EditText) view.findViewById(R.id.et_supplier);
        etSupplier.setText(data.getSupplier());


        Log.d(TAG,"商品position="+data.getPosition());
        Button btnModify = (Button) view.findViewById(R.id.btn_add_goods);

        btnModify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String barcode = etCode.getText().toString();
                String goodsName = etName.getText().toString();
                String supplier = etSupplier.getText().toString();
                String price = etMoney.getText().toString();
                String psec = etGuide.getText().toString();

                presenter.saveGoodsInfo(data.getPosition(),barcode,goodsName,supplier,price,psec,GoodsPageActivity.this);

                dialog.dismiss();
            }
        });
        ActivityUtil.backgroundAlpha(1f, this);
        dialog.show();
    }


}
