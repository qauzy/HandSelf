package cn.iyunbei.handself.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;
import org.greenrobot.greendao.query.Query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.iyunbei.handself.MyApp;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.GoodsBean;
import cn.iyunbei.handself.bean.GoodsBeanDao;
import cn.iyunbei.handself.bean.OrderBeanDao;
import cn.iyunbei.handself.bean.OrderIdDao;
import cn.iyunbei.handself.contract.MainContract;
import cn.iyunbei.handself.greendao.DaoSession;
import cn.iyunbei.handself.greendao.GoodsBeanDaoDao;
import cn.iyunbei.handself.greendao.GreenDaoHelper;
import cn.iyunbei.handself.greendao.OrderBeanDaoDao;
import cn.iyunbei.handself.greendao.OrderIdDaoDao;
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


    /**
     * 整体方法  从这里开始操作数据库
     *
     * @param goodsList
     * @param numMap
     * @param ctx
     * @param tolMon
     * @param tolNum
     */
    @Override
    public void saveOrderDatas(final List<GoodsBean.DataBean> goodsList, final Map<Integer, Integer> numMap, Context ctx, double tolMon, int tolNum) {

        DaoSession daoSession = MyApp.getDaoSession();

//        GoodsBeanDaoDao goodsBeanDaoDao = daoSession.getGoodsBeanDaoDao();
//
//        //订单表 存入 订单号(时间戳)，商品id，单个商品的数量，此订单的
//        OrderBeanDaoDao orderBeanDaoDao = daoSession.getOrderBeanDaoDao();

        //订单id表  存入用时间戳生成的订单号  以及这个时间戳可以反推时间
//        OrderIdDaoDao orderIdDaoDao = daoSession.getOrderIdDaoDao();

        final int nowTimestamp = TimeUtil.getNowTimestamp();

        final AsyncSession asyncSession = daoSession.startAsyncSession();

        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation asyncOperation) {
                if (asyncSession.isCompleted()) {
                    /**
                     * 此处的逻辑 1.查询orderId中的条数，是几条  就说明是几个临时订单在挂着
                     *           2.将值返回给页面，在页面中更新UI对应的操作
                     */
                    insertOrderBeanDao(nowTimestamp, goodsList, numMap);

//                    queryTempOrderNum();
//                    mView.hideProgress();
//                    mView.showToast("数据插入成功");
                }
            }
        });

        asyncSession.insert(new OrderIdDao(null, nowTimestamp, tolNum, tolMon));
//        for (int i = 0; i < goodsList.size(); i++) {
//            //订单表中  存入订单号码(时间戳),
//            GoodsBean.DataBean dataBean = goodsList.get(i);
//
//            asyncSession.insert(new OrderBeanDao(null, nowTimestamp, dataBean.getGoods_id(),
//                    numMap.get(dataBean.getGoods_id())));
//
//            //存入商品属性
//            asyncSession.insert(new GoodsBeanDao(null, dataBean.getGoods_id(), dataBean.getSpec(),
//                    dataBean.getGoods_name(), Double.parseDouble(dataBean.getGoods_price()), Integer.parseInt(dataBean.getBarcode())));
//        }

//        asyncSession.insert(goodsList);
//        asyncSession.insert(new GoodsBeanDao(null, dataBean.getGoods_id(), dataBean.getSpec(),
//                        dataBean.getGoods_name(), Double.parseDouble(dataBean.getGoods_price()), Integer.parseInt(dataBean.getBarcode())));


        // TODO: 2018/8/24 以下为正常的操作 but现在我想在异步操作  获取数据库的操作状态
//        /**
//         * 此处需要生成一个唯一标示，用与区分单个订单，另外 还需要将此订单id单独存起来，因为此时的订单id是本地生成的，不与服务器相关，
//         * 因此还需要将此临时的orderId存储起来以保证单个订单和商品的相对应关系
//         */
//
//
//        if (goodsList != null && goodsList.size() > 0) {
//            //将此次点击back时候生成的订单号存入数据库  这个id 同时也是用户点击存储临时订单时候的时间戳  可以反推时间
//            orderIdDaoDao.insert(new OrderIdDao(null, nowTimestamp, tolNum, tolMon));
//
//            for (int i = 0; i < goodsList.size(); i++) {
//                //订单表中  存入订单号码(时间戳),
//                GoodsBean.DataBean dataBean = goodsList.get(i);
//
//                orderBeanDaoDao.insert(new OrderBeanDao(null, nowTimestamp, dataBean.getGoods_id(),
//                        numMap.get(dataBean.getGoods_id())));
//
//                //存入商品属性
//                goodsBeanDaoDao.insert(new GoodsBeanDao(null, dataBean.getGoods_id(), dataBean.getSpec(),
//                        dataBean.getGoods_name(), Double.parseDouble(dataBean.getGoods_price()), Integer.parseInt(dataBean.getBarcode())));
//            }
//
//        }
    }

    private void insertOrderBeanDao(int nowTimestamp, final List<GoodsBean.DataBean> goodsList, Map<Integer, Integer> numMap) {
        DaoSession daoSession = MyApp.getDaoSession();
        final AsyncSession asyncSession = daoSession.startAsyncSession();

        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation asyncOperation) {
                if (asyncOperation.isCompleted()) {
                    insertGoodsProperties(goodsList);
                }
            }
        });

