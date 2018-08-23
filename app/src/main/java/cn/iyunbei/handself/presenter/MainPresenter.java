package cn.iyunbei.handself.presenter;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.GoodsBean;
import cn.iyunbei.handself.contract.MainContract;
import cn.iyunbei.handself.model.MainModel;
import jt.kundream.base.BasePresenter;
import jt.kundream.utils.CurrencyUtils;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

//    List<GoodsBean> goodsBeanList = new ArrayList<>();

    private RequestCallback.GetGoodsCallback getGoodsCallback = new RequestCallback.GetGoodsCallback() {
        @Override
        public void succ(GoodsBean.DataBean bean) {
            //此处的逻辑处理，应该是将单个商品，放进集合中，将集合返给页面，在页面中展示
            mView.manageData(bean);
//            goodsBeanList.add(bean);
//            mView.showGoodsList(goodsBeanList);
        }

        @Override
        public void fial(String err) {
            mView.showToast(err);
        }
    };

    @Override
    public void addGoods(String s, String token) {

        GoodsBean.DataBean dataBean = new GoodsBean.DataBean();
        dataBean.setBarcode(s);
        dataBean.setGoods_id(Integer.parseInt(s));
        dataBean.setGoods_name("山东大葱");
        dataBean.setGoods_price("12.22");
        dataBean.setSpec("捆");
        mView.manageData(dataBean);
        // TODO: 2018/8/23   以下是正确逻辑
//        if (TextUtils.isEmpty(s)) {
//            mView.showToast("条码不正确");
//        } else {
//            MainModel.requestGoods(s, token, getGoodsCallback);
//        }
    }

    @Override
    public void checkGoodsIsSame(Map<Integer, Integer> numMap, List<GoodsBean.DataBean> list, GoodsBean.DataBean bean) {
        int goodsId = bean.getGoods_id();
        int num;
        if (numMap.get(goodsId) != null) {
            num = numMap.get(goodsId) + 1;
        } else {
            num = 1;
        }

        ArrayList<Integer> idList = new ArrayList<>();

        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                idList.add(list.get(i).getGoods_id());
            }

            if (!idList.contains(goodsId)) {
                mView.addList(bean);
            }

        } else {
            mView.addList(bean);
        }

        mView.setNumMap(goodsId, num);
    }

    @Override
    public void calcTotal(List<GoodsBean.DataBean> goodsList, Map<Integer, Integer> numMap) {
        BigDecimal totalMoney = BigDecimal.valueOf(0);
        int totalNum = 0;
        if (goodsList.size() < 1) {
            mView.showEmptyView();
        }

        for (int i = 0; i < goodsList.size(); i++) {
            int goods_id = goodsList.get(i).getGoods_id();
            totalNum += numMap.get(goods_id);
            BigDecimal singleGoodsMoney = CurrencyUtils.multiply(CurrencyUtils.toBigDecimal(goodsList.get(i).getGoods_price()), CurrencyUtils.toBigDecimal(String.valueOf(numMap.get(goods_id))));
            totalMoney = CurrencyUtils.add(totalMoney, singleGoodsMoney);
        }
        mView.setToalData(totalMoney, totalNum);
    }
}
