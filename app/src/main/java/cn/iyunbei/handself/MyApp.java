package cn.iyunbei.handself;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.posapi.PosApi;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpHeaders;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.iyunbei.handself.bean.TempOrderBean;
import cn.iyunbei.handself.greendao.DaoMaster;
import cn.iyunbei.handself.greendao.DaoSession;
import jt.kundream.base.BaseApplication;
import jt.kundream.utils.ToastUtils;
import okhttp3.OkHttpClient;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:
 **/
public class MyApp extends BaseApplication {
    PosApi mPosApi = null;


    /**
     * 临时订单存储的集合，因为需求是关闭这个页面或者app的时候就小时  所以可以直接存放在当前页面的集合中
     */
    private List<TempOrderBean> tempList ;

    public List<TempOrderBean> getTempList() {
        return this.tempList;
    }

    public void setTempList(List<TempOrderBean> tempList) {
        this.tempList = tempList;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = getApplicationContext();
        initPosapi();
        initOkGo();
        initDatabase();
    }

    private void initOkGo() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置全局读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //设置全局写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //设置全局链接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        HttpHeaders headers = new HttpHeaders();
//        headers.put();

        OkGo.getInstance().init(this)
                .setOkHttpClient(builder.build())
                .setCacheMode(CacheMode.NO_CACHE)
                .setRetryCount(3);
//                .addCommonParams();
    }

    private static Context instance;

    public static Context getInstance() {

        return instance;
    }

    private void initPosapi() {
        //posap初始化
        mPosApi = PosApi.getInstance(this);
        //根据型号进行初始化mPosApi类
        if (Build.MODEL.contains("LTE") || android.os.Build.DISPLAY.contains("3508") ||
                android.os.Build.DISPLAY.contains("403") ||
                android.os.Build.DISPLAY.contains("35S09")) {
            mPosApi.initPosDev("ima35s09");
        } else if (Build.MODEL.contains("5501")) {
            mPosApi.initPosDev("ima35s12");
        } else {
            mPosApi.initPosDev(PosApi.PRODUCT_MODEL_IMA80M01);
        }
        mPosApi.setOnComEventListener(new PosApi.OnCommEventListener() {
            @Override
            public void onCommState(int i, int i1, byte[] bytes, int i2) {

                switch (i) {
                    case PosApi.POS_INIT:
                        if (i1 == PosApi.COMM_STATUS_SUCCESS) {
                            ToastUtils.showShort(getApplicationContext(), "设备初始化成功");
                        } else {
                            ToastUtils.showShort(getApplicationContext(), "设备初始化失败");
                        }
                        break;
                }
            }
        });

    }


    private static DaoMaster.DevOpenHelper devOpenHelper;
    private static SQLiteDatabase database;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    public static void initDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        devOpenHelper = new DaoMaster.DevOpenHelper(MyApp.getInstance(), "temp-order_db", null);//数据库名  临时订单存储数据库
        database = devOpenHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public static SQLiteDatabase getDb() {
        return database;
    }

}