//        List<OrderBeanDao> list = new ArrayList<>();

        for (int i = 0; i < goodsList.size(); i++) {
            //订单表中  存入订单号码(时间戳),
            GoodsBean.DataBean dataBean = goodsList.get(i);

//            list.add(new OrderBeanDao(null, nowTimestamp, dataBean.getGoods_id(), numMap.get(dataBean.getGoods_id())));

            asyncSession.insert(new OrderBeanDao(null, nowTimestamp, dataBean.getGoods_id(),
                    numMap.get(dataBean.getGoods_id())));

//            //存入商品属性
//            asyncSession.insert(new GoodsBeanDao(null, dataBean.getGoods_id(), dataBean.getSpec(),
//                    dataBean.getGoods_name(), Double.parseDouble(dataBean.getGoods_price()), Integer.parseInt(dataBean.getBarcode())));
        }

    }

    /**
     * 存入商品属性  先判断是否有存储过这个商品  如果有的话  就不再存入了
     *
     * @param goodsList
     */
    private void insertGoodsProperties(List<GoodsBean.DataBean> goodsList) {
        DaoSession daoSession = MyApp.getDaoSession();
        final AsyncSession asyncSession = daoSession.startAsyncSession();

        GoodsBeanDaoDao goodsBeanDaoDao = daoSession.getGoodsBeanDaoDao();

        List<GoodsBeanDao> list = goodsBeanDaoDao.queryBuilder().build().list();

        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation asyncOperation) {
                //全部数据存入之后  查询现在数据库中存入了多少临时订单
                if (asyncOperation.isCompleted())
                    queryTempOrderNum();
            }
        });

//        List<GoodsBeanDao> list1 = new ArrayList<>();
        for (int i = 0; i < goodsList.size(); i++) {

            GoodsBean.DataBean dataBean = goodsList.get(i);
            if (list.contains(dataBean)) {
                continue;
            }
//            list1.add(new GoodsBeanDao(null, dataBean.getGoods_id(), dataBean.getSpec(),
//                    dataBean.getGoods_name(), Double.parseDouble(dataBean.getGoods_price()), Integer.parseInt(dataBean.getBarcode())));
            //存入商品属性
            asyncSession.insert(new GoodsBeanDao(null, dataBean.getGoods_id(), dataBean.getSpec(),
                    dataBean.getGoods_name(), Double.parseDouble(dataBean.getGoods_price()), Integer.parseInt(dataBean.getBarcode())));
        }
//        asyncSession.insert(list1);
    }

    /**
     * 查询数据库中的临时订单总数
     */
    private void queryTempOrderNum() {
        final DaoSession daoSession = MyApp.getDaoSession();
//        long count = 0;
//        OrderIdDaoDao orderIdDaoDao = daoSession.getOrderIdDaoDao();
//        orderIdDaoDao.queryBuilder().count();

        AsyncSession asyncSession = daoSession.startAsyncSession();
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation asyncOperation) {
                if (asyncOperation.isCompleted()) {
                    List<OrderIdDao> orderNumList = new ArrayList<>();

                    orderNumList = (List<OrderIdDao>) asyncOperation.getResult();
                    mView.setThisOrderTemp(orderNumList.size());
                }
            }
        });
        asyncSession.queryList(daoSession.getOrderIdDaoDao().queryBuilder().build());
//        asyncSession.queryList();
//        count = asyncSession.queryBuilder(OrderIdDao.class).count();
    }

    /**
     * 查询数据库全部数据
     */
    public void quaryAllData() {
        DaoSession daoSession = MyApp.getDaoSession();
        AsyncSession asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation asyncOperation) {
                String s = asyncOperation.getResult().toString();
                Log.e("database====", s);
            }
        });

        asyncSession.loadAll(GoodsBeanDao.class);
        asyncSession.loadAll(OrderIdDao.class);
        asyncSession.loadAll(OrderBeanDao.class);
    }
}
