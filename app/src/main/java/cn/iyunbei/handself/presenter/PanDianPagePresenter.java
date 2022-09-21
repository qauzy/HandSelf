package cn.iyunbei.handself.presenter;

import android.content.Context;
import android.text.TextUtils;

import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.GoodsBean;
import cn.iyunbei.handself.bean.PanDianingBean;
import cn.iyunbei.handself.bean.TempOrderBean;
import cn.iyunbei.handself.contract.PanDianPageContract;
import cn.iyunbei.handself.model.MainModel;
import cn.iyunbei.handself.model.PanDianPageModel;
import jt.kundream.base.BasePresenter;
import jt.kundream.utils.CommonUtil;

public class PanDianPagePresenter extends BasePresenter<PanDianPageContract.View> implements PanDianPageContract.Presenter {


    private RequestCallback.PanDianingCallback netCallback = new RequestCallback.PanDianingCallback() {
        @Override
        public void succ(PanDianingBean bean) {
            mView.hideProgress();
            mView.showContent(bean);
        }


        @Override
        public void Fail(String errMsg) {
            mView.hideProgress();
            mView.showToast(errMsg);
        }
    };
    private RequestCallback.CreateParofitCallback createProfitCallback = new RequestCallback.CreateParofitCallback() {
        @Override
        public void succ(String profit_id, String profit_status) {
            mView.showNewProfit(profit_id, profit_status);
        }

        @Override
        public void Fail(String errMsg) {
            mView.showToast(errMsg);
        }
    };


    @Override
    public void reqPanDianing(Context ctx, int pd_id, int page) {
        if (pd_id == -1) {
            // TODO: 2018/9/11 当没有盘点单号的时候，需要生成一个新的盘点单
//            mView.showEmpty();
            new PanDianPageModel().createProfit(CommonUtil.getString(ctx, "token"), createProfitCallback);
        } else {
            new PanDianPageModel().reqPanDianing(CommonUtil.getString(ctx, "token"), pd_id, page, netCallback);
        }
    }

    /**
     * 单个商品的盘点数据录入
     *
     * @param pd_id
     * @param barcode
     * @param num
     * @param token
     */
    public void saveGoodsNum(int pd_id, String barcode, String num, String token) {
        new PanDianPageModel().saveGoodsNum(pd_id, barcode, num, token, pdGoodsCallback);
    }

    private RequestCallback.PanDianGoodsCallback pdGoodsCallback = new RequestCallback.PanDianGoodsCallback() {
        @Override
        public void succ(String succMsg) {
            /**
             * 此处数据录入成功之后  要通知界面  更改数据  将实际数量改为输入的数量
             */
            mView.editNumSucc();
        }

        @Override
        public void Fail(String errMsg) {

        }
    };

    /**
     * 设置盘点单完成
     *
     * @param context
     * @param pd_id
     */
    public void setPanDianOk(Context context, int pd_id) {
        new PanDianPageModel().setPDOk(CommonUtil.getString(context, "token"), pd_id, pdOkCallback);
    }

    private RequestCallback.PdOkCallback pdOkCallback = new RequestCallback.PdOkCallback() {
        @Override
        public void succ(String succ) {
            mView.showToast(succ);
            mView.closeAct();
        }

        @Override
        public void Fail(String errMsg) {
            mView.showToast(errMsg);
        }
    };

    /**
     * 获取单个商品，如果正确，在页面弹出盘点的dialog
     *
     * @param context
     * @param barCode
     */
    public void reqGoods(Context context, String barCode) {
        if (TextUtils.isEmpty(barCode)) {
            mView.showToast("条码不正确");
        } else {
            PanDianPageModel.requestGoods(barCode, CommonUtil.getString(context, "token"), getGoodsCallback);
        }
    }

    private RequestCallback.GetGoodsCallback getGoodsCallback = new RequestCallback.GetGoodsCallback() {
        @Override
        public void succ(GoodsBean bean) {
            String goods_name = bean.getData().getGoodsName();
            String barcode = bean.getData().getBarcode();
            mView.showPdGoodsDlg(goods_name, barcode);
        }

        @Override
        public void fial(String err) {
            mView.showToast(err);
        }
    };

}
