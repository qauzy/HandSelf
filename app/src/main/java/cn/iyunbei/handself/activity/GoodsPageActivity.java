package cn.iyunbei.handself.activity;

import static android.widget.ListPopupWindow.MATCH_PARENT;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.ai.edge.core.base.CallException;
import com.baidu.ai.edge.core.base.Consts;
import com.baidu.ai.edge.core.infer.InferManager;
import com.baidu.ai.edge.core.util.FileUtil;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.king.mlkit.vision.camera.CameraScan;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.iyunbei.handself.MyApp;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.adapter.GoodsListAdapter;
import cn.iyunbei.handself.bean.GoodsBean;
import cn.iyunbei.handself.bean.GoodsDataBean;
import cn.iyunbei.handself.bean.GoodsListBean;
import cn.iyunbei.handself.contract.GoodsContract;
import cn.iyunbei.handself.presenter.GoodsPresenter;
import cn.iyunbei.handself.presenter.SpeechUtils;
import cn.iyunbei.handself.service.ScanService;
import cn.iyunbei.handself.utils.EditTextWithText;
import jt.kundream.base.BaseActivity;
import jt.kundream.utils.ActivityUtil;
import jt.kundream.utils.ToastUtils;

/**
 * ????????????????????????????????????
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright??? ??2017-2018
 * @author: YangTiankun created on 2018/8/30
 * @e-mail: 245086168@qq.com
 * @desc:????????????
 **/
public class GoodsPageActivity extends BaseActivity<GoodsContract.View, GoodsPresenter> implements GoodsContract.View {

    private static final int REQUEST_GOODS_OCR = 303;  //??????OCR??????????????????
    private String modelName = "";
    private String version = "";
    private String soc;
    private ArrayList<String> socList = new ArrayList<>();
    private int modelType;

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


    private ScanService mScanService;
    private RequestCallback.ItemViewOnClickListener itemClickListener = new RequestCallback.ItemViewOnClickListener() {
        @Override
        public void itemViewClick(View view) {
            //

            switch (view.getId()) {

                //????????????????????????
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

    /**
     * OCR??????????????????????????????
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GOODS_OCR) {
            GoodsDataBean goods = new GoodsDataBean();
            goods.setGoodsName(data.getExtras().getString("goodsName"));
            goods.setBarcode(data.getExtras().getString("barcode"));
            goods.setSupplier(data.getExtras().getString("supplier"));
            goods.setSpec(data.getExtras().getString("spec"));
            goods.setPrice("");
            showInputDialog(goods);
        }
    }

    private void startOCRActivity(String barcode) {
        Intent intent = new Intent(GoodsPageActivity.this, CameraActivity.class);
        intent.putExtra("name", modelName);
        intent.putExtra("model_type", modelType);
        intent.putExtra("barcode", barcode);

        intent.putExtra("soc", soc);
        ActivityUtil.startActivityForResult(this,intent, REQUEST_GOODS_OCR);
    }
    @OnClick({R.id.iv_left, R.id.iv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                //????????????????????????????????????
                if(mScanService.isRunnning()){
                    mScanService.stop();
                }else{
                    finish();
                }

                break;

            case R.id.iv_right:
                /**
                 * ???????????????????????? ????????????  ??????????????????
                 */
                //???????????????PopupMenu??????,??????????????????Context???????????????????????????
                PopupMenu menu = new PopupMenu(GoodsPageActivity.this,view);
                //??????????????????
                menu.getMenuInflater().inflate(R.menu.context, menu.getMenu());
                //???????????????????????????
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()){
                            case R.id.goods_add_scan:
                                mScanService.start();
                                break;
                            case R.id.goods_add_manual:
                                Toast.makeText(GoodsPageActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });
                //???????????????show()????????????????????????
                menu.show();
//                startUICameraActivity();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        //?????????????????????????????????????????????
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public void initView() {
        tvLeft.setVisibility(View.GONE);
        tvTitle.setText("????????????");
        tvRight.setVisibility(View.GONE);
        ivRight.setImageResource(R.mipmap.add);
//        presenter.getGoodsList(page,5,this);
        //??????????????????????????????????????????
        rvGoodsList.setSwipeMenuCreator(mSwipeMenuCreator);
        SwipeMenuRecyclerView.LoadMoreListener loadMore = new SwipeMenuRecyclerView.LoadMoreListener(){

            @Override
            public void onLoadMore() {
                //????????????????????????
                page++;
                presenter.getGoodsList(page,10,GoodsPageActivity.this);
                rvGoodsList.loadMoreFinish(false,true);

            }
        };
        rvGoodsList.setLoadMoreListener(loadMore);
//        rvGoodsList.setAutoLoadMore(true);
        rvGoodsList.loadMoreFinish(false,true);


        //????????????
        mScanService = new ScanService(this) {
            @Override
            public void onScanResult(String code) {
                Log.d(TAG,code);
                presenter.reqGoods(GoodsPageActivity.this,code);
            }
        };
    }
    private SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeMenu, SwipeMenu swipeMenu1, int i) {
            deleteItem = new SwipeMenuItem(getContext());
            // ????????????????????????????????????
            deleteItem.setHeight(MATCH_PARENT);
            deleteItem.setWidth(80);
            deleteItem.setText("??????");
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
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initPermission();
        presenter.getGoodsList(page,10,this);
        initConfig();
        boolean checkChip = checkChip();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void showData(List<GoodsDataBean> list) {
//        mDatas.clear();
        mDatas.addAll(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvGoodsList.setLayoutManager(linearLayoutManager);
        rvGoodsList.setItemAnimator(new DefaultItemAnimator());
        //??????????????????????????????
        mAdapter = new GoodsListAdapter(this, R.layout.item_goods, mDatas, itemClickListener);

        rvGoodsList.setAdapter(mAdapter);

    }
    @Override
    public void showResult(GoodsDataBean data) {
        //
        if(data.getGoodsName().isEmpty() || data.getPrice().isEmpty() || data.getSpec().isEmpty() || data.getSupplier().isEmpty()){

        }else{
            mDatas.set(data.getPosition(),data);
            mAdapter.notifyItemChanged(data.getPosition());
            ToastUtils.showShort(getApplicationContext(), "??????????????????");

        }
    }

    @Override
    public void showGoods(GoodsDataBean data) {
        //????????????
        mScanService.stop();
        //
        if(data.getGoodsName().isEmpty() ||  data.getSpec().isEmpty() || data.getSupplier().isEmpty()){
            //??????????????????OCR??????
            MyApp.getInstance().say("????????????????????????????????????????????????????????????????????????????????????");
            AlertDialog alertDialog = new AlertDialog.Builder(GoodsPageActivity.this)
                    .setTitle("??????")//??????
                    .setMessage("?????????"+data.getBarcode()+"??????????????????????????????OCR?????????????????????")//??????
                    .setCancelable(false) //??????????????????????????????
                    .setPositiveButton("???",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            startOCRActivity(data.getBarcode());
                        }
                    })
                    .setNegativeButton("???", new DialogInterface.OnClickListener() {//????????????
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            showInputDialog(data);
                        }
                    })
                    .create();

            final Window window = alertDialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.GRAY));

