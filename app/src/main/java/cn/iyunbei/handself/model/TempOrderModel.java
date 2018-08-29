package cn.iyunbei.handself.model;

import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.iyunbei.handself.MyApp;
import cn.iyunbei.handself.RequestCallback;
import cn.iyunbei.handself.bean.OrderIdDao;
import cn.iyunbei.handself.bean.TempOrderBean;
import cn.iyunbei.handself.contract.TempOrderContract;
import cn.iyunbei.handself.greendao.DaoSession;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public class TempOrderModel implements TempOrderContract.Model {

    @Override
    public void quaryOrderIdList(Context ctx, final RequestCallback.QuarySqlCallback callback) {

        DaoSession daoSession = MyApp.getDaoSession();
        AsyncSession asyncSession = daoSession.startAsyncSession();

        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation asyncOperation) {
                if (asyncOperation.isCompleted()) {
                    /**
                     * 此处查出来第一个表的内容
                     */
//                    Object result = asyncOperation.getResult();
                    List<OrderIdDao> list = (List<OrderIdDao>) asyncOperation.getResult();

                    callback.quarySqlOrderIdSucc(list);


//                    String s = new Gson().toJson(TempOrderBean.class);
//
//                    Log.e("json========",s);

//                    getAllData(list, callback);
//                    callback.quarySqlOrderIdSucc(list);
                    /**
                     * 下一句得出来的是第二个订单表中的内容
                     */
//                    ((OrderIdDao) ((ArrayList) list).get(0)).daoSession.queryBuilder(OrderBeanDao.class).build().list();
                    /**
                     * 此处查出来的是所有的商品表的数据
                     */
//                    ((OrderIdDao) ((ArrayList) result).get(0)).daoSession.queryBuilder(OrderBeanDao.class).build()
//                      .list().get(8).daoSession.queryBuilder(GoodsBeanDao.class).build().list();
                    Log.e("tag========", list.toString());
                }
            }
        });
        /**
         * 先查询总共有多少订单
         */
//        asyncSession.queryList(daoSession.getOrderIdDaoDao().queryBuilder().build().list());

        asyncSession.queryList(daoSession.getOrderIdDaoDao().queryBuilder().build());
    }

    /**
     * 获取所有数据 i++) {
     * <p>
     * 此处略有尴尬  TMD不会写啊
     *
     * @param list
     * @param callback
     */
    private void getAllData(List<OrderIdDao> list, RequestCallback.QuarySqlCallback callback) {


        Map<Integer, Integer> numMap = new HashMap<>();


    }

}

