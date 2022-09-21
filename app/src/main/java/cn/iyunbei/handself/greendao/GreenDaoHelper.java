package cn.iyunbei.handself.greendao;

import android.database.sqlite.SQLiteDatabase;

import cn.iyunbei.handself.MyApp;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:获取session对象
 **/
public class GreenDaoHelper {
    private static DaoMaster.DevOpenHelper devOpenHelper;
    private static SQLiteDatabase database;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    /**
     * 初始化greenDao
     * 建议放在Application 中进行
     */

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
