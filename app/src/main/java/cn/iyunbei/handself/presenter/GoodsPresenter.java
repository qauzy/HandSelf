package cn.iyunbei.handself.presenter;

import android.content.Context;
import android.text.TextUtils;

import java.util.List;

import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.GoodsBean;
import cn.iyunbei.handself.bean.GoodsDataBean;
import cn.iyunbei.handself.bean.GoodsListBean;
import cn.iyunbei.handself.bean.PanDianBean;
import cn.iyunbei.handself.contract.GoodsContract;
import cn.iyunbei.handself.contract.PanDianContract;
import cn.iyunbei.handself.model.GoodsListModel;
import cn.iyunbei.handself.model.GoodsModel;
import cn.iyunbei.handself.model.PanDianModel;
import cn.iyunbei.handself.model.PanDianPageModel;
import jt.kundream.base.BasePresenter;
import jt.kundream.utils.CommonUtil;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/8/30
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public class GoodsPresenter extends BasePresenter<GoodsContract.View> implements GoodsContract.Presenter {

    private RequestCallback.GetGoodsListCallback getGoodsListCallback = new RequestCallback.GetGoodsListCallback() {
        @Override
        public void succ(List<GoodsDataBean> list) {
            mView.hideProgress();
            if (list.size() < 1) {

            } else {
                mView.showData(list);
            }

        }

        @Override
        public void Fail(String errMsg) {
            mView.hideProgress();

        }
    };


    private RequestCallback.UpdateInfoCallback updateInfoCallback = new RequestCallback.UpdateInfoCallback() {
        @Override
        public void succ(GoodsDataBean data) {
            mView.hideProgress();
            mView.showResult(data);
        }
        @Override
        public void Fail(String errMsg) {
            mView.hideProgress();

        }
    };

    private RequestCallback.GetGoodsCallback getGoodsCallback = new RequestCallback.GetGoodsCallback() {
        @Override
        public void succ(GoodsBean bean) {
            GoodsDataBean data = bean.getData();
            mView.showGoods(data);
        }

        @Override
        public void fial(Integer code,String msg) {
            if(code == 10005){
                GoodsDataBean data = new GoodsDataBean();
                data.setGoodsName("");
                data.setSpec("");
                data.setSupplier("");
                data.setPrice("");
                data.setBarcode(msg);
                mView.showGoods(data);
                return;
            }
            mView.showToast(msg);
        }
    };

    @Override
    public void getGoodsList(Integer page,Integer pageSize,Context ctx) {
        mView.showProgress();
        new GoodsListModel().getGoodsList(page,pageSize, getGoodsListCallback);
    }

    @Override
    public void saveGoodsInfo(Integer position, String barcode, String goodsName, String supplier, String price,String psec,Context ctx) {
        mView.showProgress();
        new GoodsModel().saveGoodsInfo(position,barcode, goodsName, supplier, price,psec, updateInfoCallback);
    }

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
            new GoodsModel().requestGoods(barCode, CommonUtil.getString(context, "token"), getGoodsCallback);
        }
    }
}
