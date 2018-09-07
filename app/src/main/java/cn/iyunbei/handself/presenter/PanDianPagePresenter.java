package cn.iyunbei.handself.presenter;

import android.content.Context;

import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.PanDianingBean;
import cn.iyunbei.handself.contract.PanDianPageContract;
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


    @Override
    public void reqPanDianing(Context ctx, int pd_id, int page) {
        if (pd_id == -1) {
            mView.showEmpty();
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

        }

        @Override
        public void Fail(String errMsg) {

        }
    };
}
