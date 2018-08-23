package cn.iyunbei.handself.presenter;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.GoodsBean;
import cn.iyunbei.handself.contract.MainContract;
import cn.iyunbei.handself.model.MainModel;
import jt.kundream.base.BasePresenter;

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
//        if (num < 1) {
//            numMap.put(goodsId, 1);
//        } else {
//            numMap.put(goodsId, ++num);
//        }

        ArrayList<Integer> idList = new ArrayList<>();

        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                idList.add(list.get(i).getGoods_id());
            }

            if (!idList.contains(goodsId)) {
                mView.addList(bean);
            }

//            for (int i = 0; i < list.size(); i++) {
//                if (list.get(i).getGoods_id() != goodsId) {
//                    mView.addList(bean);
//                    break;
//                } else {
//                    continue;
//                }
//            }
        } else {
            mView.addList(bean);
        }


//        if (!list.contains(bean)) {
//            mView.addList(bean);
//        }
        mView.setNumMap(goodsId, num);
    }
}
