package cn.iyunbei.handself.presenter;

import android.content.Context;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.GoodsBean;
import cn.iyunbei.handself.bean.GoodsDataBean;
import cn.iyunbei.handself.bean.Single;
import cn.iyunbei.handself.bean.TempOrderBean;
import cn.iyunbei.handself.contract.MainContract;
import cn.iyunbei.handself.model.GoodsModel;
import cn.iyunbei.handself.model.MainModel;
import jt.kundream.base.BasePresenter;
import jt.kundream.utils.CurrencyUtils;
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
            TempOrderBean.TempGoodsBean goodsBean = new TempOrderBean.TempGoodsBean();
            GoodsDataBean data = bean.getData();

            goodsBean.setGoods_id(data.getId());
            goodsBean.setSpec(data.getSpec());
            goodsBean.setGoods_price(data.getPrice());
            goodsBean.setGoods_name(data.getGoodsName());
            goodsBean.setBarcode(data.getBarcode());
            goodsBean.setSupplier(data.getSupplier());
            goodsBean.setGoods_number(1);
            mView.hideProgress();
            mView.manageData(goodsBean);
        }

        @Override
        public void fial(Integer code,String msg) {
            mView.hideProgress();
            if(code == 10005){
                GoodsDataBean data = new GoodsDataBean();
                data.setBarcode(msg);
                mView.showResult(data);
                return;
            }
            mView.showToast(msg);
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

    @Override
    public void saveGoodsInfo(Integer position, String barcode, String goodsName, String supplier, String price,String psec,Context ctx) {
        mView.showProgress();
        new GoodsModel().saveGoodsInfo(position,barcode, goodsName, supplier, price,psec, updateInfoCallback);
    }


    @Override
    public void addGoods(String s) {

//        TempOrderBean.TempGoodsBean bean = new TempOrderBean.TempGoodsBean();
//        bean.setBarcode(s);
//        bean.setGoods_id(Integer.parseInt(s));
//        bean.setGoods_name("河南胡辣汤");
//        bean.setGoods_price("12.44");
//        bean.setSpec("/张");
//        bean.setGoods_number(1);
//        mView.manageData(bean);
        // TODO: 2018/8/23   以下是正确逻辑
        if (TextUtils.isEmpty(s)) {
            mView.showToast("条码不正确");
        } else {
            mView.showProgress();
            MainModel.requestGoods(s,getGoodsCallback);
        }
    }

    @Override
    public void checkGoodsIsSame(Map<Integer, Integer> numMap, List<TempOrderBean.TempGoodsBean> list, TempOrderBean.TempGoodsBean bean) {
        Integer goodsId = bean.getGoods_id();
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
    public void calcTotal(List<TempOrderBean.TempGoodsBean> goodsList, Map<Integer, Integer> numMap) {
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


    /**
     * 根据最新的需求  这个数据如果app退出就清空了 所以更改为使用list存储
     * <p>
     * 此方法内逻辑梳理：
     * 全局的变量集合，长度是可变的，如果按照下面这种写法，只能有一个临时订单，所以更改！
     *
     * @param goodsList
     * @param numMap
     * @param ctx
     * @param tolMon
     * @param tolNum
     */
    @Override
    public void saveOrderDatas(List<TempOrderBean.TempGoodsBean> goodsList, Map<Integer, Integer> numMap, Context ctx, double tolMon, int tolNum) {
        //用当前时间生成时间戳  作为订单号
        int nowTimestamp = TimeUtil.getNowTimestamp();

        List<TempOrderBean.TempGoodsBean> newGoodsList = new ArrayList<>();

        for (int i = 0; i < goodsList.size(); i++) {
            TempOrderBean.TempGoodsBean bean = goodsList.get(i);
            bean.setGoods_number(numMap.get(bean.getGoods_id()));
            newGoodsList.add(bean);
        }

        //将所有的商品拿出来 作为一个集合 添加到临时商品集合中
        List<TempOrderBean.TempGoodsBean> tempGoodsList = new ArrayList<>();
        tempGoodsList.addAll(newGoodsList);

        //组合成真是需要存储的集合元素
        TempOrderBean bean = new TempOrderBean(nowTimestamp, tolNum, tolMon, tempGoodsList);
//        tempList.add(bean);
        //获取app中已经存储的临时订单
        List<TempOrderBean> applicationList = Single.getInstance().getTempList();

        if (applicationList != null) {
            applicationList.add(bean);
        } else {
            List<TempOrderBean> tempList = new ArrayList<>();
            tempList.add(bean);
            Single.getInstance().setTempList(tempList);
        }


//        applicationList.add(bean);
//        if (applicationList.size() > 0) {
//            //如果已经有临时订单了，那么就对临时订单的集合数量中加入1个元素
//            applicationList.add(bean);
//        } else {
//            //如果还没有临时订单，那么就对临时订单集合进行设置
//            List<TempOrderBean> tempList = new ArrayList<>();
//            tempList.add(bean);
//            Single.getInstance().setTempList(tempList);
//        }

        mView.setThisOrderTemp(Single.getInstance().getTempList().size());
    }


//    @Override
//    public void saveOrderDatas(final List<GoodsDataBean> goodsList, final Map<Integer, Integer> numMap, Context ctx, double tolMon, int tolNum) {
//
//        DaoSession daoSession = MyApp.getDaoSession();
//
////        GoodsBeanDaoDao goodsBeanDaoDao = daoSession.getGoodsBeanDaoDao();
////
////        //订单表 存入 订单号(时间戳)，商品id，单个商品的数量，此订单的
////        OrderBeanDaoDao orderBeanDaoDao = daoSession.getOrderBeanDaoDao();
//
//        //订单id表  存入用时间戳生成的订单号  以及这个时间戳可以反推时间
////        OrderIdDaoDao orderIdDaoDao = daoSession.getOrderIdDaoDao();
//
//        final int nowTimestamp = TimeUtil.getNowTimestamp();
//
//        final AsyncSession asyncSession = daoSession.startAsyncSession();
//
//        asyncSession.setListenerMainThread(new AsyncOperationListener() {
//            @Override
//            public void onAsyncOperationCompleted(AsyncOperation asyncOperation) {
//                if (asyncSession.isCompleted()) {
//                    /**
//                     * 此处的逻辑 1.查询orderId中的条数，是几条  就说明是几个临时订单在挂着
//                     *           2.将值返回给页面，在页面中更新UI对应的操作
//                     */
//                    insertOrderBeanDao(nowTimestamp, goodsList, numMap);
//                }
//            }
//        });
//
//        asyncSession.insert(new OrderIdDao(null, nowTimestamp, tolNum, tolMon));
//    }
//
//    private void insertOrderBeanDao(int nowTimestamp, final List<GoodsDataBean> goodsList, Map<Integer, Integer> numMap) {
//        DaoSession daoSession = MyApp.getDaoSession();
//        final AsyncSession asyncSession = daoSession.startAsyncSession();
//
//        asyncSession.setListenerMainThread(new AsyncOperationListener() {
//            @Override
//            public void onAsyncOperationCompleted(AsyncOperation asyncOperation) {
//                if (asyncOperation.isCompleted()) {
//                    insertGoodsProperties(goodsList);
//                }
//            }
//        });
//
//
//        for (int i = 0; i < goodsList.size(); i++) {
//            //订单表中  存入订单号码(时间戳),
//            GoodsDataBean dataBean = goodsList.get(i);
//
//
//            asyncSession.insert(new OrderBeanDao(null, nowTimestamp, dataBean.getGoods_id(),
//                    numMap.get(dataBean.getGoods_id())));
//
//        }
//
//    }
//
//    /**
//     * 存入商品属性  先判断是否有存储过这个商品  如果有的话  就不再存入了
//     *
//     * @param goodsList
//     */
//    private void insertGoodsProperties(List<GoodsDataBean> goodsList) {
//        DaoSession daoSession = MyApp.getDaoSession();
//        final AsyncSession asyncSession = daoSession.startAsyncSession();
//
//        GoodsBeanDaoDao goodsBeanDaoDao = daoSession.getGoodsBeanDaoDao();
//
//        List<GoodsBeanDao> list = goodsBeanDaoDao.queryBuilder().build().list();
//
//        asyncSession.setListenerMainThread(new AsyncOperationListener() {
//            @Override
//            public void onAsyncOperationCompleted(AsyncOperation asyncOperation) {
//                //全部数据存入之后  查询现在数据库中存入了多少临时订单
//                if (asyncOperation.isCompleted())
//                    queryTempOrderNum();
//            }
//        });
//
//
//        /**
//         * 重新整理逻辑  如果数据库中的list中已经包含了某个goods，那么此goods就不再写入了
//         * 1.循环list  获取到所有的订单id集合 即：将所有的数据库中的商品id拿出来 单独放入一个集合中newGoodsIdList
//         * 2.遍历新的需要存入的商品集合goodsList，将每次获取到的goodsid拿出来与newGoodsIdList对比，如果包含，就跳过，否则 在数据库中插入新数据
//         */
//
//        List<Long> newGoodsIdList = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//            newGoodsIdList.add(list.get(i).getGoodsId());
//        }
//
//        for (int i = 0; i < goodsList.size(); i++) {
//            if (newGoodsIdList.contains(goodsList.get(i).getGoods_id())) {
//                continue;
//            } else {
//                GoodsDataBean dataBean = goodsList.get(i);
//                asyncSession.insert(new GoodsBeanDao(null, dataBean.getGoods_id(), dataBean.getSpec(),
//                        dataBean.getGoods_name(), Double.parseDouble(dataBean.getGoods_price()), Integer.parseInt(dataBean.getBarcode())));
//            }
//        }
//
//    }
//
//    /**
//     * 查询数据库中的临时订单总数
//     */
//    private void queryTempOrderNum() {
//        final DaoSession daoSession = MyApp.getDaoSession();
////        long count = 0;
////        OrderIdDaoDao orderIdDaoDao = daoSession.getOrderIdDaoDao();
////        orderIdDaoDao.queryBuilder().count();
//
//        AsyncSession asyncSession = daoSession.startAsyncSession();
//        asyncSession.setListenerMainThread(new AsyncOperationListener() {
//            @Override
//            public void onAsyncOperationCompleted(AsyncOperation asyncOperation) {
//                if (asyncOperation.isCompleted()) {
//                    List<OrderIdDao> orderNumList = new ArrayList<>();
//
//                    orderNumList = (List<OrderIdDao>) asyncOperation.getResult();
//                    mView.setThisOrderTemp(orderNumList.size());
//                }
//            }
//        });
//        asyncSession.queryList(daoSession.getOrderIdDaoDao().queryBuilder().build());
//    }
//
//    /**
//     * 查询数据库全部数据
//     */
//    public void quaryAllData() {
//        DaoSession daoSession = MyApp.getDaoSession();
//        AsyncSession asyncSession = daoSession.startAsyncSession();
//        asyncSession.setListener(new AsyncOperationListener() {
//            @Override
//            public void onAsyncOperationCompleted(AsyncOperation asyncOperation) {
//                String s = asyncOperation.getResult().toString();
//                Log.e("database====", s);
//            }
//        });
//
//        asyncSession.loadAll(GoodsBeanDao.class);
//        asyncSession.loadAll(OrderIdDao.class);
//        asyncSession.loadAll(OrderBeanDao.class);
//    }


}