            alertDialog.show();




        }else if( data.getPrice().isEmpty()){
            MyApp.getInstance().say("????????????????????????????????????????????????");
            showInputDialog(data);
        }else{
            MyApp.getInstance().say("????????????????????????????????????????????????????????????");
            showInputDialog(data);
        }

    }

    /**
     * ??????????????????dialog
     */
    private void showInputDialog(GoodsDataBean data) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(this, R.layout.dialog_input, null);
        // dialog.setView(view);// ????????????????????????????????????dialog
        // ???????????????0,?????????2.x???????????????????????????
        dialog.setView(view, 0, 0, 0, 0);

        TextView etCode = (TextView) view.findViewById(R.id.tv_goods_code);
        etCode.setText(data.getBarcode());
        EditText etGuide = (EditText) view.findViewById(R.id.et_goods_guige);
        etGuide.setText(data.getSpec());
        EditText etName = (EditText) view.findViewById(R.id.et_goods_name);
        etName.setText(data.getGoodsName());
        EditTextWithText etMoney = (EditTextWithText) view.findViewById(R.id.et_money);
        if(data.getPrice().isEmpty()){
            etMoney.setLeadText("???");
            etMoney.setText("");
        }else{
            etMoney.setLeadText("???");
            etMoney.setText(data.getPrice());
        }

        EditText etSupplier = (EditText) view.findViewById(R.id.et_supplier);
        etSupplier.setText(data.getSupplier());


        Log.d(TAG,"??????position="+data.getPosition());
        Button btnModify = (Button) view.findViewById(R.id.btn_add_goods);

        btnModify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String barcode = etCode.getText().toString();
                String goodsName = etName.getText().toString();
                String supplier = etSupplier.getText().toString();
                String price = etMoney.getText().toString();
                String psec = etGuide.getText().toString();
                Log.d(TAG,"??????psec="+psec);
                if(barcode.isEmpty() || goodsName.isEmpty() || supplier.isEmpty() || price.isEmpty() || psec.isEmpty()){
                    ToastUtils.showShort(getApplicationContext(), "??????????????????");
                    return;
                }
                presenter.saveGoodsInfo(data.getPosition(),barcode,goodsName,supplier,price,psec,GoodsPageActivity.this);

                dialog.dismiss();
            }
        });
        ActivityUtil.backgroundAlpha(1f, this);
        dialog.show();
    }
    /**
     * demo???????????????????????????????????????????????????arm?????????
     */
    private void initConfig() {
        if (initConfigFromDemoConfig()) {
            Log.i(TAG, "Initialized by demo/config.json");
            return;
        }
        if (initConfigFromDemoConf()) {
            Log.i(TAG, "Initialized by demo/conf.json");
            return;
        }

        /* ???infer/????????? */
        String confJson = FileUtil.readAssetsFileUTF8StringIfExists(getAssets(),
                Consts.ASSETS_DIR_ARM + "/conf.json");
        if (!TextUtils.isEmpty(confJson)) {
            try {
                JSONObject confObj = new JSONObject(confJson);
                modelName = confObj.optString("modelName", "");

                String str = confObj.optString("soc", Consts.SOC_ARM);
                String[] socs = str.split(",");
                socList.addAll(Arrays.asList(socs));

                modelType = confObj.getInt("modelType");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            confJson = FileUtil.readAssetsFileUTF8StringIfExists(getAssets(),
                    Consts.ASSETS_DIR_ARM + "/infer_cfg.json");
            try {
                JSONObject confObj = new JSONObject(confJson);
                socList.add(Consts.SOC_ARM);
                modelType = confObj.getJSONObject("model_info").getInt("model_kind");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG, "Initialized by arm#*.json");
    }

    /**
     * ?????????
     */
    private boolean initConfigFromDemoConfig() {
        String confJson = FileUtil.readAssetsFileUTF8StringIfExists(getAssets(), "demo/config.json");
        if (TextUtils.isEmpty(confJson)) {
            return false;
        }
        try {
            JSONObject confObj = new JSONObject(confJson);
            modelName = confObj.optString("modelName", "");

            String str = confObj.optString("soc", Consts.SOC_ARM);
            String[] socs = str.split(",");
            socList.addAll(Arrays.asList(socs));

            modelType = confObj.getInt("modelType");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * ????????????
     */
    private boolean initConfigFromDemoConf() {
        String confJson = FileUtil.readAssetsFileUTF8StringIfExists(getAssets(), "demo/conf.json");
        if (TextUtils.isEmpty(confJson)) {
            return false;
        }
        try {
            JSONObject confObj = new JSONObject(confJson);
            modelName = confObj.optString("modelName", "");
            socList.add(Consts.SOC_ARM);

            String inferCfgJson = FileUtil.readAssetsFileUTF8StringIfExists(getAssets(),
                    Consts.ASSETS_DIR_ARM + "/infer_cfg.json");
            if (TextUtils.isEmpty(inferCfgJson)) {
                return false;
            }
            JSONObject inferCfgObj = new JSONObject(inferCfgJson);
            modelType = inferCfgObj.getJSONObject("model_info").getInt("model_kind");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean checkChip() {
        if (socList.contains(Consts.SOC_DSP) && Build.HARDWARE.equalsIgnoreCase("qcom")) {
            soc = Consts.SOC_DSP;
            return true;
        }
        if (socList.contains(Consts.SOC_ADRENO_GPU) && Build.HARDWARE.equalsIgnoreCase("qcom")) {
            soc = Consts.SOC_ADRENO_GPU;
            return true;
        }
        if (socList.contains(Consts.SOC_NPU) && Build.HARDWARE.contains("kirin980")) {
            soc = "npu200";
            return true;
        }
        if (socList.contains(Consts.SOC_NPU_VINCI) && (Build.HARDWARE.contains("kirin810")
                || Build.HARDWARE.contains("kirin820") || Build.HARDWARE.contains("kirin990"))) {
            soc = Consts.SOC_NPU_VINCI;
            return true;
        }
        if (socList.contains(Consts.SOC_ARM_GPU)) {
            try {
                if (InferManager.isSupportOpencl()) {
                    soc = Consts.SOC_ARM_GPU;
                    return true;
                }
            } catch (CallException e) {
                Toast.makeText(getApplicationContext(), e.getErrorCode() + ", " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
        if (socList.contains(Consts.SOC_ARM)) {
            soc = Consts.SOC_ARM;
            return true;
        }
        return false;
    }

    /**
     * android 6.0 ??????????????????????????????
     */
    private void initPermission() {
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // ?????????????????????????????????.

            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }
    }

}
