package cn.iyunbei.handself.presenter;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.GoodsBean;
import cn.iyunbei.handself.contract.MainContract;
import cn.iyunbei.handself.model.MainModel;
import jt.kundream.base.BasePresenter;
import jt.kundream.utils.TimeUtil;

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
        public void succ(GoodsBean bean) {
            //此处的逻辑处理，应该是将单个商品，放进集合中，将集合返给页面，在页面中展示
            mView.manageData(bean);
//            goodsBeanList.add(bean);
//            mView.showGoodsList(goodsBeanList);
        }

        @Override
        public void fial(String err) {

        }
    };

    @Override
    public void addGoods(String s) {
        if (TextUtils.isEmpty(s)) {
            mView.showToast("条码不正确");
        } else {
            // TODO: 2018/8/20 请求商品 之后需要添加token
            MainModel.requestGoods(s, getGoodsCallback);
        }
    }
}
